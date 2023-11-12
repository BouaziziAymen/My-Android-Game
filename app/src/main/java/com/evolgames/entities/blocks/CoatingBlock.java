package com.evolgames.entities.blocks;

import android.util.Pair;
import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.mesh.mosaic.MosaicMesh;
import com.evolgames.entities.properties.CoatingProperties;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.Utils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

public final class CoatingBlock extends AssociatedBlock<CoatingBlock, CoatingProperties> {

  public Vector2 position;
  public float value;
  private boolean isOnFire;
  private transient MosaicMesh mesh;
  private int layerId;
  private boolean hasFlame;
  private transient HashSet<CoatingBlock> neighbors;
  private float area;
  private float step;
  private boolean pulverized;
  private transient LayerBlock parent;

  @Override
  protected CoatingBlock getThis() {
    return this;
  }

  public int getLayerId() {
    return layerId;
  }

  public void setLayerId(int layerId) {
    this.layerId = layerId;
  }

  private void updateColor() {
    mesh.onColorsUpdated();
  }

  public MosaicMesh getMesh() {
    return mesh;
  }

  public void setMesh(MosaicMesh mesh) {
    this.mesh = mesh;
  }

  public void centerCoreCoatingBlock() {
    if (position == null) {
      position = new Vector2();
    }
    position.set(GeometryUtils.calculateCentroid(getVertices()));
  }

  @Override
  public void performCut(Cut cut) {
    setAborted(true);
    Pair<ArrayList<Vector2>, ArrayList<Vector2>> list =
        BlockUtils.splitVerticesSimple(cut, getVertices());
    CoatingBlock b1 = createChildBlock();
    b1.initialization(list.first, getProperties().copy(), getId());
    CoatingBlock b2 = createChildBlock();
    b2.initialization(list.second, getProperties().copy(), getId());

    b1.centerCoreCoatingBlock();
    b2.centerCoreCoatingBlock();
    addBlock(b1);
    addBlock(b2);
  }

  @Override
  protected void calculateArea() {
    area = GeometryUtils.getArea(getVertices());
  }

  @Override
  protected boolean shouldCalculateArea() {
    return true;
  }

  @Override
  protected CoatingBlock createChildBlock() {
    CoatingBlock child = new CoatingBlock();
    child.setStep(step);
    return child;
  }

  @Override
  public void computeTriangles() {
    super.computeTriangles();
    List<Vector2> triangles = getTriangles();
    for (int i = 0; i < triangles.size(); i += 3) {
      Vector2 centroid = GeometryUtils.calculateCentroid(triangles, i, 3);
      for (int j = 0; j < 3; j++) {
        Vector2 v = triangles.get(i + j);
        Vector2 u = Vector2Pool.obtain(v).sub(centroid).nor().mul(0.05f);
        v.add(u);
        Vector2Pool.recycle(u);
      }
    }
  }

  @Override
  public void translate(Vector2 translation) {
    Utils.translatePoints(getVertices(), translation);
    computeTriangles();
    centerCoreCoatingBlock();
  }

  public float getArea() {
    return area;
  }

  public Vector2 getPosition() {
    return position;
  }

  public void setPosition(Vector2 position) {
    this.position = position;
  }

  public void applyDeltaTemperature(double delta) {
    getProperties().applyDeltaTemperature(delta);
  }

  public double getTemperature() {
    return getProperties().getTemperature();
  }

  public void setTemperature(double temperature) {
    getProperties().setTemperature(temperature);
  }

  public void update() {
    double temperature = getTemperature();
    if (getProperties().isFlammable()) {
      double ignitionTemperature = getProperties().getIgnitionTemperature();
      double chemicalEnergy = getProperties().getChemicalEnergy();
      double initialChemicalEnergy = getProperties().getInitialChemicalEnergy();

      if (temperature > ignitionTemperature && chemicalEnergy >= initialChemicalEnergy / 100f) {
        setOnFire(true);
      } else if (temperature < ignitionTemperature) {
        setOnFire(false);
      }

      if (isOnFire()) {
        double deltaE = getFlameTemperature() / 1000;
        if (chemicalEnergy - deltaE >= 0) chemicalEnergy -= deltaE;
        else {
          chemicalEnergy = 0;
        }
        if (chemicalEnergy < initialChemicalEnergy / 100f) setOnFire(false);
        getProperties().setBurnRatio(1 - chemicalEnergy / initialChemicalEnergy);
      }
      getProperties().setChemicalEnergy(chemicalEnergy);
    }
    getProperties().updateColors();

    updateColor();
  }

  public float distance(Vector2 point) {
    return point.dst(position);
  }

  public boolean hasFlame() {
    return hasFlame;
  }

  public void setFlame(boolean b) {
    hasFlame = b;
  }

  public HashSet<CoatingBlock> getNeighbors() {
    return neighbors;
  }

  public void setNeighbors(HashSet<CoatingBlock> neighbors) {
    this.neighbors = neighbors;
  }

  public boolean isOnFire() {
    return isOnFire;
  }

  public void setOnFire(boolean onFire) {
    isOnFire = onFire;
  }

  public int getNx() {
    return getProperties().getRow();
  }

  public void setNx(int nx) {
    getProperties().setRow(nx);
  }

  public int getNy() {
    return getProperties().getColumn();
  }

  public void setNy(int ny) {
    getProperties().setColumn(ny);
  }

  public double getFlameTemperature() {
    return getProperties().getFlameTemperature();
  }

  public float getStep() {
    return step;
  }

  public void setStep(float step) {
    this.step = step;
  }

  public boolean isPulverized() {
    return pulverized;
  }

  public void setPulverized(boolean pulverized) {
    this.pulverized = pulverized;
  }

  public LayerBlock getParent() {
    return parent;
  }

  public void setParent(LayerBlock parent) {
    this.parent = parent;
  }
}
