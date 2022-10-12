package com.evolgames.entities.factories;

import com.evolgames.entities.properties.JuiceProperties;
import com.evolgames.entities.Liquid;
import com.evolgames.entities.Material;
import com.evolgames.helpers.utilities.MyColorUtils;

import org.andengine.util.adt.color.Color;


import java.util.ArrayList;

public class MaterialFactory {
    private static final MaterialFactory INSTANCE = new MaterialFactory();
    public static MaterialFactory getInstance() {
        return INSTANCE;
    }
    public void create() {    }


    public ArrayList<Material> materials;
    public ArrayList<Liquid> liquids;
    public Material getMaterialByIndex(int index){
        return this.materials.get(index);
    }


    private MaterialFactory() {

        this.liquids = new ArrayList<>();
        liquids.add(new Liquid(new JuiceProperties("Blood", MyColorUtils.bloodColor)));
        liquids.add(new Liquid(new JuiceProperties("Goo", new Color(0.1f,0.1f,0.1f,0.5f))));


        this.materials = new ArrayList<>();
        this.materials.add(new Material("Wood", 0, new Color(102 / 255f, 51 / 255f, 0 / 255f), 0.5f, 1, 0.5f, 0.5f, true, 573, 2000, 6000));
        this.materials.add(new Material("Steel", 1, new Color(202/ 255f, 204/ 255f, 206/ 255f), 8f, 1, 0.2f, 4f, false, 0, 0, 0));
        this.materials.add(new Material("Iron", 2, new Color(161 / 255f, 157 / 255f, 148 / 255f), 7f, 1, 0.25f, 3.8f, false, 0, 0, 0));
        this.materials.add(new Material("Gold", 3, new Color(212 / 255f, 175 / 255f, 55 / 255f), 10f, 1, 0.3f, 4f, false, 0, 0, 0));
        this.materials.add(new Material("Copper", 4, new Color(200 / 255f, 117 / 255f, 51 / 255f), 12.9f, 1, 0.08f, 2.2f, false, 0, 0, 0));
        this.materials.add(new Material("Lead", 5, new Color(127 / 255f, 127 / 255f, 127 / 255f), 11.3f, 1, 0.05f, 1.8f, false, 0, 0, 0));//CHECK
        this.materials.add(new Material("Tin", 6, new Color(127 / 255f, 127 / 255f, 127 / 255f), 7.3f, 1, 0.35f, 1.8f, false, 0, 0, 0));
        this.materials.add(new Material("Brass", 7, new Color(181 / 255f, 166 / 255f, 66 / 255f), 8.55f, 1, 0.26f, 1.6f, false, 0, 0, 0));
        this.materials.add(new Material("Aluminium", 8, new Color(173 / 255f, 178 / 255f, 189 / 255f), 2.7f, 1, 0.32f, 1.9f, false, 0, 0, 0));
        this.materials.add(new Material("Ceramic", 9, new Color(254 / 255f, 255 / 255f, 253 / 255f), 3.21f, 1, 0.3f, 0.2f, false, 0, 0, 0));
        this.materials.add(new Material("Bronze", 10, new Color(205 / 255f, 127 / 255f, 50 / 255f), 8.15f, 1, 0.29f, 3f, false, 0, 0, 0));
        this.materials.add(new Material("Flesh", 11, new Color(255/255f, 204f/255f, 153f/255f), 1.01f, 1, 0f, 0.2f, liquids.get(0), 0.3f, 0.6f, 1f, true, 200 + 273, 700, 300));
        this.materials.add(new Material("Hard Flesh", 12, new Color(255/255f, 204f/255f, 153f/255f), 1.8f, 1, 0.05f, 0.4f, liquids.get(0), 0.25f, 0.3f, 0.5f, true, 220 + 273, 650, 300));
        this.materials.add(new Material("Asphalt", 13, new Color(0.5f, 0.6f, 0.5f), 1000f, 1f, 0f, 10f, false, 0, 0, 0));
        this.materials.add(new Material("S", 14, new Color(1f, 0f, 1f), 8f, 1, 0.5f, 0.1f,liquids.get(1),0.3f,0.4f,0.5f, false, 0, 0, 0));
        this.materials.add(new Material("Clay", 15, new Color(132 / 255f, 92 / 255f, 64 / 255f), 1.6f, 1, 0.3f, 0.1f, false, 0, 0, 0));

    }


    public Liquid getLiquidByIndex(int i) {
        return liquids.get(i);
    }
}
