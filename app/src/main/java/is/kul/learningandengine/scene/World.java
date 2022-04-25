package is.kul.learningandengine.scene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import is.kul.learningandengine.basics.Block;
import is.kul.learningandengine.basics.DCGameEntity;
import is.kul.learningandengine.basics.DCGameEntityPair;
import is.kul.learningandengine.basics.GameGroup;
import is.kul.learningandengine.basics.Grain;
import is.kul.learningandengine.helpers.UnionFind;
import is.kul.learningandengine.helpers.Utils;
import is.kul.learningandengine.particlesystems.Spark;

public class World extends Entity {
	static ArrayList<FixturePair> filters;

	public Entity gameGroups;
	public static PhysicsWorld physicsWorld;
	public static int AMBIENT_TEMPERATURE = 290;
	public static AtomicInteger entityIDS;
	public ArrayList<Fixture> getNearFixtures(Vector2 lower, Vector2 upper){
        nearFixtures.clear();

        World.physicsWorld.QueryAABB(queryCallBack, lower.x, lower.y, upper.x, upper.y);
		return this.nearFixtures;
	}


	public HashSet<Block> applyRadiance(Vector2 worldCenter, int counter){
		HashSet<Block> result = new 	HashSet<>();

		Vector2 delta = new Vector2(10,0);
		Utils.rotateVector2(delta,360 * counter/360f);
		Vector2 other = worldCenter.cpy().add(delta);

        World.physicsWorld.rayCast(rayCastCallBack2, worldCenter, other);







		return result;
	}













	private ArrayList<Fixture> nearFixtures;
	private QueryCallback queryCallBack = fixture -> {
World.this.nearFixtures.add(fixture);
		return true;
	};

	MyRayCastCallback rayCastCallBack = new MyRayCastCallback();


	SimpleRaycastCallback rayCastCallBack2 = new SimpleRaycastCallback();
	ExtremityRaycastCallback extremityrayCastCallBack = new ExtremityRaycastCallback();
public Vector2 getExtremity(Vector2 start, Vector2 direction){
	return this.extremityrayCastCallBack.determineExtremity(start,direction, World.physicsWorld);
}

	private Vector2 center;
	private Vector2 rcenter;








	Vector2 normal;
	public Cut getSpecificCutLimited(ArrayList<Vector2> limits, DCGameEntity specific, int ID,Vector2 center, Vector2 center2){
        counter = 0;

        l0x = limits.get(0).x;
        l0y = limits.get(0).y;
        l1x = limits.get(1).x;
        l1y = limits.get(1).y;
		this.center = center.cpy();
        rcenter =center2.cpy();
		Vector2 v = new Vector2(this.l1x - this.l0x, this.l1y - this.l0y).nor();
        this.normal =new Vector2 (-v.y,v.x);
		//Log.e("normal", ""+normal);
		return getComputeLimited(limits,specific,ID, center);
	}




	public void computeSpecificCutLimited(ArrayList<Vector2> limits, DCGameEntity specific, int ID,Vector2 center){
        counter = 0;
        l0x = limits.get(0).x;
        l0y = limits.get(0).y;
        l1x = limits.get(1).x;
        l1y = limits.get(1).y;
        computeLimited(limits,specific,ID, center);
	}


	public void computeSpecificCut(ArrayList<Vector2> limits, DCGameEntity specific, int ID){
        counter = 0;
        l0x = limits.get(0).x;
        l0y = limits.get(0).y;
        l1x = limits.get(1).x;
        l1y = limits.get(1).y;
        compute(limits,specific,ID);
	}






