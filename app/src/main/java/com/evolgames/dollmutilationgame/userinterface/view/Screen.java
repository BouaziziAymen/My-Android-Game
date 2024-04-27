package com.evolgames.dollmutilationgame.userinterface.view;

import java.util.Arrays;

public enum Screen {
    DRAW_SCREEN,
    JOINTS_SCREEN,
    ITEMS_SCREEN,
    IMAGE_SCREEN,
    SAVE_SCREEN,
    NONE;

    public static Screen fromName(String name) {
        return Arrays.stream(values()).filter(e -> e.name().equals(name)).findFirst().orElse(null);
    }

}
