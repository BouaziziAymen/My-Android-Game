package is.kul.learningandengine.basics;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.evolgames.helpers.utilities.BlockUtils;

import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import is.kul.learningandengine.factory.BasicFactory;
import is.kul.learningandengine.graphicelements.LayerProperty;
import is.kul.learningandengine.helpers.Box2DSeparator;
import is.kul.learningandengine.helpers.Utils;
import is.kul.learningandengine.scene.Cut;
import is.kul.learningandengine.scene.World;

public class Block implements Comparable<Block> {
    public boolean done;
    public Polarity polarity = Polarity.NEUTRAL;
    public BlockType blockType;
    public BlockImageSubType blockSubType;
    public LayerProperty PROPERTIES;
    public ArrayList<Vector2> VERTICES;
    public ArrayList<Vector2> MESHTRIANGLES;
    public Vector2 startup;
    public Color color;
    public BlockGrid grid;
    public boolean hasGrid;
    public ArrayList<Block> childrenBlocks;
    public ArrayList<Block> associatedBlocks;
    public ArrayList<Vector2> bounds;
    public ArrayList<Grain> grains;
    public Fixture fixture;
    public BlockGrid parentgrid;
    public ArrayList<Fixture> fixtures;
    public int parentID;
    public int secondaryID;
    public String code;
    public Grain GRAIN;
    protected boolean ABORTED, ERROR, MESHTRIANGLESGENERATED;
    Vector2 position;
    int ID, ORDER;
    HashSet<JointKey> keys;
    int associatedOrder;
    boolean finished;
    String s1;
    String s2;
    private ArrayList<Expandable> expandables;
    private float min_y;
    private float max_y;
    private float min_x;
    private float max_x;

    public Block(ArrayList<Vector2> verts) {//DUMMY BLOCK TYPE 1
        blockType = BlockType.DUMMY;
        VERTICES = verts;
        childrenBlocks = new ArrayList<Block>();

    }

    public Block() {//DUMMY BLOCK TYPE 1
        blockType = BlockType.VOID;
        childrenBlocks = new ArrayList<Block>();
    }

    public Block(ArrayList<Vector2> verts, LayerProperty properties, Color color, int order, int ID, boolean valid) {

        blockType = BlockType.NORMAL;
        this.ID = ID;
        if (!Utils.IsClockwise(verts)) Collections.reverse(verts);
        VERTICES = verts;
        startup = calculateCentroid();
        this.expandables = new ArrayList<Expandable>();
        childrenBlocks = new ArrayList<Block>();
        associatedBlocks = new ArrayList<Block>();
        fixtures = new ArrayList<Fixture>();
        keys = new HashSet<JointKey>();
        PROPERTIES = properties;
        this.color = color;
        ORDER = order;
        if (valid) {


            this.computeMeshTriangles();

            this.computeBodyTriangles();

            //fillGrid();

        }

    }

    public Block(ArrayList<Vector2> points, Vector2 position) {
        VERTICES = points;
        if (!Utils.IsClockwise(VERTICES)) Collections.reverse(this.VERTICES);
        childrenBlocks = new ArrayList<Block>();
        this.blockType = BlockType.IMAGE;
        this.position = position;
        computeMeshTriangles();
        ID = -1;
    }

    public void interpolate(Body oldBody, Body newBody, Vector2 d) {
        Vector2 O = oldBody.getPosition();
        Vector2 Op = newBody.getPosition();
        Vector2 OOp = Op.sub(O).mul(32);
        Vector2 t = newBody.getLocalVector(d).cpy().mul(32);
        float theta = oldBody.getAngle() - newBody.getAngle();
        float alpha = newBody.getAngle();
        float sin = (float) Math.sin(theta);
        float cos = (float) Math.cos(theta);

        float Sin = (float) Math.sin(alpha);
        float Cos = (float) Math.cos(alpha);
        float hx = Cos * OOp.x + Sin * OOp.y;
        float hy = -Sin * OOp.x + Cos * OOp.y;

        ArrayList<Vector2> list = new ArrayList<Vector2>();
        for (int i = 0; i < this.VERTICES.size(); i++) {
            Vector2 v = this.VERTICES.get(i);
            float x = -hx + cos * v.x - sin * v.y;
            float y = -hy + sin * v.x + cos * v.y;

            list.add(new Vector2(x + t.x, y + t.y));
        }


        this.VERTICES = list;

        MESHTRIANGLESGENERATED = false;
        computeMeshTriangles();

    }

