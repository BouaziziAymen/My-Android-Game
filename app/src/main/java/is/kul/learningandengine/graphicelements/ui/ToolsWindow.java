package is.kul.learningandengine.graphicelements.ui;

import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import is.kul.learningandengine.GameActivity;
import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.GameScene;
import is.kul.learningandengine.scene.UISignal;

public class ToolsWindow extends OrganizedWindow {
	//{title,hormg,vermg,pmg,range,
	// verts,branch,fixedspeed,fixedx,fixedy,
	// cutstart,magtitle,cltitle,done,undo,
	// fpmagnet,spmagnet,mult,haslimits,create,
	// addimage,pipe}
private static final boolean[][] grid = new boolean[][]{

		                            //{title,hormg,vermg,pmg,range,verts,branch,fixedspeed,fixedx,fixedy, cutstart,magtitle,cltitle,done,undo,fpmagnet,spmagnet,mult,haslimits,create,addimage,pipe}
		                            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
                                    { true, true, true, true, true, true,false,false,false,false,false, true,false,false,false, true, true,false,false,false,false,false},
		                            { true, true, true, true, true,false,false,false,false,false,false, true,false,false,false,false,false,false,false,false,false,false},
		                            { true, true, true, true, true,false, true,false,false,false,false, true,false,false,false,false,false,false,false,false,false,false},
		                            { true,false,false,false,false,false,false, true, true, true,false,false,false,false,false,false,false,false,false,false,false,false},
			                        { true,false,false,false, true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
			                        { true, true, true, true,false,false,false,false, true, true,false, true,false,false,false,false,false,false,false,false,false,false},
			                        { true, true, true, true,false,false,false,false,false,false,false, true,false,false,false,false,false,false,false,false,false,false},
			                        { true,false,false,false,false,false,false,false,false,false,true, false, true,false,false,false,false,false,false,false,false,false},
			                        { true,false,false,false,false,false,false,false, true, true,false,false,false,false,false,false,false, true,false,false,false,false},
			                        { true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false, true,false,false,false,false},
			                        { true, true, true, true, true,false,false,false,false,false,false, true,false,false,false,false,false,false, true,false,false,false},
			                        { true, true, true, true, true,false,false,false,false,false,false, true,false,false,false,false,false,false,false,false,false,false},
			                        { true, true, true, true, true,false,false,false,false,false,false, true,false,false,false,false,false,false,false,false,false,false},
			                        { true, true, true, true, true,false,false,false,false,false,false, true,false,false,false,false,false,false, true,false,false,false},
			                        { true, true, true, true, true,false,false,false,false,false,false, true,false,false,false,false,false,false,false,false,false,false},
			                        { true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false, true,false},
			                        { true,false,false,false,false,false,false,false, true, true,false,false,false,false,false,false,false,false,false,false,false,false},
			                        { true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false, true,false,false},
			                        { true,false,false,false,false,false,false,false, true, true,false,false,false,false,false,false,false,false,false,false,false,false},
			                        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false, true},
	};
	private final OnoffWindowMainElement branch;
	private final Text magTitle;
	private final Text title;
	private final OnoffWindowMainElement fixedX;
	private final OnoffWindowMainElement fixedY;
	private final OnoffWindowMainElement AnalogSpeed;
    private final OnoffWindowMainElement magnetFirstPoint;
    private final OnoffWindowMainElement magnetSecondPoint;
	private final ActivateElement cutStart;
	private final Text cutLayerName;
    private final TextedButton done;
    private final TextedButton undo;
private final TextedNumericInput numVertices;
private final Button horMagnet;
private final Button verMagnet;
private final Button pointMagnet;
private final TextedNumericInput range;
	private final OnoffWindowMainElement multiple;
	private final OnoffWindowMainElement hasLimits;
	private final ActivateElement create;
	private final CoordinatesElement coords;
	private final ActivateElement addimage;
	private final ActivateElement addpipedcolor;
	private final ColorSlot pipeColorSlot;

	boolean active;
	State previous = State.NONE;
State state;

