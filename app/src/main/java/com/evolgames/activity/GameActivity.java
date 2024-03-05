package com.evolgames.activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.evolgames.activity.components.PlayUIFragment;
import com.evolgames.gameengine.R;
import com.evolgames.scenes.MainScene;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.controller.MultiTouchController;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.IGameInterface;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

public class GameActivity extends BaseGameActivity {

    public static final int CAMERA_WIDTH = 800;
    public static final int CAMERA_HEIGHT = 480;
    private static final int RESULT_LOAD_IMAGE = 1;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    /**
     * Called when the activity is first created.
     */
    final int MY_PERMISSIONS_REQUEST = 7;
    public Engine engine;
    private Camera camera;
    private MainScene scene;
    private PlayUIFragment gameUIFragment;

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw mHeight and mWidth of image
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {

            int halfHeight = height / 2;
            int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String strPath, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        // options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(strPath, options);
        // Calculate inSampleSize
        options.inSampleSize = GameActivity.calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return bitmap;
    }

    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) {
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        GameActivity.SCREEN_HEIGHT = metrics.heightPixels;
        GameActivity.SCREEN_WIDTH = metrics.widthPixels;

        engine = new LimitedFPSEngine(pEngineOptions, 60);
        engine.setTouchController(new MultiTouchController());
        engine.registerUpdateHandler(new FPSLogger());
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        return engine;
    }

    @Override
    protected void onSetContentView() {
        this.mRenderSurfaceView = new RenderSurfaceView(this);
        this.mRenderSurfaceView.setRenderer(this.mEngine, this);
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        this.camera = new SmoothCamera(0, 0, GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT, 1000 * 32f, 1000 * 32f, 5f);
        this.camera.setZClippingPlanes(-1, 1);

        IResolutionPolicy resolutionPolicy = new FillResolutionPolicy();
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, resolutionPolicy, this.camera);

        engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);

        // IMPROVE GRAPHICS FORCE 32 BIT
        engineOptions.getRenderOptions().setDithering(true);
        engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedAlphaSize(8);
        engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedRedSize(8);

        engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedGreenSize(8);
        engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedBlueSize(8);

        Debug.i("Engine configured");
        return engineOptions;
    }

    @Override
    public void onCreateResources(IGameInterface.OnCreateResourcesCallback pOnCreateResourcesCallback) {
        ResourceManager.getInstance().create(this, this.getEngine(), camera, this.getVertexBufferObjectManager());
        ResourceManager.getInstance().loadFonts();
        ResourceManager.getInstance().loadImages();
        ResourceManager.getInstance().loadGameAudio();
        ResourceManager.getInstance().loadBatches();
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(IGameInterface.OnCreateSceneCallback pOnCreateSceneCallback) {
        this.scene = new MainScene(this.camera);
        pOnCreateSceneCallback.onCreateSceneFinished(this.scene);
    }

    @Override
    public void onPopulateScene(Scene pScene, IGameInterface.OnPopulateSceneCallback pOnPopulateSceneCallback) {
        this.scene.populate();
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    @Override
    public synchronized void onSurfaceCreated(GLState pGLState) {
        super.onSurfaceCreated(pGLState);
        if (scene != null) {
            scene.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (scene != null) {
            scene.onPause();
        }
    }

    public boolean onKeyDown(final int keyCode, final KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }

    public void startLoadPictureIntent(String[] permissions) {

        /// Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                // Show an explanation
                Toast.makeText(this, "Load images from your storage", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST);
            }
        } else {

            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            this.startActivityForResult(i, GameActivity.RESULT_LOAD_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST) { // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                this.startActivityForResult(i, GameActivity.RESULT_LOAD_IMAGE);
            } // else permission denied
        }
    }

    @Override
    protected synchronized void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GameActivity.RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap b = BitmapFactory.decodeFile(picturePath);
            int width = Math.min(b.getWidth(), 2048);
            int height = Math.min(b.getHeight(), 2048);
            Bitmap bitmap = GameActivity.decodeSampledBitmapFromResource(picturePath, width, height);

            if (bitmap != null) {
                ResourceManager.getInstance().loadImage(bitmap);
                scene.onBackgroundImageLoaded();
            } else {
                Toast toast = Toast.makeText(this, "Invalid Image", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @SuppressWarnings("unused")
    public void returnToHomeScreen() {
        // Create an intent to go back to the home screen
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Start the intent
        startActivity(intent);
    }

    public void requestImagePermission() {
        // Permission request logic
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ResourceManager.getInstance().activity.startLoadPictureIntent(new String[]{READ_MEDIA_VISUAL_USER_SELECTED, READ_MEDIA_IMAGES});
            // requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ResourceManager.getInstance().activity.startLoadPictureIntent(new String[]{READ_MEDIA_IMAGES});
        } else {
            ResourceManager.getInstance().activity.startLoadPictureIntent(new String[]{READ_EXTERNAL_STORAGE});
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout gameContainer = this.findViewById(R.id.game_container);
        gameContainer.addView(mRenderSurfaceView, 0);
        this.gameUIFragment = new PlayUIFragment();
        replaceFragment(this.gameUIFragment);
    }
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.hide(fragment);
        transaction.commit();
    }

    public void installGameUi() {
        getSupportFragmentManager().beginTransaction()
                .show(this.gameUIFragment)
                .commit();
    }

    public void installEditorUi() {
        getSupportFragmentManager().beginTransaction()
                .hide(this.gameUIFragment)
                .commit();
    }

    public void installMenuUi() {
        getSupportFragmentManager().beginTransaction()
                .hide(this.gameUIFragment)
                .commit();
    }
}