    public Block getStainBlock(float x, float y, TextureRegion imageTextureRegion) {

        float height = 168;
        float width = 168;
        float halfW = width / 2f;
        float halfH = height / 2f;

        ArrayList<Vector2> pts = new ArrayList<Vector2>();
        pts.add(new Vector2(x - halfW, y - halfH));
        pts.add(new Vector2(x + halfW, y - halfH));
        pts.add(new Vector2(x + halfW, y + halfH));
        pts.add(new Vector2(x - halfW, y + halfH));
        Block block = new Block(pts);
        ArrayList<Vector2> path = new ArrayList<Vector2>();
        for (int i = 0; i < this.VERTICES.size(); i++) path.add(this.VERTICES.get(i).cpy());
        Block stainBlock = block.applyClipByPath(path, new Vector2(x, y));
        stainBlock.setID(-1);
        stainBlock.position = new Vector2(x, y);
        stainBlock.blockType = BlockType.IMAGE;
        stainBlock.blockSubType = BlockImageSubType.STAIN;
        associatedBlocks.add(stainBlock);
        if (!Utils.IsClockwise(block.VERTICES)) Collections.reverse(block.VERTICES);
        return stainBlock;

    }

    boolean isConvex(int i) {
        int ni = i == this.VERTICES.size() - 1 ? 0 : i + 1;
        int pi = (i == 0 ? this.VERTICES.size() : i) - 1;
        return Utils.convex(this.VERTICES.get(pi), this.VERTICES.get(i), this.VERTICES.get(ni));
    }

    public void sortAssociatedBlocks(ArrayList<Block> splinters, String compterendue) {


    }

    void calculateBounds() {
        float min_y = Float.MAX_VALUE;
        float max_y = Float.MIN_VALUE;

        float min_x = Float.MAX_VALUE;
        float max_x = Float.MIN_VALUE;
        for (Vector2 v : this.VERTICES) {
            if (v.y < min_y) min_y = v.y;
            if (v.y > max_y) max_y = v.y;
        }


        for (Vector2 v : this.VERTICES) {
            if (v.x < min_x) min_x = v.x;
            if (v.x > max_x) max_x = v.x;
        }

        this.bounds = new ArrayList<Vector2>();
        this.bounds.add(new Vector2(min_x - 10, min_y - 10));
        this.bounds.add(new Vector2(max_x + 10, min_y - 10));
        this.bounds.add(new Vector2(max_x + 10, max_y + 10));
        this.bounds.add(new Vector2(min_x - 10, max_y + 10));

    }

    public Vector2 calculateCentroid() {
        float x = 0;
        float y = 0;
        int pointCount = VERTICES.size();
        for (int i = 0; i < pointCount; i++) {
            final Vector2 point = VERTICES.get(i);
            x += point.x;
            y += point.y;
        }

        x = x / pointCount;
        y = y / pointCount;

        return new Vector2(x, y);
    }

    public void generateGrid() {

        min_y = Float.MAX_VALUE;
        max_y = Float.MIN_VALUE;

        min_x = Float.MAX_VALUE;
        max_x = Float.MIN_VALUE;
        for (Vector2 v : this.VERTICES) {
            if (v.y < min_y) min_y = v.y;
            if (v.y > max_y) max_y = v.y;
        }

        for (Vector2 v : this.VERTICES) {
            if (v.x < min_x) min_x = v.x;
            if (v.x > max_x) max_x = v.x;
        }


        Grain base = new Grain(0, 0, startup.x, startup.y, this);

        grains = new ArrayList<Grain>();
        grains.add(base);
        testGrain(base);

        for (Grain g : grains) {
            g.temperature = World.AMBIENT_TEMPERATURE;
        }
        grid = new BlockGrid(grains, this.VERTICES, this.startup);

        this.hasGrid = true;
    }

    Grain getGrain(int nx, int ny) {
        for (Grain g : grains)
            if (!g.inter)
                if (g.nx == nx && g.ny == ny) return g;
        return null;
    }

    //RECURSIVE
    Grain findGrain(Grain center, int dx, int dy) {
        float x = center.position.x;
        float y = center.position.y;
        int nx = center.nx;
        int ny = center.ny;
        //System.out.println("begin:"+(x+dx));

        Grain newgrain = getGrain(nx + dx, ny + dy);
        if (newgrain != null) {

            return newgrain;
        }
        boolean out = false;
        if (x + 16 * dx > max_x) out = true;
        if (x - 16 * dx < min_x) out = true;
        if (y + 16 * dy > max_y) out = true;
        if (y - 16 * dy < min_y) out = true;
        if (newgrain == null) {
            Vector2 inter = getIntersection(center.position, new Vector2(x + dx * 16.1f, y + dy * 16.1f));
            if (inter == null) {

                Grain g = new Grain(nx + dx, ny + dy, x + dx * 16, y + dy * 16, this);
                grains.add(g);
                if (!out) return g;

            } else {
                //Grain g= new Grain(nx+dx,ny+dy,inter.x,inter.y);
                // g.inter = true;
                // grains.add(g);
            }
        }

        return null;


    }

