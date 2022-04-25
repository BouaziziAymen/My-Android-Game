package is.kul.learningandengine.polygonarithmetic;

import is.kul.learningandengine.helpers.UnionFind;
import is.kul.learningandengine.helpers.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import org.andengine.entity.Entity;

import com.badlogic.gdx.math.Vector2;

public class Cutter  {
	public ArrayList<Vector2> YANGS;
	public ArrayList<Vector2> YINS;
	public ArrayList<Compass> COMPASSES;
	public ArrayList<Twin> YANGTWINS;

	public ArrayList<Twin> YINTWINS;

	public ArrayList<ArrayList<Vector2>> DECOMPOSITION;

	
	float elapsed;
	boolean PROCESS = true;

	public void addPoint(Vector2 v) {

		DECOMPOSITION.get(DECOMPOSITION.size() - 1).add(v);
	}




	Compass isAtCompass(POLARITY polarity, DIRECTION direction, int INDEX,
			Compass last) {

		Compass result = null;
		for (Compass c : COMPASSES) {

			if (polarity == POLARITY.YANG) {
				if (direction == DIRECTION.NEGATIVE) {
					if (c.POSITIVE_YANG_OPEN && INDEX == c.POSITION.NORTH)
						result = c;
				} else if (direction == DIRECTION.POSITIVE) {
					if (c.NEGATIVE_YANG_OPEN && INDEX == c.POSITION.SOUTH)
						result = c;
				}
			} else if (polarity == POLARITY.YIN) {
				if (direction == DIRECTION.NEGATIVE) {
					if (c.POSITIVE_YIN_OPEN && INDEX == c.POSITION.EAST)
						result = c;
				} else if (direction == DIRECTION.POSITIVE) {
					if (c.NEGATIVE_YIN_OPEN && INDEX == c.POSITION.WEST)
						result = c;
				}
			}

		}

		return result;
	}

	public boolean areYangTwins(Compass compass1, Compass compass2) {
		if (compass1.POSITION.NORTH == compass2.POSITION.NORTH)
			if (compass1.POSITION.SOUTH == compass2.POSITION.SOUTH)
				return true;

		return false;
	}

	public boolean areYinTwins(Compass compass1, Compass compass2) {
		if (compass1.POSITION.EAST == compass2.POSITION.EAST)
			if (compass1.POSITION.WEST == compass2.POSITION.WEST)
				return true;

		return false;
	}

	public Vessel vessel;

	/************************/
	void clean() {

		for (int i = 0; i < DECOMPOSITION.size(); i++) {
			for (Twin twin1 : YANGTWINS) {
				if ((DECOMPOSITION.get(i)).indexOf(twin1.point) != -1) {
					DECOMPOSITION.get(i).remove(twin1.point);
				}
			}
			for (Twin twin2 : YINTWINS) {
				if ((DECOMPOSITION.get(i)).indexOf(twin2.point) != -1) {
					DECOMPOSITION.get(i).remove(twin2.point);
				}
			}

		}

	}

	boolean cutIsInside() {

		for (int i = 0; i < YANGS.size(); i++) {
			for (int j = 0; j < YINS.size(); j++) {
				int ii = (i == YANGS.size() - 1) ? 0 : i + 1;
				int jj = (j == YINS.size() - 1) ? 0 : j + 1;
				if (Utils.doLinesIntersect(YANGS.get(i), YANGS.get(ii),
						YINS.get(j), YINS.get(jj)))
					return false;
			}
		}
		return true;

	}

	public Cutter(ArrayList<Vector2> yangs, ArrayList<Vector2> yins) {
		YANGS = yangs;
		YINS = yins;

		DECOMPOSITION = new ArrayList<ArrayList<Vector2>>();
	
			findCompasses();
			findTwins();
			addPoints();
			findCompasses();
		

			while (PROCESS) {

			
					for (int i = 0; i < COMPASSES.size(); i++) {

						if (COMPASSES.get(i).isActive()) {

							DECOMPOSITION.add(new ArrayList<Vector2>());

							vessel = new Vessel(COMPASSES.get(i), this);

							break;
						}

					}

				boolean noActive = true;
				for (Compass c : COMPASSES) {
					if (c.isActive()) {
						noActive = false;
					}
				}
				if (noActive) {
				
					this.clean();

					PROCESS = false;
				} 

			}
		
			
			
			
			
			
			
			
		}

	

	Twin findTwin(int index) {
		for (Twin twin : YANGTWINS) {

			if (twin.index == index)
				return twin;
		}

		return null;

	}

