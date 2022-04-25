package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.scene.UISignal;
import is.kul.learningandengine.scene.UISignal.SignalName;

import org.andengine.entity.IEntity;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class OrganizedWindow extends Window {

	 Container container;

	public OrganizedWindow(float X, float Y, int columns, int rows,
			VertexBufferObjectManager pVertexBufferObjectManager,
			boolean HasPadding) {
		super(X, Y, columns, rows, pVertexBufferObjectManager, HasPadding,true,true,true);


        this.container =new Container(rows*32,-rows*32+25);
        this.container.setZIndex(500);
        this.attachChild(this.container);
        sortChildren();
	}

	@Override
	public void diffuse(UISignal signal) {
		super.diffuse(signal);
		
		if(signal.signalName== SignalName.foldwindow&&signal.event==UISignal.Event.Clicked){
            this.container.setVisible(true);
		}
		if(signal.signalName== SignalName.foldwindow&&signal.event==UISignal.Event.Unclicked){
            this.container.setVisible(false);
		}
		
	}

	@Override
	public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {
		boolean touched = false;

            if (super.OnTouchScene(pSceneTouchEvent))if(isVisible()) touched = true;

            if (this.container.OnTouchScene(pSceneTouchEvent))if(isVisible())  touched = true;

		return touched;
	}

	@Override
	void attachChild(IEntity entity,int room, int room2){

		entity.setPosition(this.rooms2[room2], this.N %2==0 ?-12.5f+(-room+ this.N /2)*25:(-room+ this.N /2)*25);

        this.container.attachChild(entity);
	
	}
	

	void attachChild2(IEntity entity,int room, float positionx){

		entity.setPosition(positionx, this.N %2==0 ?-12.5f+(-room+ this.N /2)*25:(-room+ this.N /2)*25);

        this.container.attachChild(entity);

	}


	void updateChild(IEntity entity,int room, float positionx){

		entity.setPosition(positionx, this.N %2==0 ?-12.5f+(-room+ this.N /2)*25:(-room+ this.N /2)*25);



	}




}