    public Cut slice(Vector2 center, Vector2 e1, Vector2 e2) {
        float e = 16f;
        pair pair1, pair2;
        pair1 = getIntersectionData(center, e1);
        pair2 = getIntersectionData(center, e2);
        if (pair1 == null || pair2 == null) return null;
        Vector2 P1 = VERTICES.get(pair1.i);
        Vector2 P2 = VERTICES.get(pair1.ni);
        Vector2 Q1 = VERTICES.get(pair2.i);
        Vector2 Q2 = VERTICES.get(pair2.ni);

        float side1 = P1.dst(P2);
        float side2 = Q1.dst(Q2);
        Vector2 X1 = pair1.v;
        Vector2 X2 = pair2.v;
        boolean cut = true;
        if (side1 >= 2 * e) {//SIDE BIG ENOUGH
            float d1 = X1.dst(P1);
            float d2 = X1.dst(P2);
            if (d1 <= d2) {
                if (d1 >= e) {//DO NOTHING BECAUSE ALL IS ALRIGHT AND X1 CAN BE SAFE

                } else {//PUT X1 AT DISTANCE e FROM P1
                    Vector2 u = P2.cpy().sub(P1).nor().mul(e);
                    X1 = P1.cpy().add(u);
                }
            } else {
                if (d2 >= e) {//DO NOTHING BECAUSE ALL IS ALRIGHT AND X1 CAN BE SAFE

                } else {//PUT X1 AT DISTANCE e FROM P2
                    Vector2 u = P1.cpy().sub(P2).nor().mul(e);
                    X1 = P2.cpy().add(u);
                }
            }
        } else {//SIDE TOO SMALL APPROXIMATE TO ONE OF NEIGHBORS
            float d1 = X1.dst(P1);
            float d2 = X1.dst(P2);

            if (d1 <= d2) {
                X1 = P1;
            } else {
                X1 = P2;
            }
        }


        if (side2 >= 2 * e) {//SIDE BIG ENOUGH
            float d1 = X2.dst(Q1);
            float d2 = X2.dst(Q2);
            if (d1 <= d2) {
                if (d1 >= e) {//DO NOTHING BECAUSE ALL IS ALRIGHT AND X2 CAN BE SAFE

                } else {//PUT X2 AT DISTANCE e FROM Q1
                    Vector2 u = Q2.cpy().sub(Q1).nor().mul(e);
                    X2 = Q1.cpy().add(u);
                }
            } else {
                if (d2 >= e) {//DO NOTHING BECAUSE ALL IS ALRIGHT AND X1 CAN BE SAFE

                } else {//PUT X2 AT DISTANCE e FROM Q2
                    Vector2 u = Q1.cpy().sub(Q2).nor().mul(e);
                    X2 = Q2.cpy().add(u);
                }
            }
        } else {//SIDE TOO SMALL APPROXIMATE TO ONE OF NEIGHBORS
            float d1 = X2.dst(Q1);
            float d2 = X2.dst(Q2);

            if (d1 <= d2) {
                X2 = Q1;
            } else {
                X2 = Q2;
            }
        }

        //TEST IF X1 AND X2 ARE NEIGHBORS DON'T CUT ELSE CUT
        //X1 AND X2 DIRECT NEIGHBORS
        //X1 IS ONE OF SIDES OF X2
        //X2 IS ONE OF SIDES OF X1


        if ((X1 == Q1 || X1 == Q2) || (X2 == P1 || X2 == P2)) cut = false;
        int k = VERTICES.indexOf(X1);
        int l = VERTICES.indexOf(X2);
        if (k != -1 && l != -1)
            if (Utils.areNeighbors(k, l, VERTICES.size())) cut = false;
        if (X1 == X2) cut = false;

        Cut CUT = new Cut(X1, X2, getID());
        CUT.x1a = P1;
        CUT.x1b = P2;
        CUT.x2a = Q1;
        CUT.x2b = Q2;
        CUT.ip2 = pair1.ni;
        CUT.iq2 = pair2.ni;


        if (cut)
            return CUT;
        else return null;
    }

    public pair getIntersectionData(Vector2 head, Vector2 next) {
        ArrayList<pair> candidates = new ArrayList<pair>();
        for (int i = 0; i < VERTICES.size(); i++) {
            int ni = (i == VERTICES.size() - 1) ? 0 : i + 1;
            Vector2 v1 = VERTICES.get(i);
            Vector2 v2 = VERTICES.get(ni);
            Vector2 u = (v1.cpy().sub(v2)).nor().mul(0.1f);
            Vector2 V1 = v1.cpy().add(u);
            Vector2 V2 = v2.cpy().sub(u);
            Vector2 intersection = Utils.lineIntersectPoint(head, next, V1, V2);
            if (intersection != null) {
                pair pair = new pair(intersection, i, ni);
                pair.value = head.dst(intersection);
                candidates.add(pair);
            }
        }
        Collections.sort(candidates);
        if (candidates.size() > 0)
            return candidates.get(0);
        else return null;

    }