	public ToolsWindow() {
		super(2*64,64+32, 4, 3, ResourceManager.getInstance().vbom, false);
		MessageBox box = new MessageBox(-64-32, 64+32-6, 5);
        attachChild(box);

		title = new Text(0, 0, ResourceManager.getInstance().font2, "Title", 20, ResourceManager.getInstance().vbom);
		attachChild2(title, 0, 0);

        this.numVertices = new TextedNumericInput("Number Of Vertices: ", 0, 0, 3, 3,32, NumType.POSINT);
        this.numVertices.numericInput.setIntValue(7);
        attachChild(this.numVertices, 1, 2);
        this.horMagnet = new Button(0, 0, ResourceManager.getInstance().magnetHorTextureRegion, ResourceManager.getInstance().vbom,
				Button.ButtonType.Selector, UISignal.SignalName.HorizentalMagnet);
        this.verMagnet = new Button(0, 0, ResourceManager.getInstance().magnetVerTextureRegion, ResourceManager.getInstance().vbom,
				Button.ButtonType.Selector, UISignal.SignalName.VerticalMagnet);
        this.pointMagnet = new Button(0, 0, ResourceManager.getInstance().magnetPointTextureRegion, ResourceManager.getInstance().vbom,
				Button.ButtonType.Selector, UISignal.SignalName.PointMagnet);

        attachChild2(this.horMagnet, 2, -64+32);
        attachChild2(this.verMagnet, 2, 24-64+32);
        attachChild2(this.pointMagnet, 2,48-64+32);
        this.range = new TextedNumericInput("Range:  ", 0, 0, 1, 1,32, NumType.POSINT);
        this.range.numericInput.setIntValue(16);
        attachChild2(this.range, 2, 64+16);
		 branch = new OnoffWindowMainElement("Continue");
		attachChild2(branch,1,-32);

		multiple = new OnoffWindowMainElement("Multiple");
		multiple.click();
		attachChild2(multiple,1,-32);


		AnalogSpeed = new OnoffWindowMainElement("Analog");
		fixedX = new OnoffWindowMainElement("Fixed X");
		fixedY = new OnoffWindowMainElement("Fixed Y");
        magnetFirstPoint = new OnoffWindowMainElement("First");
		magnetFirstPoint.click();
        magnetSecondPoint = new OnoffWindowMainElement("Second");

		coords = new CoordinatesElement(0,0);
		attachChild2(coords,5,-coords.background.width/2);


		hasLimits = new OnoffWindowMainElement("Has Limits");
		attachChild2(hasLimits,1,0);
		attachChild2(AnalogSpeed,1,0);
		attachChild2(fixedX,3,-48);
		attachChild2(fixedY,3,32);
		attachChild2(magnetFirstPoint,3,-64);
        attachChild2(magnetSecondPoint,3,16);

 cutStart = new ActivateElement("Cut!",0, Button.ButtonType.OneOnly);
		create = new ActivateElement("Create",1, Button.ButtonType.OneClick);
		addimage = new ActivateElement("Add Image",2, Button.ButtonType.OneClick);
		addpipedcolor = new ActivateElement("Add Color",3, Button.ButtonType.OneClick);
		pipeColorSlot = new ColorSlot(0,0, Color.BLACK);
		attachChild2(create,1,0);
		attachChild2(cutStart,1,0);
		attachChild2(addimage,1,0);
		attachChild2(addpipedcolor,1,0);
		attachChild2(pipeColorSlot,1,addpipedcolor.getWidth()/2+10);
		magTitle = new Text(0, 0, ResourceManager.getInstance().font3, "Magnetic Grid", 20, ResourceManager.getInstance().vbom);
        attachChild2(magTitle, 2, -64-16);


		cutLayerName = new Text(0, 0, ResourceManager.getInstance().font3, "xxx", 20, ResourceManager.getInstance().vbom);
		attachChild2(cutLayerName, 2, -64-16);
branch.setVisible(false);
		numVertices.setVisible(false);

		done = new TextedButton("Done",ResourceManager.getInstance().validateTextureRegion, Button.ButtonType.OneClick, UISignal.SignalName.Done,true);

        undo = new TextedButton("Undo",ResourceManager.getInstance().removeTextureRegion, Button.ButtonType.OneClick, UISignal.SignalName.Undo,true);
        attachChild2(done, 2, -32);
        attachChild2(undo, 2, 32);
		active = true;
        setState(State.NONE);



	}

