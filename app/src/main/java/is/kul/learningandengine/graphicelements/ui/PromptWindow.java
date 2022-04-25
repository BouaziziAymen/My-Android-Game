package is.kul.learningandengine.graphicelements.ui;

import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.GameScene;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

public class PromptWindow extends Panel {

    boolean active;
    UISignal signal;
    UIHandler source;
private void echoSignal(){
    signal.confirmed = true;
    source.diffuse(signal);
    this.setVisible(false);
}
    public void setSignal(UISignal signal, UIHandler source) {
        this.signal = signal;
        this.source = source;
    }

    public PromptWindow() {
        super(400, 240, 6, true,true);

        Text text = new Text(width/2, 0, ResourceManager.getInstance().dungeonFont, "Are you sure?", ResourceManager.getInstance().vbom);
        this.attachChild(text);
        active = true;


    }


    @Override
    public void diffuse(UISignal signal) {
        UserInterface ui = (UserInterface) getParent();
        if(signal.signalName == UISignal.SignalName.YesPanel&&signal.event == UISignal.Event.Clicked){
            this.echoSignal();
        }

        if(signal.signalName == UISignal.SignalName.NoPanel&&signal.event == UISignal.Event.Clicked){
            this.setVisible(false);
        }
        ui.diffuse(signal);


    }


    @Override
    public boolean OnTouchScene(TouchEvent touch) {
if(this.isVisible()) {
    if (super.OnTouchScene(touch)) return true;

}
        return false;
    }


}
