package is.kul.learningandengine.graphicelements.ui;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.adt.color.Color;

import is.kul.learningandengine.ResourceManager;

public class ColorSlot extends Entity {

    Mesh circle;
    ColorSlot(float x, float y,Color color){
        Sprite sprite = new Sprite(x, y, ResourceManager.getInstance().slotTextureRegion, ResourceManager.getInstance().vbom);
        attachChild(sprite);

        float[] circle_vertices = new float[ 12*3+3];
        float RAY = 8;


        for(int i = 0; i < 12; i++)
        {
            float theta = (float) (2* Math.PI* (float)i / (float)12);//get the current angle

            circle_vertices[3*i] = (float) (RAY * Math.cos(theta));//calculate the x component
            circle_vertices[3*i+1] = (float) (RAY * Math.sin(theta));//calculate the y component
            circle_vertices[3*i+2] = (float) (RAY * Math.sin(theta));
        }
        this.circle = new Mesh(x, y, circle_vertices, 12,  DrawMode.TRIANGLE_FAN, ResourceManager.getInstance().vbom);
        this.circle.setColor(color);
        attachChild(this.circle);
    }
    @Override
    public Color getColor(){
        return circle.getColor();
    }
    @Override
    public void setColor(Color color){
        circle.setColor(color);
    }
}
