package com.evolgames.entities.factories;

import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.basics.Liquid;
import com.evolgames.gameengine.R;
import com.evolgames.utilities.MyColorUtils;

import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Materials {
    public static List<Liquid> liquids;

    static {
        liquids = new LinkedList<>();
        liquids.add(new Liquid(0, ResourceManager.getInstance().getString(R.string.blood), 0f, MyColorUtils.bloodColor));
        liquids.add(new Liquid(1, ResourceManager.getInstance().getString(R.string.petrol), 0.5f, new Color(156f / 255f, 89 / 255f, 0, 0.5f)));
        liquids.add(new Liquid(2, ResourceManager.getInstance().getString(R.string.juice), 0f, new Color(1f,1f,1f,0.2f)));
        liquids.add(new Liquid(3, ResourceManager.getInstance().getString(R.string.kerosene), 0.8f, new Color(80/255f,166/255f,1f)));
        liquids.add(new Liquid(4, ResourceManager.getInstance().getString(R.string.water), 0f, new Color(65f/255f,107f/255f,223/255f,0.1f)));
        liquids.add(new Liquid(5, ResourceManager.getInstance().getString(R.string.milk), 0f, new Color(1f,1f,1f)));
    }

    public static Liquid getLiquidByIndex(int i) {
        return liquids.get(i);
    }

}
