package is.kul.learningandengine.factory;

import org.andengine.util.adt.color.Color;

import java.util.ArrayList;



public class MaterialFactory {

private static final MaterialFactory INSTANCE = new MaterialFactory();

public ArrayList<Material> materials;
public Material getMaterial(int index){
	return this.materials.get(index);
}
private MaterialFactory() {

    this.materials = new ArrayList<Material>();
    this.materials.add(new Material("Wood",0.27f,1,0.5f,new Color(102/255f, 51/255f, 0/255f),0,4f));
    this.materials.add(new Material("Steel",8f,1,0.2f,new Color(224/255f, 223/255f, 219/255f),1,6.75f));
    this.materials.add(new Material("Iron",7f,1,0.25f,new Color(230/255f, 231/255f, 232/255f),2,4.5f));
    this.materials.add(new Material("Gold",19.3f,1,0.3f,new Color(212/255f,175/255f,55/255f),3,2.75f));
    this.materials.add(new Material("Copper",8.9f,1,0.28f,new Color(200/255f,117/255f,51/255f),4,2.75f));
    this.materials.add(new Material("Tin",7.3f,1,0.35f,new Color(127/255f,127/255f,127/255f),5,1.65f));
    this.materials.add(new Material("Brass",8.55f,1,0.26f,new Color(181/255f,166/255f,66/255f),6,3.5f));
    this.materials.add(new Material("Aluminium",2.7f,1,0.32f,new Color(173/255f,178/255f,189/255f),7,2.45f));
    this.materials.add(new Material("Ceramic",3.21f,1,0.3f,new Color(254/255f,255/255f,253/255f),8,3.3f));
    this.materials.add(new Material("Bronze",8.15f,1,0.29f,new Color(205/255f, 127/255f, 50/255f),9,4f));
    this.materials.add(new Material("Flesh",80f,1,0.25f,new Color(1f, 0.8f, 0.6f),10,0.5f));
    this.materials.add(new Material("Asphalt",1000f,1f,0f,new  Color(0.5f,0.6f,0.5f),11,1.5f));
    this.materials.add(new Material("S",800f,1,0.5f,new Color(1f, 0f, 1f),12,4f));

}

public static MaterialFactory getInstance() {
return MaterialFactory.INSTANCE;
}
public void create() {

}

}