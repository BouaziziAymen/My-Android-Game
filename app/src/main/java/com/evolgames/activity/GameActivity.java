package com.evolgames.activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.evolgames.activity.components.BadReviewDialog;
import com.evolgames.activity.components.CreateItemDialog;
import com.evolgames.activity.components.EditItemDialog;
import com.evolgames.activity.components.EditorHelpDialog;
import com.evolgames.activity.components.HelpDialog;
import com.evolgames.activity.components.MenuUIFragment;
import com.evolgames.activity.components.OptionsDialog;
import com.evolgames.activity.components.PlayUIFragment;
import com.evolgames.activity.components.RateUsDialog;
import com.evolgames.gameengine.R;
import com.evolgames.helpers.ItemMetaData;
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

import java.io.File;
import java.util.List;

public class GameActivity extends BaseGameActivity {

    public static final int CAMERA_WIDTH = 800;
    public static final int CAMERA_HEIGHT = 480;
    public static final String MAP_KEY = "GameMap";
    public static final String SOUND_KEY = "Sound";
    public static final String MUSIC_KEY = "Music";
    public static final String HINTS_KEY = "Hints";
    public static final String VIBRATION_KEY = "Vibration";
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String DEVELOPER_EMAIL = "aymendotbouazizi@gmail.com";
    private static final String EMAIL_SUBJECT = "Mutilate : Item";
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    /**
     * Called when the activity is first created.
     */
    final int MY_PERMISSIONS_REQUEST = 7;
    public Engine engine;
    private Camera camera;
    private MainScene mainScene;
    private PlayUIFragment gameUIFragment;
    private NativeUIController uiController;
    private UIType installedUI;
    private MenuUIFragment menuUIFragment;
    private CreateItemDialog createItemDialog;
    private EditItemDialog editItemDialog;
    private OptionsDialog optionsDialog;

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
        options.inJustDecodeBounds = true; // Set this to true to retrieve the dimensions only
        BitmapFactory.decodeFile(strPath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set to retrieve the resized bitmap
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(strPath, options);
        // Resize the bitmap if needed
        if (bitmap != null) {
            return Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, true);
        } else {
            return null; // Error handling: return null if bitmap decoding fails
        }
    }

    public UIType getInstalledUI() {
        return installedUI;
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
        this.camera = new SmoothCamera(0, 0, GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT, 12000 * 32f, 12000 * 32f, 1f);
        ((SmoothCamera) this.camera).setBounds(-Float.MAX_VALUE, 0, Float.MAX_VALUE, Float.MAX_VALUE);
        ((SmoothCamera) this.camera).setBoundsEnabled(true);
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
        ResourceManager.getInstance().loadPreferences();
        ResourceManager.getInstance().loadGameImages();
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(IGameInterface.OnCreateSceneCallback pOnCreateSceneCallback) {
        this.mainScene = new MainScene(this.camera);
        this.uiController.setMainScene(this.mainScene);
        String saved = this.loadStringFromPreferences("saved_tool_filename");
        this.saveStringToPreferences("SCENE", "MENU");
        if (!saved.isEmpty()) {
            ItemMetaData item = ResourceManager.getInstance().getItemsMap().values().stream().flatMap(List::stream)
                    .filter(e -> e.getFileName().equals(saved)).findFirst().orElse(null);
            ResourceManager.getInstance().setEditorItem(item);
        }
        pOnCreateSceneCallback.onCreateSceneFinished(this.mainScene);
    }

    @Override
    public void onPopulateScene(Scene pScene, IGameInterface.OnPopulateSceneCallback pOnPopulateSceneCallback) {
        this.mainScene.populate();
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    @Override
    public synchronized void onSurfaceCreated(GLState pGLState) {
        super.onSurfaceCreated(pGLState);
        ResourceManager.getInstance().loadGameAudio();
        mEngine.getSoundManager().onResume();
        if (mainScene != null) {
            mainScene.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mEngine != null && mEngine.getMusicManager() != null) {
            mEngine.getMusicManager().releaseAll();
        }
        if (mEngine != null && mEngine.getSoundManager() != null) {
            mEngine.getSoundManager().onPause();
        }
        if (mainScene != null) {
            mainScene.onPause();
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
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GameActivity.RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            assert selectedImage != null;
            Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
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
                mainScene.onBackgroundImageLoaded();
            } else {
                Toast toast = Toast.makeText(this, "Invalid Image", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public NativeUIController getUiController() {
        return uiController;
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
        hideStatusBar();

        setContentView(R.layout.activity_main);
        FrameLayout gameContainer = this.findViewById(R.id.game_container);
        gameContainer.addView(mRenderSurfaceView, 0);
        uiController = new NativeUIController(this);
        uiController.fillItemsMap();

        this.gameUIFragment = new PlayUIFragment();
        this.menuUIFragment = new MenuUIFragment();
        this.createItemDialog = new CreateItemDialog();
        this.editItemDialog = new EditItemDialog();
        this.optionsDialog = new OptionsDialog();
    }

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void showOptionsDialog() {
        getSupportFragmentManager().beginTransaction().hide(menuUIFragment).commit();
        optionsDialog.show(getSupportFragmentManager(), "optionsDialog");
    }

    public void showEditItemDialog() {
        try {
            createItemDialog.dismiss();
        } catch (Throwable t) {
        }

        getSupportFragmentManager().beginTransaction().hide(menuUIFragment).hide(createItemDialog).commit();
        editItemDialog.show(getSupportFragmentManager(), "editItemDialog");
    }


    public void showCreateItemDialog() {
        try {
            editItemDialog.dismiss();
        } catch (Throwable t) {
        }

        // Hide the menuUIFragment
        getSupportFragmentManager().beginTransaction().hide(menuUIFragment).hide(editItemDialog).commit();
// Show the dialog fragment
        createItemDialog.show(getSupportFragmentManager(), "createItemDialog");
    }

    public void installGameUi() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, this.gameUIFragment).show(this.gameUIFragment).hide(this.menuUIFragment).commit();
        this.installedUI = UIType.GAME;
    }

    public void installEditorUi() {
        getSupportFragmentManager().beginTransaction().hide(this.gameUIFragment).hide(this.menuUIFragment).commit();
        this.installedUI = UIType.EDITOR;
    }

    public void installMenuUi() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, this.menuUIFragment).hide(this.gameUIFragment).show(this.menuUIFragment).commit();
        this.installedUI = UIType.MENU;
    }


    public void sendEmailWithAttachment(String fileName) {
        // Create a file object representing the XML file
        File xmlFile = new File(getFilesDir(), fileName);

        // Generate a content URI for the file using FileProvider
        Uri contentUri = FileProvider.getUriForFile(this, "com.evolgames", xmlFile);

        // Create an email intent with appropriate parameters
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{DEVELOPER_EMAIL});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);
        emailIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant read permission to the receiver

        // Start the email activity
        startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }

    public PlayUIFragment getGameUIFragment() {
        return gameUIFragment;
    }

    public void openBadReviewDialog() {
        BadReviewDialog dialog = new BadReviewDialog(this);
        dialog.show();
    }

    public void saveOptions() {
        saveStringToPreferences(MAP_KEY, ResourceManager.getInstance().getMapString());
        saveBooleanToPreferences(SOUND_KEY, ResourceManager.getInstance().isSound());
        saveBooleanToPreferences(MUSIC_KEY, ResourceManager.getInstance().isMusic());
        saveBooleanToPreferences(HINTS_KEY, ResourceManager.getInstance().isHints());
        saveBooleanToPreferences(VIBRATION_KEY, ResourceManager.getInstance().isVibration());
    }

    public void showProceedToSend(String selectedItemFile){
        // Create an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

// Set the message
        builder.setMessage(R.string.send_item_instructions);

// Set the positive button (Proceed)
        builder.setPositiveButton(R.string.proceed, (dialog, id) -> {
            // Proceed action here
            sendEmailWithAttachment(selectedItemFile);
        });

// Set the negative button (Cancel)
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
            // Cancel action here
            dialog.dismiss();
        });