    public Vector2 getIntersection(Vector2 head, Vector2 next) {
        ArrayList<pair> candidates = new ArrayList<pair>();
        for (int i = 0; i < VERTICES.size(); i++) {
            int ni = (i == VERTICES.size() - 1) ? 0 : i + 1;
            Vector2 v1 = VERTICES.get(i);
            Vector2 v2 = VERTICES.get(ni);
            Vector2 u = (v1.cpy().sub(v2)).nor().mul(0.1f);
            Vector2 V1 = v1.cpy().add(u);
            Vector2 V2 = v2.cpy().sub(u);
            Vector2 intersection = Utils.lineIntersectPoint(head, next, V1, V2);
            if (intersection != null) {
                pair pair = new pair(intersection, i);
                pair.value = head.dst(intersection);
                candidates.add(pair);
            }
        }
        Collections.sort(candidates);
        if (candidates.size() > 0)
            return candidates.get(0).v;
        else return null;

    }

    void testGrain(Grain center) {

        //grains.add(center);

        center.used = true;
        Grain up = findGrain(center, 0, 1);
        Grain down = findGrain(center, 0, -1);
        Grain right = findGrain(center, 1, 0);
        Grain left = findGrain(center, -1, 0);
        Grain upright = findGrain(center, 1, 1);
        Grain upleft = findGrain(center, -1, 1);
        Grain downright = findGrain(center, 1, -1);
        Grain downleft = findGrain(center, -1, -1);


        if (up != null) if (!up.used && !up.inter) testGrain(up);
        if (down != null) if (!down.used && !down.inter) testGrain(down);
        if (right != null) if (!right.used && !right.inter) testGrain(right);
        if (left != null) if (!left.used && !left.inter) testGrain(left);
        if (upright != null) if (!upright.used && !upright.inter) testGrain(upright);
        if (upleft != null) if (!upleft.used && !upleft.inter) testGrain(upleft);
        if (downright != null) if (!downright.used && !downright.inter) testGrain(downright);
        if (downleft != null) if (!downleft.used && !downleft.inter) testGrain(downleft);
    }

    float getDistance(Vector2 center) {
        if (Utils.PointInPolygon(center, this.VERTICES)) return 0;
        float distance = Float.MAX_VALUE;
        for (Vector2 v : this.VERTICES) {
            float d = center.dst(v);
            if (d < distance) distance = d;
        }
        return distance;
    }

    public void computeMeshTriangles() {
        if (!this.MESHTRIANGLESGENERATED) {
            this.MESHTRIANGLES = (ArrayList<Vector2>) BasicFactory.triangulator.computeTriangles(this.VERTICES);
            this.MESHTRIANGLESGENERATED = true;
        }

    }

    public ArrayList<ArrayList<Vector2>> computeBodyTriangles() {
        ArrayList<ArrayList<Vector2>> BODYTRIANGLESLISTS = new ArrayList<ArrayList<Vector2>>();


        //TRY TO SEPARATE IF FAILED RETURN FALSE
        //SEPARATE CONCAVE POLYGONS FOR EACH GROUP this preserves zindex
        ArrayList<Vector2> list = new ArrayList<>();
        for (Vector2 v : VERTICES) list.add(v.cpy().mul(1 / 32f));


        ArrayList<ArrayList<Vector2>> decomposed = Utils.decompose(list);
        for (int j = 0; j < decomposed.size(); j++) {
            BODYTRIANGLESLISTS.add(decomposed.get(j));
        }


        return BODYTRIANGLESLISTS;


    }

    public ArrayList<ArrayList<Vector2>> computeBodyTrianglesThrow() throws Throwable {
        ArrayList<ArrayList<Vector2>> BODYTRIANGLESLISTS = new ArrayList<ArrayList<Vector2>>();


        //TRY TO SEPARATE IF FAILED RETURN FALSE
        //SEPARATE CONCAVE POLYGONS FOR EACH GROUP this preserves zindex
        ArrayList<ArrayList<Vector2>> SeparatedPoints;

        SeparatedPoints = Box2DSeparator.separate(VERTICES, 30);

        for (ArrayList<Vector2> list : SeparatedPoints) for (Vector2 v : list) v.mul(1 / 32f);

        for (int i = 0; i < SeparatedPoints.size(); i++) {

            ArrayList<ArrayList<Vector2>> decomposed = Utils.decompose(SeparatedPoints.get(i));
            for (int j = 0; j < decomposed.size(); j++) {
                BODYTRIANGLESLISTS.add(decomposed.get(j));
            }
        }


        return BODYTRIANGLESLISTS;


    }

    boolean getError() {

        if (this.childrenBlocks.size() > 0) {
            for (Block block : this.childrenBlocks) {
                if (block.ERROR) return true;
            }
        } else {
            if (!this.ABORTED)
                return this.ERROR;
        }
        return false;
    }

    ArrayList<Block> getBlocksCareless() {
        ArrayList<Block> result = new ArrayList<Block>();
        if (this.childrenBlocks.size() > 0) {
            for (Block block : this.childrenBlocks) {
                result.addAll(block.getBlocksCareless());
            }
        } else {
            if (!this.ABORTED)
                result.add(this);
        }
        return result;
    }

