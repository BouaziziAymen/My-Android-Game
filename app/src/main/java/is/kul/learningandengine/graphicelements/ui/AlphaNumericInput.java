package is.kul.learningandengine.graphicelements.ui;

import org.andengine.entity.Entity;

import is.kul.learningandengine.scene.GameScene;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

public class AlphaNumericInput  extends TextPlace  {

    NumType type;
    int max;

    public AlphaNumericInput(String name,float x, float y, int n, Entity background) {
        super(name, x,y,background,-25);
        setLimit(n);
        this.type = type;
    }

    private void setLimit(int n){
        this.max = n;

    }
    void setText(String textString){


        this.text.setTextPoles(textString,textString+"-");
    }

String getText(){
        return textString;
}

    void appendCharacter(char c) {

        String textString = (String) this.text.text1;
        boolean proceed = true;

        if (c == '@') {
            if (textString != null && textString.length() > 0) {
                textString = textString.substring(0, textString.length() - 1);
            }
        } else if (c == '&')
            textString += " ";

        if (c == '@' || c == '&')
            proceed = false;
        if (c == '.') {
            if (textString.length() == 0)
                proceed = false;
            if (textString.length() > 0)
                if (textString.lastIndexOf('.') != -1)
                    proceed = false;
        }

        if (proceed) {


                textString += c;

        }
        this.text.setTextPoles(textString, textString+"-");


    }

    @Override
    public void diffuse(UISignal signal) {
        UIHandler ui = (UIHandler) getParent();
if(signal.signalName!= UISignal.SignalName.Rename)
        ui.diffuse(signal);
if(signal.signalName== UISignal.SignalName.Rename)signal.signalName= UISignal.SignalName.TextField;
        if (signal.signalName == UISignal.SignalName.TextField && signal.event == UISignal.Event.Clicked) {

            GameScene.ui.signal_keyboard_On(this);

        }

        if (signal.signalName == UISignal.SignalName.TextField && signal.event == UISignal.Event.Unclicked) {

            GameScene.ui.signal_keyboard_Off(this);
        }
    }


}