// Create the AlertDialog
        AlertDialog dialog = builder.create();

// Show the dialog
        dialog.show();
    }

    public void showHelpDialog() {
        HelpDialog dialog = new HelpDialog(this);
        dialog.show();
    }
    public void showEditorHelpDialog() {
        EditorHelpDialog dialog = new EditorHelpDialog(this);
        dialog.show();
    }

    public void showRateUsDialog() {
        RateUsDialog dialog = new RateUsDialog(this);
        dialog.show();
    }

    public void openPlayStoreForReview() {
        String packageName = this.getPackageName();
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }
    }

    public final void saveStringToPreferences(String key, String value) {
        SharedPreferences preferences =
                this
                        .getSharedPreferences("RAG_DOLL_MUT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public final void saveBooleanToPreferences(String key, boolean value) {
        SharedPreferences preferences =
                this
                        .getSharedPreferences("RAG_DOLL_MUT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public final boolean loadBooleanFromPreferences(String key) {
        SharedPreferences preferences =
                this
                        .getSharedPreferences("RAG_DOLL_MUT", Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    public final String loadStringFromPreferences(String key, String defaultValue) {
        SharedPreferences preferences =
                this
                        .getSharedPreferences("RAG_DOLL_MUT", Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }

    public final boolean loadBooleanFromPreferences(String key, boolean defaultValue) {
        SharedPreferences preferences =
                this
                        .getSharedPreferences("RAG_DOLL_MUT", Context.MODE_PRIVATE);
        return preferences.getBoolean(key, defaultValue);
    }

    public final String loadStringFromPreferences(String key) {
        SharedPreferences preferences =
                this
                        .getSharedPreferences("RAG_DOLL_MUT", Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }


    public enum UIType {
        GAME, EDITOR, MENU
    }
}
