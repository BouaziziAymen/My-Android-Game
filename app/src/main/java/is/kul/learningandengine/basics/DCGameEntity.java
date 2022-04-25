package is.kul.learningandengine.basics;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointEdge;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.basics.JointKey.KeyType;
import is.kul.learningandengine.factory.BasicFactory;
import is.kul.learningandengine.helpers.Array;
import is.kul.learningandengine.helpers.UnionFind;
import is.kul.learningandengine.helpers.Utils;
import is.kul.learningandengine.particlesystems.FireParticleSystem;
import is.kul.learningandengine.scene.Cut;
import is.kul.learningandengine.scene.Fire;
import is.kul.learningandengine.scene.GameScene;
import is.kul.learningandengine.scene.World;

public class DCGameEntity extends MCMesh {
	public HashSet<NonRotatingChild> nonRotatingChildren;
	public GameEntityContactListener listener;
	public PhysicsConnector connector;
	protected Body body;

	public Vector2 sail;
	public int smart;
	public ArrayList<Block> blocks;
	float Vsquare;
	String entityName;
	HashSet<JointKey> keys;
	public float qe;
	int INITIAL;
	private GameGroup parent;
	private int tcreation;
	public boolean penetration;
	int pcounter;

    public DCGameEntity dive;
	public boolean alive = true;

	void setPenetration() {
        this.pcounter = 0;
        this.penetration = true;
	}

    public int mark;
	public DCGameEntity(float pX, float pY, float rot, MeshInfo meshInfo, String name, boolean initialVisibility) {

		super(pX, pY, rot, meshInfo, name);
		mark = World.entityIDS.getAndIncrement();

        this.entityName = name;
        this.qe = 0.01f;
        setVisible(initialVisibility);

		ResourceManager.getInstance().activity.runOnUpdateThread(new Runnable() {
																	 @Override
																	 public void run() {

                                                                         DCGameEntity.this.setVisible(true);

																	 }
																 }

		);


        this.keys = new HashSet<JointKey>();
        this.vregister = new ArrayList<Float>();
        this.varegister = new ArrayList<Float>();
        this.stains = new ArrayList<Stain>();
        this.setTcreation(World.step);
        this.nonRotatingChildren = new HashSet<NonRotatingChild>();

	}
	public boolean testPoint(float x,float y){
		for(Block b:blocks)if(b.testPoint(x,y))return true;
		return false;
	}

	public void addHalfChild(NonRotatingChild entity) {
		entity.setParent(this);
        nonRotatingChildren.add(entity);
	}

	int time,dtimer;

