package is.kul.learningandengine.graphicelements.ui;


import android.util.Log;

import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;

import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

public class ColorPanel extends Panel {
    ArrayList<ColorSquare> squares;
    ColorPanel(float x, float y) {
        super(x, y, 8, true, true);
        squares = new ArrayList<ColorSquare>();
addColor(Color.RED);
        addColor(Color.GREEN);
        addColor(Color.YELLOW);

    }
   private void addSquare(ColorSquare sq, int room){
        this.attachChild(sq,room);
        squares.add(sq);
    }
    public void addColor(Color color){
        int n = squares.size();
        ColorSquare square = new ColorSquare(color,0,0);
        addSquare(square,n);
    }


    @Override
    public void diffuse(UISignal signal) {
        UIHandler ui = (UIHandler) getParent();

if(signal.signalName== UISignal.SignalName.ColorSquare&&signal.event== UISignal.Event.Clicked){
    for(ColorSquare sq:squares)if(sq!=signal.source)sq.unselect();
}

ui.diffuse(signal);
    }

    @Override
    public boolean OnTouchScene(TouchEvent touch) {
        boolean touched = false;
        if(this.isVisible()){

           if(super.OnTouchScene(touch))touched=true;
            for(ColorSquare sq:squares){
                if(sq.OnTouchScene(touch))touched=true;
            }

        }
         return touched;
    }
}
