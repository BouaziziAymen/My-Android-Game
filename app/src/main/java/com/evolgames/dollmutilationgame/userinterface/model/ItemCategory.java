package com.evolgames.dollmutilationgame.userinterface.model;

import com.evolgames.gameengine.R;

public enum ItemCategory {
    SWORD(R.string.category_sword, R.string.hint_melee), AXE(R.string.category_axe, R.string.hint_melee),
    SPEAR(R.string.category_spear, R.string.hint_melee),PRODUCE(R.string.category_produce, R.string.hint_produce),
    BULLET(R.string.category_bullet,true),
    SHELL(R.string.category_shell,true),
    KNIFE(R.string.category_knife,R.string.hint_melee),
    OTHER(R.string.category_other,R.string.hint_other),
    MACE(R.string.category_mace, R.string.hint_melee),
    GRENADE(R.string.category_grenade, R.string.hint_grenade),
    GUN(R.string.category_gun, R.string.hint_gun),
    HEAVY(R.string.category_heavy_weapon, R.string.hint_heavy),
    PROJECTILE(R.string.category_projectile,true),
    ROCKET(R.string.category_rocket,R.string.hint_rocket),
    ARROW(R.string.category_arrow,true),
    BOW(R.string.category_bow, R.string.hint_bow);
    public final int nameId;
    public final boolean nonCreatable;
    public final int hint;

    ItemCategory(int nameId, int hint) {
        this.nameId = nameId;
        this.nonCreatable = false;
        this.hint = hint;
    }
    ItemCategory(int nameId, boolean nonCreatable) {
        this.nameId = nameId;
        this.nonCreatable = nonCreatable;
        this.hint = -1;
    }
    public static ItemCategory fromName(String categoryString) {
        for (ItemCategory category : values()) {
            if (category.name().equalsIgnoreCase(categoryString)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No ItemCategory with name: " + categoryString);
    }
}
