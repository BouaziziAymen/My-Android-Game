package is.kul.learningandengine.scene;

import org.andengine.entity.IEntity;
import is.kul.learningandengine.graphicelements.LayerProperty;
import is.kul.learningandengine.graphicelements.ui.JointProperty;

public class UISignal {

	public boolean confirmed;
	public SignalName signalName;
	public UISignal.Event event;
	public IEntity source;
	public Object data;
	public UISignal(SignalName signalName){
		this.signalName = signalName;

	}
	public UISignal(SignalName signalName, UISignal.Event event, IEntity source, boolean confirmed){
		this.signalName = signalName;
		this.event = event;
		this.source = source;
		this.confirmed = confirmed;
	}


	public UISignal(SignalName signalName, UISignal.Event event, IEntity source){
		this.signalName = signalName;
	this.event = event;
	this.source = source;
	confirmed = true;
		}
	public UISignal(SignalName signalName, UISignal.Event event, IEntity source, Object data){
		this.signalName = signalName;
	this.event = event;
	this.source = source;
	this.data = data;
	confirmed = true;
	}
	

public enum Event{
	Clicked, Unclicked, Released, changed, Rename
}
public enum SignalName {
	BodyElementMain, BodyElementAddLayerButton, BodyElementDecaleButton,
	BodyElementTextField, ColorSelector, DecorationButtonMain, DecorationButtonTextField, foldwindow, JointButtonMain, JointButtonOptions, JointButtonRemove, JointButtonTextField, JointOptionsDoneButton, JointOptionCancelButton, Keyboard, LayerButtonMain, LayerButtonOptions, LayerButtonCopy, LayerButtonDecale, LayerButtonUpArrow, Scroller, scrolldownarrow, scrolluparrow, LayerButtonDownArrow, LayerButtonRemove, LayerButtonAddDecoration, LayerButtonTextField, Rename, LayerOptionButtonMain, LayerOptionTextField, ONOFF, TextField, AddBodyButton, SimpleTextedButton, SimpleSecondaryButtonMain, SimpleSecondaryButtonTextField, revjointButton, weldjointButton, disjointButton, prisjointButton, movejointButton, INSERTPOINTBUTTON, PATHBUTTON, MOVEELEMENTBUTTON, REMOVEPOINTBUTTON, POLYGONBUTTON, MIRRORBUTTON, PERPENDBUTTON, CUTBUTTON, DECALEBUTTON, ACCEPTBUTTON, REFUSEBUTTON, DRAWBOARDBUTTON, JOINTBOARDBUTTON, QuantitySelector, Select, closewindow, ColorButton, messageBox, HorizentalMagnet, VerticalMagnet, COLLISIONBOARDBUTTON, AddCategoryButton, CollisionButtonOptions, CollisionButtonRemove, CategoryButtonMain, CategoryNameButtonMain, Updated, Done, Undo, StartCutting, Activate, DecorationButtonRemove, ROTATEBUTTON, DecorationButtonOptions, BodyOptionButton, YesButton, NoButton, IMAGEBOARDBUTTON, ROTATEIMAGEBUTTON, MOVEIMAGEBUTTON, SCALEIMAGEBUTTON, PIPEBUTTON, ClosePanel, NoPanel, YesPanel, ColorSquare, PointMagnet
	

}

}
