package com.evolgames.gameengine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import com.evolgames.helpers.FontLoader;
import com.evolgames.helpers.MyLetter;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
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
import org.andengine.util.adt.color.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import is.kul.learningandengine.BitmapTextureAtlasSource;

public class ResourceManager {
    // single instance is created only
    private static final ResourceManager INSTANCE = new ResourceManager();
    public TextureRegion Aregion;
    public GameActivity activity;
    public TextureRegion diskTextureRegion;
    public Camera firstCamera;
    public VertexBufferObjectManager vbom;
    public BuildableBitmapTextureAtlas gameTextureAtlas;
    public TextureRegion base, knob;
    public BitmapTextureAtlas texturedMesh;
    public TiledTextureRegion imageTextureRegion;
    public TextureRegion liquidParticle;
    public TextureRegion pokemon;
    public TiledTextureRegion button;
    public ArrayList<ITextureRegion> window;
    public TiledTextureRegion windowFoldTextureRegion;
    public TiledTextureRegion windowCloseTextureRegion;
    public TiledTextureRegion layerButtonTextureRegion;
    public TiledTextureRegion removeTextureRegion;
    public Font font;
    public TiledTextureRegion mainButtonTextureRegion;
    public TiledTextureRegion largeLampTextureRegion;
    public TextureRegion trailTextureRegion;
    public TextureRegion upperTextureRegion;
    public TextureRegion lowerTextureRegion;
    public ITextureRegion scrollerKnobTextureRegion;
    public TiledTextureRegion addTextureRegion;
    public ArrayList<TiledTextureRegion> upButtonTextureRegions = new ArrayList<>();
    public ArrayList<TiledTextureRegion> downButtonTextureRegions = new ArrayList<>();
    public ArrayList<ArrayList<ITextureRegion>> quantity;
    public ArrayList<TiledTextureRegion> buttons;
    public TiledTextureRegion smallOptionsTextureRegion;
    public TextureRegion colorSelectorTextureRegion;
    public ArrayList<ITextureRegion> panel;
    public TiledTextureRegion panelButtonTextureRegion1;
    public TiledTextureRegion panelButtonTextureRegion2;
    public TiledTextureRegion squareTextureRegion;
    public TiledTextureRegion add1;
    public TiledTextureRegion minus1;
    public TextureRegion slotTextureRegion;
    public TiledTextureRegion smallButtonTextureRegion;
    public TiledTextureRegion simpleButtonTextureRegion;
    public TiledTextureRegion onoffTextureRegion;
    public Engine engine;
    public TiledTextureRegion decaleTextureRegion;
    public ITextureRegion bloodParicle;
    public TiledTextureRegion optionsCenterTextureRegion;
    public TextureRegion mOnScreenControlBaseTextureRegion;
    public TiledTextureRegion controlButton;
    public TiledTextureRegion addPointTextureRegion;
    public TiledTextureRegion movePointTextureRegion;
    public TiledTextureRegion removePointTextureRegion;
    public TiledTextureRegion addPolygonTextureRegion;
    public TiledTextureRegion mirrorTextureRegion;
    public TiledTextureRegion rotateTextureRegion;
    public TextureRegion centeredDiskTextureRegion;
    public TextureRegion handPointTextureRegion;
    public TiledTextureRegion drawBigButton, collisionBigButton, jointBigButton, imageBigButton;
    public TiledTextureRegion revoluteTextureRegion, weldTextureRegion, distanceTextureRegion, prismaticTextureRegion, moveJointTextureRegion;
    public TextureRegion doubleDiskTextureRegion;
    public TextureRegion diamondTextureRegion;
    public TextureRegion emptySquareTextureRegion;
    public TextureRegion doubleSquareTextureRegion;
    public TextureRegion targetCircleTextureRegion;
    public TiledTextureRegion scaleButtonTextureRegion;
    public TiledTextureRegion rotateImageButtonTextureRegion;
    public TiledTextureRegion pipeButtonTextureRegion;
    public TiledTextureRegion moveImageButtonTextureRegion;
    public TextureRegion plasmaParticle1, plasmaParticle2, plasmaParticle3, plasmaParticle4;
    public TextureRegion slotInnerTextureRegion;
    public TextureRegion sketchTextureRegion;
    public Bitmap sketchBitmap;
    public TiledTextureRegion optionsPointTextureRegion;
    public TiledTextureRegion optionsVerTextureRegion;
    public TiledTextureRegion optionsHorTextureRegion;
    public TiledTextureRegion optionsDirTextureRegion;
    public TiledTextureRegion optionsMagnetTextureRegion;
    public TiledTextureRegion optionsLinesTextureRegion;
    public Camera secondCamera;
    public TiledTextureRegion targetButtonTextureRegion;
    public TextureRegion handTextureRegion;
    public TextureRegion armTextureRegion;
    public TiledTextureRegion handleTextureRegion;
    public TextureRegion handle1;
    public TextureRegion base1;
    public TiledTextureRegion trigger1;
    public ArrayList<GameSound> gunshotSounds;
    private FontLoader fontLoader;
    private BuildableBitmapTextureAtlas texture;
    public TextureRegion aimCircleTextureRegion;
    public TiledTextureRegion saveBigButton;

