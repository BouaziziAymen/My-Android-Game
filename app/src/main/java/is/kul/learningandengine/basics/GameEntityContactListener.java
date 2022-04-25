package is.kul.learningandengine.basics;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.factory.BasicFactory;
import is.kul.learningandengine.scene.Cut;
import is.kul.learningandengine.scene.GameScene;
import is.kul.learningandengine.scene.World;
import is.kul.learningandengine.scene.cutElement;

import static is.kul.learningandengine.scene.World.register;


public class GameEntityContactListener implements ContactListener {
	DCGameEntity parent;
	public GameEntityContactListener(DCGameEntity entity){
		parent = entity;
	}

public static Vector2 getContactCentralPoint(Contact contact){
	int N = contact.getWorldManifold().getNumberOfContactPoints();
	Vector2 p0 = null;
	Vector2[] points = contact.getWorldManifold().getPoints();
	if(N==1)p0 = points[0].cpy();
	else if(N==2)p0 = points[0].cpy().add(points[1]).mul(0.5f);
	return p0;
}

float getEdges(DCGameEntity entity,Vector2 p0, Vector2 u) {
	float fmax = -Float.MAX_VALUE, fmin = Float.MAX_VALUE;
	Body body;

	for (Block b : entity.blocks)
		for (Vector2 p : b.VERTICES) {
			float f = (p0.cpy().sub(entity.getBody().getWorldPoint(p.cpy().mul(1 / 32F)))).dot(u);
			if (f < fmin) fmin = f;
			if (f > fmax) fmax = f;
		}

	return Math.abs(fmax-fmin);
}

