package is.kul.learningandengine.helpers;
import android.util.Log;
public class FPSCounter {
long startTime = System.nanoTime();
public int frames;

public void logFrame() {
    this.frames++;
if(System.nanoTime() - this.startTime >= 10000000000l) {
Log.d("FPSCounter", "fps: " + this.frames /10);
    this.frames = 0;
    this.startTime = System.nanoTime();
}
}
}
