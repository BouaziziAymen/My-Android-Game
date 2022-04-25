package is.kul.learningandengine.graphicelements;

public class BodyProperty {
    int bodyID;
public String bodyName;
    public int getBodyID() {
        return bodyID;
    }

    enum bodyType{ MeleeWeapon,RangedWeapon}
    BodyProperty(String bodyName,int ID){
        bodyID = ID;
        this.bodyName = bodyName;
    }
}
