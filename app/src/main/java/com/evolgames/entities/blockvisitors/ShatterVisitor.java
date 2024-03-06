package com.evolgames.entities.blockvisitors;

import static com.evolgames.physics.PhysicsConstants.MINIMUM_STABLE_SPLINTER_AREA;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.cut.ShatterData;
import com.evolgames.utilities.BlockUtils;
import com.evolgames.physics.PhysicsConstants;
import com.evolgames.physics.WorldFacade;
import java.util.ArrayDeque;
import java.util.Iterator;

public class ShatterVisitor extends BreakVisitor<LayerBlock> {
  private final Vector2 localImpactPoint;
  private final WorldFacade worldFacade;
  private final GameEntity gameEntity;
  private float availableImpulse;
  private boolean isError = false;

  public ShatterVisitor(
      float impulse, Vector2 localPoint, WorldFacade worldFacade, GameEntity gameEntity) {
    super();
    this.availableImpulse = impulse;
    this.localImpactPoint = localPoint;
    this.worldFacade = worldFacade;
    this.gameEntity = gameEntity;
  }

  private void processBlock(LayerBlock layerBlock, boolean isFirst) {
    float pulverizationImpulse = calculatePulverizationImpulse(layerBlock);
    if (availableImpulse > pulverizationImpulse) {
      layerBlock.setAborted(true);
      layerBlock
          .getBlockGrid()
          .getCoatingBlocks()
          .forEach(coatingBlock -> coatingBlock.setPulverized(true));
      worldFacade.pulverizeBlock(layerBlock, gameEntity);
      shatterPerformed = true;
      availableImpulse -= pulverizationImpulse;
    } else {
      ShatterData data = BlockUtils.applyCut(layerBlock, localImpactPoint);
      if (data == null) {
        isError = true;
        return;
      }
      if (availableImpulse - data.getDestructionEnergy() >= 0) {

        if (!data.isNonValid()&& (isFirst||layerBlock.getBlockArea()>= 5*MINIMUM_STABLE_SPLINTER_AREA)) {
          shatterPerformed = true;
          layerBlock.performCut(data.getDestructionCut());
        }
        availableImpulse -= data.getDestructionEnergy();
      }
    }
  }

  public float calculatePulverizationImpulse(LayerBlock layerBlock) {
    float totalRatio = 0;
    for (CoatingBlock coatingBlock : layerBlock.getBlockGrid().getCoatingBlocks()) {
      float ratio = (float) (1.1f - coatingBlock.getProperties().getBurnRatio());
      totalRatio += ratio * coatingBlock.getArea() / layerBlock.getBlockArea();
    }
    totalRatio /= layerBlock.getBlockGrid().getCoatingBlocks().size();
    return (float)
        (1200f
            * Math.sqrt(
                PhysicsConstants.TENACITY_FACTOR
                    * layerBlock.getProperties().getTenacity()
                    * PhysicsConstants.PULVERIZATION_CONSTANT
                    * totalRatio
                    * layerBlock.getBlockArea()));
  }

  @Override
  public void visitTheElement(LayerBlock cutBlock) {
    ArrayDeque<LayerBlock> deque = new ArrayDeque<>();
    deque.push(cutBlock);
    while (!deque.isEmpty()) {
      LayerBlock current = deque.peek();
      if (current == null) {
        break;
      }
      if (current.isNotAborted()) {
        this.processBlock(current,cutBlock==current); // visit
      }

      if (this.isExhausted() || this.isError()) {
        break;
      }
      for (LayerBlock child : current.getChildren()) {
        if (child.isNotAborted()) {
          deque.addLast(child);
        }
      }

      deque.pop();
    }
    Iterator<LayerBlock> iterator = cutBlock.createIterator();
    while (iterator.hasNext()) {
      LayerBlock bl = iterator.next();
      if (bl.isNotAborted()) {
        splintersBlocks.add(bl);
      } else {
        abortedBlocks.add(bl);
      }
    }
  }

  public boolean isExhausted() {
    return availableImpulse <= 0;
  }

  public boolean isError() {
    return isError;
  }
}