    ArrayList<Block> getBlocks() {
        ArrayList<Block> result = new ArrayList<Block>();
        if (this.childrenBlocks.size() > 0) {
            for (Block block : this.childrenBlocks) {
                result.addAll(block.getBlocks());
            }
        } else {
            if (!this.ABORTED)
                result.add(this);
        }
        return result;
    }

    public String applyCyclicCut(ArrayList<Vector2> path) {
        if (Utils.isInside(path, VERTICES)) {
            VERTICES = path;
            return "";
        }
        Outline outline = new Outline(VERTICES, path);

        applyCuts(outline.getCuts(), null);
        return Arrays.toString(outline.getCuts().toArray()) + outline.string;
    }

    public Block applyClipByPathLarge(ArrayList<Vector2> path, Vector2 center) {//TO DO REVISE THIS METHOD
        if (Utils.included(this.VERTICES, path)) {
            VERTICES.clear();
            return null;
        }
        applyCyclicCut(path);
        outer:
        for (Block b : getBlocks()) {
            if (Utils.PointInPolygon(center, b.VERTICES)) {

                for (Vector2 v : b.VERTICES) if (!Utils.PointInPolygon(v, path)) continue outer;

                VERTICES = b.VERTICES;
                break;
            }
        }
        childrenBlocks.clear();
        return this;

    }

    public Block applyClipByPath(ArrayList<Vector2> path, Vector2 center) {//TO DO REVISE THIS METHOD


        String s = applyCyclicCut(path);


        //GameScene.plotter.drawPoint(center, Color.YELLOW, 1, 0);
        outer:
        for (Block b : getBlocks()) {
            if (Utils.PointInPolygon(center, b.VERTICES)) {

                if (!Utils.isInside(b.VERTICES, path)) {

                    Log.e("error" + center, "" + Arrays.toString(b.VERTICES.toArray()));
                    Log.e("error", "" + Arrays.toString(path.toArray()));
                    Log.e("error", s);
                }

                VERTICES = b.VERTICES;

            }
        }
        childrenBlocks.clear();
        return this;

    }

    boolean test(ArrayList<Cut> cuts) {
        for (Cut cut : cuts) {
            if (cut.getId() == ID && !cut.isHalf()) {
                int index1 = Utils.getIndex(cut.getP1(), VERTICES);
                int index2 = Utils.getIndex(cut.getP2(), VERTICES);
                if (index1 != -1 && index2 != -1) {
                    Vector2 p1 = cut.getP1();
                    Vector2 p2 = cut.getP2();

                    int nindex1 = index1 == this.VERTICES.size() - 1 ? 0 : index1 + 1;
                    int nindex2 = index2 == this.VERTICES.size() - 1 ? 0 : index2 + 1;
                    Vector2 a = this.VERTICES.get(index1);
                    Vector2 na = this.VERTICES.get(nindex1);
                    Vector2 b = this.VERTICES.get(index2);
                    Vector2 nb = this.VERTICES.get(nindex2);
                    if (p1.dst(a) < 0.5f) return false;
                    if (p1.dst(na) < 0.5f) return false;
                    if (p2.dst(b) < 0.5f) return false;
                    if (p2.dst(nb) < 0.5f) return false;
                } else return false;
            }
        }
        return true;
    }

    ArrayList<Vector2> getCentroids() {

        ArrayList<Vector2> centroids = new ArrayList<Vector2>();

        for (int i = 0; i < this.MESHTRIANGLES.size(); i += 3) {
            centroids.add(Utils.getTriangleCentroid(this.MESHTRIANGLES.get(i), this.MESHTRIANGLES.get(i + 1), this.MESHTRIANGLES.get(i + 2)));

        }
        return centroids;
    }

