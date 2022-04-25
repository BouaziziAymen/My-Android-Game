package com.evolgames.userinterface.sections;

import com.evolgames.userinterface.view.basics.Element;

import java.util.Collections;

public class ThreeLevelSection<Primary extends Element,Secondary extends Element,Tertiary extends Element,Quaternary extends Element> extends CompositeSection<TwoLevelSection<Secondary,Tertiary,Quaternary>,Primary,Secondary> {

    public ThreeLevelSection(int sectionKey, Primary primary, boolean isActive) {
        super(sectionKey, primary, isActive);
    }

    public ThreeLevelSection(Element e) {
        super(e);
    }
    public void swapSecondaries(int index1, int index2){
        Collections.swap(children,index1,index2);

    }
    public void addSecondary(int secondaryKey, Secondary secondary,boolean isActive) {
        TwoLevelSection<Secondary,Tertiary,Quaternary> secondarySection = new TwoLevelSection<>(secondaryKey,secondary,isActive);
        secondary.setSection(secondarySection);
        children.add(secondarySection);
    }

    public void addSecondary(int secondaryKey, Secondary secondary) {
        addSecondary(secondaryKey,secondary,true);
    }
    public void addTertiary(int secondaryKey, int tertiaryKey, Tertiary tertiary,boolean isActive){
        TwoLevelSection<Secondary, Tertiary,Quaternary> secondarySection =getSectionByKey(secondaryKey);
        secondarySection.addSecondary(tertiaryKey,tertiary,isActive);
    }

    public void addQuaternary(int secondaryKey, int tertiaryKey, int quaternaryKey,Quaternary quaternary){
        TwoLevelSection<Secondary, Tertiary,Quaternary> secondarySection =getSectionByKey(secondaryKey);
        secondarySection.addTertiary(tertiaryKey,quaternaryKey,quaternary);
    }

    public TwoLevelSection<Secondary, Tertiary,Quaternary> getSectionByKey(int secondaryKey) {
        for(TwoLevelSection<Secondary,Tertiary,Quaternary> section:getChildren()){
            if(section.getSectionKey()==secondaryKey)return section;
        }
        return null;
    }

    public int getIndexOfKey(int secondaryKey) {
        for(TwoLevelSection<Secondary,Tertiary,Quaternary> section:getChildren()){
            if(section.getSectionKey()==secondaryKey)return getChildren().indexOf(section);
        }
        return -1;
    }

    public TwoLevelSection<Secondary, Tertiary,Quaternary> getSectionByIndex(int index) {
        return children.get(index);
    }

    public void removeSection(int secondaryKey) {
        children.remove(getSectionByKey(secondaryKey));
    }


}
