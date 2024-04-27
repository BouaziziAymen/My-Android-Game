package com.evolgames.dollmutilationgame.entities.factories;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.entities.basics.Liquid;
import com.evolgames.dollmutilationgame.utilities.MyColorUtils;
import com.evolgames.gameengine.R;

import org.andengine.util.adt.color.Color;

import java.util.LinkedList;
import java.util.List;

public class Materials {
    public static List<Liquid> liquids;
    public static int BLOOD = 0;
    public static int PETROL = 1;
    public static int JUICE = 2;
    public static int KEROSENE = 3;
    public static int WATER = 4;
    public static int MILK = 5;

    static {
        liquids = new LinkedList<>();
        liquids.add(new Liquid(BLOOD, ResourceManager.getInstance().getString(R.string.blood), 0f, MyColorUtils.bloodColor));
        liquids.add(new Liquid(PETROL, ResourceManager.getInstance().getString(R.string.petrol), 0.5f, new Color(156f / 255f, 89 / 255f, 0, 0.6f)));
        liquids.add(new Liquid(JUICE, ResourceManager.getInstance().getString(R.string.juice), 0f, new Color(1f,1f,1f,0.4f)));
        liquids.add(new Liquid(KEROSENE, ResourceManager.getInstance().getString(R.string.kerosene), 0.8f, new Color(80/255f,166/255f,1f,0.3f)));
        liquids.add(new Liquid(WATER, ResourceManager.getInstance().getString(R.string.water), 0f, new Color(65f/255f,107f/255f,223/255f,0.1f)));
        liquids.add(new Liquid(MILK, ResourceManager.getInstance().getString(R.string.milk), 0f, new Color(1f,1f,1f,0.8f)));
    }

    public static Liquid getLiquidByIndex(int i) {
        return liquids.get(i);
    }

}