	@Override
	public void beginContact(Contact contact) {
		if(parent.alive)
World.register(contact,null);
if(true)return;
		//Log.e("contact","contact"+contact.isEnabled());
//GET KINETIC ENERGY AND CHECK

		Body bodyA = contact.getFixtureA().getBody();
		final Body bodyB = contact.getFixtureB().getBody();
		DCGameEntity entity1 =(DCGameEntity) bodyA.getUserData();
		final DCGameEntity entity2 =(DCGameEntity) bodyB.getUserData();

        if(World.existInDpList(entity1,entity2))return;
		int blockID2 =(Integer) contact.getFixtureB().getUserData();
		int blockID1 =(Integer) contact.getFixtureA().getUserData();
		Block block1 = entity1.getBlock(blockID1);
		Block block2 = entity2.getBlock(blockID2);
		if(block1==null||block2==null)return;
		Vector2 p0 = getContactCentralPoint(contact);




		 DCGameEntity penetrator,penetrated;
		if(bodyA.getType()!=BodyType.DynamicBody){
			if(block2.getPROPERTIES().getHardness()<block1.getPROPERTIES().getHardness())return;
			penetrator=entity2;penetrated=entity1;}
		else if(bodyB.getType()!=BodyType.DynamicBody){penetrator=entity1;penetrated=entity2;}
		else {
			if(bodyA.getLinearVelocity().len2() > bodyB.getLinearVelocity().len2()) {
				if(block1.getPROPERTIES().getHardness()<block2.getPROPERTIES().getHardness())return;
				penetrator=entity1;penetrated=entity2;}
			else {
				if(block2.getPROPERTIES().getHardness()<block1.getPROPERTIES().getHardness())return;
				penetrator=entity2;penetrated=entity1;}
		}
		final Body penetratorBody = penetrator.getBody();
		final Body penetratedBody = penetrated.getBody();







		if(Math.abs(block1.getPROPERTIES().getHardness()-block2.getPROPERTIES().getHardness())<0.05f){

			Log.e("cat", "---------------------------------------------------");

			Log.e("cat", "collision cat" + contact.getFixtureA().getFilterData().categoryBits);
			Log.e("cat", "collision mask" + contact.getFixtureA().getFilterData().maskBits);

			Log.e("cat", "collision cat" + contact.getFixtureB().getFilterData().categoryBits);
			Log.e("cat", "collision mask" + contact.getFixtureB().getFilterData().maskBits);


			DCGameEntity e = penetrator.dive;
			if(e!=null&&e!=penetrated) {

				BasicFactory.getInstance().updateEntity(e, penetrator, new Vector2());


			}
			return;
		}




		Vector2 displacementVectorInverted = penetrator.previousBodyPosition.cpy().sub(penetratorBody.getPosition());



		Vector2 fm1 = penetratorBody.getLinearVelocityFromWorldPoint(p0).mul( penetrator.getMass()*60);
		Vector2 fm2 = penetratedBody.getLinearVelocityFromWorldPoint(p0).mul( penetrated.getMass()*60);

		Vector2 Fm = fm1.sub(fm2);

		float value = Fm.len();
		//GameScene.plotter.drawPoint(p0.cpy().mul(32),Color.YELLOW,1,0);

		if(penetratorBody.getLinearVelocity().dot(penetratorBody.getLinearVelocity())<10)return;


        Log.e("STEP","begin"+value);
		if(value<10)return;

		//GameScene.plotter.drawPoint(p0.cpy().mul(32),Color.RED,1,0);

		Vector2 u = Fm.cpy().nor();
		Vector2 tangent = new Vector2(-u.y, u.x);
//ADDED
float d1,d2;
		float coverage = Math.min(d1=this.getEdges(penetrated,p0,u),d2=this.getEdges(penetrator,p0,u));

		float d = Math.max(0.001f,coverage/8);

Log.e("coverage",d1+":"+d2+"/"+d);

		ArrayList<ArrayList<Interval>> intervals2 = new ArrayList<ArrayList<Interval>>();
		ArrayList<ArrayList<Interval>> intervals1 = new ArrayList<ArrayList<Interval>>();
		int j = 0;
		boolean weld = false;
		boolean b = false;

		while(true){
			Vector2 pmi = p0.cpy().sub(u.cpy().mul((j+1) * d));
			Vector2 upperPointWorld = pmi.cpy().add(tangent.cpy().mul(10f));
			Vector2 lowerPointWorld = pmi.cpy().add(tangent.cpy().mul(-10f));
			ArrayList<Cut> allCuts = GameScene.world.compute(upperPointWorld, lowerPointWorld, null, -1,false);
			ArrayList<Cut> cuts1 = new ArrayList<Cut>();
			ArrayList<Cut> cuts2 = new ArrayList<Cut>();
			for(Cut cut: allCuts)if(penetrator.hasBrother(cut.getEntity()))cuts1.add(cut);else cuts2.add(cut);
			float s = j/50f;
			Color c1 = new Color(0,s,0);
			Color c2 = new Color(1-s,0,0);
		//	for(Cut cut:cuts1)GameScene.plotter.drawLine2(cut.getP1().cpy().mul(32).add(0,240),cut.getP2().cpy().mul(32).add(0,240),Color.RED,1);
		//	for(Cut cut:cuts2)GameScene.plotter.drawLine2(cut.getP1().cpy().mul(32).add(0,240),cut.getP2().cpy().mul(32).add(0,240),Color.GREEN,1);

			if(cuts1.size()==0) {break;}
			ArrayList<Interval> list = new ArrayList<Interval>();
			computeCuts(list,cuts1,tangent, pmi);
			intervals1.add(list);
			list = new ArrayList<Interval>();
			if (cuts2.size() > 0) {
				computeCuts(list,cuts2,tangent, pmi);
			}

			intervals2.add(0,list);
			if(j%1000==0&&j>0)break;

		j++;
		}

		int i = 0;
		Vector2 pi = p0.cpy();
		Vector2 pj = p0.cpy();

        DCGameEntity last = null;
		HashSet<DCGameEntity> set = new HashSet<DCGameEntity>();
		boolean validated=false;

		outer:
		while(true) {


			 pi.add(u.cpy().mul(d));
			pj.sub(u.cpy().mul(d));

			Vector2 upperPointWorld = pi.cpy().add(tangent.cpy().mul(10f));
			Vector2 lowerPointWorld = pi.cpy().add(tangent.cpy().mul(-10f));

			ArrayList<Cut> allCuts = GameScene.world.compute(upperPointWorld, lowerPointWorld, penetrated, -1,false);
			ArrayList<Cut> cuts = new ArrayList<Cut>();
			for(Cut cut: allCuts)if(cut.getEntity()==penetrated){

				cuts.add(cut);

			}

			if(cuts.size()==0){

                    contact.setEnabled(false);
penetrator.dive = penetrated;
                    HashSet<DCGameEntity> brothers = penetrator.getBrothers();
                    for (DCGameEntity p : brothers) {
                        DCGameEntityPair pair = new DCGameEntityPair(penetrated, p);
                        register(pair);
                    }

                    Log.e("STEP", "go through" + i);
                    break outer;

			}


            ArrayList<Interval> list1 = new ArrayList<Interval>();
            if (cuts.size() > 0) {
                computeCuts(list1,cuts,tangent, pi);
            }
            intervals2.add(list1);





            int n = intervals1.size();
			int m = intervals2.size();


			int min = Math.min(n, m);
			int max = Math.max(n, m);
			int [] trail = new int[min];
			for(int counter=0;counter<min;counter++)trail[counter]=-counter;
			for(int counter=0;counter<min;counter++)trail[counter]+=i+j;
			int count = 0;
			for(int k=0;k<min;k++)if(trail[k]<max&&trail[k]>=0) count++;
			int[][] pairs = new int[2][count];
			count = 0;
			for(int k=0;k<min;k++) if(trail[k]<max&&trail[k]>=0)
			{pairs[0][count]=trail[k];pairs[1][count]=k;count++;}

			//Log.e("spartax","pairsA:"+Arrays.toString(pairs[0]));
			//Log.e("spartax","pairsB:"+Arrays.toString(pairs[1]));
			for(int k=0;k<pairs[0].length;k++){

				ArrayList<Interval> first = intervals2.get(pairs[0][k]);
				ArrayList<Interval> second = intervals1.get(pairs[1][k]);

				 result res = computeIntersection(first, second,d);


				if (value >= res.energy) {
					value -= res.energy;
					//Log.e("sparta","onStep energy reduced:"+res.energy);

				} else {

					if(i>2&&penetrated.testPoint(pi.x,pi.y)) {

						weld = true;

					}
					Log.e("STEP","dissipated at:"+i);
					break outer;
				}
			}
			if(i%1000==0&&i>0)break outer;

			i++;


		}
		//TEST UPPER PART
for(int c=0;c<intervals1.size();c++)
for(Interval interval:intervals1.get(c))interval.recycle();
for(int c=0;c<intervals2.size();c++)
for(Interval interval:intervals2.get(c))interval.recycle();
		//GameScene.plotter.drawText(p0.cpy().mul(32),flag+"/"+weld+"/"+i);

		if(penetrated!=null)
		if(weld) {

BasicFactory.getInstance().updateEntity(penetrated,penetrator,u.cpy().mul(i*d));


		} else if(i<=2){
DCGameEntity e = penetrator.dive;
if(e!=null&&e!=penetrated) {
	BasicFactory.getInstance().updateEntity(e, penetrator, new Vector2());

}
        }





	}



