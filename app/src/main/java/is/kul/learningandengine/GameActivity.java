package is.kul.learningandengine;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.widget.Toast;

import org.andengine.engine.Engine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.controller.MultiTouchController;
import org.andengine.ui.IGameInterface;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import java.io.IOException;

import is.kul.learningandengine.scene.AbstractScene;
import is.kul.learningandengine.scene.GameScene;
public class GameActivity extends BaseGameActivity {

public static final int CAMERA_WIDTH = 800;
public static final int CAMERA_HEIGHT = 480;
	
	
public static int SCREEN_WIDTH;
public static int SCREEN_HEIGHT;
private MyCamera camera;
private MyCamera splitCamera1;
private MyCamera splitCamera2;
private GameScene scene;


@Override 
public Engine onCreateEngine(EngineOptions pEngineOptions){
    DisplayMetrics metrics = new DisplayMetrics();
    this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

    GameActivity.SCREEN_HEIGHT =  metrics.heightPixels;
    GameActivity.SCREEN_WIDTH =	metrics.widthPixels;

	
	Engine engine = new MyEngine(pEngineOptions, this.splitCamera1, this.splitCamera2);
    engine.setTouchController(new MultiTouchController());
		engine.registerUpdateHandler(new FPSLogger());

	return engine;
	

}

	
	
	@Override
	public EngineOptions onCreateEngineOptions() {
        this.camera = new MyCamera(0, 0, GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT);
        this.splitCamera1 = new MyCamera(0, 0, GameActivity.CAMERA_WIDTH >>1, GameActivity.CAMERA_HEIGHT);
        this.splitCamera2 = new MyCamera(GameActivity.CAMERA_WIDTH >>1, 0, GameActivity.CAMERA_WIDTH >>1, GameActivity.CAMERA_HEIGHT);

        this.camera.setZClippingPlanes(-1, 1);




	IResolutionPolicy resolutionPolicy = new
	FillResolutionPolicy();
	EngineOptions engineOptions = new EngineOptions(true,
	ScreenOrientation.LANDSCAPE_FIXED, resolutionPolicy,
            this.camera);
	engineOptions.getAudioOptions().setNeedsMusic(true).
	setNeedsSound(true);
	engineOptions.setWakeLockOptions(WakeLockOptions.
	SCREEN_ON);

	//IMPROVE GRAPHICS FORCE 32 BIT
	engineOptions.getRenderOptions().setDithering(true);

	engineOptions.getRenderOptions().getConfigChooserOptions().
	setRequestedAlphaSize(8);
	engineOptions.getRenderOptions().getConfigChooserOptions().
	setRequestedRedSize(8);

	engineOptions.getRenderOptions().getConfigChooserOptions().
	setRequestedGreenSize(8);
	engineOptions.getRenderOptions().getConfigChooserOptions().
	setRequestedBlueSize(8);

	
	Debug.i("Engine configured");
	return engineOptions;
	}