	public void computeCut(ArrayList<Vector2> limits){

        counter = 0;
        l0x = limits.get(0).x;
        l0y = limits.get(0).y;
        l1x = limits.get(1).x;
        l1y = limits.get(1).y;
		Vector2 v = new Vector2(this.l1x - this.l0x, this.l1y - this.l0y).nor();
        this.normal =new Vector2 (-v.y,v.x);
        compute(limits);

	}
	int counter;
	float l0x,l0y,l1x,l1y;
	private Rectangle r;
	private int radiantcounter;
	private int rcounter;
	private Iterator<Grain> radianceIterator;
	private Grain radiantPoint;
	private int k;
	private int totalHeatGrains;
	public void compute(ArrayList<Vector2> limits){

		ArrayList<cutElement> cutElements = new ArrayList<cutElement>();
        this.rayCastCallBack.setElements(cutElements,null,-1,true);

		Vector2 limit1 = limits.get(0);
		Vector2 limit2 = limits.get(1);
        this.rayCastCallBack.setDirection(true);
        World.physicsWorld.rayCast(this.rayCastCallBack,limit1,limit2);
        this.rayCastCallBack.setDirection(false);
        World.physicsWorld.rayCast(this.rayCastCallBack,limit2,limit1);
		//	Log.e("parabol", ""+cutElements.size());
		World.result res = computeElements(cutElements);
		boolean test = true;
		for(int i=0;i<res.cuts.size();i++){
			ArrayList<Cut> entityCuts = res.cuts.get(i);
			DCGameEntity entity = res.entities.get(i);
			if(!entity.test(entityCuts)){test = false;break;}
		}

		if(test){

			for(int i=0;i<res.cuts.size();i++){
				ArrayList<Cut> entityCuts = res.cuts.get(i);
				DCGameEntity entity = res.entities.get(i);
				//if(counter>0)

				entity.applyCuts(entityCuts);

			}
		}
		else if(Math.abs(this.counter)<10) {

			if(this.counter ==0) this.counter++;
			else if(this.counter >=1) this.counter *=-1;
			else if(this.counter <=-1){
                this.counter *=-1;
                this.counter++;}
			float dx = this.counter *0.5f/32f* this.normal.x;
			float dy = this.counter *0.5f/32f* this.normal.y;
			limits.get(0).set(this.l0x +dx, this.l0y +dy);
			limits.get(1).set(this.l1x +dx, this.l1y +dy);
			//Log.e("pair repeat", "repeat"+counter);
            this.compute(limits);
		}
	}

	public ArrayList<Cut> compute(Vector2 limit1, Vector2 limit2, DCGameEntity specific, int ID, boolean convert){
		ArrayList<cutElement> intervals = new ArrayList<cutElement>();
        this.rayCastCallBack.setElements(intervals,specific,ID,convert);
        this.rayCastCallBack.setDirection(true);
        World.physicsWorld.rayCast(this.rayCastCallBack,limit1,limit2);
        this.rayCastCallBack.setDirection(false);
        World.physicsWorld.rayCast(this.rayCastCallBack,limit2,limit1);

		World.result res = computeElements(intervals);
ArrayList<Cut> cuts = new ArrayList<Cut>();
			for(ArrayList<Cut> c:res.cuts)cuts.addAll(c);
		 return cuts;

	}
	public void compute(ArrayList<Vector2> limits,DCGameEntity specific, int ID){

		ArrayList<cutElement> cutElements = new ArrayList<cutElement>();
        this.rayCastCallBack.setElements(cutElements,specific,ID,true);

		Vector2 limit1 = limits.get(0);
		Vector2 limit2 = limits.get(1);
        this.rayCastCallBack.setDirection(true);
        World.physicsWorld.rayCast(this.rayCastCallBack,limit1,limit2);
        this.rayCastCallBack.setDirection(false);
        World.physicsWorld.rayCast(this.rayCastCallBack,limit2,limit1);

		World.result res = computeElements(cutElements);
		boolean test = true;
		for(int i=0;i<res.cuts.size();i++){
			ArrayList<Cut> entityCuts = res.cuts.get(i);
			DCGameEntity entity = res.entities.get(i);
			if(!entity.test(entityCuts)){test = false;break;}
		}
		if(test){

			for(int i=0;i<res.cuts.size();i++){
				ArrayList<Cut> entityCuts = res.cuts.get(i);
				DCGameEntity entity = res.entities.get(i);
				entity.applyCuts(entityCuts);
			}
		}
		else {
			if(this.counter ==0) this.counter++;
			if(this.counter >=1) this.counter *=-1;
			if(this.counter <=-1){
                this.counter *=-1;
                this.counter++;}
			limits.get(0).set(this.l0x + this.counter *0.5f, this.l0y);
			limits.get(1).set(this.l1x + this.counter *0.5f, this.l1y);
            this.compute(limits);
		}
	}




