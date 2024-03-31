package com.evolgames.userinterface.control;

import java.util.Arrays;

public enum CreationAction {
    ADD_POINT, MOVE_POINT, REMOVE_POINT, ADD_POLYGON, NONE, MIRROR, ROTATE, SHIFT, REVOLUTE, PRISMATIC, WELD, DISTANCE, MOVE_JOINT_POINT, MOVE_IMAGE, ROTATE_IMAGE, SCALE_IMAGE, PIPING, PROJECTILE, MOVE_TOOL_POINT, AMMO, FIRE_SOURCE, BOMB, LIQUID_SOURCE, DRAG, SPECIAL_POINT;

    public static CreationAction fromName(String name) {
        return Arrays.stream(values()).filter(e -> e.name().equals(name)).findFirst().orElse(null);
    }

}
