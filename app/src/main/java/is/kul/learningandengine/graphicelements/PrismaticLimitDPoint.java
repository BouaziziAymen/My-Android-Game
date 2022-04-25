package is.kul.learningandengine.graphicelements;

import org.andengine.entity.IEntity;

public class PrismaticLimitDPoint extends DPoint {
    public PrismaticLimitDPoint(float x, float y, float scale) {
        super(x, y, scale, 0, DFactory.DrawingType.TYPEPOINT);
    }

    public void updatePosition(float dX, float dY) {

        IEntity parent =  this.getParent().getParent();
        if(parent instanceof PrismaticPoint){
setModified(true);
       if(((PrismaticPoint) parent).isFirstLimit(this)){
           super.updatePosition(dX,dY);
           ((PrismaticPoint)parent).onUpdateFirstLimitPosition();
       } else {
           ((PrismaticPoint)parent).onUpdateSecondLimitPosition(dY);

       }
        }




    }
}
