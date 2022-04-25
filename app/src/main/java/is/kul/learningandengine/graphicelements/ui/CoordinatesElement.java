package is.kul.learningandengine.graphicelements.ui;

import android.util.Log;

import org.andengine.entity.Entity;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

public class CoordinatesElement extends Entity implements LayoutElement {
    BaseLamp background;
    Text text;
    State state = State.None;

    enum State {
        Double, Single, None
    }

    CoordinatesElement(float x, float y) {
        super(x, y);
        background = new BaseLamp(0, 0, 8);
        this.attachChild(background);
        text = new Text(0, 0, ResourceManager.getInstance().font3, "",100, ResourceManager.getInstance().vbom);

        this.attachChild(text);
        centerText();
    }

    private void setText(String string) {
        Log.e("coord",string);
        text.setText(string);
        centerText();
    }

   private void centerText() {
        text.setPosition(background.width / 2, 0);
    }

    Coordinated coordinated;

    public void bind(Coordinated cor) {
        Log.e("coord","bind");
        coordinated = cor;
    }
    @Override
    protected void onManagedUpdate(float unused) {

        if(coordinated!=null){
            if(coordinated.isModified()) {
                setText(coordinated.getCoordinates());
                coordinated.setModified(false);
            }
        } else setText("");
    }

    @Override
    public void setActive(boolean b) {
        active = b;
    }

    boolean active;

    @Override
    public boolean isActive() {
        return active;
    }


}
