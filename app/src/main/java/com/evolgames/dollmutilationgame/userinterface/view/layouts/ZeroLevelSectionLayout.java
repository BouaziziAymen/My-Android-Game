package com.evolgames.dollmutilationgame.userinterface.view.layouts;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;
import com.evolgames.dollmutilationgame.userinterface.view.sections.ZeroLevelSection;

public class ZeroLevelSectionLayout<Primary extends Element>
        extends SectionLayout<ZeroLevelSection<Primary>, Primary> {
    public ZeroLevelSectionLayout(float pX, float pY, Direction direction) {
        super(pX, pY, direction);
    }

    @Override
    protected ZeroLevelSection<Primary> createSection(int key, Primary p, boolean isActive) {
        return new ZeroLevelSection<>(key, p);
    }

    @Override
    protected ZeroLevelSection<Primary> createDummySection(Element e) {
        return new ZeroLevelSection<>(e);
    }

    public void removePrimary(int primaryKey) {
        sectionsList.remove(getSectionByKey(primaryKey));
    }

    public ZeroLevelSection<Primary> getSectionByKey(int sectionKey) {
        for (ZeroLevelSection<Primary> section : sectionsList)
            if (sectionKey == section.getSectionKey()) return section;
        return null;
    }

    @Override
    public void updateLayout() {
        super.updateLayout();
        for (int i = 0; i < sectionsList.size(); i++) {

            ZeroLevelSection<Primary> section = sectionsList.get(i);
            if (section.isDummy()) {
                addToLayout(section.getDummyElement());
                continue;
            }
            addToLayout(section.getPrimary());
        }
    }

    public int getPrimariesSize() {
        return sectionsList.size();
    }

    public Primary getPrimaryByIndex(int index) {
        return sectionsList.get(index).getPrimary();
    }
}
