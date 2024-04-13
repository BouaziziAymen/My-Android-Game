package com.evolgames.userinterface.model;

import com.evolgames.gameengine.R;

public enum ItemCategory {
    SWORD(R.string.category_sword), AXE(R.string.category_axe), SPEAR(R.string.category_spear),PRODUCE(R.string.category_produce),
    BULLET(R.string.category_bullet,true),
    SHELL(R.string.category_shell,true),
    KNIFE(R.string.category_knife),
    OTHER(R.string.category_other),
    MACE(R.string.category_mace),
    GRENADE(R.string.category_grenade),
    GUN(R.string.category_gun),
    HEAVY(R.string.category_heavy_weapon),
    PROJECTILE(R.string.category_projectile,true),
    ROCKET(R.string.category_rocket),
    ARROW(R.string.category_arrow,true),
    BOW(R.string.category_bow);
    public final int nameId;
    public final boolean nonCreatable;

    ItemCategory(int nameId) {
        this.nameId = nameId;
        this.nonCreatable = false;
    }
    ItemCategory(int nameId, boolean nonCreatable) {
        this.nameId = nameId;
        this.nonCreatable = nonCreatable;
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