    void applyCuts(ArrayList<Cut> cuts, Block first) {

        for (Cut cut : cuts) {
            if (!cut.isDead() && cut.getId() == ID && !cut.isHalf()) {


                cut.setDead(true);

                ArrayList<ArrayList<Vector2>> group;

                if (blockType == BlockType.NORMAL) {
                    group = Utils.splitFinal(cut, VERTICES);
                } else {
                    int index1 = Utils.getIndex(cut.getP1(), VERTICES);
                    int index2 = Utils.getIndex(cut.getP2(), VERTICES);
                    Vector2 p1 = cut.getP1();
                    Vector2 p2 = cut.getP2();
                    group = Utils.splitPrime(p1, p2, VERTICES, index1, index2);
                }

                //ArrayList<ArrayList<Vector2>> group = Utils.splitPrime(p1, p2, VERTICES, index1, index2,cut.inners);

                ArrayList<Vector2> group1 = group.get(0);
                ArrayList<Vector2> group2 = group.get(1);


                boolean isValid1 = BlockUtils.isValid(group1);
                Block piece1;
                if (blockType == BlockType.NORMAL) {
                    piece1 = new Block(group1, this.PROPERTIES, this.color, this.ORDER, this.ID, isValid1);
                    piece1.hasGrid = true;
                    piece1.parentgrid = first.grid;
                    piece1.setAborted(!isValid1);
                } else {
                    piece1 = new Block(group1, position);
                    piece1.parentID = parentID;
                    piece1.color = color;
                    piece1.blockType = blockType;
                    piece1.blockSubType = blockSubType;
                    piece1.setID(getID());
                    piece1.GRAIN = GRAIN;
                }

                childrenBlocks.add(piece1);

                piece1.setPolarity(Polarity.YIN);

                boolean isValid2 = BlockUtils.isValid(group2);
                Block piece2;
                if (blockType == BlockType.NORMAL) {
                    piece2 = new Block(group2, this.PROPERTIES, this.color, this.ORDER, this.ID, isValid2);
                    piece2.hasGrid = true;
                    piece2.parentgrid = first.grid;
                    piece2.setAborted(!isValid2);

                } else {
                    piece2 = new Block(group2, position);
                    piece2.parentID = parentID;
                    piece2.color = color;
                    piece2.blockType = blockType;
                    piece2.blockSubType = blockSubType;
                    piece2.setID(getID());
                    piece2.GRAIN = GRAIN;
                }
                childrenBlocks.add(piece2);

                piece2.setPolarity(Polarity.YANG);


                if (this.keys != null)
                    for (JointKey key : this.keys) {

                        if (Utils.PointInPolygon(key.anchor, piece1.VERTICES)) piece1.keys.add(key);
                        else if (Utils.PointInPolygon(key.anchor, piece2.VERTICES))
                            piece2.keys.add(key);
                        else {
                            if (piece1.distance(key.anchor) <= piece2.distance(key.anchor))
                                piece1.keys.add(key);
                            else piece2.keys.add(key);
                        }
                    }


                if (this.associatedBlocks != null)
                    for (Block associated : associatedBlocks) {


                        associated.projectCut(cut.getP1(), cut.getP2());
                        ArrayList<Block> cutassociated = associated.getBlocks();


                        for (Block b : cutassociated) {


                            if (b.polarity == Polarity.YANG) piece2.associatedBlocks.add(b);
                            else if (b.polarity == Polarity.YIN) piece1.associatedBlocks.add(b);
                            else if (b.polarity == Polarity.NEUTRAL) {


                                if (Utils.isInside(b.getCentroids(), piece1.VERTICES)) {
                                    piece1.associatedBlocks.add(b);
                                } else if (Utils.isInside(b.getCentroids(), piece2.VERTICES)) {
                                    piece2.associatedBlocks.add(b);
                                }


                            }
                            b.setPolarity(Polarity.NEUTRAL);

                        }
                    }

                if (isValid1) piece1.applyCuts(cuts, first);
                if (isValid2) piece2.applyCuts(cuts, first);
                //GameScene.plotter.detachChildren();
                //for(Vector2 v:group1)GameScene.plotter.drawPoint(v, Color.RED, 1f, 0);
                //for(Vector2 v:group2)GameScene.plotter.drawPoint(v, Color.BLUE, 1f, 0);
                break;


            }
        }


    }

    public void projectCut(Vector2 first, Vector2 second) {

        Vector2 e = second.cpy().sub(first).nor().mul(0.1f);

        ArrayList<Block.inter> intersections = new ArrayList<Block.inter>();
        for (int i = 0; i < this.VERTICES.size(); i++) {
            int ni = i == this.VERTICES.size() - 1 ? 0 : i + 1;
            Vector2 v1 = this.VERTICES.get(i);
            Vector2 v2 = this.VERTICES.get(ni);
            Vector2 u = new Vector2(v1.x - v2.x, v1.y - v2.y).nor().mul(0.01f);

            Vector2 V1 = new Vector2(v1.x + u.x, v1.y + u.y);
            Vector2 V2 = new Vector2(v2.x - u.x, v2.y - u.y);
            Vector2 v = Utils.lineIntersectPoint(first.x - e.x, first.y - e.y,
                    second.x + e.x, second.y + e.y, V1.x, V1.y, V2.x, V2.y);
            if (v != null) {
                if (Utils.PointOnLineSegment(V1, V2, v, 0.001f)) {
                    Block.inter inter = new Block.inter(v);
                    inter.value = v.dst(first);

                    intersections.add(inter);
                }
            }
        }
        Collections.sort(intersections);


        ArrayList<Cut> cuts = new ArrayList<>();
        for (int i = 1; i < intersections.size(); i += 2) {
            int pi = i - 1;
            Block.inter inter1 = intersections.get(pi);
            Block.inter inter2 = intersections.get(i);
            cuts.add(new Cut(inter1.v, inter2.v, -1));

        }

        applyCuts(cuts, null);

    }

    private void setAborted(boolean aborted) {
        ABORTED = aborted;

    }

