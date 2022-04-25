package com.evolgames.userinterface.view.layouts;

import com.evolgames.userinterface.sections.Section;
import com.evolgames.userinterface.view.basics.Element;
import java.util.ArrayList;
import java.util.Comparator;


public abstract class SectionLayout<S extends Section<Primary>,Primary extends Element> extends LinearLayout{
    protected ArrayList<S> sectionsList = new ArrayList<>();
    public SectionLayout(float pX, float pY, Direction direction) {
        super(pX, pY, direction,5);
    }
    protected abstract S createSection(int key,Primary p,boolean isActive);
    protected abstract S createDummySection(Element e);

    public void addPrimary(int key, Primary p,boolean isActive){
        S section = createSection(key,p,isActive);
        p.setSection(section);
        section.setActive(isActive);
        sectionsList.add(section);
        sectionsList.sort(Comparator.comparing(Section::getSectionKey));
    }
    public void addDummySection(Element element){
        S section = createDummySection(element);
        element.setSection(section);
        sectionsList.add(section);
    }







}