    public boolean getCenterMagnet() {
		return pointMagnet.isPressed();
	}

	public boolean getHorizontalMagnet() {
		return horMagnet.isPressed();
	}

	public boolean getVerticalMagnet() {
		return verMagnet.isPressed();
	}

	public float getRange(){
		return range.getValue();
	}

	public boolean isAnalog() {
		return AnalogSpeed.isOn();
	}

	public boolean isFixedX() {
		return fixedX.isOn();
	}

	public boolean isFixedY() {
		return fixedY.isOn();
	}
	public boolean isMultiple() {
		return multiple.isOn();
	}

	public void update(){
		if(GameScene.model.getActiveLayerID()==-1){
			previous = state;
			setState(State.NONE);
			active=false;
return;
		} else { active = true;
		setState(previous);

		}
		if(GameScene.model.getActiveLayer().hasCuttingDecoration())cutStart.hightlight();
		else cutStart.unhighlight();

	}
	public void updateCutLayerName(String s){
		cutLayerName.setText(s);
	}

	public void setState(State state){
if(!active)return;
	    if(this.state!=null) {

			switch (this.state.v) {
				case 1:

					numVertices.numericInput.save(ElementName.NumberPolygon);
					range.numericInput.save(ElementName.RangePolygon);
					break;

			}
		}
			this.state = state;
			switch (this.state.v) {
				case 1:
					numVertices.numericInput.restoreValue(ElementName.NumberPolygon);
					range.numericInput.restoreValue(ElementName.RangePolygon);
				break;
			}



int k = state.v;
switch (state) {

	case NONE:
		break;
	case POLYGON:
		title.setText("Draw Polygon");
		break;
	case SHAPE:
		title.setText("Draw Path");
		break;
	case INSERT:
		title.setText("Insert Point");
		break;
	case MOVE:
		title.setText("Move Point");
		break;
	case REMOVE:
		title.setText("Remove Point");
		break;
	case MIRROR:
		title.setText("Mirror");
		break;
	case PERPEND:
		title.setText("Right Angles Path");
		break;
	case CUT:
		title.setText("Cut Layer");
		break;
	case DECALE:
		title.setText("Shift");
		break;
	case ROTATE:
		title.setText("Rotate");
		break;
	case REVOLUTE:
		title.setText("Revolute Joint");
		break;
	case WELD:
		title.setText("Weld Joint");
		break;
	case DISTANCE:
		title.setText("Distance Joint");
		break;
	case PRISMATIC:
		title.setText("Prismatic Joint");
		break;
	case MOVEJOINT:
		title.setText("Move");
	case IMAGEMAIN:
		title.setText("Image Main Menu");
		break;
	case MOVEIMAGE:
		title.setText("Move Image");
		break;
	case CREATEMAIN:
		title.setText("Draw Main Menu");
		break;
	case PIPER:
		title.setText("Pipe Color");
		break;
}


//{title,hormg,vermg,pmg,range,verts,branch,analogspeed,fixedx,fixedy,cutstart,magtitle}
if(grid[k][0])title.setVisible(true);else title.setVisible(false);
if(grid[k][1])horMagnet.setVisible(true);else horMagnet.setVisible(false);
if(grid[k][2])verMagnet.setVisible(true);else verMagnet.setVisible(false);
if(grid[k][3])pointMagnet.setVisible(true);else pointMagnet.setVisible(false);
if(grid[k][4]){range.setVisible(true);}else range.setVisible(false);
if(grid[k][5])numVertices.setVisible(true);else numVertices.setVisible(false);
if(grid[k][6]) branch.setVisible(true);else branch.setVisible(false);
if(grid[k][7]) AnalogSpeed.setVisible(true);else AnalogSpeed.setVisible(false);
if(grid[k][8]) fixedX.setVisible(true);else fixedX.setVisible(false);
if(grid[k][9]) fixedY.setVisible(true);else fixedY.setVisible(false);
if(grid[k][10]) cutStart.setVisible(true);else cutStart.setVisible(false);
if(grid[k][11]) magTitle.setVisible(true);else magTitle.setVisible(false);
if(grid[k][12]) cutLayerName.setVisible(true);else cutLayerName.setVisible(false);
if(grid[k][13]) done.setVisible(true);else done.setVisible(false);
if(grid[k][14]) undo.setVisible(true);else undo.setVisible(false);
if(grid[k][15]) magnetFirstPoint.setVisible(true);else magnetFirstPoint.setVisible(false);
if(grid[k][16]) magnetSecondPoint.setVisible(true);else magnetSecondPoint.setVisible(false);
if(grid[k][17]) multiple.setVisible(true);else multiple.setVisible(false);
if(grid[k][18]) hasLimits.setVisible(true);else hasLimits.setVisible(false);
if(grid[k][19]) create.setVisible(true);else create.setVisible(false);
if(grid[k][20]) addimage.setVisible(true);else addimage.setVisible(false);
if(grid[k][21]) {pipeColorSlot.setVisible(true);addpipedcolor.setVisible(true);}else{ pipeColorSlot.setVisible(false);addpipedcolor.setVisible(false);}
}

