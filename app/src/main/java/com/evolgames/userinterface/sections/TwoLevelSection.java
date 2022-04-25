package com.evolgames.userinterface.sections;

import com.evolgames.userinterface.view.basics.Element;

import java.util.Collections;


public class TwoLevelSection<Primary extends Element,Secondary extends Element,Tertiary extends Element> extends CompositeSection<OneLevelSection<Secondary,Tertiary>,Primary,Secondary> {
    public TwoLevelSection(int sectionKey,Primary primary, boolean isActive) {
        super(sectionKey,primary, isActive);
    }
public TwoLevelSection(Element dummyElement){
        super(dummyElement);

}


     public void swapSecondaries(int index1, int index2){
         Collections.swap(children,index1,index2);

     }
    public void addSecondary(int secondaryKey, Secondary secondary,boolean isActive) {
      OneLevelSection<Secondary,Tertiary> secondarySection = new OneLevelSection<>(secondaryKey,secondary,isActive);
        secondary.setSection(secondarySection);
      children.add(secondarySection);
    }

    public void addSecondary(int secondaryKey, Secondary secondary) {
        addSecondary(secondaryKey,secondary,true);
    }
    public void addTertiary(int secondaryKey, int tertiaryKey, Tertiary tertiary){
        OneLevelSection<Secondary, Tertiary> secondarySection =getSectionByKey(secondaryKey);
        secondarySection.addSecondary(tertiaryKey,tertiary);
    }


    public OneLevelSection<Secondary, Tertiary> getSectionByKey(int secondaryKey) {
        for(OneLevelSection<Secondary,Tertiary> section:getChildren()){
            if(section.getSectionKey()==secondaryKey)return section;
        }
        return null;
    }

    public int getIndexOfKey(int secondaryKey) {
        for(OneLevelSection<Secondary,Tertiary> section:getChildren()){
            if(section.getSectionKey()==secondaryKey)return getChildren().indexOf(section);
        }
        return -1;
    }

    public OneLevelSection<Secondary, Tertiary> getSectionByIndex(int index) {
        return children.get(index);
    }

    public void removeSection(int secondaryKey) {
        children.remove(getSectionByKey(secondaryKey));
    }
}
