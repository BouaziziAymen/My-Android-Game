package com.evolgames.dollmutilationgame.userinterface.view.sections;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;

public class Section<Primary extends Element> {
    private final int sectionKey;
    private boolean isActive;
    private Primary primary;
    private boolean isDummy;
    private Element dummyElement;

    public Section(int key, Primary primary, boolean isActive) {
        this.sectionKey = key;
        this.primary = primary;
        this.isActive = isActive;
    }

    public Section(Element dummyElement) {
        this.isDummy = true;
        this.dummyElement = dummyElement;
        sectionKey = -1;
    }

    public int getSectionKey() {
        return sectionKey;
    }

    public boolean isDummy() {
        return isDummy;
    }

    public Element getDummyElement() {
        return dummyElement;
    }

    public Primary getPrimary() {

        return primary;
    }

    public void setPrimary(Primary primary) {
        this.primary = primary;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