	public Cut getComputeLimited(ArrayList<Vector2> limits,DCGameEntity specific, int ID, Vector2 center){

		ArrayList<cutElement> cutElements = new ArrayList<cutElement>();
        this.rayCastCallBack.setElements(cutElements,specific,ID,true);

		Vector2 limit1 = limits.get(0);
		Vector2 limit2 = limits.get(1);
        this.rayCastCallBack.setDirection(true);
        World.physicsWorld.rayCast(this.rayCastCallBack,limit1,limit2);
        this.rayCastCallBack.setDirection(false);
        World.physicsWorld.rayCast(this.rayCastCallBack,limit2,limit1);

		World.result res = computeElements(cutElements);
		//Log.e("pair begin","begin");
		if(res.cuts.size()==0)return null;
//Log.e("pair middle","has cuts");

		Iterator<Cut> iterator = res.cuts.get(0).iterator();
		boolean ok = false;
		while(iterator.hasNext()){
			Cut cut = iterator.next();

			if(Utils.vectorEquivalence2(cut.getP2(), center)
					||Utils.vectorEquivalence2(cut.getP1(), center)
					||Utils.PointOnLineSegment(cut.getP1(), cut.getP2(), center, 1f)){
				ok = true;
				//Log.e("pair valid", cut+"");
				ArrayList<Cut> cuts = new ArrayList<Cut>();
				cuts.add(cut);

				if(specific.test(cuts)){
					//Log.e("pair tested", cut+"");
					return cut;
				}}
		}
		if(!ok)return null;

		//Log.e("pair repeat", "repeat"+counter);
		if(Math.abs(this.counter)<10) {
			if(this.counter ==0) this.counter++;
			else if(this.counter >=1) this.counter *=-1;
			else if(this.counter <=-1){
                this.counter *=-1;
                this.counter++;}
			float dx = this.normal.x* this.counter *0.5f/32f;
			float dy = this.normal.y* this.counter *0.5f/32f;
			limits.get(0).set(this.l0x +dx, this.l0y +dy);
			limits.get(1).set(this.l1x +dx, this.l1y +dy);
			Vector2 v = new Vector2();
			v.set(this.rcenter.x+dx, this.rcenter.y+dy);
			center  = specific.getBody().getLocalPoint(v).cpy().mul(32f);


			return this.getComputeLimited(limits,specific,ID,center);
		}










		return null;
	}

public ArrayList<cutElement>  getElements(Vector2 limit1, Vector2 limit2,DCGameEntity specific, int ID){

	ArrayList<cutElement> cutElements = new ArrayList<cutElement>();
    this.rayCastCallBack.setElements(cutElements,specific,ID,false);

    this.rayCastCallBack.setDirection(true);
    World.physicsWorld.rayCast(this.rayCastCallBack,limit1,limit2);
    this.rayCastCallBack.setDirection(false);
    World.physicsWorld.rayCast(this.rayCastCallBack,limit2,limit1);
	return cutElements;
}

	public ArrayList<cutElement>  getElementsMinus(Vector2 limit1, Vector2 limit2,DCGameEntity specific, int ID){

		ArrayList<cutElement> cutElements = new ArrayList<cutElement>();
		this.rayCastCallBack.setElements(cutElements,specific,ID,false);
		rayCastCallBack.specificInverted = true;
		this.rayCastCallBack.setDirection(true);
		World.physicsWorld.rayCast(this.rayCastCallBack,limit1,limit2);
		this.rayCastCallBack.setDirection(false);
		World.physicsWorld.rayCast(this.rayCastCallBack,limit2,limit1);
		return cutElements;
	}



	public void computeLimited(ArrayList<Vector2> limits,DCGameEntity specific, int ID, Vector2 center){

		ArrayList<cutElement> cutElements = new ArrayList<cutElement>();
        this.rayCastCallBack.setElements(cutElements,specific,ID,true);

		Vector2 limit1 = limits.get(0);
		Vector2 limit2 = limits.get(1);
        this.rayCastCallBack.setDirection(true);
        World.physicsWorld.rayCast(this.rayCastCallBack,limit1,limit2);
        this.rayCastCallBack.setDirection(false);
        World.physicsWorld.rayCast(this.rayCastCallBack,limit2,limit1);

		World.result res = computeElements(cutElements);
		//Log.e("limited", ""+cutElements.size());

		if(res.cuts.size()==0)return;
		Iterator<Cut> iterator = res.cuts.get(0).iterator();
		while(iterator.hasNext()){

			Cut cut = iterator.next();
			if(!Utils.PointOnLineSegment(cut.getP1(), cut.getP2(), center, 0.01f))iterator.remove();

		}
		boolean test = true;
		DCGameEntity entity = res.entities.get(0);
		if(!entity.test(res.cuts.get(0))){test = false;}
		if(test)entity.applyCuts(res.cuts.get(0));


		else {
			if(this.counter ==0) this.counter++;
			if(this.counter >=1) this.counter *=-1;
			if(this.counter <=-1){
                this.counter *=-1;
                this.counter++;}
			limits.get(0).set(this.l0x + this.counter *0.5f/32f, this.l0y);
			limits.get(1).set(this.l1x + this.counter *0.5f/32f, this.l1y);
			//center.set(this.center.x+counter*0.5f,this.center.y);
            this.computeLimited(limits,specific,ID,center);
		}
	}



