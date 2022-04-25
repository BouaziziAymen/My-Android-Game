package is.kul.learningandengine;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.IEntity;

public class MyCamera extends SmoothCamera {
private IEntity chaseEntity;



@Override
public void reset() {
super.reset();
    this.gameOver = false;
    this.set(0, 0, GameActivity.CAMERA_WIDTH,
GameActivity.CAMERA_HEIGHT);
    this.setCenterDirect(GameActivity.CAMERA_WIDTH / 2,
GameActivity.CAMERA_HEIGHT / 2);
}
public boolean gameOver;
public MyCamera(float pX, float pY, float pWidth, float pHeight) {
super(pX, pY, pWidth, pHeight, 3000f, 1000f, 10f);

}
@Override
public void setChaseEntity(IEntity pChaseEntity) {
super.setChaseEntity(pChaseEntity);
    chaseEntity = pChaseEntity;
}
@Override
public void updateChaseEntity() {
if (this.chaseEntity != null) {
if (this.chaseEntity.getY() > this.getCenterY()) {
    this.setCenter(this.getCenterX(), this.chaseEntity.getY());
} else if (this.chaseEntity.getY() < this.getYMin() && !this.gameOver) {
    this.setCenter(this.getCenterX(), this.chaseEntity.getY() - this.getHeight());
    this.gameOver = true;
}
}
}
}