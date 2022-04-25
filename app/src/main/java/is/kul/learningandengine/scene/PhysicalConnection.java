package is.kul.learningandengine.scene;

public class PhysicalConnection {
public PhysicalConnection(PhysicalConnection.Connection_Type type, ContactWrapper cw) {
	this.type = type;
    cWrapper = cw;
	}
enum Connection_Type{
	TOUCHINGTOGETHER,SLIDINGTOGETHER,IMPACTAONB,IMPACTBONA
}
PhysicalConnection.Connection_Type type;
ContactWrapper cWrapper;

public String toString(){
	return "("+ this.cWrapper.entityA.ID+"/"+ this.cWrapper.entityB.ID+":"+ this.type +")";
}

}