	public World.result computeElements(ArrayList<cutElement> cutElements ){

		UnionFind unionFinder = new UnionFind(cutElements.size());

		for(int i=0;i<cutElements.size();i++){
			for(int j=i+1;j<cutElements.size();j++){

				if(cutElements.get(i).isCohesive(cutElements.get(j))){

					unionFinder.union(i, j);

				}


			}
		}

		unionFinder.compute();

		Iterator<HashSet<Integer>> iterator=	unionFinder.myDict.values().iterator();
		ArrayList<DCGameEntity> cutEntities = new ArrayList<DCGameEntity>();
		ArrayList<ArrayList<Cut>> cuts = new ArrayList<ArrayList<Cut>>();
		while(iterator.hasNext()){

			ArrayList<Cut> entityCuts = new ArrayList<Cut>();
			HashSet<Integer> set = iterator.next();
			ArrayList<cutElement> list = new ArrayList<cutElement>();
			for(Integer i:set)list.add(cutElements.get(i));
			Collections.sort(list);
			//	Log.e("samantha", Arrays.toString(list.toArray()));

			Body body = list.get(0).body;

			DCGameEntity entity = (DCGameEntity) body.getUserData();

//ADDING THE EDGES OF THE CUT
cutElement e = new cutElement(list.get(0).fixture,new Vector2(l0x,l0y),0,false);
e.TYPE = cutElement.type.END;
list.add(0,e);
e = new cutElement(list.get(0).fixture,new Vector2(l1x,l1y),0,false);
e.TYPE = cutElement.type.END;
list.add(e);


			ArrayList<Slice> slices = new ArrayList<Slice>();

			for(int i=1;i<list.size();i++){
				cutElement current = list.get(i);
				cutElement previous = list.get(i-1);
				if(!current.isEorS() && previous.isEorS())
					slices.add(new Slice(previous, current,current.fixture));
				else if(!current.isEorS() && previous.isEnd()) {
					slices.add(new Slice(previous, current, current.fixture));

				}
				else if (current.isEnd() && previous.isEorS()) {
					slices.add(new Slice(previous, current, current.fixture));

				}

			}

			ArrayList<Integer> indices = new ArrayList<Integer>();
			indices.add(0);
			for(int i=0;i<slices.size()-1;i++){
				Slice current = slices.get(i);
				Slice next = slices.get(i+1);
				if(!Utils.vectorEquivalence(current.getSecond().getPoint(), next.getFirst().getPoint())){
					indices.add(i);
					indices.add(i+1);
				}
			}
			indices.add(slices.size()-1);
			ArrayList<ArrayList<Slice>> groups = new ArrayList<ArrayList<Slice>>();
			for(int i=0;i<indices.size();i+=2)
				groups.add(new ArrayList<Slice>(slices.subList(indices.get(i), indices.get(i+1)+1)));



			for(ArrayList<Slice> group:groups){
				if(group.size()>=1){
					cutElement first = group.get(0).getFirst();
					cutElement last = group.get(group.size()-1).getSecond();
					Cut cut = new Cut(first.getPoint(), last.getPoint(),first.fixtureId);
					cut.entity = entity;
					entityCuts.add(cut);
					for(Slice s:group)if(s.getFirst().isEnd()||s.getSecond().isEnd())
					cut.setHalf(true);
				}
			}

			if(!cutEntities.contains(entity)){
				cutEntities.add(entity);
				cuts.add(entityCuts);
			} else {
				int x = cutEntities.indexOf(entity);
				cuts.get(x).addAll(entityCuts);
			}


		}




		return new World.result(cuts,cutEntities);
	}

    public void removePair(DCGameEntity entity1, DCGameEntity entity2) {
		Iterator<DCGameEntityPair> it = World.dcpList.iterator();
		while(it.hasNext()){

			DCGameEntityPair pair = it.next();
			if(pair.equal(entity1,entity2))it.remove();

		}


    }