	private void addPoints() {
		ArrayList<Vector2> temp = new ArrayList<Vector2>();
		for (int i = 0; i < YANGS.size(); i++) {
			temp.add(YANGS.get(i));
			for (int j = 0; j < YANGTWINS.size(); j++) {
				if (YANGTWINS.get(j).index == i)
					temp.add(YANGTWINS.get(j).point);
			}
		}
		YANGS = temp;

		ArrayList<Vector2> temp2 = new ArrayList<Vector2>();
		for (int i = 0; i < YINS.size(); i++) {
			temp2.add(YINS.get(i));
			for (int j = 0; j < YINTWINS.size(); j++) {
				if (YINTWINS.get(j).index == i)
					temp2.add(YINTWINS.get(j).point);
			}
		}
		YINS = temp2;

	}

	
	boolean doLineIntersectWithPolygon(Vector2 p1, Vector2 p2,
			ArrayList<Vector2> points) {
		for (int i = 0; i < points.size(); i++) {

			int ni = (i == points.size() - 1) ? 0 : i + 1;

			if (Utils.doLinesIntersect(p1, p2, points.get(i), points.get(ni)))
				return true;
		}
		return false;
	}

	Vector2 midPoint(Vector2 v1, Vector2 v2) {
		return v1.cpy().add(v2.cpy().sub(v1).mul(0.0001f));
	}

	void findCompasses() {
		COMPASSES = new ArrayList<Compass>();
		for (int j = 0; j < YINS.size(); j++) {
			for (int i = 0; i < YANGS.size(); i++) {

				int ni = (i == YANGS.size() - 1) ? 0 : i + 1;
				int nj = (j == YINS.size() - 1) ? 0 : j + 1;
				Vector2 intersectionPoint = Utils.intersectionPoint(
						YANGS.get(i), YANGS.get(ni), YINS.get(j), YINS.get(nj));
				if (intersectionPoint != null) {
					Compass compass = null;
					float yangDistance = intersectionPoint.dst(YANGS.get(i));
					float yinDistance = intersectionPoint.dst(YINS.get(j));
					boolean pyang = !Utils.PointInPolygon(
							midPoint(intersectionPoint, YANGS.get(ni)), YINS);
					boolean nyang = !Utils.PointInPolygon(
							midPoint(intersectionPoint, YANGS.get(i)), YINS);

					boolean pyin = Utils.PointInPolygon(
							midPoint(intersectionPoint, YINS.get(nj)), YANGS);
					boolean nyin = Utils.PointInPolygon(
							midPoint(intersectionPoint, YINS.get(j)), YANGS);

					compass = new Compass(intersectionPoint, pyang, pyin,
							nyang, nyin, new Position(ni, i, nj, j),
							yangDistance, yinDistance);
					COMPASSES.add(compass);

				}
			}
		}

	}

	void findTwins() {
		findYangTwins();
		findYinTwins();
	}

	void findYangTwins() {
		YANGTWINS = new ArrayList<Twin>();
		UnionFind twinFinder = new UnionFind(COMPASSES.size());

		for (int i = 0; i < COMPASSES.size(); i++) {
			for (int j = i + 1; j < COMPASSES.size(); j++) {

				if (areYangTwins(COMPASSES.get(i), COMPASSES.get(j)))
					twinFinder.union(i, j);

			}

		}

		twinFinder.compute();

		Iterator<HashSet<Integer>> iterator = twinFinder.myDict.values()
				.iterator();

		while (iterator.hasNext()) {
			HashSet<Integer> set = iterator.next();
			Integer[] group = set.toArray(new Integer[set.size()]);
			ArrayList<Compass> list = new ArrayList<Compass>();
			for (int i = 0; i < group.length; i++) {
				list.add(COMPASSES.get(group[i]));
			}
			Collections.sort(list, new YangComparator());
			for (int i = 0; i < group.length; i++) {
				group[i] = COMPASSES.indexOf(list.get(i));

			}

			for (int i = 0; i < group.length - 1; i++) {
				YANGTWINS.add(
						0,
						new Twin(COMPASSES.get(group[i]), COMPASSES
								.get(group[i + 1])));

			}

		}

	}

	void findYinTwins() {
		YINTWINS = new ArrayList<Twin>();
		UnionFind twinFinder = new UnionFind(COMPASSES.size());

		for (int i = 0; i < COMPASSES.size(); i++) {
			for (int j = i + 1; j < COMPASSES.size(); j++) {

				if (areYinTwins(COMPASSES.get(i), COMPASSES.get(j)))
					twinFinder.union(i, j);

			}

		}

		twinFinder.compute();

		Iterator<HashSet<Integer>> iterator = twinFinder.myDict.values()
				.iterator();

		while (iterator.hasNext()) {
			HashSet<Integer> set = iterator.next();
			Integer[] group = set.toArray(new Integer[set.size()]);
			ArrayList<Compass> list = new ArrayList<Compass>();
			for (int i = 0; i < group.length; i++) {
				list.add(COMPASSES.get(group[i]));
			}
			Collections.sort(list, new YinComparator());
			for (int i = 0; i < group.length; i++) {
				group[i] = COMPASSES.indexOf(list.get(i));

			}

			for (int i = 0; i < group.length - 1; i++) {
				YINTWINS.add(
						0,
						new Twin(COMPASSES.get(group[i]), COMPASSES
								.get(group[i + 1]), true));

			}

		}

	}

}