	@Override
	public void diffuse(UISignal signal) {
		UserInterface ui = (UserInterface) getParent();

		if(signal.signalName== UISignal.SignalName.ONOFF&&signal.event==UISignal.Event.Clicked){
			if((Button)signal.source==multiple.getButton()) {
				ui.out_signal_setMovable();

			}
		}

		if(signal.signalName== UISignal.SignalName.ONOFF&&signal.event==UISignal.Event.Unclicked){
            if((Button)signal.source==multiple.getButton()) {
				ui.out_signal_resetActiveLayers();
			}
        }



		if(signal.signalName==UISignal.SignalName.Activate&&signal.event==UISignal.Event.Clicked){
if(((ActivateElement)signal.source).ID==0)
		ui.diffuse(new UISignal(UISignal.SignalName.StartCutting));
			if(((ActivateElement)signal.source).ID==1) {
				GameScene.model.create();
				GameScene.action = GameScene.PlayerAction.DRAG;
//ResourceManager.getInstance().activity.startLoadPictureIntent();
			}
			if(((ActivateElement)signal.source).ID==2) {
             ResourceManager.getInstance().activity.startLoadPictureIntent();
			}
			if(((ActivateElement)signal.source).ID==3) {
				ui.addColorToPanel(pipeColorSlot.getColor());
			}
		}

		if(signal.signalName==UISignal.SignalName.revjointButton){
			if(signal.event==UISignal.Event.Clicked)
				setState(State.REVOLUTE);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.NONE);
		}
		if(signal.signalName==UISignal.SignalName.weldjointButton){
			if(signal.event==UISignal.Event.Clicked)
				setState(State.WELD);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.NONE);
		}
		if(signal.signalName==UISignal.SignalName.disjointButton){
			if(signal.event==UISignal.Event.Clicked)
				setState(State.DISTANCE);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.NONE);
		}

		if(signal.signalName==UISignal.SignalName.prisjointButton){
			if(signal.event==UISignal.Event.Clicked)
				setState(State.PRISMATIC);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.NONE);
		}

		if(signal.signalName==UISignal.SignalName.movejointButton){
			if(signal.event==UISignal.Event.Clicked)
				setState(State.MOVEJOINT);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.NONE);
		}




		if(signal.signalName==UISignal.SignalName.POLYGONBUTTON){
			if(signal.event==UISignal.Event.Clicked)
setState(State.POLYGON);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.CREATEMAIN);
		}

		if(signal.signalName==UISignal.SignalName.PATHBUTTON){
			if(signal.event==UISignal.Event.Clicked)
			setState(State.SHAPE);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.CREATEMAIN);
		}
		if(signal.signalName==UISignal.SignalName.INSERTPOINTBUTTON){
			if(signal.event==UISignal.Event.Clicked)
				setState(State.INSERT);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.CREATEMAIN);
		}
		if(signal.signalName==UISignal.SignalName.MOVEELEMENTBUTTON){
			if(signal.event==UISignal.Event.Clicked)
				setState(State.MOVE);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.CREATEMAIN);
		}
		if(signal.signalName==UISignal.SignalName.PERPENDBUTTON){
			if(signal.event==UISignal.Event.Clicked)
				setState(State.PERPEND);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.CREATEMAIN);
		}
		if(signal.signalName==UISignal.SignalName.MIRRORBUTTON){
			if(signal.event==UISignal.Event.Clicked)
				setState(State.MIRROR);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.CREATEMAIN);
		}
		if(signal.signalName==UISignal.SignalName.REMOVEPOINTBUTTON){
			if(signal.event==UISignal.Event.Clicked)
				setState(State.REMOVE);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.CREATEMAIN);
		}
		if(signal.signalName==UISignal.SignalName.DECALEBUTTON){
			if(signal.event==UISignal.Event.Clicked)
				setState(State.DECALE);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.CREATEMAIN);
		}
		if(signal.signalName==UISignal.SignalName.CUTBUTTON){
			if(signal.event==UISignal.Event.Clicked)
				setState(State.CUT);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.CREATEMAIN);
		}
		if(signal.signalName==UISignal.SignalName.ROTATEBUTTON){
			if(signal.event==UISignal.Event.Clicked)
				setState(State.ROTATE);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.NONE);
		}

		if(signal.signalName==UISignal.SignalName.IMAGEBOARDBUTTON){
			if(signal.event==UISignal.Event.Clicked)
				setState(State.IMAGEMAIN);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.NONE);
		}
		if(signal.signalName==UISignal.SignalName.DRAWBOARDBUTTON){
			if(signal.event==UISignal.Event.Clicked)
				setState(State.CREATEMAIN);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.NONE);
		}
		if(signal.signalName==UISignal.SignalName.MOVEIMAGEBUTTON){
			if(signal.event==UISignal.Event.Clicked)
				setState(State.MOVEIMAGE);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.NONE);
		}
		if(signal.signalName==UISignal.SignalName.SCALEIMAGEBUTTON){
			if(signal.event==UISignal.Event.Clicked)
				setState(State.SCALEIMAGE);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.NONE);
		}

		if(signal.signalName==UISignal.SignalName.PIPEBUTTON){
			if(signal.event==UISignal.Event.Clicked)
				setState(State.PIPER);
			else if(signal.event==UISignal.Event.Unclicked)
				setState(State.NONE);
		}
		if(signal.signalName==UISignal.SignalName.TextField&&signal.event==UISignal.Event.Unclicked){
			int value = this.numVertices.numericInput.getIntValue();
			ui.out_signal_setPolygonNumVertices(value);
		}



	}



	@Override
	public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {
		if(isVisible()){
        if(super.OnTouchScene(pSceneTouchEvent))return true;
		}
		return false;
	}

	public boolean isFirstPointMagnet() {
		return magnetFirstPoint.isOn();
	}

	public boolean isSecondPointMagnet() {
		return magnetSecondPoint.isOn();
	}
	public boolean isHasLimits() {
		return hasLimits.isOn();
	}

	public void setPipeColor(Color color) {
		 pipeColorSlot.setColor(color);
	}

    public Color getPipeColor() {
		return pipeColorSlot.circle.getColor();
    }

    enum State{

	NONE(0), POLYGON(1),SHAPE(2),INSERT(3),MOVE(4),
		REMOVE(5),MIRROR(6),PERPEND(7),CUT(8),
		DECALE(9),ROTATE(10),REVOLUTE(11),WELD(12),
		DISTANCE(13),PRISMATIC(14),MOVEJOINT(15),IMAGEMAIN(16),MOVEIMAGE(17),
		CREATEMAIN(18), SCALEIMAGE(19),PIPER(20);
		int v;
		State(int i){
		v = i;
		}
}
void setCoordinated(Coordinated bounded){
		this.coords.bind(bounded);
}



}