	public static void checkForPending(Fixture fixtureA, Fixture fixtureB) {
		Iterator<FixturePair> iterator = filters.iterator();
	while(iterator.hasNext()){

		FixturePair filter = iterator.next();
		boolean remove = false;
		if(filter.pendingContact.getFixtureA()==fixtureA&&filter.pendingContact.getFixtureB()==fixtureB)remove = true;
		if(filter.pendingContact.getFixtureA()==fixtureB&&filter.pendingContact.getFixtureB()==fixtureA)remove = true;
		if(remove)iterator.remove();
	}
	}

	public static void removeFromDpList(DCGameEntity entity1, DCGameEntity entity2) {
		Iterator<DCGameEntityPair> it = dcpList.iterator();
		while(it.hasNext()){

			DCGameEntityPair pair = it.next();
			if(pair.equal(entity1,entity2))it.remove();
		}
	}


	class result {
		ArrayList<ArrayList<Cut>> cuts;
		ArrayList<DCGameEntity> entities;
		result(ArrayList<ArrayList<Cut>> cuts, 	ArrayList<DCGameEntity> entities){
			this.cuts = cuts;
			this.entities = entities;
		}
	}

/*
 * 		ArrayList<Vector2> cuttingPath = new ArrayList<Vector2>();
			int a = first.RootPointOrder;
			int b = second.RootPointOrder;
				for(int j=a+1;j<b+1;j++)cuttingPath.add(body.getLocalPoint(limits.get(j)).cpy().mul(32f));


		if(entityCuts.size()==0){
			Cut cut = new Cut(first.point,second.point,first.fixtureId,cuttingPath);
			entityCuts.add(cut);
		} else {
			Cut previous = entityCuts.get(entityCuts.size()-1);
			if(previous.getP2().dst(first.point)<0.0001f)

		}

 */

public static void registerFilter( Fixture fixture1,  Fixture fixture2){
filters.add(new FixturePair(fixture1,fixture2));
}
public void removeFilter(Fixture fixture1,  Fixture fixture2){
	Iterator<FixturePair> iterator = filters.iterator();
	while(iterator.hasNext()){

		FixturePair pair = iterator.next();
		if(pair.equivalent(fixture1,fixture2))iterator.remove();
	}
}
	public World(PhysicsWorld world){
        physicsWorld = world;

        this.gameGroups = new Entityx();
        attachChild(this.gameGroups);
        this.entityIDS = new AtomicInteger();
        World.pendingcWrappers = new   ArrayList<ContactWrapper> () ;
        World.reserve = new   ArrayList<ContactWrapper> () ;
        World.fires = new ArrayList<Fire>();
        nearFixtures = new ArrayList<Fixture>();
        sparkReservoir = new HashSet<Spark>();
        radianceReservoir = new ArrayList<Grain>();
        this.innerHeatReservoir = new HashSet<Grain>();
        dcpList = new ArrayList<DCGameEntityPair>();
		filters = new ArrayList<FixturePair>();



	}



	public void attach(IEntity entity){
        attachChild(entity);
        sortChildren();
	}

	public void addGroup(GameGroup group) {
        this.gameGroups.attachChild(group);
        this.gameGroups.sortChildren();
	}
	public GameGroup getGroupByIndex(int index){
		return (GameGroup) this.gameGroups.getChildByIndex(index);
	}
	public int getNumberOfGroups(){
		return this.gameGroups.getChildCount();
	}
	public static int step;




