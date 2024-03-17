package com.evolgames.userinterface.view.layouts;

import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.sections.OneLevelSection;
import com.evolgames.userinterface.view.sections.ZeroLevelSection;

import java.util.ArrayList;

public class OneLevelSectionLayout<Primary extends Element, Secondary extends Element>
        extends SectionLayout<OneLevelSection<Primary, Secondary>, Primary> {
    public OneLevelSectionLayout(float pX, float pY, Direction direction) {
        super(pX, pY, direction);
    }

    @Override
    protected OneLevelSection<Primary, Secondary> createSection(
            int key, Primary p, boolean isActive) {
        return new OneLevelSection<>(key, p, isActive);
    }

    @Override
    protected OneLevelSection<Primary, Secondary> createDummySection(Element e) {
        return new OneLevelSection<>(e);
    }

    public void addSecondary(int primaryKey, int secondaryKey, Secondary s) {
        OneLevelSection<Primary, Secondary> section = getSectionByKey(primaryKey);
        section.addSecondary(secondaryKey, s);
    }

    public OneLevelSection<Primary, Secondary> getSectionByKey(int sectionKey) {
        for (OneLevelSection<Primary, Secondary> section : sectionsList)
            if (sectionKey == section.getSectionKey()) return section;
        return null;
    }

    public Primary getPrimaryByIndex(int index) {
        return sectionsList.get(index).getPrimary();
    }

    public Secondary getSecondaryByIndex(int primaryKey, int index) {
        return getSectionByKey(primaryKey).getSectionByIndex(index).getPrimary();
    }

    public int getPrimariesSize() {
        return sectionsList.size();
    }

    public int getSecondariesSize(int primaryKey) {
        return getSectionByKey(primaryKey).getChildren().size();
    }

    @Override
    public void updateLayout() {
        super.updateLayout();
        for (int i = 0; i < sectionsList.size(); i++) {
            OneLevelSection<Primary, Secondary> section = sectionsList.get(i);
            if (section.isDummy()) {
                addToLayout(section.getDummyElement());
                continue;
            } else addToLayout(section.getPrimary());
            if (!section.isActive()) {
                continue;
            }
            ArrayList<ZeroLevelSection<Secondary>> secondaryFields = section.getChildren();
            for (int j = 0; j < secondaryFields.size(); j++) {
                ZeroLevelSection<Secondary> secondarySection = secondaryFields.get(j);
                addToLayout(secondarySection.getPrimary());
            }
        }
    }

    public void removePrimary(int primaryKey) {
        OneLevelSection removedSection = null;
        for (OneLevelSection s : sectionsList) {
            if (s.getSectionKey() == primaryKey) {
                removedSection = s;
                break;
            }
        }
        if (removedSection != null) {
            sectionsList.remove(removedSection);
        }
    }

    public ArrayList<Primary> getPrimaries() {
        ArrayList<Primary> primaries = new ArrayList<>();
        for (OneLevelSection<Primary, Secondary> section : sectionsList) {
            primaries.add(section.getPrimary());
        }
        return primaries;
    }

    public void removeSecondary(int primaryKey, int secondaryKey) {
        System.out.println("------------------Remove Secondary:" + primaryKey + ":" + secondaryKey);
        getSectionByKey(primaryKey).removeSection(secondaryKey);
    }

    public Primary getPrimary(int primaryKey) {
        return getSectionByKey(primaryKey).getPrimary();
    }

    public Secondary getSecondary(int primaryKey, int secondaryKey) {
        return getSectionByKey(primaryKey).getSectionByKey(secondaryKey).getPrimary();
    }
}
