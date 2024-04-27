package com.evolgames.dollmutilationgame.entities.serialization.serializers;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.evolgames.dollmutilationgame.entities.serialization.infos.InitInfo;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.basics.SpecialEntityType;
import com.evolgames.dollmutilationgame.entities.blocks.JointBlock;
import com.evolgames.dollmutilationgame.entities.blocks.LayerBlock;
import com.evolgames.dollmutilationgame.entities.factories.GameEntityFactory;
import com.evolgames.dollmutilationgame.entities.init.AngularVelocityInit;
import com.evolgames.dollmutilationgame.entities.init.BodyInit;
import com.evolgames.dollmutilationgame.entities.init.BodyInitImpl;
import com.evolgames.dollmutilationgame.entities.init.BodyNotActiveInit;
import com.evolgames.dollmutilationgame.entities.init.BulletInit;
import com.evolgames.dollmutilationgame.entities.init.LinearVelocityInit;
import com.evolgames.dollmutilationgame.entities.init.RecoilInit;
import com.evolgames.dollmutilationgame.entities.init.TransformInit;
import com.evolgames.dollmutilationgame.entities.usage.MeleeUse;
import com.evolgames.dollmutilationgame.entities.usage.Use;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEntitySerializer {
    public static Map<String, GameEntity> entities = new HashMap<>();
    private int zIndex;

    private InitInfo initInfo;
    private List<LayerBlock> layerBlocks;
    private List<Use> useList;
    private BodyDef.BodyType bodyType;
    private SpecialEntityType specialEntityType;
    private String name;
    private String uniqueId;
    private boolean mirrored;

    @SuppressWarnings("unused")
    GameEntitySerializer() {
    }

    GameEntitySerializer(GameEntity gameEntity) {
        this.layerBlocks = gameEntity.getBlocks();
        this.initInfo = gameEntity.getCreationInitInfo();
        this.bodyType = gameEntity.getBodyType();
        this.name = gameEntity.getName();
        this.uniqueId = gameEntity.getUniqueID();
        this.zIndex = gameEntity.getZIndex();
        this.mirrored = gameEntity.isMirrored();
        this.specialEntityType = gameEntity.getType();
        this.useList = gameEntity.getUseList();
        this.useList.stream().filter(e -> e instanceof MeleeUse).map(e -> (MeleeUse) e).forEach(MeleeUse::prepareToSerialize);
        this.layerBlocks.forEach(b -> b.getAssociatedBlocks().stream().filter(e -> e instanceof JointBlock).map(e -> (JointBlock) e).filter(
                j -> j.isNotAborted() && j.getBrother() != null
        ).forEach(
                JointBlock::generateJointInfo));

    }

    public void afterLoad() {
        for (LayerBlock layerBlock : this.layerBlocks) {
            layerBlock.refillGrid();
        }
    }

    public GameEntity create() {
        BodyInit bodyInit = getBodyInit();
        return GameEntityFactory.getInstance()
                .createGameEntity(
                        initInfo.getX(),
                        initInfo.getY(),
                        initInfo.getAngle(),
                        mirrored,
                        bodyInit,
                        layerBlocks,
                        bodyType,
                        name);
    }

    private BodyInit getBodyInit() {
        BodyInit bodyInit;
        BodyInitImpl bodyInitImpl = new BodyInitImpl(initInfo.getFilter());
        bodyInit = new BulletInit(bodyInitImpl, initInfo.isBullet());
        bodyInit = new TransformInit(bodyInit, initInfo.getX(), initInfo.getY(), initInfo.getAngle());
        bodyInit = new BodyNotActiveInit(bodyInit, initInfo.isNotActive());
        if (initInfo.getLinearVelocity() != null) {
            bodyInit = new LinearVelocityInit(bodyInit, initInfo.getLinearVelocity());
        }
        if (initInfo.getAngularVelocity() != 0) {
            bodyInit = new AngularVelocityInit(bodyInit, initInfo.getAngularVelocity());
        }
        if (initInfo.getMuzzleVelocity() != null) {
            bodyInit =
                    new RecoilInit(
                            bodyInit,
                            null,
                            initInfo.getRecoil(),
                            initInfo.getMuzzleVelocity(),
                            initInfo.getPoint());
        }
        return bodyInit;
    }

    public void afterCreate(GameEntity gameEntity) {
        gameEntity.redrawStains();
        gameEntity.setUniqueId(this.uniqueId);
        gameEntity.setType(this.specialEntityType);
        entities.put(gameEntity.getUniqueID(), gameEntity);
        gameEntity.setUseList(useList);
        gameEntity.setZIndex(this.zIndex);
    }

    @SuppressWarnings("unused")
    public void setLayerBlocks(ArrayList<LayerBlock> layerBlocks) {
        this.layerBlocks = layerBlocks;
    }
}
