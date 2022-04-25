package is.kul.learningandengine.graphicelements;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

import java.util.ArrayList;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.GameScene;

public class DImage extends Entity {
    private float halfWidth,halfHeight;
    private Sprite sprite;
    Entity contour;
DVector main;
SizeLimitDImage xyLimit;
Bitmap bitmap;
Rectangle pipe;
    public DImage(float pX, float pY,float w, float h, ITextureRegion pTextureRegion, Bitmap bitmap) {
        super(pX,pY);
        sprite = new Sprite(0, 0, w, h, pTextureRegion, ResourceManager.getInstance().vbom);
        this.attachChild(sprite);
        contour = new Entity();
         halfWidth = w/2;
         halfHeight = h/2;
this.bitmap = bitmap;

        this.attachChild(contour);
        updateContour();
        main = new DVector(new Vector2(0,0),new Vector2(halfWidth,halfHeight),org.andengine.util.adt.color.Color.BLUE,3);
        this.attachChild(main);
        main.attachChild(xyLimit = new SizeLimitDImage(halfWidth,halfHeight, GameScene.model.scale));
        pipe = new Rectangle(halfWidth,halfHeight,20,20,ResourceManager.getInstance().vbom);
        this.attachChild(pipe);
        pipe.setColor(org.andengine.util.adt.color.Color.RED);
}
public void updateContour(){
        contour.detachChildren();
    ArrayList<Vector2> points = new ArrayList<Vector2>();
    points.add(new Vector2(halfWidth,halfHeight));
    points.add(new Vector2(halfWidth,-halfHeight));
    points.add(new Vector2(-halfWidth,-halfHeight));
    points.add(new Vector2(-halfWidth,halfHeight));

    for(int i=0;i<4;i++){
        int ni = (i==3)?0:i+1;
        Vector2 pi = points.get(i);
        Vector2 pni = points.get(ni);
        Line line = new Line(pi.x,pi.y,pni.x,pni.y,5,ResourceManager.getInstance().vbom);
        line.setColor(Color.GREEN);
        contour.attachChild(line);
    }
}
    public void onUpdateLimitPosition(SizeLimitDImage limit) {
         halfWidth  = limit.getX();
         halfHeight = limit.getY();
        main.update(new Vector2(0,0),new Vector2(halfWidth,halfHeight));
sprite.setWidth(2*halfWidth);
sprite.setHeight(2*halfHeight);
updateContour();
    }

    public org.andengine.util.adt.color.Color getColorAt(float touchX,float touchY){
        if(sprite.contains(touchX,touchY)) {
            float[] pos = sprite.convertSceneCoordinatesToLocalCoordinates(touchX, touchY);
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            int x = (int)Math.floor (pos[0] / (2 * halfWidth) * width);
            int y = height - (int) Math.floor(pos[1] / (2 * halfHeight) * height);
            int pixel = bitmap.getPixel(x, y);
            float red = Color.red(pixel)/255f;
            float green = Color.green(pixel)/255f;
            float blue = Color.blue(pixel)/255f;
            float alpha = Color.alpha(pixel)/255f;
            return new org.andengine.util.adt.color.Color(red,green,blue,alpha);

        }
        return null;
    }
}