    public static ResourceManager getInstance() {
        return ResourceManager.INSTANCE;
    }

    public void loadFonts() {


        this.font = FontFactory.create(this.activity.getFontManager(),
                this.activity.getTextureManager(), 256, 256,
                Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL), 8,
                true, Color.WHITE_ABGR_PACKED_INT);
        this.font.load();
        this.font.prepareLetters("01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.,!?".toCharArray());

    }

    public MyLetter getLetter(int fontId, char character) {
        return fontLoader.getLetter(fontId, character);
    }

    public void loadImages() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        this.texturedMesh = new BitmapTextureAtlas(activity.getTextureManager(), 256, 128, TextureOptions.REPEATING_BILINEAR);
        this.imageTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.texturedMesh, this.activity, "stainbw.png", 0, 0, 14, 1);
        this.texturedMesh.load();

        this.texture = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 2048);


        this.gameTextureAtlas = new BuildableBitmapTextureAtlas(this.activity.getTextureManager(), 2048, 2048, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.base = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "base.png");
        this.knob = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "knob.png");
        this.pokemon = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "bulbasaur.png");
        this.liquidParticle = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "particle/waterSmall.png");
        this.plasmaParticle1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "particle/f1.png");
        this.plasmaParticle2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "particle/f2.png");
        this.plasmaParticle3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "particle/f3.png");
        this.plasmaParticle4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "particle/f4.png");


        this.button = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "removebig.png", 1, 2);


        this.revoluteTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "boards/revolute.png", 1, 3);
        this.weldTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "boards/weld.png", 1, 3);
        this.distanceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "boards/distance.png", 1, 3);
        this.prismaticTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "boards/prismatic.png", 1, 3);
        this.moveJointTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "boards/movejoint.png", 1, 3);


        this.drawBigButton = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "main_board/drawoption.png", 2, 1);
        this.jointBigButton = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "main_board/jointoption.png", 2, 1);
        this.imageBigButton = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "main_board/imageoption.png", 2, 1);
        this.collisionBigButton = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "main_board/collisionoption.png", 2, 1);
        this.saveBigButton = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "main_board/saveoption.png", 2, 1);

        this.optionsPointTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "options/point.png", 1, 2);
        this.optionsVerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "options/hor.png", 1, 2);
        this.optionsHorTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "options/ver.png", 1, 2);
        this.optionsDirTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "options/dir.png", 1, 2);

        this.optionsCenterTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "options/center.png", 1, 2);

        this.optionsMagnetTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "options/magnet.png", 1, 2);
        this.optionsLinesTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "options/lines.png", 1, 2);


        this.window = new ArrayList<>();
        for (int i = 0; i <= 17; i++)
            this.window.add(BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "windows/w" + i + ".png"));
        this.windowFoldTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "windows/Buttons/fold.png", 1, 3);
        this.windowCloseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "windows/Buttons/close.png", 1, 3);
        this.layerButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "layerbutton.png", 1, 2);
        this.removeTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "removeround.png", 1, 2);
        this.mainButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "bodybutton.png", 1, 2);
        this.largeLampTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "decorbutton.png", 1, 2);
        this.trailTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "scroller/middle.png");
        this.upperTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "scroller/upper.png");
        this.lowerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "scroller/lower.png");
        this.addTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "add.png", 3, 1);
        this.add1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "addgreen.png", 1, 3);
        this.minus1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "minusred.png", 1, 3);

        this.upButtonTextureRegions.add(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "uparrow.png", 1, 3));
        this.downButtonTextureRegions.add(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "downarrow.png", 1, 3));

        this.upButtonTextureRegions.add(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "uparrowblack.png", 1, 3));
        this.downButtonTextureRegions.add(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "downarrowblack.png", 1, 3));
        this.smallOptionsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "smalloptions.png", 1, 2);
        this.colorSelectorTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "colorchart.png");

        this.buttons = new ArrayList<>();
        this.buttons.add(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "keyboard/sbl.png", 1, 2));
        this.buttons.add(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "keyboard/sbm.png", 1, 2));
        this.buttons.add(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "keyboard/sbr.png", 1, 2));
        this.buttons.add(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "keyboard/bb.png", 1, 2));
        this.buttons.add(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "keyboard/mb.png", 1, 2));
        this.slotTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "slot.png");
        this.slotInnerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "slotinner.png");
        this.handTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "hand.png");
        this.armTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "arm.png");

        this.handle1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "controllers/handle1.png");
        this.base1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "controllers/base1.png");

        this.trigger1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "controllers/buttons1.png", 2, 1);


        this.handPointTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "shapes/hand1.png");
        this.diskTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "shapes/circle.png");
        this.centeredDiskTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "shapes/centerCircle.png");
        this.doubleDiskTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "shapes/doublecircle.png");
        this.diamondTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "shapes/diamond.png");
        this.emptySquareTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "shapes/square.png");
        this.doubleSquareTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "shapes/doublesquare.png");
        this.targetCircleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "shapes/targetcircle.png");
        this.aimCircleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "shapes/aimcircle.png");


        this.scaleButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "boards/scale.png", 1, 3);
        this.rotateImageButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "boards/rotate.png", 1, 3);
        this.pipeButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "boards/pipebutton.png", 1, 3);
        this.moveImageButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "boards/moveimage.png", 1, 3);

        this.targetButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "boards/target.png", 1, 3);


        this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "base.png");
        this.controlButton = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "controllers/controller.png", 4, 1);

        this.panelButtonTextureRegion1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "panel/panelButton1.png", 3, 1);
        this.panelButtonTextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "panel/panelButton2.png", 3, 1);
        this.smallButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "smallButton.png", 1, 2);

        this.simpleButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "simplefourbutton.png", 1, 2);

        this.onoffTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "onoff.png", 2, 1);
        this.panel = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            panel.add(BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "panel/p" + i + ".png"));


        this.quantity = new ArrayList<>();

        String key = "abgnrtz";
        for (int k = 0; k < key.length(); k++) {
            this.quantity.add(new ArrayList<>());
            for (int i = 0; i < 3; i++)
                this.quantity.get(k).add(BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "quantity/" + key.charAt(k) + i + ".png"));
        }


        this.scrollerKnobTextureRegion = createEmptyTextureRegion(10, 100);
        this.squareTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "colorslot.png", 1, 2);


        this.addPointTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "boards/insert.png", 1, 3);
        this.movePointTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "boards/movepoint.png", 1, 3);
        this.removePointTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "boards/remove.png", 1, 3);
        this.addPolygonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "boards/polygon.png", 1, 3);
        this.mirrorTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "boards/mirror.png", 1, 3);
        this.rotateTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "boards/rotate.png", 1, 3);
        this.decaleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "boards/decale.png", 1, 3);
        this.handleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "boards/hand.png", 1, 3);

        this.fontLoader = new FontLoader(gameTextureAtlas);


        fontLoader.loadFont(0, Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD), 40, Color.WHITE_ABGR_PACKED_INT, true);
        fontLoader.loadFont(1, Typeface.create(Typeface.SERIF, Typeface.BOLD), 15, Color.WHITE_ABGR_PACKED_INT, true);
        fontLoader.loadFont(2, Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC), 12, Color.WHITE_ABGR_PACKED_INT, true);
        fontLoader.loadFont(3, Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD), 12, Color.WHITE_ABGR_PACKED_INT, true);


        try {
            this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<>(2, 0, 1));
            this.gameTextureAtlas.load();

        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            throw new RuntimeException("Error while loading game textures", e);
        }


    }

    public float getFontHeight(int fontId, String textString) {
        return fontLoader.getHeight(fontId, textString);
    }


    private TextureRegion createEmptyTextureRegion(int width, int height) {
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE_ARGB_PACKED_INT);
        backgroundPaint.setStyle(Paint.Style.FILL);
        final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap);
        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), backgroundPaint);

        return BitmapTextureAtlasTextureRegionFactory.createFromSource(gameTextureAtlas, new BitmapTextureAtlasSource(bitmap));
    }

    public void create(GameActivity activity, Engine engine, Camera firstCamera, Camera secondCamera, VertexBufferObjectManager vbom) {
        this.activity = activity;
        this.engine = engine;
        this.firstCamera = firstCamera;
        this.secondCamera = secondCamera;
        this.vbom = vbom;

    }


    public void loadGameAudio() {
        try {
            Map<String, Integer> map = Stream.of(new Object[][]{
                    {"shot1", 1},
                    {"assault1", 4},
                    {"assault2", 2},
                    {"shot2", 1},
                    {"shot3", 1},
                    {"assault3", 3},
                    {"shot4", 1},
                    {"shot5", 1},
                    {"shot6", 1},
                    {"shot7", 1},
            }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));
            String[] names = new String[]{"shot1", "assault1", "assault2", "shot2", "shot3", "assault3", "shot4", "shot5", "shot6", "shot7"};
            int[] numbers = new int[]{1, 4, 2, 1, 1, 3, 1, 1, 1, 1};

            gunshotSounds = new ArrayList<>();
            SoundFactory.setAssetBasePath("sfx/");

            for (int i = 1; i <= 10; i++) {
                int n = numbers[i - 1];
                List<Sound> list = new ArrayList<>();
                for (int j = 1; j <= n; j++) {
                    System.out.println(i+"------"+j);
                    Sound soundShot = SoundFactory.createSoundFromAsset
                            (this.activity.getSoundManager(), this.activity, "gunshot/gun" + i + "/" + j + ".wav");
                    list.add(soundShot);
                }
                gunshotSounds.add(new GameSound(list, names[i - 1]));
            }

            MusicFactory.setAssetBasePath("mfx/");
//            this.music = MusicFactory.createMusicFromAsset
//                    (this.activity.getMusicManager(), this.activity, "music.mp3");
        } catch (Exception e) {
            throw new RuntimeException("Error while loading audio", e);
        }
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
}
