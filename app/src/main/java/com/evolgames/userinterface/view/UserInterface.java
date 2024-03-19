package com.evolgames.userinterface.view;

import com.evolgames.activity.ResourceManager;
import com.evolgames.scenes.AbstractScene;
import com.evolgames.userinterface.view.basics.Container;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.inputs.Touchable;
import com.evolgames.userinterface.view.visitor.ContentTraverser;
import com.evolgames.userinterface.view.visitor.IsUpdatedVisitBehavior;
import com.evolgames.userinterface.view.visitor.ShadeVisitBehavior;
import com.evolgames.userinterface.view.visitor.StepVisitBehavior;
import com.evolgames.userinterface.view.visitor.TouchVisitBehavior;
import com.evolgames.userinterface.view.visitor.VisitBehavior;

import org.andengine.input.touch.TouchEvent;

public abstract class UserInterface<T extends AbstractScene> extends Container
        implements Touchable {

    protected final T scene;
    // behaviors
    protected final StepVisitBehavior updateVisitBehavior = new StepVisitBehavior();
    // other
    private final ContentTraverser contentTraverser = new ContentTraverser();
    private final VisitBehavior resetUpdateVisitBehavior =
            new VisitBehavior() {
                @Override
                protected void visitElement(Element e) {
                    e.setUpdated(false);
                }

                @Override
                protected boolean forkCondition(Element e) {
                    return true;
                }

                @Override
                protected boolean carryOnCondition() {
                    return true;
                }
            };
    private final TouchVisitBehavior hudTouchVisitBehavior = new TouchVisitBehavior();
    private final IsUpdatedVisitBehavior isUpdatedVisitBehavior = new IsUpdatedVisitBehavior();
    private final VisitBehavior drawVisitBehavior =
            new VisitBehavior() {
                @Override
                protected void visitElement(Element e) {
                    if (e.isVisible()) {
                        e.drawSelf();
                    }
                }

                @Override
                protected boolean forkCondition(Element e) {
                    return e.isVisible();
                }

                @Override
                protected boolean carryOnCondition() {
                    return true;
                }
            };
    private float zoomFactor = 1f;

    protected UserInterface(T scene) {
        this.scene = scene;
        scene.getHud().attachChild(ResourceManager.getInstance().hudBatcher);
        scene.attachChild(ResourceManager.getInstance().sceneBatcher);
    }

    public void detachSelf() {
        ResourceManager.getInstance().hudBatcher.detachSelf();
        ResourceManager.getInstance().sceneBatcher.detachSelf();
    }

    @Override
    public void drawSelf() {
        contentTraverser.setBehavior(drawVisitBehavior);
        contentTraverser.traverse(this, false);
        ResourceManager.getInstance().hudBatcher.submit();
        ResourceManager.getInstance().sceneBatcher.submit();
    }

    public void step() {
        contentTraverser.setBehavior(updateVisitBehavior);
        contentTraverser.traverse(this, false);
        checkUpdated();
        if (isUpdated()) {
            drawSelf();
        }
        resetUpdate();
    }

    private void resetUpdate() {
        contentTraverser.setBehavior(resetUpdateVisitBehavior);
        contentTraverser.traverse(this, true);
    }

    public abstract void onTouchScene(TouchEvent pTouchEvent);

    @Override
    public boolean onTouchHud(TouchEvent pTouchEvent) {
        hudTouchVisitBehavior.setSceneTouchEvent(pTouchEvent);
        hudTouchVisitBehavior.setTouched(false);
        hudTouchVisitBehavior.setLocked(false);
        contentTraverser.setBehavior(hudTouchVisitBehavior);
        contentTraverser.traverse(this, false, true);
        return hudTouchVisitBehavior.isTouched();
    }

    public float getZoomFactor() {
        return zoomFactor;
    }

    public void updateZoom(float pZoomFactor) {
        zoomFactor = pZoomFactor;
        for (Element e : getContents()) {
            e.updateZoom(pZoomFactor);
        }
    }

    private void checkUpdated() {
        contentTraverser.setBehavior(isUpdatedVisitBehavior);
        isUpdatedVisitBehavior.setUpdated(false);
        contentTraverser.traverse(this, true);
        setUpdated(isUpdatedVisitBehavior.isUpdated());
    }

    public void shade(Element excepted) {
        ShadeVisitBehavior shadeVisitBehavior = new ShadeVisitBehavior();
        shadeVisitBehavior.setExcepted(excepted);
        shadeVisitBehavior.setShadeAction(ShadeVisitBehavior.ShadeAction.Hide);
        contentTraverser.setBehavior(shadeVisitBehavior);
        contentTraverser.traverse(this, false);
    }

    public void undoShade() {
        ShadeVisitBehavior shadeVisitBehavior = new ShadeVisitBehavior();
        shadeVisitBehavior.setShadeAction(ShadeVisitBehavior.ShadeAction.Show);
        contentTraverser.setBehavior(shadeVisitBehavior);
        contentTraverser.traverse(this, false);
    }

    public void resume() {
    }
}
