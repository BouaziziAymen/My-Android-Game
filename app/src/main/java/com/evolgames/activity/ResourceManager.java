package com.evolgames.activity;

import static com.evolgames.activity.GameActivity.HINTS_KEY;
import static com.evolgames.activity.GameActivity.MAP_KEY;
import static com.evolgames.activity.GameActivity.MUSIC_KEY;
import static com.evolgames.activity.GameActivity.SOUND_KEY;
import static com.evolgames.activity.GameActivity.VIBRATION_KEY;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Vibrator;

import com.evolgames.gameengine.R;
import com.evolgames.helpers.FontLoader;
import com.evolgames.helpers.ItemMetaData;
import com.evolgames.helpers.MyLetter;
import com.evolgames.userinterface.model.ItemCategory;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.color.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResourceManager {
    // single instance is created only
    private static final ResourceManager INSTANCE = new ResourceManager();

    public GameActivity activity;
    public TextureRegion diskTextureRegion;
    public Camera firstCamera;
    public VertexBufferObjectManager vbom;
    public BuildableBitmapTextureAtlas uiTextureAtlas;
    public BuildableBitmapTextureAtlas gameplayTextureAtlas;
    public TiledTextureRegion stainTextureRegions;
    public TextureRegion liquidParticle;

    public ArrayList<ITextureRegion> window;
    public TiledTextureRegion windowFoldTextureRegion;
    public TiledTextureRegion windowCloseTextureRegion;
    public TiledTextureRegion layerButtonTextureRegion;
    public TiledTextureRegion removeTextureRegion;

    public TiledTextureRegion mainButtonTextureRegion;
    public TiledTextureRegion largeLampTextureRegion;
    public TextureRegion trailTextureRegion;
    public TextureRegion upperTextureRegion;
    public TextureRegion lowerTextureRegion;
    public ITextureRegion scrollerKnobTextureRegion;
    public TiledTextureRegion addTextureRegion;
    public ArrayList<TiledTextureRegion> upButtonTextureRegions;
    public ArrayList<TiledTextureRegion> downButtonTextureRegions;
    public HashMap<String, ArrayList<ITextureRegion>> quantity;
    public ArrayList<TiledTextureRegion> keyboardButtons;
    public TiledTextureRegion smallOptionsTextureRegion;
    public TextureRegion colorSelectorTextureRegion;
    public ArrayList<ITextureRegion> panel;
    public TiledTextureRegion panelButtonTextureRegion1;
    public TiledTextureRegion panelButtonTextureRegion2;
    public TiledTextureRegion squareTextureRegion;
    public TiledTextureRegion add1;
    public TiledTextureRegion minus1;
    public TextureRegion slotTextureRegion;
    public TiledTextureRegion smallButtonTextureRegion, plusButtonTextureRegion;
    public TiledTextureRegion simpleButtonTextureRegion;
    public TiledTextureRegion onOffTextureRegion;
    public Engine engine;
    public TiledTextureRegion decaleTextureRegion;
    public TiledTextureRegion optionsCenterTextureRegion;
    public TextureRegion controlButton;
    public TiledTextureRegion addPointTextureRegion;
    public TiledTextureRegion movePointTextureRegion;
    public TiledTextureRegion removePointTextureRegion;
    public TiledTextureRegion addPolygonTextureRegion;
    public TiledTextureRegion mirrorTextureRegion;
    public TiledTextureRegion rotateTextureRegion;
    public TextureRegion centeredDiskTextureRegion;
    public TiledTextureRegion drawBigButton, collisionBigButton, jointBigButton, imageBigButton;
    public TiledTextureRegion revoluteTextureRegion,
            weldTextureRegion,
            distanceTextureRegion,
            prismaticTextureRegion,
            moveJointTextureRegion;
    public TextureRegion doubleDiskTextureRegion;
    public TextureRegion diamondTextureRegion;
    public TextureRegion emptySquareTextureRegion;
    public TextureRegion doubleSquareTextureRegion;
    public TextureRegion targetCircleTextureRegion;
    public TiledTextureRegion scaleButtonTextureRegion;
    public TiledTextureRegion rotateImageButtonTextureRegion;
    public TiledTextureRegion pipeButtonTextureRegion;
    public TiledTextureRegion moveImageButtonTextureRegion;
    public TextureRegion dotParticle, smokeParticle, plasmaParticle;
    public TextureRegion slotInnerTextureRegion;
    public TextureRegion sketchTextureRegion;
    public Bitmap sketchBitmap;
    public TiledTextureRegion optionsPointTextureRegion;
    public TiledTextureRegion optionsVerTextureRegion;
    public TiledTextureRegion optionsHorTextureRegion;
    public TiledTextureRegion optionsDirTextureRegion;
    public TiledTextureRegion optionsMagnetTextureRegion;
    public TiledTextureRegion optionsLinesTextureRegion;
    public TiledTextureRegion targetButtonTextureRegion;
    public TiledTextureRegion ammoTextureRegion;
    public List<GameSound> projectileSounds, penetrationSounds;
    public Sound slashSound, bluntSound, glueSound, gunEmptySound;
    public TextureRegion aimCircleTextureRegion;
    public TextureRegion focusTextureRegion;
    public TiledTextureRegion helpBigButton;
    public TiledTextureRegion saveBigButton;
    public TiledTextureRegion homeBigButton;
    public TextureRegion pixelParticle;
    public ArrayList<ITextureRegion> buttonRegionsA;
    public TiledTextureRegion infoBlueButton;
    public TiledTextureRegion rotationAntiClockTextureRegion;
    public TiledTextureRegion rotationClockTextureRegion;
    public TiledTextureRegion checkBoxTextureRegion;
    public TiledTextureRegion bombTextureRegion;
    public TiledTextureRegion specialPointTextureRegion;
    public TextureRegion targetShapeTextureRegion;
    public TextureRegion bombShapeTextureRegion;

    public TextureRegion specialPointShapeTextureRegion;
    public TextureRegion casingShapeTextureRegion;
    public TiledTextureRegion fireSourceTextureRegion;
    public TiledTextureRegion liquidSourceTextureRegion;
    public TextureRegion fireShapeTextureRegion;
    public TextureRegion liquidShapeTextureRegion;
    public TextureRegion projectileDragTextureRegion;
    public TiledTextureRegion projDragTextureRegion;
    public TiledTextureRegion showHideTextureRegion;
    public TextureRegion playTextureRegion;
    public Music mMusic, windSound, clockSound;
    public Sound onSound, offSound, meteorSound;
    public TextureRegion frostParticle;
    public ArrayList<Sound> motorSounds;
    private FontLoader fontLoader;
    private BuildableBitmapTextureAtlas texture;
    private Map<ItemCategory, List<ItemMetaData>> itemsMap;
    private ItemMetaData editorItem;
    private ItemMetaData selectedItemMetaData;
    private String mapString;
    private boolean sound, music, hints, vibration;
    private String lettersList;
    public TiledTextureRegion evilEyesTextureRegion;
    private HashMap<String, Integer> itemsTranslationMap;
    private Vibrator vibrator;
    public BitmapTextureAtlas texturedMesh;


    public String getLettersList() {
        return lettersList;
    }

    public static ResourceManager getInstance() {
        return ResourceManager.INSTANCE;
    }

    public static Bitmap createMirroredBitmap(Bitmap srcBitmap) {
        // Create a new bitmap with the same width and height as the source bitmap
        Bitmap mirroredBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());

        // Create a canvas with the mirrored bitmap
        Canvas canvas = new Canvas(mirroredBitmap);

        // Create a matrix for mirroring
        Matrix matrix = new Matrix();
        matrix.setScale(-1, 1, srcBitmap.getWidth() / 2f, srcBitmap.getHeight() / 2f); // Horizontal mirroring

        // Apply the matrix to the canvas
        canvas.drawBitmap(srcBitmap, matrix, null);

        return mirroredBitmap;
    }

    public String getMapString() {
        return mapString;
    }

    public void setMapString(String mapString) {
        this.mapString = mapString;
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public boolean isVibration() {
        return vibration;
    }

    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        if (music) {
            this.mMusic.play();
        } else {
            this.mMusic.pause();
        }
        this.music = music;
    }

    public void loadPreferences() {
        this.mapString = activity.loadStringFromPreferences(MAP_KEY, "Wood");
        this.sound = activity.loadBooleanFromPreferences(SOUND_KEY, true);
        this.music = activity.loadBooleanFromPreferences(MUSIC_KEY, true);
        this.hints = activity.loadBooleanFromPreferences(HINTS_KEY, true);
        this.vibration = activity.loadBooleanFromPreferences(VIBRATION_KEY, false);
    }

    public void loadFonts() {
    }

    public MyLetter getLetter(int fontId, char character) {
        return fontLoader.getLetter(fontId, character);
    }
    public void loadGameImages(){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");


        this.texturedMesh =
                new BitmapTextureAtlas(
                        activity.getTextureManager(), 256, 128, TextureOptions.REPEATING_BILINEAR);
        this.stainTextureRegions =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.texturedMesh, this.activity, "stainbw.png", 0, 0, 15, 1);
        this.texturedMesh.load();



        this.gameplayTextureAtlas =
                new BuildableBitmapTextureAtlas(
                        this.activity.getTextureManager(),
                        512,
                        512,
                        BitmapTextureFormat.RGBA_8888,
                        TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        this.liquidParticle =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.gameplayTextureAtlas, this.activity.getAssets(), "particle/liquid.png");
        this.frostParticle =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.gameplayTextureAtlas, this.activity.getAssets(), "particle/frost.png");
        this.dotParticle =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.gameplayTextureAtlas, this.activity.getAssets(), "particle/dot.png");
        this.smokeParticle =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.gameplayTextureAtlas, this.activity.getAssets(), "particle/smoke.png");
        this.plasmaParticle =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.gameplayTextureAtlas, this.activity.getAssets(), "particle/plasma.png");
        this.pixelParticle =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.gameplayTextureAtlas, this.activity.getAssets(), "particle/pixel.png");

        this.evilEyesTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.gameplayTextureAtlas, this.activity.getAssets(), "evileyes.png", 5, 1);

        this.playTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.gameplayTextureAtlas, this.activity.getAssets(), "play.png");

        this.focusTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.gameplayTextureAtlas, this.activity.getAssets(), "focus.png");


        try {
            this.gameplayTextureAtlas.build(new BlackPawnTextureAtlasBuilder<>(2, 0, 1));
            this.gameplayTextureAtlas.load();

        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            throw new RuntimeException("Error while loading game textures", e);
        }

    }


    public void loadUserInterfaceImages() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        this.texture = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 2048);

        this.uiTextureAtlas =
                new BuildableBitmapTextureAtlas(
                        this.activity.getTextureManager(),
                        1024,
                        1024,
                        BitmapTextureFormat.RGBA_8888,
                        TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        this.revoluteTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/revolute.png", 1, 3);
        this.weldTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/weld.png", 1, 3);
        this.distanceTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/distance.png", 1, 3);
        this.prismaticTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/prismatic.png", 1, 3);
        this.moveJointTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/movejoint.png", 1, 3);

        this.drawBigButton =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "main_board/drawoption.png", 2, 1);
        this.jointBigButton =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "main_board/jointoption.png", 2, 1);
        this.imageBigButton =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "main_board/imageoption.png", 2, 1);
        this.collisionBigButton =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas,
                        this.activity.getAssets(),
                        "main_board/collisionoption.png",
                        2,
                        1);

        this.helpBigButton =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "main_board/questionoption.png", 2, 1);

        this.saveBigButton =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "main_board/saveoption.png", 2, 1);

        this.homeBigButton =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "main_board/homeoption.png", 2, 1);


        this.optionsPointTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "options/point.png", 1, 2);
        this.optionsVerTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "options/hor.png", 1, 2);
        this.optionsHorTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "options/ver.png", 1, 2);
        this.optionsDirTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "options/dir.png", 1, 2);

        this.optionsCenterTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "options/center.png", 1, 2);

        this.optionsMagnetTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "options/magnet.png", 1, 2);
        this.optionsLinesTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "options/lines.png", 1, 2);

        this.window = new ArrayList<>();
        for (int i = 0; i <= 17; i++) {
            this.window.add(
                    BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                            this.uiTextureAtlas, this.activity.getAssets(), "windows/w" + i + ".png"));
        }
        this.windowFoldTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "windows/Buttons/fold.png", 1, 3);
        this.windowCloseTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "windows/Buttons/close.png", 1, 3);
        this.layerButtonTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "layerbutton.png", 1, 2);
        this.removeTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "removeround.png", 1, 2);
        this.showHideTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "showhide.png", 1, 2);
        this.mainButtonTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "bodybutton.png", 1, 2);
        this.largeLampTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "decorbutton.png", 1, 2);
        this.trailTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "scroller/middle.png");
        this.upperTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "scroller/upper.png");
        this.lowerTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "scroller/lower.png");
        this.addTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "add.png", 3, 1);
        this.add1 =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "addgreen.png", 1, 3);
        this.minus1 =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "minusred.png", 1, 3);

        this.infoBlueButton =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "infoblue.png", 1, 3);

        this.upButtonTextureRegions = new ArrayList<>();
        this.upButtonTextureRegions.add(
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "uparrow.png", 1, 3));

        this.downButtonTextureRegions = new ArrayList<>();
        this.downButtonTextureRegions.add(
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "downarrow.png", 1, 3));
        this.rotationAntiClockTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "rotationanticlock.png", 1, 3);
        this.rotationClockTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "rotationclock.png", 1, 3);

        this.upButtonTextureRegions.add(
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "uparrowblack.png", 1, 3));
        this.downButtonTextureRegions.add(
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "downarrowblack.png", 1, 3));
        this.smallOptionsTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "smalloptions.png", 1, 2);
        this.colorSelectorTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "colorchart.png");

        this.keyboardButtons = new ArrayList<>();
        this.keyboardButtons.add(
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "keyboard/sbl.png", 1, 2));
        this.keyboardButtons.add(
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "keyboard/sbm.png", 1, 2));
        this.keyboardButtons.add(
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "keyboard/sbr.png", 1, 2));
        this.keyboardButtons.add(
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "keyboard/bb.png", 1, 2));
        this.keyboardButtons.add(
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "keyboard/mb.png", 1, 2));
        this.slotTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "slot.png");
        this.slotInnerTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "slotinner.png");

        this.diskTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "shapes/circle.png");
        this.centeredDiskTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "shapes/centercircle.png");
        this.doubleDiskTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "shapes/doublecircle.png");
        this.diamondTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "shapes/diamond.png");
        this.emptySquareTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "shapes/square.png");
        this.doubleSquareTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "shapes/doublesquare.png");
        this.targetCircleTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "shapes/targetcircle.png");
        this.aimCircleTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "shapes/aimcircle.png");


        this.targetShapeTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "shapes/target.png");
        this.bombShapeTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "shapes/bomb.png");
        this.specialPointShapeTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "shapes/specialpoint.png");
        this.casingShapeTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "shapes/casing.png");
        this.fireShapeTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "shapes/fire.png");
        this.liquidShapeTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "shapes/liquid.png");
        this.projectileDragTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "shapes/projdrag.png");


        this.scaleButtonTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/scale.png", 1, 3);
        this.rotateImageButtonTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/rotate.png", 1, 3);
        this.pipeButtonTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/pipebutton.png", 1, 3);
        this.moveImageButtonTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/moveimage.png", 1, 3);

        this.targetButtonTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/target.png", 1, 3);

        this.controlButton =
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "controllers/control.png");

        this.panelButtonTextureRegion1 =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "panel/panelButton1.png", 3, 1);
        this.panelButtonTextureRegion2 =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "panel/panelButton2.png", 3, 1);
        this.smallButtonTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "smallButton.png", 1, 2);

        this.plusButtonTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "smallButtonPlus.png", 1, 2);


        this.simpleButtonTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "simplefourbutton.png", 1, 2);
        this.onOffTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "onoff.png", 2, 1);
        this.panel = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            panel.add(
                    BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                            this.uiTextureAtlas, this.activity.getAssets(), "panel/p" + i + ".png"));

        this.quantity = new HashMap<>();

        String letters = "abcgnrtz";
        for (int k = 0; k < letters.length(); k++) {
            String key = String.valueOf(letters.charAt(k));
            this.quantity.put(key, new ArrayList<>());
            for (int i = 0; i < 3; i++) {
                Objects.requireNonNull(this.quantity.get(key))
                        .add(
                                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                                        this.uiTextureAtlas,
                                        this.activity.getAssets(),
                                        "quantity/" + letters.charAt(k) + i + ".png"));
            }
        }

        this.scrollerKnobTextureRegion = createEmptyTextureRegion(10, 100);
        this.squareTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "colorslot.png", 1, 2);

        this.addPointTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/insert.png", 1, 3);
        this.movePointTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/movepoint.png", 1, 3);
        this.removePointTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/remove.png", 1, 3);
        this.addPolygonTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/polygon.png", 1, 3);
        this.mirrorTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/mirror.png", 1, 3);
        this.rotateTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/rotate.png", 1, 3);
        this.decaleTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/decale.png", 1, 3);

        this.ammoTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/ammo.png", 1, 3);
        this.bombTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/bomb.png", 1, 3);
        this.specialPointTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/specialpoint.png", 1, 3);
        this.fireSourceTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/firesource.png", 1, 3);
        this.liquidSourceTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/liquidsource.png", 1, 3);
        this.projDragTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "boards/projdrag.png", 1, 3);


        this.checkBoxTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                        this.uiTextureAtlas, this.activity.getAssets(), "checkbox.png", 1, 3);
        this.fontLoader = new FontLoader(uiTextureAtlas);

        this.buttonRegionsA = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            this.buttonRegionsA.add(
                    BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                            this.uiTextureAtlas, this.activity.getAssets(), "button/b" + i + ".png"));
        }

        this.lettersList = "01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.,_!?¿+-#:'äöüßÄÖÜáéíóúñÁÉÍÓÚÑâêôçãõÉÈÀÇÊÂÛôÔèàçêâûŒœÇèÉÊêÀÂÛÔôÉéûÈÇáÁíÍóÓúÚñÑâÂêÊôÔàÀçÇãÃõÕéÉèÈíÍúÚüÜöÖäÄßÜîÎ";
        fontLoader.loadFont(
                0,
                Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD),
                40,
                Color.WHITE_ABGR_PACKED_INT,
                true);
        fontLoader.loadFont(
                1, Typeface.create(Typeface.SERIF, Typeface.BOLD), 15, Color.WHITE_ABGR_PACKED_INT,
                true);
        fontLoader.loadFont(
                2,
                Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC),
                12,
                Color.WHITE_ABGR_PACKED_INT,
                true);
        fontLoader.loadFont(
                3,
                Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD),
                12,
                Color.WHITE_ABGR_PACKED_INT,
                true);

        try {
            this.uiTextureAtlas.build(new BlackPawnTextureAtlasBuilder<>(2, 0, 1));
            this.uiTextureAtlas.load();

        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            throw new RuntimeException("Error while loading game textures", e);
        }
    }

    public float getFontHeight(int fontId) {
        return fontLoader.getHeight(fontId);
    }

    public float getFontWidth(int fontId, String text) {
        return fontLoader.getWidth(fontId, text);
    }

    private TextureRegion createEmptyTextureRegion(int width, int height) {
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE_ARGB_PACKED_INT);
        backgroundPaint.setStyle(Paint.Style.FILL);
        final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap);
        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), backgroundPaint);

        return BitmapTextureAtlasTextureRegionFactory.createFromSource(
                uiTextureAtlas, new BitmapTextureAtlasSource(bitmap));
    }

    public void create(
            GameActivity activity, Engine engine, Camera firstCamera, VertexBufferObjectManager vbom) {
        this.activity = activity;
        this.engine = engine;
        this.firstCamera = firstCamera;
        this.vbom = vbom;
        this.vibrator = (Vibrator) activity.getSystemService(BaseGameActivity.VIBRATOR_SERVICE);

    }

    public void loadGameAudio() {
        try {


            String[] names =
                    new String[]{
                            "missile",
                            "pistol",
                            "rifle",
                            "shotgun",
                            "explosion"
                    };
            int[] numbers = new int[]{2, 7, 4, 1, 3};

            SoundFactory.setAssetBasePath("sfx/");

            bluntSound =
                    SoundFactory.createSoundFromAsset(
                            this.activity.getSoundManager(),
                            this.activity,
                            "blunt.wav");

            glueSound =
                    SoundFactory.createSoundFromAsset(
                            this.activity.getSoundManager(),
                            this.activity,
                            "glue.wav");

            Sound motor1 = SoundFactory.createSoundFromAsset(
                    this.activity.getSoundManager(),
                    this.activity,
                    "motor1.wav");

            Sound motor2 = SoundFactory.createSoundFromAsset(
                    this.activity.getSoundManager(),
                    this.activity,
                    "motor2.wav");

            Sound motor3 = SoundFactory.createSoundFromAsset(
                    this.activity.getSoundManager(),
                    this.activity,
                    "motor3.wav");
            motorSounds = new ArrayList<>();
            motor1.setLooping(true);
            motor2.setLooping(true);
            motor3.setLooping(true);
            motorSounds.add(motor1);
            motorSounds.add(motor2);
            motorSounds.add(motor3);

            slashSound =
                    SoundFactory.createSoundFromAsset(
                            this.activity.getSoundManager(),
                            this.activity,
                            "slash.wav");
            gunEmptySound =
                    SoundFactory.createSoundFromAsset(
                            this.activity.getSoundManager(),
                            this.activity,
                            "gun_empty.wav");

            meteorSound =
                    SoundFactory.createSoundFromAsset(
                            this.activity.getSoundManager(),
                            this.activity,
                            "meteor.wav");


            onSound =
                    SoundFactory.createSoundFromAsset(
                            this.activity.getSoundManager(),
                            this.activity,
                            "on.wav");
            offSound =
                    SoundFactory.createSoundFromAsset(
                            this.activity.getSoundManager(),
                            this.activity,
                            "off.wav");

            projectileSounds = new LinkedList<>();
            for (int i = 0; i < names.length; i++) {
                int n = numbers[i];
                for (int j = 0; j < n; j++) {
                    String path = names[i] + "/" + j + ".wav";
                    Sound soundShot =
                            SoundFactory.createSoundFromAsset(
                                    this.activity.getSoundManager(),
                                    this.activity,
                                    path);

                    projectileSounds.add(new GameSound(soundShot, names[i] + j, GameSound.SoundType.PROJECTILE));
                }
            }

            penetrationSounds = new LinkedList<>();
            for (int j = 0; j < 13; j++) {
                String path = "penetration/" + j + ".wav";
                Sound soundShot =
                        SoundFactory.createSoundFromAsset(
                                this.activity.getSoundManager(),
                                this.activity,
                                path);
                penetrationSounds.add(new GameSound(soundShot, "penetration" + j, GameSound.SoundType.PENETRATION));

            }

            MusicFactory.setAssetBasePath("mfx/");
            try {
                mMusic = MusicFactory.createMusicFromAsset(this.activity.getMusicManager(), this.activity, "music_main.mp3");
                mMusic.setLooping(true); // Loop the music
                mMusic.setVolume(0.2f);

                windSound = MusicFactory.createMusicFromAsset(this.activity.getMusicManager(), this.activity, "wind.mp3");
                windSound.setLooping(true); // Loop the music

                clockSound = MusicFactory.createMusicFromAsset(this.activity.getMusicManager(), this.activity, "clock.mp3");
                clockSound.setLooping(true); // Loop the music

                setMusic(this.music);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while loading audio", e);
        }
    }

    public GameSound getProjectileSound(String title) {
        return projectileSounds.stream().filter(e -> e.getTitle().equals(title)).findFirst().get();
    }

    public List<GameSound> getProjectileSounds() {
        return projectileSounds;
    }

    public void loadImage(Bitmap bitmap) {
        this.texture.clearTextureAtlasSources();
        BitmapTextureAtlasSource source = new BitmapTextureAtlasSource(bitmap);
        this.texture.addTextureAtlasSource(source, null);
        this.texture.load();
        sketchTextureRegion = TextureRegionFactory.createFromSource(this.texture, source, 0, 0);
        sketchBitmap = bitmap;
    }

    public String getString(int id) {
        return activity.getString(id);
    }

    public Map<ItemCategory, List<ItemMetaData>> getItemsMap() {
        return itemsMap;
    }

    public void setItemsMap(Map<ItemCategory, List<ItemMetaData>> itemsMap) {
        this.itemsMap = itemsMap;
    }

    public ItemMetaData getEditorItem() {
        return editorItem;
    }

    public void setEditorItem(ItemMetaData editorItem) {
        this.editorItem = editorItem;
    }

    public void mirrorSketch() {
        if (sketchBitmap != null) {
            loadImage(createMirroredBitmap(sketchBitmap));
        }
    }

    public ItemMetaData getSelectedItemMetaData() {
        return selectedItemMetaData;
    }

    public void setSelectedItemMetaData(ItemMetaData selectedItemMetaData) {
        this.selectedItemMetaData = selectedItemMetaData;
    }

    public void tryPlaySound(Sound sound, float volume, int priority) {
        if (this.sound) {
            sound.setPriority(priority);
            sound.setVolume(volume);
            sound.play();
        }
    }

    public void fillItemNamesTranslation(){
        itemsTranslationMap = new HashMap<>();
        itemsTranslationMap.put("Dirk#", R.string.dirk);
        itemsTranslationMap.put("Knife#", R.string.knife);
        itemsTranslationMap.put("Throwing Knife 1#", R.string.throwing_knife_1);
        itemsTranslationMap.put("Throwing Knife 2#", R.string.throwing_knife_2);
        itemsTranslationMap.put("Rocket 1#", R.string.rocket_1);
        itemsTranslationMap.put("Rocket 2#", R.string.rocket_2);
        itemsTranslationMap.put("Claymore#", R.string.claymore);
        itemsTranslationMap.put("Egyptian#", R.string.egyptian);
        itemsTranslationMap.put("Falchion#", R.string.falchion);
        itemsTranslationMap.put("Flaming Sword#", R.string.flaming_sword);
        itemsTranslationMap.put("Gladius#", R.string.gladius);
        itemsTranslationMap.put("Jian#", R.string.jian);
        itemsTranslationMap.put("Katana#", R.string.katana);
        itemsTranslationMap.put("Longsword#", R.string.longsword);
        itemsTranslationMap.put("Rapier#", R.string.rapier);
        itemsTranslationMap.put("Sabre#", R.string.sabre);
        itemsTranslationMap.put("Archery Target#", R.string.archery_target);
        itemsTranslationMap.put("Chair 1#", R.string.chair_1);
        itemsTranslationMap.put("Chair 2#", R.string.chair_2);
        itemsTranslationMap.put("Chair 3#", R.string.chair_3);
        itemsTranslationMap.put("Dragon#", R.string.dragon);
        itemsTranslationMap.put("Pandora's Jar#", R.string.pandoras_jar);
        itemsTranslationMap.put("Table 1#", R.string.table_1);
        itemsTranslationMap.put("Table 2#", R.string.table_2);
        itemsTranslationMap.put("Table 3#", R.string.table_3);
        itemsTranslationMap.put("Frag. Grenade 1#", R.string.frag_grenade_1);
        itemsTranslationMap.put("Frag. Grenade 2#", R.string.frag_grenade_2);
        itemsTranslationMap.put("Frag. Grenade 3#", R.string.frag_grenade_3);
        itemsTranslationMap.put("Frag. Grenade 4#", R.string.frag_grenade_4);
        itemsTranslationMap.put("Incendiary Bomb#", R.string.incendiary_bomb);
        itemsTranslationMap.put("Molotov Bomb#", R.string.molotov_bomb);
        itemsTranslationMap.put("Heavy Machine Gun#", R.string.heavy_machine_gun);
        itemsTranslationMap.put("Howitzer#", R.string.howitzer);
        itemsTranslationMap.put("Mini Tank#", R.string.mini_main_battle_tank);
        itemsTranslationMap.put("Baseball Bat#", R.string.baseball_bat);
        itemsTranslationMap.put("Cudgel#", R.string.cudgel);
        itemsTranslationMap.put("Flanged Mace 2#", R.string.flanged_mace_2);
        itemsTranslationMap.put("Flanged Mace#", R.string.flanged_mace);
        itemsTranslationMap.put("Morning Star#", R.string.morning_star);
        itemsTranslationMap.put("Warhammer#", R.string.warhammer);
        itemsTranslationMap.put("Assault Rifle 1#", R.string.assault_rifle_1);
        itemsTranslationMap.put("Assault Rifle 2#", R.string.assault_rifle_2);
        itemsTranslationMap.put("Assault Rifle 3#", R.string.assault_rifle_3);
        itemsTranslationMap.put("Flame Thrower 1#", R.string.flame_thrower_1);
        itemsTranslationMap.put("Machine Gun 1#", R.string.machine_gun_1);
        itemsTranslationMap.put("Machine Gun 2#", R.string.machine_gun_2);
        itemsTranslationMap.put("Pistol 1#", R.string.pistol_1);
        itemsTranslationMap.put("Pistol 2#", R.string.pistol_2);
        itemsTranslationMap.put("Pistol 3#", R.string.pistol_3);
        itemsTranslationMap.put("Revolver#", R.string.revolver);
        itemsTranslationMap.put("Rifle 1#", R.string.rifle_1);
        itemsTranslationMap.put("Rifle 2#", R.string.rifle_2);
        itemsTranslationMap.put("Rocket Launcher 1#", R.string.rocket_launcher_1);
        itemsTranslationMap.put("Rocket Launcher 2#", R.string.rocket_launcher_2);
        itemsTranslationMap.put("Submachine Gun 1#", R.string.submachine_gun_1);
        itemsTranslationMap.put("Submachine Gun 2#", R.string.submachine_gun_2);
        itemsTranslationMap.put("Javelin#", R.string.javelin);
        itemsTranslationMap.put("Pike#", R.string.pike);
        itemsTranslationMap.put("Pilum#", R.string.pilum);
        itemsTranslationMap.put("Spear#", R.string.spear);
        itemsTranslationMap.put("Bazooka Rocket#", R.string.bazooka_rocket);
        itemsTranslationMap.put("RPG Rocket#", R.string.rpg_rocket);
        itemsTranslationMap.put("Shell 120mm#", R.string.shell_120mm);
        itemsTranslationMap.put("Shell 155mm#", R.string.shell_155mm);
        itemsTranslationMap.put("Bow 2#", R.string.bow_2);
        itemsTranslationMap.put("Bow#", R.string.bow);
        itemsTranslationMap.put("Longbow#", R.string.longbow);
        itemsTranslationMap.put("Arrow 1#", R.string.arrow_1);
        itemsTranslationMap.put("Arrow 2#", R.string.arrow_2);
        itemsTranslationMap.put("Arrow 3#", R.string.arrow_3);
        itemsTranslationMap.put("Battle Axe#", R.string.battle_axe);
        itemsTranslationMap.put("Bearded Axe#", R.string.bearded_axe);
        itemsTranslationMap.put("Francisca#", R.string.francisca);
        itemsTranslationMap.put("Apple#", R.string.apple);
        itemsTranslationMap.put("Kiwi#", R.string.kiwi);
        itemsTranslationMap.put("Lemon#", R.string.lemon);
        itemsTranslationMap.put("Pumpkin#", R.string.pumpkin);
        itemsTranslationMap.put("Melon#", R.string.melon);
        itemsTranslationMap.put("Orange#", R.string.orange);
        itemsTranslationMap.put("Pear#", R.string.pear);
        itemsTranslationMap.put("Watermelon#", R.string.watermelon);
        itemsTranslationMap.put("Bullet 0.50 BMG#", R.string.bullet_0_50_bmg);
        itemsTranslationMap.put("Bullet 5mm#", R.string.bullet_5mm);
        itemsTranslationMap.put("Bullet 7mm#", R.string.bullet_7mm);
        itemsTranslationMap.put("Bullet 9mm#", R.string.bullet_9mm);
        itemsTranslationMap.put("Petrol Canister#", R.string.petrol_canister);
        itemsTranslationMap.put("Kerosene Canister#", R.string.kerosene_canister);

        itemsTranslationMap.put("Cup 1#", R.string.cup_1);
        itemsTranslationMap.put("Cup 2#", R.string.cup_2);
        itemsTranslationMap.put("Cup 3#", R.string.cup_3);
        itemsTranslationMap.put("Milk Bottle#", R.string.milk_bottle);
        itemsTranslationMap.put("Water Bottle#", R.string.water_bottle);
        itemsTranslationMap.put("Iron Ball#", R.string.iron_ball);
        itemsTranslationMap.put("Glass#", R.string.a_glass);
        itemsTranslationMap.put("Firework#", R.string.firework);
    }

    public Integer getTranslatedItemStringId(String name){
        if(itemsTranslationMap.containsKey(name)){
            return itemsTranslationMap.get(name);
        }
        return -1;
    }

    public boolean isHints() {
        return hints;
    }

    public void setHints(boolean hints) {
        this.hints = hints;
    }

    public void disposeOfEditorResources() {
        if(uiTextureAtlas!=null) {
            this.uiTextureAtlas.unload();
            this.uiTextureAtlas = null;
        }
    }


    public void disposeOfPlayResources() {
        if(texturedMesh!=null) {
            this.texturedMesh.unload();
            this.texturedMesh = null;
        }
        if(gameplayTextureAtlas!=null) {
            this.gameplayTextureAtlas.unload();
            this.gameplayTextureAtlas = null;
        }
    }

    public void vibrate(int duration) {
        if(this.vibration){
            vibrator.vibrate(duration);
        }
    }
}
