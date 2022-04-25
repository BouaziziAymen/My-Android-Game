package is.kul.learningandengine.graphicelements;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.helpers.MathUtils;
import is.kul.learningandengine.scene.GameScene;

public class RotationIndicator extends Entity {
Vector2 begin;
    RotationIndicator(Vector2 begin, Vector2 end){
Vector2 dir = end.cpy().sub(begin).nor().mul(32);
this.begin = begin;

        Line line = new Line(begin.x, begin.y, end.x, end.y, 3, ResourceManager.getInstance().vbom);
        line.setColor(1f,0f,0f);
        attachChild(line);

        setZIndex(1000);
        Vector2 direction = new Vector2(begin.x-end.x,begin.y-end.y);
        Vector2 unit1 = MathUtils.rotateZ(direction, (float) (Math.PI/6)).nor();
        Vector2 unit2 =MathUtils.rotateZ(direction, (float) (-Math.PI/6)).nor();


        Line stroke1 = new Line(end.x, end.y, end.x+20*unit1.x, end.y+20*unit1.y, 3, ResourceManager.getInstance().vbom);
        Line stroke2 = new Line(end.x, end.y, end.x+20*unit2.x, end.y+20*unit2.y, 3, ResourceManager.getInstance().vbom);
        stroke1.setColor(1f,1f,0f);
        attachChild(stroke1);
        stroke2.setColor(1f,1f,0f);
        attachChild(stroke2);

    }

    void update(Vector2 end){
        this.detachChildren();
        Line line = new Line(begin.x, begin.y, end.x, end.y, 3, ResourceManager.getInstance().vbom);
        line.setColor(1f,0f,0f);
        attachChild(line);

        setZIndex(1000);
        Vector2 direction = new Vector2(begin.x-end.x,begin.y-end.y);
        Vector2 unit1 = MathUtils.rotateZ(direction, (float) (Math.PI/6)).nor();
        Vector2 unit2 =MathUtils.rotateZ(direction, (float) (-Math.PI/6)).nor();


        Line stroke1 = new Line(end.x, end.y, end.x+20*unit1.x, end.y+20*unit1.y, 3, ResourceManager.getInstance().vbom);
        Line stroke2 = new Line(end.x, end.y, end.x+20*unit2.x, end.y+20*unit2.y, 3, ResourceManager.getInstance().vbom);
        stroke1.setColor(1f,1f,0f);
        attachChild(stroke1);
        stroke2.setColor(1f,1f,0f);
        attachChild(stroke2);

    }
}
