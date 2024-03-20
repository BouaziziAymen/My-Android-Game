package com.evolgames.userinterface.view;

import com.evolgames.userinterface.control.CreationAction;

import java.util.Arrays;

public enum Screen {
    DRAW_SCREEN,
    JOINTS_SCREEN,
    ITEMS_SCREEN,
    IMAGE_SCREEN,
    SAVE_SCREEN,
    NONE;
    public static Screen fromName(String name){
        return Arrays.stream(values()).filter(e->e.name().equals(name)).findFirst().orElse(null);
    }

}
