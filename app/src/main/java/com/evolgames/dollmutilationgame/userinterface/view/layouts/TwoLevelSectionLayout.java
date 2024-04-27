package com.evolgames.dollmutilationgame.userinterface.view.layouts;


import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;
import com.evolgames.dollmutilationgame.userinterface.view.sections.OneLevelSection;
import com.evolgames.dollmutilationgame.userinterface.view.sections.TwoLevelSection;
import com.evolgames.dollmutilationgame.userinterface.view.sections.ZeroLevelSection;

import java.util.ArrayList;

public class TwoLevelSectionLayout<
        Primary extends Element, Secondary extends Element, Tertiary extends Element>
        extends SectionLayout<TwoLevelSection<Primary, Secondary, Tertiary>, Primary> {
    public TwoLevelSectionLayout(float pX, float pY, Direction direction) {
        super(pX, pY, direction);
    }

    public TwoLevelSection<Primary, Secondary, Tertiary> getSectionByKey(int sectionKey) {
        for (TwoLevelSection<Primary, Secondary, Tertiary> section : sectionsList)
            if (sectionKey == section.getSectionKey()) return section;
        return null;
    }

    public Primary getPrimaryByIndex(int index) {
        return sectionsList.get(index).getPrimary();
    }

    public Secondary getSecondaryByIndex(int primaryKey, int index) {
        return getSectionByKey(primaryKey).getSectionByIndex(index).getPrimary();
    }

    public Tertiary getTertiaryByIndex(int primaryKey, int secondaryKey, int index) {
        return getSectionByKey(primaryKey)
                .getSectionByKey(secondaryKey)
                .getChildren()
                .get(index)
                .getPrimary();
    }

    public int getPrimariesSize() {
        return sectionsList.size();
    }

    public int getSecondariesSize(int primaryKey) {
        return getSectionByKey(primaryKey).getChildren().size();
    }

    public int getTertiariesSize(int primaryKey, int secondaryKey) {
        return getSectionByKey(primaryKey).getSectionByKey(secondaryKey).getChildren().size();
    }

    public Secondary getSecondary(int primaryKey, int secondaryKey) {
        return getSectionByKey(primaryKey).getSectionByKey(secondaryKey).getPrimary();
    }

    public Tertiary getTertiary(int primaryKey, int secondaryKey, int tertiaryKey) {
        return getSectionByKey(primaryKey)
                .getSectionByKey(secondaryKey)
                .getSectionByKey(tertiaryKey)
                .getPrimary();
    }

    @Override
    protected TwoLevelSection<Primary, Secondary, Tertiary> createSection(
            int key, Primary p, boolean isActive) {
        return new TwoLevelSection<>(key, p, isActive);
    }

    @Override
    protected TwoLevelSection<Primary, Secondary, Tertiary> createDummySection(Element e) {
        return new TwoLevelSection<Primary, Secondary, Tertiary>(e);
    }

    public void addSecondary(int primaryKey, int secondaryKey, Secondary s, boolean isActive) {
        TwoLevelSection<Primary, Secondary, Tertiary> section = getSectionByKey(primaryKey);
        section.addSecondary(secondaryKey, s, isActive);
    }

    public void addSecondary(int primaryKey, int secondaryKey, Secondary s) {
        addSecondary(primaryKey, secondaryKey, s, false);
    }

    public void addTertiary(int primaryKey, int secondaryKey, int tertiaryKey, Tertiary t) {
        TwoLevelSection<Primary, Secondary, Tertiary> section = getSectionByKey(primaryKey);
        section.addTertiary(secondaryKey, tertiaryKey, t);
    }

    @Override
    public void updateLayout() {
        super.updateLayout();
        for (int i = 0; i < sectionsList.size(); i++) {
            TwoLevelSection<Primary, Secondary, Tertiary> section = sectionsList.get(i);
            if (section.isDummy()) {
                addToLayout(section.getDummyElement());
                continue;
            } else addToLayout(section.getPrimary());
            if (!section.isActive()) continue;

            ArrayList<OneLevelSection<Secondary, Tertiary>> secondarySections = section.getChildren();
            for (int j = 0; j < secondarySections.size(); j++) {
                OneLevelSection<Secondary, Tertiary> secondarySection = secondarySections.get(j);
                addToLayout(secondarySection.getPrimary());
                if (!secondarySection.isActive()) continue;
                ArrayList<ZeroLevelSection<Tertiary>> tertiarySections = secondarySection.getChildren();
                for (int k = 0; k < tertiarySections.size(); k++) {
                    ZeroLevelSection<Tertiary> tertiarySection = tertiarySections.get(k);
                    addToLayout(tertiarySection.getPrimary());
                }
            }
        }
    }

    public void removeSecondary(int primaryKey, int secondaryKey) {
        getSectionByKey(primaryKey).removeSection(secondaryKey);
    }

    public void removePrimary(int primaryKey) {
        sectionsList.remove(getSectionByKey(primaryKey));
    }

    public void removeTertiary(int primaryKey, int secondaryKey, int tertiaryKey) {
        getSectionByKey(primaryKey).getSectionByKey(secondaryKey).removeSection(tertiaryKey);
    }

    public ArrayList<Primary> getPrimaries() {
        ArrayList<Primary> primaries = new ArrayList<>();
        for (TwoLevelSection<Primary, Secondary, Tertiary> section : sectionsList) {
            if (section.getPrimary() != null) primaries.add(section.getPrimary());
        }
        return primaries;
    }

    public Primary getPrimaryByKey(int primaryKey) {
        return getSectionByKey(primaryKey).getPrimary();
    }
}
