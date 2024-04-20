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

import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;

public abstract class UserInterface<T extends AbstractScene> extends Container
        implements Touchable {

    protected final SpriteBatch hudBatcher;
    protected final SpriteBatch sceneBatcher;
    protected final T scene;
    private final StepVisitBehavior updateVisitBehavior;
    private final ContentTraverser contentTraverser;
    private final VisitBehavior resetUpdateVisitBehavior;
    private final TouchVisitBehavior hudTouchVisitBehavior;
    private final IsUpdatedVisitBehavior isUpdatedVisitBehavior;
    private final VisitBehavior drawVisitBehavior;
    // behaviors

    private float zoomFactor = 1f;

    protected UserInterface(T scene) {

        this.scene = scene;

        hudBatcher =
                new SpriteBatch(ResourceManager.getInstance().uiTextureAtlas,
                        10000,
                        ResourceManager.getInstance().vbom);
        sceneBatcher =
                new SpriteBatch(ResourceManager.getInstance().uiTextureAtlas,
                        10000,
                        ResourceManager.getInstance().vbom);

        scene.getHud().attachChild(hudBatcher);
        scene.attachChild(sceneBatcher);

        hudBatcher.setZIndex(10);
        sceneBatcher.setZIndex(10);

       updateVisitBehavior = new StepVisitBehavior();
        // other
        contentTraverser = new ContentTraverser();
        resetUpdateVisitBehavior =
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
       hudTouchVisitBehavior = new TouchVisitBehavior();
       isUpdatedVisitBehavior = new IsUpdatedVisitBehavior();
       drawVisitBehavior =
                new VisitBehavior() {
                    @Override
                    protected void visitElement(Element e) {
                        if (e.isVisible()) {
                            try {
                                e.drawSelf(hudBatcher,sceneBatcher);
                            } catch (Throwable t){
                                System.err.println(t);
                            }

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
    }

    public void detachSelf() {
       hudBatcher.detachSelf();
       sceneBatcher.detachSelf();
       hudBatcher.dispose();
       sceneBatcher.dispose();
    }

    @Override
    public void drawSelf(SpriteBatch hudBatcher, SpriteBatch sceneBatcher) {
        contentTraverser.setBehavior(drawVisitBehavior);
        contentTraverser.traverse(this, false);
        hudBatcher.submit();
        sceneBatcher.submit();
    }

    public void step() {
            contentTraverser.setBehavior(updateVisitBehavior);
            contentTraverser.traverse(this, false);
            checkUpdated();
            if (isUpdated()) {
                drawSelf(hudBatcher, sceneBatcher);
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
