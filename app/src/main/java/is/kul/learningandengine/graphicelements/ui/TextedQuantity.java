package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.ResourceManager;

import org.andengine.entity.text.Text;

public class TextedQuantity extends Quantity {

	public TextedQuantity(float x, float y, int size, int color, String name) {
		super(x, y, size, color);
		Text text = new Text(x, y, ResourceManager.getInstance().font3, name, 30, ResourceManager.getInstance().vbom);
        attachChild(text);
		text.setX(text.getX()-text.getWidth()/2);
	}

}