	@Override
	public void endContact(Contact contact) {

		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();
		DCGameEntity entity1 =(DCGameEntity) bodyA.getUserData();
		DCGameEntity entity2 =(DCGameEntity) bodyB.getUserData();
		if(World.existInDpList(entity1,entity2)) {
			World.removeFromDpList(entity1, entity2);
		}
		if(entity1!=null)if(entity1.dive==entity2)entity1.dive = null;
        if(entity2!=null)if(entity2.dive==entity1)entity2.dive = null;


World.unregisterContact(contact);



		}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();
		DCGameEntity entity1 =(DCGameEntity) bodyA.getUserData();
		DCGameEntity entity2 =(DCGameEntity) bodyB.getUserData();
if(World.existInDpList(entity1,entity2))contact.setEnabled(false);



	}


	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();
		DCGameEntity entity1 =(DCGameEntity) bodyA.getUserData();
		DCGameEntity entity2 =(DCGameEntity) bodyB.getUserData();
		if(parent.alive)
		if(entity1.getBody().getLinearVelocity().len()>0.1&&entity1.getBody().getType()== BodyType.DynamicBody
		|| entity2.getBody().getLinearVelocity().len()>0.1&&entity2.getBody().getType()== BodyType.DynamicBody);

		//	register(contact, impulse);


	}
	ArrayList<Interval>  compute( ArrayList<Interval> intervals){
		if(intervals.size()<=1)return intervals;
	ArrayList<Interval> base = new ArrayList<Interval>();
	ArrayList<Interval> result = new ArrayList<Interval>();

	base.add(intervals.get(0));
	result.add(intervals.get(0));
	for(int e=1;e<intervals.size();e++) {
		result.clear();
		for (Interval v : base) result.addAll(v.merge(intervals.get(e)));
		base.clear();
		base.addAll(result);
	}
	Iterator<Interval> iterator1 = result.iterator();
	while(iterator1.hasNext()){
		Interval f = iterator1.next();
		Iterator<Interval> iterator2 = result.iterator();
		while(iterator2.hasNext()){
			Interval s = iterator2.next();
			if(f!=s){
				if(s.isIncludedIn(f)){iterator1.remove();break;}
			}
		}
	}
return result;
}
	void computeCuts(ArrayList<Interval> list1,ArrayList<Cut> cuts,
					 Vector2 tangent, Vector2 pi){

	for(int k=0;k<cuts.size();k++) {
		Cut cut = cuts.get(k);
		Vector2 u1 = cut.getP1().cpy().sub(pi);
		Vector2 u2 = cut.getP2().cpy().sub(pi);
		float a1 = u1.dot(tangent);
		float a2 = u2.dot(tangent);
		float hardness = cut.getEntity().getBlock(cut.getId()).getPROPERTIES().getHardness();
		Interval i = IntervalPool.obtain(a1,a2,hardness);
        i.setEntity(cut.getEntity());

		list1.add(i);


	}

}
class result{

