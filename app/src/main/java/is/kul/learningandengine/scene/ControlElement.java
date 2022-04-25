package is.kul.learningandengine.scene;

import org.andengine.entity.Entity;

public class ControlElement {
    public int getID() {
        return ID;
    }

    public Entity getAssociate() {
        return associate;
    }

    enum Type{
       AnalogController,DigitalController
    }
    enum Function{
ControlMoveableElement
    }
    Function function;
    Type type;
    int ID;
    Entity associate;
    boolean visible;
    ControlElement(Type type,Function func, int ID){
       this.type = type;
       this.function = func;
       this.ID = ID;
    }
    void setAssociate(Entity associate){
        this.associate = associate;
    }
public void setVisible(boolean visible){
        this.visible = visible;
       if(associate!=null) associate.setVisible(visible);
}
}
