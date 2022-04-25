package is.kul.learningandengine.graphicelements.ui;


import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;
import is.kul.learningandengine.scene.UISignal.SignalName;

public class Window extends Entity implements UIHandler {
int N,M;
protected float[] rooms2;
Entity midPadding;
Entity padding;
Entity window;
private Button folder;
private Button closer;


	public Window(float X, float Y,int columns, int rows,VertexBufferObjectManager pVertexBufferObjectManager,
			
			
			
			boolean HasPadding, boolean minimizable, boolean closable, boolean movable){
		super(X,Y);
        this.padding = new Entity();
        this.window = new Entity();
        this.midPadding = new Entity();

        attachChild(this.window);
        attachChild(this.padding);
        attachChild(this.midPadding);
        this.N = rows*64 /25 -2;
        this.M = 2*columns;


        this.rooms2 = new float[this.M];
			 
			if(this.M %2==0){
				for(int i = 0; i< this.M; i++){
                    this.rooms2[i]=16-16*(-i+ this.M /2);
				}
			} else {
				for(int i = 0; i< this.M; i++)
                    this.rooms2[i]=-16*(-i+ this.M /2);
			}
		
		
		
		int[][] key = new int[rows][columns];

		for(int i=0;i<rows;i++)
			for(int j=0;j<columns;j++){
				if(i==0&&j!=0&&j!=columns-1)key[0][j]=13;
				else if(i==rows-1&&j!=0&&j!=columns-1)key[i][j]=7;
				
				else if(j==0&&i!=0&&i!=rows-1)key[i][0]=9;
				else if(j==columns-1&&i!=0&&i!=rows-1)key[i][j]=11;
				else key[i][j]=10;
			}
		key[0][0]=12;
		key[0][columns-1]=14;
		key[rows-1][0]=6;
		key[rows-1][columns-1]=8;
	
		
		
		
		
		float x0 =-columns/2f* 64+32;
	
		float y0 =-rows/2f* 64+32;
		
		for(int i=0;i<rows;i++){
			
			for(int j=0;j<columns;j++){
				float x=x0+j*64-j;
				float y=y0+i*64-i;
				
				
			Sprite	 sprite= new Sprite(x, y, ResourceManager.getInstance().window.get(key[i][j]), pVertexBufferObjectManager);


                this.window.attachChild(sprite);
		
			}
		}
		
		
		if(HasPadding){
			int N = 0;
			if(movable)N++;
			if(minimizable)N++;
			if(closable)N++;
		int begin = columns - N;	
		for(int j=0;j<columns;j++){
			float x=x0+j*64-j;
			float y=rows/2f* 64+32-8;
			int k;
			if(j==0)k=0;else if(j==columns-1)k=2;else k=1;
			if(j>=begin)k+=15;
			
		Sprite	 sprite= new Sprite(x, y, ResourceManager.getInstance().window.get(k), pVertexBufferObjectManager);

            this.padding.attachChild(sprite);
		}

            this.padding.setZIndex(999);
			
		
		
		
		for(int j=0;j<columns;j++){
			float x=x0+j*64-j;
			float y=rows/2f* 64-8;
			int k;
			if(j==0)k=3;else if(j==columns-1)k=5;else k=4;
			
		Sprite	 sprite= new Sprite(x, y, ResourceManager.getInstance().window.get(k), pVertexBufferObjectManager);

            this.midPadding.attachChild(sprite);
		}

            this.midPadding.setZIndex(999);
		int k = 0;
	if(minimizable)
		
		{
		
			float x=x0+(columns-1)*64-(columns-1);
			float y=rows/2f* 64+32-8;
            this.folder = new Button(x-3, y+2, ResourceManager.getInstance().windowFoldTextureRegion, pVertexBufferObjectManager, Button.ButtonType.Selector, SignalName.foldwindow,true);
            attachChild(this.folder);
		k++;
		}
	if(closable)

	{

		float x=x0+(columns-1-k)*64-(columns-1-k);
		float y=rows/2f* 64+32-8;
        this.closer = new Button(x, y+2, ResourceManager.getInstance().windowCloseTextureRegion, pVertexBufferObjectManager, Button.ButtonType.OneClick, SignalName.closewindow);
        attachChild(this.closer);
	k++;
	}
	
		
		}

        sortChildren();
	}
	void attachChild(IEntity entity,int room, int room2){

		entity.setPosition(this.rooms2[room2], this.N %2==0 ?-12.5f+(-room+ this.N /2)*25:(-room+ this.N /2)*25);

        attachChild(entity);

	}
	void moveChild(IEntity entity,int room){
		entity.setPosition(entity.getX(), this.N %2==0 ?-12.5f+(-room+ this.N /2)*25:(-room+ this.N /2)*25);


	}
	@Override
	public void diffuse(UISignal signal) {

		if(signal.signalName== SignalName.foldwindow&&signal.event==UISignal.Event.Clicked){


            this.window.setVisible(true);
            this.midPadding.setVisible(true);
			}

		if(signal.signalName== SignalName.foldwindow&&signal.event==UISignal.Event.Unclicked){

            this.window.setVisible(false);
            this.midPadding.setVisible(false);
			}
		if(signal.signalName== SignalName.closewindow&&signal.event==UISignal.Event.Unclicked){

            setVisible(false);
			}



	}
	@Override
	public boolean OnTouchScene(TouchEvent touch) {

		if(this.folder !=null){
			if(this.folder.OnTouchScene(touch))return true;
		}
		if(this.closer !=null){
			if(this.closer.OnTouchScene(touch))return true;
		}
		if(this.window.isVisible())
		for(int i = 0; i< this.window.getChildCount(); i++)
			if(this.window.getChildByIndex(i).contains(touch.getX(), touch.getY()))return true;
		for(int i = 0; i< this.padding.getChildCount(); i++)
			if(this.padding.getChildByIndex(i).contains(touch.getX(), touch.getY()))return true;
		for(int i = 0; i< this.midPadding.getChildCount(); i++)
			if(this.midPadding.getChildByIndex(i).contains(touch.getX(), touch.getY()))return true;



		return false;
	}
	public void closeWindow(){
        diffuse(new UISignal(UISignal.SignalName.closewindow,UISignal.Event.Unclicked,this));
	}
	public void unfoldWindow(){
        this.folder.unclick();

	}
	public void foldWindow(){
        this.folder.click();

	}

	public void updateColor(Color color) {
		//DO NOTHING
	}


}
