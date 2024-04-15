package com.evolgames.entities.factories;

import com.evolgames.entities.basics.Liquid;
import com.evolgames.utilities.MyColorUtils;

import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Materials {
    public static List<Liquid> liquids;

    static {
        liquids = new LinkedList<>();
        liquids.add(new Liquid(0, "Blood", 0f, MyColorUtils.bloodColor));
        liquids.add(new Liquid(1, "Petrol", 0.5f, new Color(156f / 255f, 89 / 255f, 0, 0.5f)));
        liquids.add(new Liquid(2, "Juice", 0f, new Color(1f,1f,1f,0.2f)));
    }

    public static Liquid getLiquidByIndex(int i) {
        return liquids.get(i);
    }

}
