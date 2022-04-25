package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import org.andengine.entity.text.Text;

public class TextedSelector extends QuantitySelector implements LayoutElement {

public int ID;
public TextedSelector(float initialValue, int ID,int size, int strokeSize, String name, float x, float y, float v0, float v1, int color) {
super(initialValue,strokeSize, x+10, y, size, color, v0, v1);
this.ID = ID;
Text text = new Text(0,0, ResourceManager.getInstance().font3, name, 30, ResourceManager.getInstance().vbom);
text.setX(-text.getWidth()/2);
    attachChild(text);

}

boolean active = true;
@Override
public void setActive(boolean b) {
    this.active = b;
	
}
@Override
public boolean isActive() {	
	return this.active;
}


}
