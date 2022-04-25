package com.evolgames.userinterface.control.behaviors.actions;

public abstract class Condition {

    public abstract boolean isCondition(float value);

    public abstract String getError();
}