    @Override
    public int compareTo(Block another) {
        return another.ORDER < this.ORDER ? 1 : -1;
    }

    public int getID() {
        // TODO Auto-generated method stub
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
        if (fixtures == null) return;

        for (Fixture f : this.fixtures) f.setUserData(new Integer(ID));

    }

    public void reset() {
        childrenBlocks.clear();
        ERROR = false;
        ABORTED = false;
        this.finished = false;

    }

    public void recycle() {
//	for(Vector2 v:this.VERTICES)Vector2Pool.recycle(v);
        //for(Vector2 v:this.MESHTRIANGLES)Vector2Pool.recycle(v);
        //for(ArrayList<Vector2> list:this.BODYTRIANGLESLISTS)for(Vector2 v:list)Vector2Pool.recycle(v);

    }

    public void internalHeatTransfer() {

        this.grid.innerHeatTransfer();


    }

    public void createFire(Grain grainOnFire) {

        Fixture fixture = this.fixtures.get(0);
        DCGameEntity entity = (DCGameEntity) fixture.getBody().getUserData();
        entity.createFire(this);

    }

    public Vector2 getPoint(Vector2 p) {

        if (testPoint(p.x / 32f, p.y / 32f)) return p;
        Vector2 p1 = p.cpy().sub(15.9f, 0);
        Vector2 p2 = p.cpy().add(15.9f, 0);


        ArrayList<Block.inter> intersections_pos = new ArrayList<Block.inter>();
        ArrayList<Block.inter> intersections_neg = new ArrayList<Block.inter>();
        Vector2 dir = p2.cpy().sub(p);
        for (int i = 0; i < this.VERTICES.size(); i++) {
            int ni = i == this.VERTICES.size() - 1 ? 0 : i + 1;
            Vector2 v1 = this.VERTICES.get(i);
            Vector2 v2 = this.VERTICES.get(ni);
            Vector2 u = new Vector2(v1.x - v2.x, v1.y - v2.y).nor().mul(0.01f);

            Vector2 V1 = new Vector2(v1.x + u.x, v1.y + u.y);
            Vector2 V2 = new Vector2(v2.x - u.x, v2.y - u.y);
            Vector2 v = Utils.lineIntersectPoint(p1.x, p1.y,
                    p2.x, p2.y, V1.x, V1.y, V2.x, V2.y);
            if (v != null) {
                Block.inter inter = new Block.inter(v);
                Vector2 t = v.cpy().sub(p);
                if (dir.dot(t) > 0) {
                    inter.value = v.dst(p);
                    intersections_pos.add(inter);
                } else {
                    inter.value = v.dst(p);
                    intersections_neg.add(inter);
                }
            }
        }
        Collections.sort(intersections_pos);
        intersections_pos.add(new Block.inter(p2));
        if (intersections_pos.size() >= 2) {
            Vector2 v1 = intersections_pos.get(0).v;
            Vector2 v2 = intersections_pos.get(1).v;
            Vector2 result = v1.add(v2).mul(0.5f);
            return result;
        }


        Collections.sort(intersections_neg);
        intersections_neg.add(new Block.inter(p1));
        if (intersections_neg.size() >= 2) {
            Vector2 v1 = intersections_neg.get(0).v;
            Vector2 v2 = intersections_neg.get(1).v;
            Vector2 result = v1.add(v2).mul(0.5f);
            return result;
        }


        return null;

    }

    public void setElements(Fixture fixture) {
        fixtures.add(fixture);

    }

    public boolean testPoint(float x, float y) {
        for (Fixture fix : this.fixtures) {
            if (fix.testPoint(x, y)) return true;
        }
        return false;
    }

    public void finish() {
        generateGrid();

    }

    public ArrayList<Grain> getGrains() {
        // TODO Auto-generated method stub
        return this.grid.allGrains;
    }

    public float getBrittleness() {
        return this.PROPERTIES.getBrittleness();
    }

    public float getTenacity() {
        return this.PROPERTIES.getTenacity();
    }

    public ArrayList<Vector2> getBlockPoints() {
        // TODO Auto-generated method stub
        return this.VERTICES;
    }

    public float[] getLayerData() {
        return BasicFactory.generateData(getMeshTriangles());

    }

    public ArrayList<Vector2> getMeshTriangles() {

        // TODO ORDER ASSOCIATED BLOCKS


        ArrayList<Vector2> triangles = new ArrayList<Vector2>();
        triangles.addAll(this.MESHTRIANGLES);
        for (Block associated : associatedBlocks)
            if (associated.blockType == BlockType.IMAGE)
                if (associated.blockSubType == BlockImageSubType.DECORATION)
                    triangles.addAll(associated.MESHTRIANGLES);

        for (Block associated : associatedBlocks)
            if (associated.blockType == BlockType.IMAGE)
                if (associated.blockSubType == BlockImageSubType.EXPANDABLE)
                    triangles.addAll(associated.MESHTRIANGLES);


        return triangles;
    }