	public void step(){



		for(Fire fire: World.fires){
			//	for(Flame flame:fire.getFlames())flame.getFire().emitter;
		}
		if(false)
		if(gameGroups.getChildCount()>1) {
if(World.step %60==0)
			for(int i=0;i<160;i++) {

                compute(new Vector2(i*5 / 32f, 0), new Vector2(i*5/32f, 480 / 32f), getGroupByIndex(1).getEntityByIndex(0), -1,true);
			}
		}


		for(Fire fire: World.fires)fire.step();

		//CHECK HERE
		if(true)
			if(World.step %240==0){

                innerHeatTransfer();
				//AND UPDATE
				for(Grain g: this.innerHeatReservoir){
					g.updateGrain();
				}
			} else {
				int n = 0;
				Iterator<Grain> grainIterator = this.innerHeatReservoir.iterator();
				while(grainIterator.hasNext()){

					Grain point = grainIterator.next();
					point.getBlock().innerHeatTransfer(point);
					n++;
					grainIterator.remove();
					if(n> this.totalHeatGrains /239)break;
				}
			}




		if(false)
			if(World.step %600==0){
                radiantHeatTransfer();
                this.radianceIterator = this.radianceReservoir.iterator();
                this.radiantcounter = 0;
			} else {
				if(this.radianceIterator !=null){
					int n = 0;
					while(n<=(int)Math.ceil(totalRadiantPoints *360/599f)){

						if(this.radiantcounter ==0&& this.radianceIterator.hasNext()) this.radiantPoint = this.radianceIterator.next();
						if(this.radiantPoint !=null){

							Body pointBody = this.radiantPoint.getBlock().fixtures.get(0).getBody();
							Vector2 worldCenter = pointBody.getWorldPoint(this.radiantPoint.position.cpy().mul(1/32f));

							while(n<=(int)Math.ceil(totalRadiantPoints *360f/599)&& this.radiantcounter <360){
                                rayCastCallBack2.temperature = this.radiantPoint.temperature;
                                rayCastCallBack2.center = worldCenter;
								applyRadiance(worldCenter, this.radiantcounter);
                                this.radiantcounter++;
								n++;
                                this.k++;
								//Log.e("radiance"+onStep,radianceReservoir.indexOf(radiantPoint) +"counter"+radiantcounter);
							}

							if(this.radiantcounter ==360){
                                this.radiantcounter = 0;
                                this.radiantPoint = null;
                                this.radianceIterator.remove();


							}
						}

						if(!this.radianceIterator.hasNext())break;
					}
				}
				//Log.e("radiancek"+onStep,""+(n<=(int)Math.ceil(this.totalRadiantPoints*360/599f))+(radianceIterator.hasNext())+""+k);
			}






		if(true)
			if(World.step %30==0){
				//FIND ALL FIRE SPARKS
				Iterator<Body> it = physicsWorld.getBodies();
				while(it.hasNext()){

					Body body=it.next();if(body!=null)if(!body.isActive())physicsWorld.destroyBody(body);}
                airConvectionFireTransfer();


			} else {

				int n = 0;
				Iterator<Spark> sparkIterator = this.sparkReservoir.iterator();
				while(sparkIterator.hasNext()){

					Spark spark = sparkIterator.next();

					n++;
					float x = spark.getX()+spark.getSystem().getX();
					float y = spark.getY()+spark.getSystem().getY();
					float w = 3;
					float h = 3;

					Vector2 v1,v2,v;

					//GameScene.plotter.drawPoint(new Vector2(x,y), Color.YELLOW, 1, 0);
					v = new Vector2(x, y).mul(1/32f);
					ArrayList<Fixture> fixturesOnFire =
                            getNearFixtures(v1 = new Vector2(x-w/2f, y-h/2f).mul(1/32f),v2 = new Vector2(x+w/2f, y+h/2f).mul(1/32f));

					if(fixturesOnFire.size() ==0)continue;



					float heat = spark.getTemperature()/10f;


					ArrayList<ArrayList<Block>> bigList = new ArrayList<ArrayList<Block>>();
					ArrayList<DCGameEntity> entityList = new ArrayList<DCGameEntity>();
					for(Fixture fix:fixturesOnFire){
						DCGameEntity entity = (DCGameEntity) fix.getBody().getUserData();
						if(!entityList.contains(entity)){entityList.add(entity);bigList.add(new ArrayList<Block>());}
						int z = entityList.indexOf(entity);
						Block block = entity.getBlock((Integer) fix.getUserData());
						if(!bigList.get(z).contains(block))
							bigList.get(z).add(block);
					}
					for(DCGameEntity entity:entityList){
						int z = entityList.indexOf(entity);
						entity.applyHeatRadiance(v, heat,bigList.get(z));
					}

					sparkIterator.remove();
					if(n> this.totalSparks /29)break;

				}

			}



		for(ContactWrapper wrapper: World.reserve){
            World.pendingcWrappers.add(wrapper);

		}

        World.reserve.clear();


		for(int i = 0; i< getNumberOfGroups(); i++){
            getGroupByIndex(i).updateGameGroup();
		}

	//	Log.e("cWrappers" + onStep, Arrays.toString(pendingcWrappers.toArray()));
		ArrayList<PhysicalConnection> connections = new ArrayList<PhysicalConnection>();


		for(ContactWrapper wrapper: World.pendingcWrappers){
			if(!wrapper.continuous){
				connections.add(new PhysicalConnection(PhysicalConnection.Connection_Type.IMPACTAONB, wrapper));
			}
			else
				connections.add(new PhysicalConnection(PhysicalConnection.Connection_Type.TOUCHINGTOGETHER, wrapper));
		}

		Iterator<PhysicalConnection> iter = connections.iterator();
		while(iter.hasNext()){

			PhysicalConnection connection = iter.next();
			if(connection.type ==PhysicalConnection.Connection_Type.IMPACTAONB){
				this.computeImpact(connection);

			}
		}
		for(PhysicalConnection connection:connections){


			if(false)
				if(World.step %60==0)
					if(connection.type==PhysicalConnection.Connection_Type.TOUCHINGTOGETHER){
                        externalHeatTransfer(connection);

					}

		}


		//Log.e("connections" + onStep, Arrays.toString(connections.toArray()));


        World.step++;


	}