	@Override
	public void onCreateResources(
	IGameInterface.OnCreateResourcesCallback pOnCreateResourcesCallback)
	throws IOException {
	ResourceManager.getInstance().create(this, (MyEngine) this.getEngine(),
            this.getEngine().getCamera(), this.getVertexBufferObjectManager());
	ResourceManager.getInstance().loadFont();
	ResourceManager.getInstance().loadGameAudio();
	ResourceManager.getInstance().loadGameGraphics();
	pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(IGameInterface.OnCreateSceneCallback
	pOnCreateSceneCallback)
	throws IOException {
        this.scene = new GameScene();
	pOnCreateSceneCallback.onCreateSceneFinished(this.scene);
	}
	@Override
	public void onPopulateScene(Scene pScene,
	IGameInterface.OnPopulateSceneCallback pOnPopulateSceneCallback)
	throws IOException {
	AbstractScene scene = (AbstractScene) pScene;
	scene.populate();
	pOnPopulateSceneCallback.onPopulateSceneFinished();
	
	}

	
	
	
	
	   private static final int RESULT_LOAD_IMAGE = 1;

	    /** Called when the activity is first created. */
		final int MY_PERMISSIONS_REQUEST = 7;
public void startLoadPictureIntent() {

	/// Here, thisActivity is the current activity
	if (ContextCompat.checkSelfPermission(this,
			Manifest.permission.READ_EXTERNAL_STORAGE)
			!= PackageManager.PERMISSION_GRANTED) {

		// Permission is not granted
		// Should we show an explanation?
		if (ActivityCompat.shouldShowRequestPermissionRationale(this,
				Manifest.permission.READ_EXTERNAL_STORAGE)) {
			// Show an explanation to the user *asynchronously* -- don't block
			// this thread waiting for the user's response! After the user
			// sees the explanation, try again to request the permission.
			Toast.makeText(this,"Load images from your storage",
					Toast.LENGTH_SHORT).show();
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
					MY_PERMISSIONS_REQUEST);
		} else {
			// No explanation needed; request the permission
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
					MY_PERMISSIONS_REQUEST);

			// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
			// app-defined int constant. The callback method gets the
			// result of the request.
		}
	} else {

		Intent i = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
		this.startActivityForResult(i, GameActivity.RESULT_LOAD_IMAGE);
	}


}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// permission was granted, yay! Do the
					// task you need to do.

					Intent i = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
					this.startActivityForResult(i, GameActivity.RESULT_LOAD_IMAGE);
				} else {
					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
			}

			// other 'case' lines to check for other
			// permissions this app might request.
		}
	}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
super.onActivityResult(requestCode, resultCode, data);
if (requestCode == GameActivity.RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
	            Uri selectedImage = data.getData();
	            String[] filePathColumn = { Media.DATA };

	            Cursor cursor = this.getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

Bitmap bitmap = getResizedBitmap(GameActivity.decodeSampledBitmapFromResource(picturePath,2048,2048),512,512);

if(bitmap!=null) {
	ResourceManager.getInstance().loadImage(bitmap);
	scene.addImage();
}
else {
	Toast toast = Toast.makeText(this, "Invalid Image", Toast.LENGTH_SHORT);
	toast.show();
     }
   }
}



public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
    int width = bm.getWidth();
    int height = bm.getHeight();
    float scaleWidth = (float) newWidth / width;
    float scaleHeight = (float) newHeight / height;
    // CREATE BlockA MATRIX FOR THE MANIPULATION
    Matrix matrix = new Matrix();
    // RESIZE THE BIT MAP
    matrix.postScale(scaleWidth, scaleHeight);

    // "RECREATE" THE NEW BITMAP
    Bitmap resizedBitmap = Bitmap.createBitmap(
        bm, 0, 0, width, height, matrix, false);
    bm.recycle();
    return resizedBitmap;
}



public static int calculateInSampleSize(Options options,
	            int reqWidth, int reqHeight) {
	        // Raw mHeight and mWidth of image
	        int height = options.outHeight;
	        int width = options.outWidth;
	        int inSampleSize = 1;    
	        if (height > reqHeight || width > reqWidth) {

	            int halfHeight = height / 2;
	            int halfWidth = width / 2;

	            // Calculate the largest inSampleSize value that is a power of 2 and
	            // keeps both
	            // mHeight and mWidth larger than the requested mHeight and mWidth.
	            while (halfHeight / inSampleSize > reqHeight
	                    && halfWidth / inSampleSize > reqWidth) {
	                inSampleSize *= 2;
	            }
	        }
	        return inSampleSize;
	    }

public static Bitmap decodeSampledBitmapFromResource(String strPath,int reqWidth, int reqHeight) {

	        // First decode with inJustDecodeBounds=true to check dimensions
	        Options options = new Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(strPath, options);
	        // Calculate inSampleSize
	        options.inSampleSize = GameActivity.calculateInSampleSize(options,reqWidth,
	                reqHeight);
	        // Decode bitmap with inSampleSize set
	        options.inJustDecodeBounds = false;
	        return BitmapFactory.decodeFile(strPath, options);
	}

}
