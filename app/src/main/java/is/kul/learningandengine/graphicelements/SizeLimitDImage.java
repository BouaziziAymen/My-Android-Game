package is.kul.learningandengine.graphicelements;

import org.andengine.entity.IEntity;


    public class SizeLimitDImage extends DPoint {
        public SizeLimitDImage(float x, float y, float scale) {
            super(x, y, scale, 0, DFactory.DrawingType.TYPEPOINT);
        }

        public void updatePosition(float dX,float dY) {

            IEntity parent =  this.getParent().getParent();
            if(parent instanceof DImage){
                if(this.getX()+dX>=5&&this.getY()+dY>=5) {
                    setModified(true);

                    super.updatePosition(dX, dY);
                    ((DImage) parent).onUpdateLimitPosition(this);
                }
                }
            }




        }