	public static void transferHeat(float m1, float m2,float d1, float d2, Grain g1, Grain g2,float heat_conductivity1,float heat_conductivity2, float specific_heat1,float specific_heat2){

		float K = g1.temperature<g2.temperature ?heat_conductivity1:heat_conductivity2;
		float d = Math.min(d2, d1);
		float crossSection = 16 * d;
		float Q = K * crossSection/16f * (g1.temperature-g2.temperature);
		float deltaT1 = -Q/(m1 * specific_heat1);
		float deltaT2 = Q/(m2 * specific_heat2);

		g1.applyHeat(deltaT1);
		g2.applyHeat(deltaT2);


	}


	private void externalHeatTransfer(PhysicalConnection connection) {

		ContactWrapper wrapper = connection.cWrapper;
		Body body1 = connection.cWrapper.entityA.getBody();
		Body body2 = connection.cWrapper.entityB.getBody();
		if(body1.getType()==BodyDef.BodyType.StaticBody)return;
		if(body2.getType()==BodyDef.BodyType.StaticBody)return;

		Vector2[] points = wrapper.points;

		if(wrapper.numberOfContactPoints>=1){
			boolean UniPoint = wrapper.numberOfContactPoints==1;
			Vector2 first = points[0];
			Vector2 second = null;
			if(!UniPoint)
				second = points[1];


			DCGameEntity entity1 = (DCGameEntity) body1.getUserData();
			DCGameEntity entity2 = (DCGameEntity) body2.getUserData();

			Block block1 = wrapper.block1;
			Block block2 = wrapper.block2;
			if(!block1.hasGrid||!block2.hasGrid)return;

			Vector2 proj1A = body1.getLocalPoint(first).cpy().mul(32);
			Vector2 proj1B = null;
			if(!UniPoint)
				proj1B = body1.getLocalPoint(second).cpy().mul(32);


			Vector2 proj2A = body2.getLocalPoint(first).cpy().mul(32);
			Vector2 proj2B = null;
			if(!UniPoint)
				proj2B = body2.getLocalPoint(second).cpy().mul(32);

			ArrayList<Grain> grains1 = new ArrayList<Grain>();
			ArrayList<Grain> grains2 = new ArrayList<Grain>();
			for(Grain g:block1.grid.borderGrains){
				if(!UniPoint){
					if(Utils.shortestDistance(proj1A.x, proj1A.y, proj1B.x, proj1B.y, g.position.x, g.position.y)<32f)
						grains1.add(g);
				}
				else if(g.distance(proj1A)<32f) grains1.add(g);
			}
			for(Grain g:block2.grid.borderGrains){
				if(!UniPoint){
					if(Utils.shortestDistance(proj2A.x, proj2A.y, proj2B.x, proj2B.y, g.position.x, g.position.y)<32f)
						grains2.add(g);
				}
				else if(g.distance(proj2A)<32f)grains2.add(g);
			}






			float mass1 = entity1.getBody().getMass()/block1.grid.allGrains.size();
			float mass2 = entity2.getBody().getMass()/block2.grid.allGrains.size();

			ArrayList<Grain> smaller = grains2.size()<grains1.size() ?grains2:grains1;
			ArrayList<Grain> bigger = grains2.size()<grains1.size() ?grains1:grains2;


			for(int i=0;i<smaller.size();i++){
				Grain g1 = smaller.get(i);
				for(int j=0;j<bigger.size();j++){
					Grain g2 = bigger.get(j);
//Log.e("transfer",g1.temperature+"/"+g2.temperature);
					transferHeat(1, 1, 1, 1, g1, g2, 6f, 6f, 10f, 10f);
				}
			}




		}

	}