    public Color[] getColors() {

        // TODO ORDER ASSOCIATED BLOCKS
        ArrayList<Color> colors = new ArrayList<Color>();
        colors.add(this.color);
        for (Block associated : associatedBlocks)
            if (associated.blockType == BlockType.IMAGE)
                if (associated.blockSubType == BlockImageSubType.DECORATION)
                    colors.add(associated.color);

        for (Block associated : associatedBlocks)
            if (associated.blockType == BlockType.IMAGE)
                if (associated.blockSubType == BlockImageSubType.EXPANDABLE)
                    colors.add(associated.color);

        return colors.toArray(new Color[colors.size()]);
    }

    public Integer[] getVertexCount() {

        // TODO ORDER ASSOCIATED BLOCKS
        ArrayList<Integer> result = new ArrayList<Integer>();
        result.add(MESHTRIANGLES.size());
        for (Block associated : associatedBlocks)
            if (associated.blockType == BlockType.IMAGE)
                if (associated.blockSubType == BlockImageSubType.DECORATION)
                    result.add(associated.MESHTRIANGLES.size());

        for (Block associated : associatedBlocks)
            if (associated.blockType == BlockType.IMAGE)
                if (associated.blockSubType == BlockImageSubType.EXPANDABLE)
                    result.add(associated.MESHTRIANGLES.size());

        return result.toArray(new Integer[result.size()]);
    }

    public LayerProperty getPROPERTIES() {
        // TODO Auto-generated method stub
        return this.PROPERTIES;
    }

    public Polarity getPolarity() {
        // TODO Auto-generated method stub
        return polarity;
    }

    public void setPolarity(Polarity polarity) {
        // TODO Auto-generated method stub
        this.polarity = polarity;
    }

    public Vector2 getIntersectionWithBounds(Vector2 first, Vector2 second) {
        ArrayList<Block.inter> intersections = new ArrayList<Block.inter>();
        if (this.bounds == null) calculateBounds();
        for (int i = 0; i < this.bounds.size(); i++) {
            int ni = i == this.bounds.size() - 1 ? 0 : i + 1;
            Vector2 v1 = this.bounds.get(i);
            Vector2 v2 = this.bounds.get(ni);
            Vector2 u = new Vector2(v1.x - v2.x, v1.y - v2.y).nor().mul(0.01f);

            Vector2 V1 = new Vector2(v1.x + u.x, v1.y + u.y);
            Vector2 V2 = new Vector2(v2.x - u.x, v2.y - u.y);
            Vector2 v = Utils.lineIntersectPoint(first.x, first.y,
                    second.x, second.y, V1.x, V1.y, V2.x, V2.y);
            if (v != null) {
                if (Utils.PointOnLineSegment(V1, V2, v, 0.001f)) {
                    Block.inter inter = new Block.inter(v);
                    inter.value = v.dst(first);

                    intersections.add(inter);
                }
            }
        }
        Collections.sort(intersections);

        if (intersections.size() > 0) return intersections.get(0).v;
        return null;

    }

    public void changeVertices(ArrayList<Vector2> newPoints) {
        VERTICES = newPoints;
        MESHTRIANGLESGENERATED = false;

    }

    public void addExpandable(Expandable ex) {
        this.expandables.add(ex);

    }

    public String toString() {
        return Arrays.toString(VERTICES.toArray());
    }

    public void innerHeatTransfer(Grain grain) {
        grid.innerHeatTransfer(grain);
    }

    public float distance(Vector2 point) {
        float dmin = Float.MAX_VALUE;
        for (int i = 0; i < VERTICES.size(); i++) {
            int ni = (i == VERTICES.size() - 1) ? 0 : i + 1;
            float d = Utils.FindDistanceToSegment(point, VERTICES.get(i), VERTICES.get(ni));
            if (d < dmin) {
                dmin = d;
            }
        }
        return dmin;
    }

    class comparator implements Comparator<Block> {

        @Override
        public int compare(Block lhs, Block rhs) {
            // TODO Auto-generated method stub
            if (lhs.associatedOrder > rhs.associatedOrder) return -1;
            if (lhs.associatedOrder < rhs.associatedOrder) return 1;
            return 0;
        }

    }

    class pair implements Comparable<pair> {
        int i, ni;
        Vector2 v;
        float value;

        pair(Vector2 v, int i) {
            this.v = v;
            this.i = i;

        }

        pair(Vector2 v, int i, int ni) {
            this.v = v;
            this.i = i;
            this.ni = ni;
        }

        @Override
        public int compareTo(pair another) {
            // TODO Auto-generated method stub
            return (this.value >= another.value) ? 1 : -1;
        }
    }

    class inter implements Comparable<Block.inter> {


        Vector2 v;
        float value;

        inter(Vector2 v) {
            this.v = v;
        }

        @Override
        public int compareTo(Block.inter another) {
            if (value > another.value) return 1;
            else return -1;
        }
    }


}

