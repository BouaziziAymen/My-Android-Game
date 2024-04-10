package com.evolgames.entities.factories;

import android.util.Log;

import com.evolgames.entities.basics.Material;

import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MaterialFactory {
    public static final int SULFUR = 33;
    public static final int IVORY = 32;
    public static final int SILVER = 31;
    public static final int MARBLE = 30;
    public static final int ROCK = 29;
    public static final int TUNGSTEN = 28;
    public static final int SILK = 27;
    public static final int KEVLAR = 26;
    public static final int GRANITE = 25;
    public static final int BONE = 24;
    public static final int BRICK = 23;
    public static final int CONCRETE = 22;
    public static final int GRAPHENE = 21;
    public static final int DIAMOND = 20;
    public static final int GLASS = 19;
    public static final int PLASTIC = 15;
    public static final int CLAY = 14;
    public static final int ASPHALT = 13;
    public static final int HARD_FLESH = 12;
    public static final int FLESH = 11;
    public static final int BRONZE = 10;
    public static final int CERAMIC = 9;
    public static final int ALUMINIUM = 8;
    public static final int BRASS = 7;
    public static final int TIN = 6;
    public static final int LEAD = 5;
    public static final int COPPER = 4;
    public static final int GOLD = 3;
    public static final int IRON = 2;
    public static final int STEEL = 1;
    public static final int WOOD = 0;
    public static final int VOID = -1;
    public static final int COAL = 34;
    public static final int CHARCOAL = 35;
    public static final int IGNIUM = 36;
    public static final int TITANIUM = 37;
    public static final int LEATHER = 38;
    public static final int CARBON_FIBER = 39;
    public static final int FIBER_GLASS = 40;
    public static final int FEATHER = 41;
    public static final int TISSUE = 42;
    private static final MaterialFactory INSTANCE = new MaterialFactory();
    private static final int RUBBER = 18;
    private static final int HARD_WOOD = 17;
    private static final int HARD_STEEL = 16;
    public static Map<String, float[]> materialProperties;
    public List<Material> materials;
    public AtomicInteger counter;

    private MaterialFactory() {
        counter = new AtomicInteger();

        this.materials = new ArrayList<>();
        materialProperties = new HashMap<>();
        //  density, restitution, friction, tenacity, hardness
        materialProperties.put("Rubber", new float[]{1.1f, 0.75f, 0.6f, 5f, 3f});
        materialProperties.put("Plastic", new float[]{1.2f, 0.5f, 0.5f, 6f, 4f});
        materialProperties.put("Wood", new float[]{0.6f, 0.35f, 0.4f, 4f, 2f});
        materialProperties.put("Void", new float[]{0.1f, 0f, 0f, 10f, 0f});
        materialProperties.put("Hard Wood", new float[]{0.8f, 0.35f, 0.4f, 4f, 3f});
        materialProperties.put("Glass", new float[]{2.5f, 0.2f, 0.3f, 0.5f, 5f});
        materialProperties.put("Copper", new float[]{8.96f, 0.25f, 0.4f, 8f, 6f});
        materialProperties.put("Steel", new float[]{7.85f, 0.25f, 0.4f, 7f, 7f});
        materialProperties.put("Hard Steel", new float[]{7.85f, 0.25f, 0.4f, 7f, 8f});
        materialProperties.put("Iron", new float[]{7.87f, 0.25f, 0.4f, 6f, 6f});
        materialProperties.put("Diamond", new float[]{3.5f, 0.075f, 0.05f, 10f, 10f});
        materialProperties.put("Graphene", new float[]{1f, 0.975f, 0.05f, 10f, 10f});
        materialProperties.put("Flesh", new float[]{1f, 0.1f, 0.7f, 0.8f, 1.5f}); // Adjusted hardness value
        materialProperties.put("Hard Flesh", new float[]{1.5f, 0.15f, 0.4f, 1f, 2f}); // Adjusted hardness value
        materialProperties.put("Concrete", new float[]{2.4f, 0.25f, 0.8f, 4.75f, 7f});
        materialProperties.put("Aluminum", new float[]{2.7f, 0.25f, 0.1f, 3.75f, 7.5f});
        materialProperties.put("Brick", new float[]{1.6f, 0.25f, 0.2f, 4.5f, 6f});
        materialProperties.put("Bone", new float[]{1.7f, 0.25f, 0.2f, 5f, 5f});
        materialProperties.put("Granite", new float[]{2.75f, 0.25f, 0.2f, 6.5f, 6.5f});
        materialProperties.put("Kevlar", new float[]{1.44f, 0.25f, 0.1f, 3.75f, 8f});
        materialProperties.put("Silk", new float[]{1.3f, 0.25f, 0.05f, 3.25f, 7f});
        materialProperties.put("Tungsten", new float[]{19.25f, 0.25f, 0.1f, 7.5f, 7.5f});
        materialProperties.put("Lead", new float[]{11.34f, 0.25f, 0.2f, 1.5f, 3f});
        materialProperties.put("Tin", new float[]{7.29f, 0.25f, 0.2f, 1.5f, 4f});
        materialProperties.put("Brass", new float[]{8.4f, 0.25f, 0.2f, 3.5f, 3.5f});
        materialProperties.put("Ceramic", new float[]{2.5f, 0.25f, 0.1f, 6.5f, 6.5f});
        materialProperties.put("Asphalt", new float[]{2.5f, 0.25f, 0.2f, 2f, 4f});
        materialProperties.put("Rock", new float[]{2.7f, 0.25f, 0.2f, 6f, 6f});
        materialProperties.put("Marble", new float[]{2.7f, 0.25f, 0.15f, 3.75f, 3.75f});
        materialProperties.put("Gold", new float[]{19.32f, 0.25f, 0.05f, 2.75f, 2.75f});
        materialProperties.put("Silver", new float[]{10.49f, 0.25f, 0.05f, 2.75f, 2.75f});
        materialProperties.put("Ivory", new float[]{1.8f, 0.25f, 0.2f, 3.25f, 3.25f});
        materialProperties.put("Sulfur", new float[]{2.07f, 0.25f, 0.3f, 2f, 2f});
        materialProperties.put("Coal", new float[]{1.1f, 0.25f, 0.3f, 2f, 2f});
        materialProperties.put("Charcoal", new float[]{0.22f, 0.25f, 0.2f, 1f, 1f});
        materialProperties.put("Clay", new float[]{2f, 0.25f, 0.2f, 0.1f, 2f});
        materialProperties.put("Bronze", new float[]{8.7f, 0.25f, 0.05f, 3.5f, 3.5f});
        materialProperties.put("Ignium", new float[]{6f, 0.075f, 0.1f, 8.5f, 8f});
        materialProperties.put("Titanium", new float[]{4.5f, 0.25f, 0.1f, 6.5f, 6.5f});
        materialProperties.put("Leather", new float[]{1.05f, 0.35f, 0.6f, 6f, 2f});
        materialProperties.put("Carbon Fiber", new float[]{1.75f, 0.4f, 0.8f, 8f, 7f});
        materialProperties.put("Fiber Glass", new float[]{2.5f, 0.5f, 0.6f, 7f, 5f});
        materialProperties.put("Feather", new float[]{0.04f, 0.25f, 0.35f, 3f, 1.5f});
        materialProperties.put("Tissue", new float[]{0.5f, 0.1f, 0.8f, 5f, 1.5f});


        Material rubber = new Material("Rubber", RUBBER, new Color(0.4f, 0.4f, 0.4f), 0.8f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material plastic = new Material("Plastic", PLASTIC, new Color(1.0f, 0.0f, 0.0f), 0.5f, 0, 0.0f, 0.0f, 0.0f, true, 600, 600, false, 0f, 300);
        Material wood = new Material("Wood", WOOD, new Color(0.545f, 0.271f, 0.075f), 0.6f, 0, 0.0f, 0.0f, 0.0f, true, 500, 700, false, 0f, 500);
        Material hardWood = new Material("Hard Wood", HARD_WOOD, new Color(0.545f, 0.271f, 0.075f), 0.6f, 0, 0.0f, 0.0f, 0.0f, true, 600, 700, false, 0f, 400);
        Material glass = new Material("Glass", GLASS, new Color(1.0f, 1.0f, 1.0f), 0.7f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material copper = new Material("Copper", COPPER, new Color(0.722f, 0.451f, 0.2f), 0.8f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material steel = new Material("Steel", STEEL, new Color(0.7f, 0.7f, 0.7f), 0.9f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material hardSteel = new Material("Hard Steel", HARD_STEEL, new Color(0.75f, 0.75f, 0.75f), 0.9f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material iron = new Material("Iron", IRON, new Color(0.878f, 0.878f, 0.878f), 0.8f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material diamond = new Material("Diamond", DIAMOND, new Color(0.0f, 0.0f, 0.0f, 0.1f), 1.0f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material graphene = new Material("Graphene", GRAPHENE, new Color(1.0f, 1.0f, 1.0f), 1.0f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material flesh = new Material("Flesh", FLESH, new Color(255 / 255f, 204f / 255f, 153f / 255f), 0.7f, 0, 0.8f, 0.3f, 0.4f, true, 600, 650, false, 0.0f, 300.0);
        Material hardFlesh = new Material("Hard Flesh", HARD_FLESH, new Color(255 / 255f, 204f / 255f, 153f / 255f), 0.7f, 0, 0.8f, 0.2f, 0.5f, true, 600, 650, false, 0.0f, 300.0);
        Material concrete = new Material("Concrete", CONCRETE, new Color(0.663f, 0.663f, 0.663f), 0.8f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material aluminum = new Material("Aluminum", ALUMINIUM, new Color(0.753f, 0.753f, 0.753f), 0.8f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material brick = new Material("Brick", BRICK, new Color(0.698f, 0.133f, 0.133f), 0.7f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material bone = new Material("Bone", BONE, new Color(1.0f, 0.98f, 0.94f), 0.6f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material granite = new Material("Granite", GRANITE, new Color(0.502f, 0.502f, 0.502f), 0.7f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material kevlar = new Material("Kevlar", KEVLAR, new Color(1.0f, 1.0f, 1.0f), 0.8f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material silk = new Material("Silk", SILK, new Color(1.0f, 1.0f, 1.0f), 0.6f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material tungsten = new Material("Tungsten", TUNGSTEN, new Color(0.753f, 0.753f, 0.753f), 0.9f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material lead = new Material("Lead", LEAD, new Color(0.663f, 0.663f, 0.663f), 0.5f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material tin = new Material("Tin", TIN, new Color(0.663f, 0.663f, 0.663f), 0.4f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material brass = new Material("Brass", BRASS, new Color(225f / 255f, 193 / 255f, 110 / 255f), 0.7f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material ceramic = new Material("Ceramic", CERAMIC, new Color(1.0f, 1.0f, 1.0f), 0.9f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material asphalt = new Material("Asphalt", ASPHALT, new Color(0.412f, 0.412f, 0.412f), 0.7f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material rock = new Material("Rock", ROCK, new Color(0.502f, 0.502f, 0.502f), 0.8f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material marble = new Material("Marble", MARBLE, new Color(1.0f, 1.0f, 1.0f), 0.6f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material gold = new Material("Gold", GOLD, new Color(1.0f, 0.843f, 0.0f), 0.9f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material silver = new Material("Silver", SILVER, new Color(0.753f, 0.753f, 0.753f), 0.9f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material ivory = new Material("Ivory", IVORY, new Color(1.0f, 1.0f, 0.941f), 0.8f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material sulfur = new Material("Sulfur", SULFUR, new Color(1.0f, 1.0f, 0.0f), 0.4f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material coal = new Material("Coal", COAL, new Color(0.078f, 0.078f, 0.078f), 0.7f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material charcoal = new Material("Charcoal", CHARCOAL, new Color(0.133f, 0.133f, 0.133f), 0.7f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material clay = new Material("Clay", CLAY, new Color(1.0f, 0.0f, 0.0f), 0.6f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material bronze = new Material("Bronze", BRONZE, new Color(0.803f, 0.584f, 0.459f), 0.7f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material ignium = new Material("Ignium", IGNIUM, new Color(1.0f, 0.5f, 0.0f), 0.9f, 0, 0.0f, 0.0f, 0.0f, true, 25.0, 10000.0, false, 0f, 1000000.0);
        Material titanium = new Material("Titanium", TITANIUM, new Color(0.5f, 0.5f, 0.5f), 0.9f, 0, 0.0f, 0.0f, 0.0f, false, 0.0, 0.0, false, 0.0f, 0.0);
        Material leather = new Material("Leather", LEATHER, new Color(0.804f, 0.522f, 0.247f), 0.7f, 0, 0.0f, 0.0f, 0.0f, true, 500, 600, false, 0.0f, 200.0f);
        Material carbonFiber = new Material("Carbon Fiber", CARBON_FIBER, new Color(0.235f, 0.235f, 0.235f), 0.83f, 0, 0.0f, 0.0f, 0.0f, true, 600, 600, false, 0.0f, 0f);
        Material fiberGlass = new Material("Fiber Glass", FIBER_GLASS, new Color(1f, 1f, 1f, 0.5f), 0.83f, 0, 0.0f, 0.0f, 0.0f, false, 0, 0, false, 0.0f, 200.0f);
        Material feather = new Material("Feather", FEATHER, new Color(0.9f, 0.9f, 0.9f), 0.83f, 0, 0.0f, 0.0f, 0.0f, true, 500, 500, false, 0.0f, 200.0f);
        Material tissue = new Material("Tissue", TISSUE, new Color(0.9f, 0.9f, 0.9f), 0.7f, 0, 0.0f, 0.0f, 0.0f, true, 500, 600, false, 0.0f, 200.0f);
        Material thevoid = new Material("Void", VOID, new Color(0f, 0f, 0f,0f), 0f, 0, 0.0f, 0.0f, 0.0f, false, 0, 0, false, 0f, 0f);
        materials.add(thevoid);
        materials.add(tissue);
        materials.add(carbonFiber);
        materials.add(fiberGlass);
        materials.add(feather);
        materials.add(iron);
        materials.add(hardWood);
        materials.add(hardSteel);
        materials.add(rubber);
        materials.add(plastic);
        materials.add(wood);
        materials.add(glass);
        materials.add(copper);
        materials.add(steel);
        materials.add(diamond);
        materials.add(graphene);
        materials.add(flesh);
        materials.add(hardFlesh);
        materials.add(concrete);
        materials.add(aluminum);
        materials.add(brick);
        materials.add(bone);
        materials.add(granite);
        materials.add(kevlar);
        materials.add(silk);
        materials.add(tungsten);
        materials.add(lead);
        materials.add(tin);
        materials.add(brass);
        materials.add(ceramic);
        materials.add(asphalt);
        materials.add(rock);
        materials.add(marble);
        materials.add(gold);
        materials.add(silver);
        materials.add(ivory);
        materials.add(sulfur);
        materials.add(coal);
        materials.add(charcoal);
        materials.add(clay);
        materials.add(bronze);
        materials.add(ignium);
        materials.add(titanium);
        materials.add(leather);
        materials.sort(Comparator.comparing(Material::getName));

    }

    public static MaterialFactory getInstance() {
        return INSTANCE;
    }

    public void create() {
    }

    public Material getMaterialByIndex(int index) {
        try {
            return this.materials.stream().filter(e -> e.getIndex() == index).findFirst().get();
        } catch (Throwable t) {
            Log.e("Error loading material", t.toString());
            return null;
        }
    }


    public enum MaterialAcousticType{
        SOFT,METAL,HARD_METAL,WOOD, ROCK, GLASS
    }

    public MaterialAcousticType getMaterialAcousticType(int materialIndex){
        if(materialIndex==WOOD||materialIndex==HARD_WOOD||materialIndex==CHARCOAL||materialIndex==SULFUR
                ||materialIndex==IVORY||materialIndex==PLASTIC||materialIndex==CARBON_FIBER){
            return MaterialAcousticType.WOOD;
        }
        if(materialIndex==FLESH||materialIndex==HARD_FLESH||materialIndex==TISSUE||materialIndex==LEATHER||materialIndex==RUBBER){
            return MaterialAcousticType.SOFT;
        }
        if(materialIndex==SILVER||materialIndex==GOLD||materialIndex==TUNGSTEN||materialIndex==BRASS
                ||materialIndex==COPPER||materialIndex==LEAD||materialIndex==TITANIUM||materialIndex==ALUMINIUM){
            return MaterialAcousticType.METAL;
        }
        if(materialIndex==IRON||materialIndex==STEEL||materialIndex==IGNIUM){
            return MaterialAcousticType.HARD_METAL;
        }
        if(materialIndex==MARBLE||materialIndex==ROCK||materialIndex==ASPHALT||materialIndex==CONCRETE||materialIndex==DIAMOND||materialIndex==GRANITE){
            return MaterialAcousticType.HARD_METAL;
        }
        if(materialIndex==GLASS||materialIndex==FIBER_GLASS||materialIndex==CLAY){
            return MaterialAcousticType.GLASS;
        }
        return null;
    }

}
