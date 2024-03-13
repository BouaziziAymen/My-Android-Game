package com.evolgames.entities.factories;

import com.evolgames.entities.basics.Liquid;
import com.evolgames.entities.basics.Material;
import com.evolgames.utilities.MyColorUtils;

import org.andengine.util.adt.color.Color;

import java.util.ArrayList;

public class Materials {
    public static ArrayList<Liquid> liquids;
    static {
        liquids = new ArrayList<>();
        liquids.add(new Liquid(0, "Blood", 0f, MyColorUtils.bloodColor));
        liquids.add(new Liquid(1, "Goo", 0f, new Color(0.1f, 0.1f, 0.1f, 0.5f)));
        liquids.add(new Liquid(2, "Petrol", 0.5f, new Color(156f / 255f, 89 / 255f, 0, 0.5f)));
    }

    public static Liquid getLiquidByIndex(int i) {
        return liquids.get(i);
    }

}
