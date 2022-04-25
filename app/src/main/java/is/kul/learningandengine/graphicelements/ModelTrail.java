package is.kul.learningandengine.graphicelements;

import org.andengine.entity.Entity;

public class ModelTrail extends Entity {
ModelTrail(){

}

void addModel(Entity model){
	
	if(getChildCount()>=3)
        getChildByIndex(0).detachSelf();
    attachChild(model);
	
}

public void cutTrail(){
	if(getChildCount()>1){
	do{

        getChildByIndex(0).detachSelf();
	}while(getChildCount()>1);}
}
}