	private void computeImpact(PhysicalConnection connection) {

		ContactWrapper contact = connection.cWrapper;

		Vector2[] points = contact.points;


		if(contact.numberOfContactPoints>0){

			Body body1 = contact.fixtureA.getBody();
			Body body2 = contact.fixtureB.getBody();
			DCGameEntity entity1 = (DCGameEntity) body1.getUserData();
			DCGameEntity entity2 = (DCGameEntity) body2.getUserData();
			if(!entity1.alive||!entity2.alive){
				return;}

			if(entity1.stop||entity2.stop){
				return;}

float impulse = entity1.getKineticEnergy()+entity2.getKineticEnergy();

			if(impulse>10000)
			{

				Vector2 point;
				if(contact.numberOfContactPoints==1)
					point = points[0];
				else {
					point = points[0].add(points[1]).mul(0.5f);
				}


				//entity1.applyImpact(contact.fixtureA, point, impulse/20);
				//entity2.applyImpact(contact.fixtureB, point, impulse/20);

			}
		}

	}
	static ArrayList<DCGameEntityPair> dcpList;
	public static void register(DCGameEntityPair pair) {
        World.dcpList.add(pair);

	}
public DCGameEntity getLastDPOccurence(DCGameEntity penetrator){
		for(int i=dcpList.size()-1;i>=0;i--){
			 DCGameEntityPair pair = dcpList.get(i);
			DCGameEntity f = pair.contains(penetrator);
			if(f!=null){
				return f;
			}
		}
		return null;
}

	public static boolean existInDpList(DCGameEntity a, DCGameEntity b){
		for(DCGameEntityPair pair: World.dcpList){
			if(pair.equal(a,b))return true;
		}
		return false;
	}
	static ArrayList<ContactWrapper> pendingcWrappers;
	static ArrayList<ContactWrapper> reserve;
	public static void register(Contact contact, ContactImpulse impulse) {

		ContactWrapper newcWrapper = new ContactWrapper(contact, World.step, impulse);
		for(ContactWrapper cWrapper: World.pendingcWrappers){
			if(newcWrapper.equivalent(cWrapper)){
				cWrapper.time= World.step;
				cWrapper.continuous = true;
				cWrapper.numberOfContactPoints = contact.getWorldManifold().getNumberOfContactPoints();
				cWrapper.setPoints(contact.getWorldManifold().getPoints());

				return;

			}
		}

        World.reserve.add(newcWrapper);

	}
	public Fire getFire(Block block){
		for(Fire fire: World.fires){
			if(fire.block==block)return fire;
		}
		return null;
	}

	public  void fireSignal(Block fireBlock, DCGameEntity entity) {
		for(Fire fire: World.fires)if(fire.block==fireBlock){
			fire.update(false);
			return;}
		Fire fire = new Fire(fireBlock, entity);
        World.fires.add(fire);


	}
	public static ArrayList<Fire> fires;







	public void removeFire(Fire fire){

        World.fires.remove(fire);
		for(Flame f:fire.getFlames())f.getFire().detachSelf();
	}
	HashSet<Spark> sparkReservoir;
	private int totalSparks;
	private void airConvectionFireTransfer(){

		for(Fire fire: World.fires){
			for(Flame flame:fire.getFlames()){
				if(flame.visible){
					ArrayList<Spark> sparks = flame.getFire().getParticles();
					for(Spark spark:sparks){
                        this.sparkReservoir.add(spark);

					}
				}
			}

		}
        totalSparks = this.sparkReservoir.size();

	}
	ArrayList<Grain> radianceReservoir;
	private int totalRadiantPoints;

	private void radiantHeatTransfer(){
		for(int i = 0; i< this.gameGroups.getChildCount(); i++){
			GameGroup group = getGroupByIndex(i);
			for(int j=0;j<group.getEntityCount();j++){
				DCGameEntity entity = group.getEntityByIndex(j);
				for(Block b:entity.blocks){
                    this.radianceReservoir.addAll(b.grid.borderGrains);
				}
			}
		}
        totalRadiantPoints = this.radianceReservoir.size();


	}







	HashSet<Grain> innerHeatReservoir;
	private void innerHeatTransfer(){
		for(int i = 0; i< this.gameGroups.getChildCount(); i++){
			GameGroup group = getGroupByIndex(i);
			for(int j=0;j<group.getEntityCount();j++){
				DCGameEntity entity = group.getEntityByIndex(j);
				if(entity.getBody().getType()==BodyDef.BodyType.DynamicBody){
					for(Block b:entity.blocks){

                        this.innerHeatReservoir.addAll(b.grid.allGrains);
					}
				}
			}
		}
        this.totalHeatGrains = this.innerHeatReservoir.size();

	}




public static void unregisterContact(Contact contact){

}



}
