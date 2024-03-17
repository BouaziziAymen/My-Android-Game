package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.basics.Liquid;
import com.evolgames.entities.cut.FreshCut;
import com.evolgames.entities.cut.SegmentFreshCut;
import com.evolgames.entities.factories.Materials;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.entities.particles.wrappers.LiquidParticleWrapper;
import com.evolgames.entities.properties.usage.LiquidContainerProperties;
import com.evolgames.entities.serialization.infos.LiquidSourceInfo;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.userinterface.model.toolmodels.UsageModel;
import com.evolgames.utilities.GeometryUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LiquidContainer extends Use {
    private List<LiquidSourceInfo> liquidSourceInfoList;
    transient private HashMap<LiquidSourceInfo, LiquidParticleWrapper> liquidSourceInfoHashMap;
    private boolean open;


    @SuppressWarnings("unused")
    public LiquidContainer() {
    }

    public LiquidContainer(UsageModel<?> usageModel, PhysicsScene<?> physicsScene, boolean mirrored) {
        this.liquidSourceInfoList =
                ((LiquidContainerProperties) usageModel.getProperties()).getLiquidSourceModelList().stream()
                        .map(m -> m.toLiquidSourceInfo(mirrored))
                        .collect(Collectors.toList());
        createLiquidSources(physicsScene.getWorldFacade());

    }

    public List<LiquidSourceInfo> getLiquidSourceInfoList() {
        return liquidSourceInfoList;
    }

    public void startPouring(int i) {
        LiquidSourceInfo liquidSourceInfo = this.liquidSourceInfoList.get(i);
        if (liquidSourceInfoHashMap.containsKey(liquidSourceInfo)) {
            Objects.requireNonNull(liquidSourceInfoHashMap.get(liquidSourceInfo)).setSpawnEnabled(true);
        }

    }

    public void stopPouring(int i) {
        LiquidSourceInfo liquidSourceInfo = this.liquidSourceInfoList.get(i);
        if (liquidSourceInfoHashMap.containsKey(liquidSourceInfo)) {
            Objects.requireNonNull(liquidSourceInfoHashMap.get(liquidSourceInfo)).setSpawnEnabled(false);
        }

    }

    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
        if (this.open) {
            open();
        } else {
            close();
        }
        for (LiquidSourceInfo liquidSourceInfo : liquidSourceInfoList) {
            Vector2 dir = liquidSourceInfo.getLiquidDirection().cpy();
            if (liquidSourceInfo.getContainerEntity().getBody() == null) {
                continue;
            }
            GeometryUtils.rotateVectorRad(dir, liquidSourceInfo.getContainerEntity().getBody().getAngle());
            int index = this.liquidSourceInfoList.indexOf(liquidSourceInfo);
            if (dir.y < 0 && this.open) {
                startPouring(index);
            } else {
                stopPouring(index);
            }
        }

    }

    @Override
    public List<PlayerSpecialAction> getActions() {
        List<PlayerSpecialAction> playerSpecialActions = new java.util.ArrayList<>();
        if (isOpen()) {
            playerSpecialActions.add(PlayerSpecialAction.SwitchOff);
        } else {
            playerSpecialActions.add(PlayerSpecialAction.SwitchOn);
        }
        return playerSpecialActions;
    }

    @Override
    public void dynamicMirror(PhysicsScene<?> physicsScene) {

    }

    public void createLiquidSources(WorldFacade worldFacade) {
        this.liquidSourceInfoHashMap = new HashMap<>();
        this.liquidSourceInfoList
                .forEach(
                        p -> {
                            Vector2 dir = p.getLiquidDirection();
                            Vector2 nor = new Vector2(-dir.y, dir.x);
                            Vector2 e = p.getLiquidSourceOrigin().cpy().mul(32f);
                            float axisExtent = p.getExtent();
                            FreshCut freshCut = new SegmentFreshCut(e.cpy().sub(axisExtent / 2 * nor.x, axisExtent / 2 * nor.y), e.cpy().add(axisExtent / 2 * nor.x, axisExtent / 2 * nor.y), false, 1f);
                            Liquid liquid = Materials.getLiquidByIndex(p.getLiquid());
                            LiquidParticleWrapper liquidParticleWrapper =
                                    worldFacade
                                            .createLiquidParticleWrapper(
                                                    p.getContainerEntity(),
                                                    freshCut,
                                                    liquid.getDefaultColor(),
                                                    liquid.getFlammability(),
                                                    (int) (10 + 10 * p.getExtent()), (int) (10 + 20 * p.getExtent())
                                            );
                            this.liquidSourceInfoHashMap.put(p, liquidParticleWrapper);
                        }
                );
    }

    public boolean isOpen() {
        return open;
    }

    public void open() {
        this.open = true;
        this.liquidSourceInfoList.forEach(t -> {
            if (t.getSealEntity() != null) {
                t.getSealEntity().getMesh().setVisible(false);
            }
        });
    }

    public void close() {
        this.open = false;
        this.liquidSourceInfoList.forEach(t -> {
            if (t.getSealEntity() != null) {
                t.getSealEntity().getMesh().setVisible(true);
            }
        });
    }

}
