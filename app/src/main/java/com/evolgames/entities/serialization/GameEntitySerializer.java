package com.evolgames.entities.serialization;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;

import java.util.ArrayList;
import java.util.List;

public class GameEntitySerializer {
    private List<LayerBlock> layerBlocks;
    @SuppressWarnings("unused")
    GameEntitySerializer(){}
    GameEntitySerializer(GameEntity gameEntity){
        this.layerBlocks = gameEntity.getBlocks();
    }
    public void afterLoad(){
        for (LayerBlock layerBlock : this.layerBlocks) {
            layerBlock.refillGrid();
        }
    }
    public void afterCreate(GameEntity gameEntity){
        for (LayerBlock layerBlock : this.layerBlocks) {
            layerBlock.setGameEntity(gameEntity);
        }
    }

    @SuppressWarnings("unused")
    public void setLayerBlocks(ArrayList<LayerBlock> layerBlocks) {
        this.layerBlocks = layerBlocks;
    }
}
