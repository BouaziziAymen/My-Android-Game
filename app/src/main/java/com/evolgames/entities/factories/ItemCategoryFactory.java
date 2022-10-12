package com.evolgames.entities.factories;

import com.evolgames.entities.ItemCategory;

import java.util.ArrayList;

public class ItemCategoryFactory {
    private static final ItemCategoryFactory INSTANCE = new ItemCategoryFactory();
    public static ItemCategoryFactory getInstance() {
        return INSTANCE;
    }
    public void create() {
    }


    public ArrayList<ItemCategory> itemCategories;

    public ItemCategory getItemCategoryByIndex(int index){
        return this.itemCategories.get(index);
    }


    private ItemCategoryFactory() {
        this.itemCategories = new ArrayList<>();
        this.itemCategories.add(new ItemCategory("c0", "Projectile"));
        this.itemCategories.add(new ItemCategory("c1", "Melee"));
        this.itemCategories.add(new ItemCategory("c2", "Ranged"));
        this.itemCategories.add(new ItemCategory("c3", "Vehicle"));
    }

}
