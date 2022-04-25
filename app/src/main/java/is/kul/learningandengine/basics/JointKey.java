package is.kul.learningandengine.basics;

import com.badlogic.gdx.math.Vector2;

public class JointKey {

JointBluePrint bluePrint;
JointKey.KeyType type;
Vector2 anchor;
boolean create;
enum KeyType{
	A,B
}
boolean dead;
public JointKey(JointBluePrint bp,JointKey.KeyType type, Vector2 v1) {
    bluePrint = bp;
    this.type = type;
    anchor = v1.cpy();

}



public JointBluePrint getBluePrint() {
return this.bluePrint;
}

}
