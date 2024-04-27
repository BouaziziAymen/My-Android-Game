package com.evolgames.dollmutilationgame.userinterface.view.sections;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;

public class OneLevelSection<Primary extends Element, Secondary extends Element>
        extends CompositeSection<ZeroLevelSection<Secondary>, Primary, Secondary> {

    public OneLevelSection(int sectionKey, Primary primary, boolean isActive) {
        super(sectionKey, primary, isActive);
    }

    public OneLevelSection(Element e) {
        super(e);
    }

    public void addSecondary(int secondaryKey, Secondary secondary) {
        ZeroLevelSection<Secondary> secondarySection = new ZeroLevelSection<>(secondaryKey, secondary);
        secondary.setSection(secondarySection);
        children.add(secondarySection);
    }

    public ZeroLevelSection<Secondary> getSectionByKey(int key) {
        for (ZeroLevelSection<Secondary> section : getChildren()) {
            if (section.getSectionKey() == key) return section;
        }
        return null;
    }

    public ZeroLevelSection<Secondary> getSectionByIndex(int index) {
        return children.get(index);
    }

    public void removeSection(int key) {
        children.remove(getSectionByKey(key));
    }
}
