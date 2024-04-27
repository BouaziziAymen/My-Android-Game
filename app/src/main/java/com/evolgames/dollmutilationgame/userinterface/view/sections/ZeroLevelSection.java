package com.evolgames.dollmutilationgame.userinterface.view.sections;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;

public class ZeroLevelSection<Primary extends Element> extends Section<Primary> {

    public ZeroLevelSection(int sectionKey, Primary primary) {

        super(sectionKey, primary, false);
    }

    public ZeroLevelSection(Element e) {
        super(e);
    }
}
