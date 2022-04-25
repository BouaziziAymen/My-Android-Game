package is.kul.learningandengine.graphicelements;


import android.graphics.Point;
import android.util.Log;

import org.andengine.entity.IEntity;

    public class RevoluteLimitDPoint extends DPoint {
        public RevoluteLimitDPoint(float x, float y, float scale) {
            super(x, y, scale, 0, DFactory.DrawingType.TYPEPOINT);

        }

        public void updatePosition(float dX, float dY) {

            IEntity parent =  this.getParent().getParent();
            if(parent instanceof RevolutePoint){
setModified(true);
                if(((RevolutePoint) parent).isFirstLimit(this))
                    ((RevolutePoint)parent).onUpdateFirstLimitPosition(dY);
                 else if(((RevolutePoint) parent).isSecondLimit(this))
                    ((RevolutePoint)parent).onUpdateSecondLimitPosition(dY);
                else if(((RevolutePoint) parent).isReferenceLimit(this))
                    ((RevolutePoint)parent).onUpdateReferenceLimitPosition(dY);


            }

        }

        @Override
        public String getCoordinates(){
            IEntity parent =  this.getParent().getParent();
            if(parent instanceof RevolutePoint) {
                RevolutePoint rev = (RevolutePoint)parent;
                if(rev.isFirstLimit(this))
                return "angle: " + String.format("%.1f", rev.getLimit1Angle());
                else if(rev.isSecondLimit(this))
                    return "angle: " + String.format("%.1f", rev.getLimit2Angle());
                else if(rev.isReferenceLimit(this))
                    return "angle: " + String.format("%.1f", normalize(rev.getReferenceAngle()));
            }
            return "";
        }
        float normalize(float angle){
            return (angle %= 360) >= 0 ? angle : (angle + 360);
        }


    }

