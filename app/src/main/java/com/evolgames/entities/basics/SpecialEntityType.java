package com.evolgames.entities.basics;

public enum SpecialEntityType {
    Default,
    Head(true),
    UpperTorso(true),
    LowerTorso,
    UpperArmRight,
    UpperArmLeft,
    LowerArmRight,
    LowerArmLeft,
    RightHand,
    LeftHand,
    UpperLegRight,
    UpperLegLeft,
    LowerLegRight,
    LowerLegLeft,
    RightFoot,
    LeftFoot;
    public final boolean vital;
    SpecialEntityType(){vital = false;}
    SpecialEntityType(boolean vital){
        this.vital = vital;
    }
}