	boolean FirstIsPenetrator;
	boolean SecondIsPenetrator;
	boolean Valid;
	float energy;
	public String toString(){
		String s ="";
		if(this.FirstIsPenetrator)s="first-is-penetrator";else s="second-is-penetrator";
		return s+"/"+ this.energy;

	}
	ArrayList<Interval> intervals;
	HashSet<DCGameEntity> entities;
	result(ArrayList<Interval> intervals,boolean FirstIsPenetrator, boolean SecondIsPenetrator, float energy, HashSet<DCGameEntity> entities){
		this.FirstIsPenetrator = FirstIsPenetrator;
		this.SecondIsPenetrator = SecondIsPenetrator;
		this.energy = energy;
		this.intervals = intervals;
		this.entities = entities;
	}
	}
result computeIntersection(ArrayList<Interval> result1, ArrayList<Interval> result2, float d){
	boolean end = false;
	HashSet<Interval> efficace1 = new HashSet<Interval>();
	HashSet<Interval> efficace2 = new HashSet<Interval>();
	float totalIntersection = 0;
	boolean ok = false;
	HashSet<DCGameEntity> entities = new HashSet<DCGameEntity>();
	for(Interval f:result1){
		for(Interval s:result2){
			float iv = f.intersectionValue(s);
			if(iv!=0){
				if(!ok)ok = true;
				totalIntersection += iv;
				efficace1.add(s);
				efficace2.add(f);

if(Math.abs(f.hardness-s.hardness)>=0.1f)
				entities.add(f .getEntity());
			}

		}

	}


	if(!ok);//RETURN BlockA UNVALID RESULT
	float eff1 = 0;
	for(Interval x:efficace1)eff1+=x.end - x.start;
	float eff2 = 0;
	for(Interval x:efficace2)eff2+=x.end - x.start;
	float ratio1 = totalIntersection/eff1;
	float ratio2 = totalIntersection/eff2;
	boolean FirstIsPenetrator = false;
	boolean SecondIsPenetrator = false;
	ArrayList<Interval> rest = new ArrayList<Interval>();
	float energy = 0;
	if(ratio1<ratio2){
		FirstIsPenetrator = true;
		for(Interval ii:result2){
			for(Interval i:result1){

        ii.applyCut(i);



			}
			rest.addAll(ii.getChildren());
			float e = ii.getEnergy();
			energy += e;
			//Log.e("compte",(ii.end-ii.start)+"de:"+e);
		}
	} else {
		SecondIsPenetrator = true;

		for(Interval i:result1){
			for(Interval ii:result2){
				i.applyCut(ii);

			}
			rest.addAll(i.getChildren());
			float e = i.getEnergy()*10*d;
			energy += e;
			//Log.e("compte", (i.end-i.start)+"de:"+e);

		}
	}


GameEntityContactListener.result res  = new result(rest,FirstIsPenetrator,SecondIsPenetrator,energy,entities);

	return res;
}
}
