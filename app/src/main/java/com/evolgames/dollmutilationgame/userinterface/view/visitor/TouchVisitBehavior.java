package com.evolgames.dollmutilationgame.userinterface.view.visitor;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Touchable;

import org.andengine.input.touch.TouchEvent;

public class TouchVisitBehavior extends VisitBehavior {

    private TouchEvent sceneTouchEvent;

    private boolean isTouched;
    private boolean locked;

    public void setSceneTouchEvent(TouchEvent e) {
        this.sceneTouchEvent = e;
    }

    @Override
    protected void visitElement(Element e) {
        if (e instanceof Touchable) {
            if (e.isVisible() && !e.isShaded()) {
                boolean touched = ((Touchable) e).onTouchHud(sceneTouchEvent);
                if (touched) {
                    isTouched = true;
                    if (e instanceof Button) {
                        setLocked(true);
                    }
                }
            }
        }
    }

    @Override
    protected boolean forkCondition(Element e) {
        return e.isVisible();
    }

    @Override
    protected boolean carryOnCondition() {
        return !isLocked();
    }

    public boolean isTouched() {
        return isTouched;
    }

    public void setTouched(boolean b) {
        isTouched = b;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
