package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.scene.UISignal;
import is.kul.learningandengine.scene.UISignal.SignalName;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class ScrollableOrganizedWindow extends OrganizedWindow {
Scroll scroll;
boolean scrollActive;

	public ScrollableOrganizedWindow(float X, float Y, int columns, int rows,
			VertexBufferObjectManager pVertexBufferObjectManager,
			boolean HasPadding) {
		super(X, Y, columns, rows, pVertexBufferObjectManager, HasPadding);

        scroll = new Scroll(rows-1, rows*2, 0, 0);
        this.scroll.setZIndex(1000);
        attachChild(this.scroll);
		
		
	}
	

	@Override
	public void diffuse(UISignal signal) {
		super.diffuse(signal);
		if(signal.signalName== SignalName.foldwindow&&signal.event==UISignal.Event.Clicked){
            this.scroll.update(this.scroll.getTotal());

			}

		if(signal.signalName== SignalName.foldwindow&&signal.event==UISignal.Event.Unclicked){

            this.scroll.setVisible(false);
		
			}
		
		
	
		
	}

}