	public void updateDCGameEntity() {
        this.time++;
		if (World.step % 60 == 0||time==0) {
			for(Block b:blocks)for(Grain g:b.getGrains())g.updateGrain();
			updateExpandablex();
		}

		for (NonRotatingChild entity : this.nonRotatingChildren) {
			if (!entity.isIgnoreUpdate()) {
				Vector2 v = getBody().getWorldPoint(new Vector2(entity.x0, entity.y0).mul(1 / 32f)).cpy().mul(32);
				if (entity.type == NRType.EMITTER) {
					FireParticleSystem system = (FireParticleSystem) entity;
					system.setPositionOfEmitter(v.x, v.y);

				} else
					//FireParticleSystem system = (FireParticleSystem) entity;


					entity.setPosition(v.x, v.y);

			}

		}


		if (getY() < -1000) {


			//Log.e("error-points"+blocks.get(0).getFixtures().size(), Arrays.toString(blocks.get(0).getBlockPoints().toArray()));
			//for(FIXTURE fix:blocks.get(0).getFixtures())
			//Log.e("error-fix", Arrays.toString(fix.fixturePoints.toArray()));

		}


		if (false)
			if (getBody() != null)
				if (getBody().getType() == BodyDef.BodyType.DynamicBody) {
					if (blocks != null)
						for (Block block : this.blocks) {

							if (block.hasGrid) {

								for (int i = 0; i < block.grid.allGrains.size(); i++) {
									Grain g = block.grid.allGrains.get(i);
									GameScene.plotter.drawPoint(g.position, Color.BLUE, 1f, 0);

								}
							}
						}

				}


        updateV();



	}
	Vector2 previousBodyPosition;
	float angle;

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
super.onManagedUpdate(pSecondsElapsed);
		if(body!=null) {
			previousBodyPosition = this.body.getPosition().cpy();
			previousBodyPosition = this.body.getPosition().cpy();
			angle = this.body.getAngle();
		}

	}


		public void setBlocks(ArrayList<Block> blocks) {
		this.blocks = blocks;

		//this.addExpandable(0);


	}

	public boolean test(ArrayList<Cut> cuts) {

		for (Block b : this.blocks) {
			if (!b.test(cuts)) return false;
		}
		return true;
	}

	public ArrayList<DCGameEntity> applyCuts(ArrayList<Cut> cuts) {
if(!body.isActive())return null;
		Iterator<Cut> iterator = cuts.iterator();
		while (iterator.hasNext()) {
			Cut cut = iterator.next();
			if (cut.isDead()) iterator.remove();
		}

		if (cuts.size() == 0) return null;
		ArrayList<Block> newBlocks = new ArrayList<Block>();



		for (Block b : this.blocks) {

			b.applyCuts(cuts, b);
			newBlocks.addAll(b.getBlocks());

		}
		for (Block block : newBlocks) {
			block.reset();

		}

		if (newBlocks.size() != 0) {
            blocks = newBlocks;



			ArrayList<DCGameEntity> entities = this.checkDivision();


            this.parent.addAll(entities);

            detach();


			return entities;
		} else {
            detach();
		}
		return null;
	}

	int getNumberOfGrains() {
		int n = 0;
		for (Block b : this.blocks) n += b.grid.allGrains.size();
		return n;
	}


	public void applyImpact(Fixture fixture, Vector2 point, float impactEnergy) {
		//if(this.blocks.size()==0)return; NOT NEEDED I GUESS
if(!alive)return;
		if (this.body.getType() == BodyDef.BodyType.DynamicBody) {

			Vector2 localPoint = this.body.getLocalPoint(point).cpy().mul(32);

			int bi1 = (Integer) fixture.getUserData();
			Block block = this.getBlock(bi1);

			if (block == null) return;

			//Log.e("pario", ""+blocks.size()+"/"+bi1+"/"+Arrays.toString(this.meshInfo.meshIds)+s);
			if (!block.hasGrid)
				return;
			float d = Float.MAX_VALUE;
			Grain nearest = null;

			for (int i = 0; i < block.grid.allGrains.size(); i++) {

				float dis = block.grid.allGrains.get(i).distance(localPoint);

				if (dis < d) {
					d = dis;
					nearest = block.grid.allGrains.get(i);

				}
			}

			if (nearest != null) {

				new Crack(this, block, nearest, impactEnergy, 0, point);

			}


		}

	}

	public void applyImpact(Block block, Vector2 point, float impactEnergy, int order) {


		if (this.body.getType() == BodyDef.BodyType.DynamicBody) {
			Vector2 localPoint = body.getLocalPoint(point).cpy().mul(32);

			if (!block.hasGrid)
				return;
			float d = Float.MAX_VALUE;
			Grain nearest = null;

			for (int i = 0; i < block.grid.allGrains.size(); i++) {

				float dis = block.grid.allGrains.get(i).distance(localPoint);
				if (dis < d) {
					d = dis;
					nearest = block.grid.allGrains.get(i);

				}
			}

			if (nearest != null) {
				new Crack(this, block, nearest, impactEnergy, order, point);

			}


		}

	}

	public void applyStain(Block stainBlock) {
		final Stain stain = new Stain(stainBlock);
		stains.add(stain);

		ResourceManager.getInstance().activity.runOnUpdateThread(new Runnable() {
																	 @Override
																	 public void run() {
																		 attachChild(stain.stainMesh);

																	 }
																 }

		);

	}

	public void addDecoration(ArrayList<Vector2> points, Color color, int blockID) {

		Block decoration = BasicFactory.getInstance().createBlock(points, 0, 0, 0);
		decoration.setID(-1);
		decoration.blockType = BlockType.IMAGE;
		decoration.blockSubType = BlockImageSubType.DECORATION;
		decoration.color = color;
		this.getBlock(blockID).associatedBlocks.add(decoration);
	}


	public void updateVertices(int blockID, ArrayList<Vector2> newPoints) {

		Block block = this.getBlock(blockID);
		block.changeVertices(newPoints);
		block.computeMeshTriangles();
		float[] data = BasicFactory.generateData(block.MESHTRIANGLES);
		for (LayerInfo layer : this.meshInfo.layers) {
			if (layer.layerID == blockID) {
				layer.basic = data;
			}
		}
		//this.vcount = this.meshInfo.getVerticesData().length/3;
		this.setUpdate();
	}


	public void applyStain(float x, float y) {
		Block block = this.blocks.get(0);
		Block stainBlock = block.getStainBlock(x, y, ResourceManager.getInstance().imageTextureRegion);

		this.applyStain(stainBlock);
	}

	ArrayList<Stain> stains;

	private ArrayList<ArrayList<Block>> getDivisionGroups() {
		System.out.println("-------------get division groups-----------");
		int N = blocks.size();
		UnionFind unionFinder = new UnionFind(N);
		for (int i = 0; i < blocks.size(); i++) {
			for (int j = i + 1; j < blocks.size(); j++) {

				if ((blocks.get(i).getPolarity() == blocks.get(j).getPolarity())) {
					if (Utils.getRelativePositionOfLayers(blocks.get(i).getBlockPoints(), blocks.get(j).getBlockPoints()))
						unionFinder.union(i, j);
				} else if (blocks.get(i).getPolarity() == Polarity.NEUTRAL || blocks.get(j).getPolarity() == Polarity.NEUTRAL)
					if (Utils.getRelativePositionOfLayers(blocks.get(i).getBlockPoints(), blocks.get(j).getBlockPoints()))
						unionFinder.union(i, j);
			}
		}

		for (Block block : blocks) block.setPolarity(Polarity.NEUTRAL);
		System.out.println("------------------------");
		for (Block block : blocks)
		System.out.println(Arrays.toString(block.getBlockPoints().toArray())+"§"+block.getPROPERTIES().getMaterialNumber());
		unionFinder.compute();

		Iterator<HashSet<Integer>> iterator = unionFinder.myDict.values().iterator();
		ArrayList<ArrayList<Block>> blockGrouping = new ArrayList<>();
		int INDEX = 0;
		while (iterator.hasNext()) {
			blockGrouping.add(new ArrayList<>());
			HashSet<Integer> set = iterator.next();
			for (Integer integer : set) {
				blockGrouping.get(INDEX).add(blocks.get(integer));
			}
			INDEX++;
		}
		//TO BE CONTINUED


		return blockGrouping;
	}


	public void setBody(Body body) {
		this.body = body;
		this.previousBodyPosition = body.getPosition().cpy();
		body.setUserData(this);
	}


	public Body getBody() {
		return this.body;
	}




	Vector2 isTouched(float x, float y, float radius) {


		for (Block block : blocks) {
			ArrayList<Vector2> vertices = block.getBlockPoints();
			if (Utils.PointInPolygon(x, y, vertices)) return Vector2Pool.obtain(x, y);
			for (int i = 0; i < vertices.size(); i++) {
				int ni = (i == vertices.size() - 1) ? 0 : i + 1;
				Vector2 inter = Utils.FindLineSegmentCircleIntersection(x, y, radius, vertices.get(i), vertices.get(ni));
				if (inter != null) {


					return inter;

				}
			}

		}

		return null;

	}

	public boolean testTouch(float tx, float ty, float radius) {
		float[] t1 = convertSceneCoordinatesToLocalCoordinates(new float[]{tx, ty});

		sail = isTouched(t1[0], t1[1], radius);

		if (sail != null) {
			return true;
		}

		return false;
	}

	public Vector2 getTouchPosition() {
		return sail;
	}

	public void addKey(JointKey key, int bn) {

		keys.add(key);
		this.getBlock(bn).keys.add(key);

	}

	public void addKeyWithout(JointKey key) {
		keys.add(key);


	}

	public void detach() {
		if(!alive)return;
		alive=false;
		body.setActive(false);


		ResourceManager.getInstance().activity.runOnUpdateThread(new Runnable() {
																	 @Override
																	 public void run() {

																	// World.physicsWorld.destroyBody(body);
																	 	detachSelf();

																	 }


																 }
		);



	}

	public float[] distance(Vector2 anchor) {

		float D = Float.MAX_VALUE;
		int a = -1;
		for (int i = 0; i < blocks.size(); i++) {
			float d = blocks.get(i).getDistance(anchor);
			if (d < D) {
				D = d;
				a = i;
			}
		}
		return new float[]{D, a};


	}


	public ArrayList<DCGameEntity> checkDivision() {

		HashSet<Fire> fires = new HashSet<Fire>();
		for (Fire fire : World.fires) {
			if (fire.getEntity() == this) {
				fires.add(fire);
			}
		}


		float x = this.getBody().getPosition().x
				* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

		float y = this.getBody().getPosition().y
				* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		float rot = this.getBody().getAngle();
		ArrayList<ArrayList<Block>> groups = getDivisionGroups();

		Vector2 velocity = this.getBody().getLinearVelocity().mul(0.5f);
		float angularv = this.getBody().getAngularVelocity() * 0.5f;
		//FIND WHERE TO TRANSFER KEYS: TO WHERE THE SAIL IN THE BLOCK IS

		int z = this.getZIndex();

		final ArrayList<DCGameEntity> childrenEntities = new ArrayList<DCGameEntity>();
		for (int i = 0; i < groups.size(); i++) {


			Collections.sort(groups.get(i));

			DCGameEntity entity = BasicFactory.getInstance().createGameEntity(groups.get(i), x, y, rot, false);

			//TRANSPORT HALF CHILDREN


			entity.getBody().setLinearVelocity(velocity);
			entity.getBody().setAngularVelocity(angularv);
			entity.setZIndex(z);

			childrenEntities.add(entity);


		}


		for (DCGameEntity entity : childrenEntities) {
			ArrayList<Block> main = new ArrayList<Block>();
			for (Block b : entity.blocks) {
				for (Block ass : b.associatedBlocks) {
					if (ass.blockSubType == BlockImageSubType.EXPANDABLE)
						if (!b.grid.allGrains.contains(ass.GRAIN.position)) {
							Grain grain = b.grid.getNearestGrain(ass.GRAIN.position);
							ass.GRAIN = grain;
						}
					main.add(ass);
				}
			}

			//entity.ex = new Expandable(entity.getBlock(0), main);
		}


		for (DCGameEntity entity : childrenEntities) {
			for (Fire fire : fires) {
                for (Block b : entity.blocks) {
						Fire fireChild = fire.extractFire(b, entity);
						if (fireChild != null) {


							World.fires.add(fireChild);


						}

					}

            }

		}


		for (Fire fire : fires) {
			if (fire.getEntity() == this) {
				GameScene.world.removeFire(fire);
			}
		}
		HashSet<JointBluePrint> bps = new HashSet<>();

		for (JointKey key : this.keys) {
			for (int i = 0; i < groups.size(); i++) {


				for (Block block : groups.get(i)) {
					if (block.keys.contains(key)) {
						Body newBody = childrenEntities.get(i).getBody();
						final JointBluePrint bp = key.bluePrint;

						if (key.type == KeyType.A) {

							bp.definition.bodyA = newBody;

						}
						else {
							bp.definition.bodyB = newBody;

						}
						childrenEntities.get(i).addKeyWithout(key);
bps.add(bp);

					//parent.recreateJoint(bp);


					}
				}
			}
		}
for(JointBluePrint bp:bps){
			if(bp.definition.bodyB.isActive()&&bp.definition.bodyA.isActive())
			this.parent.recreateJoint(bp);}
		return childrenEntities;

	}

	public void setInitial(int initial) {
        INITIAL = initial;

	}

	public void setParentGroup(GameGroup gameGroup) {
        parent = gameGroup;

	}

	public GameGroup getParentGroup() {
		return this.parent;

	}

	public void merge(DCGameEntity other) {


	}


	int counter;
	public int ID;
	public boolean stop;

	public Block getBlock(int id) {
		for (Block block : this.blocks) if (block.getID() == id) return block;
		return null;
	}
	public Block getBlock(Fixture fixture) {
		for (Block block : this.blocks) if (block.fixtures.contains(fixture)) return block;
		return null;
	}

	public int getTcreation() {
		return this.tcreation;
	}

	public void setTcreation(int tcreation) {
		this.tcreation = tcreation;
	}

	ArrayList<Float> vregister;
	ArrayList<Float> varegister;

	float angularVelocity;
	float energy;
	private Expandable ex;


	void updateV() {
		if (this.body != null) {
			if (this.vregister.size() >= 10) this.vregister.remove(0);
            this.vregister.add(getBody().getLinearVelocity().len2());

			for (Float f : this.vregister) this.Vsquare += f;
            this.Vsquare = this.Vsquare / 10;

			if (this.varegister.size() >= 10) this.varegister.remove(0);
            this.varegister.add(this.body.getAngularVelocity());
			for (Float f : this.varegister) this.angularVelocity += f;
            this.angularVelocity = this.angularVelocity / 10;

		}

	}

	public float getKineticEnergy() {
		if (getBody().getType() == BodyDef.BodyType.StaticBody) return 0;
		float Em = 1 / 2f * this.body.getInertia() * this.angularVelocity * this.angularVelocity;
		float Ev = 1 / 2f * this.body.getMass() * this.Vsquare;
        this.energy = Ev + Em;
		return this.energy;
	}

	public void addExpandable(int blockID) {


		Block block = getBlock(blockID);

        this.ex = new Expandable(block, null);

//for(Block b:ex.getBlocks()){
		//this.addExpandable(b.VERTICES, Color.BLACK, blockID);
//}


		//TODO LOOK FOR LAYERBLOCK AND ADD EXPANDABLE AND UPDATE
	}

	public void createFire(Block fireBlock) {
		// TODO Auto-generated method stub
		GameScene.world.fireSignal(fireBlock, this);
		if (ex == null) addExpandable(0);

	}

	public void applyHeatRadiance(Vector2 worldPositionOfSpark, float heat, ArrayList<Block> arrayList) {
		Vector2 center = getBody().getLocalPoint(worldPositionOfSpark).cpy().mul(32f);
		//GameScene.plotter.drawPoint(center, Color.RED, 1, 0);
		for (Block blockOnFire : arrayList) {

			if (!Utils.PointInPolygon(center, blockOnFire.VERTICES)) continue;
			if (!blockOnFire.hasGrid) continue;
			//GameScene.plotter.drawPoint(center, Color.RED, 1, 0);

			Grain affectedGrain = blockOnFire.grid.getNearestGrain(center);
			affectedGrain.applyHeat(heat);

		}

	}

	public void updateExpandablex() {

		if (this.ex == null) return;

        this.ex.update();
		if (this.ex.shapeUpdated || this.ex.colorUpdated) {
			Block block = getBlock(0);


			for (Block b : this.ex.getBlocks())
				if (block.associatedBlocks.indexOf(b) == -1) block.associatedBlocks.add(0, b);


			float[] newBasic = block.getLayerData();
			Integer[] temp = block.getVertexCount();
			int[] newVertexCount = new int[temp.length];
			for (int i = 0; i < temp.length; i++)
				newVertexCount[i] = temp[i];
			Color[] newColors = block.getColors();
            this.meshInfo.updateLayer(0, newBasic, newVertexCount, newColors);

			if (this.ex.shapeUpdated)
                setUpdate();
			else {
                setUpdateColorOnly();


			}
		}

	}

	int deathLimit;
	boolean deathTimer;

	public void setDeathTimer(int duration) {
        this.deathLimit = duration;
        this.deathTimer = true;

	}

	public void updateBlocks(ArrayList<Block> addedBlocks) {

        int N = -1;
        for (Block b : this.blocks) {
            if (b.getID() > N) N = b.getID();
        }

		for (Block block : addedBlocks) {
			N++;
			block.setID(N);
			float[] newBasic = block.getLayerData();
			Integer[] temp = block.getVertexCount();
			int[] newVertexCount = new int[temp.length];
			for (int i = 0; i < temp.length; i++)
				newVertexCount[i] = temp[i];
			Color[] newColors = block.getColors();
            this.meshInfo.addLayer(N, newBasic, newVertexCount, newColors);

		}
		setUpdate();
        this.blocks.addAll(addedBlocks);
		Log.e("tata","updateblocks"+ this.blocks.size()+"/"+ this.meshInfo.layers.size());

	}
boolean hasBrother(DCGameEntity e){
	return parent.contains(e);
}
HashSet<DCGameEntity> getBrothers(){
	HashSet<DCGameEntity> brothers = new HashSet<DCGameEntity>();
for(int i=0;i<parent.entities.getChildCount();i++)brothers.add(parent.getEntityByIndex(i));
	return brothers;
}
public float getMass(){
	return parent.getMass();
}


public String toString(){
	String s ="";
	for(Block b:blocks){
		s+= Arrays.toString(b.VERTICES.toArray())+"/";
	}
	return s;
}
boolean dragged;
	public void setDragged(boolean d){
		dragged=d;
	}
	boolean isDragged(){
		return dragged;
	}
}