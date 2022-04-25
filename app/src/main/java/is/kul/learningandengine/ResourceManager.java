package is.kul.learningandengine;

import android.graphics.Bitmap;
import android.graphics.Typeface;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;

public class ResourceManager {
// single instance is created only
private static final ResourceManager INSTANCE = new
ResourceManager();




public static ITiledTextureRegion smallButton;




public  ITextureRegion colorSelectorTextureRegion;




//common objects
public GameActivity activity;
public MyEngine engine;
public Camera camera;
public VertexBufferObjectManager vbom;
//sounds
public Sound soundFall;
public Sound soundJump;
//music
public Music music;

private BuildableBitmapTextureAtlas gameTextureAtlas;

public TiledTextureRegion playerTextureRegion;

public TiledTextureRegion enemyTextureRegion;
public TiledTextureRegion dacaleButtonTextureRegion;

public TextureRegion platformTextureRegion;

public TextureRegion cloud1TextureRegion;

public TextureRegion cloud2TextureRegion;
    public TiledTextureRegion validateTextureRegion;
    public TiledTextureRegion rotateTextureRegion;
    public TiledTextureRegion controlButton;
    public TiledTextureRegion imageButtonTextureRegion;
    public ITiledTextureRegion moveImageTextureRegion;
    public ITiledTextureRegion panelButtonTextureRegion1;
    public TiledTextureRegion scaleButtonTextureRegion;
    public TextureRegion sketchTextureRegion;
    public TiledTextureRegion pipeButtonTextureRegion;
    public ArrayList<ITextureRegion> panel;
    public TiledTextureRegion panelButtonTextureRegion2;


    public void loadGameAudio() {
try {
SoundFactory.setAssetBasePath("sfx/");
    this.soundJump = SoundFactory.createSoundFromAsset
(this.activity.getSoundManager(), this.activity, "jump.wav");
    this.soundFall = SoundFactory.createSoundFromAsset
(this.activity.getSoundManager(), this.activity, "fall.wav");
MusicFactory.setAssetBasePath("mfx/");
    this.music = MusicFactory.createMusicFromAsset
(this.activity.getMusicManager(), this.activity, "music.mp3");
} catch (Exception e) {
throw new RuntimeException("Error while loading audio", e);
}
}

//font
public Font font;




public TextureRegion mOnScreenControlBaseTextureRegion;




public TextureRegion mOnScreenControlKnobTextureRegion;




public TiledTextureRegion insertPointTextureRegion;

public TiledTextureRegion decaleTextureRegion;




public TiledTextureRegion movePointTextureRegion;




public TiledTextureRegion createPathTextureRegion;




public TiledTextureRegion removePointTextureRegion;




public TiledTextureRegion b1TextureRegion;




public ArrayList<ITextureRegion> window;
public ArrayList<ITextureRegion> lamp;
public ArrayList<TiledTextureRegion> keyboardButtons;

public ArrayList<ArrayList<ITextureRegion>> quantity;

public ArrayList<TextureRegion> messageBoxRegions;



public TextureRegion textplace;




public Font font2;




public TiledTextureRegion polygonTextureRegion;




public ITiledTextureRegion mirrorTextureRegion;




public TiledTextureRegion perpendTextureRegion;




public TiledTextureRegion cutTextureRegion;




public TiledTextureRegion acceptvTextureRegion;




public TiledTextureRegion refusevTextureRegion;




public TiledTextureRegion bodyButtonTextureRegion;




public TiledTextureRegion renameButtonTextureRegion;




public Font font3;




public TiledTextureRegion layerButtonTextureRegion;




public ITiledTextureRegion upButtonTextureRegion;




public ITiledTextureRegion downButtonTextureRegion;




public ITiledTextureRegion addTextureRegion;




public ITiledTextureRegion removeTextureRegion;





public ITextureRegion trailTextureRegion;




public TextureRegion upperTextureRegion;




public TextureRegion lowerTextureRegion;




public TextureRegion knobTextureRegion;




public TiledTextureRegion windowFoldTextureRegion;







public ITiledTextureRegion simpleButtonTextureRegion;




public TiledTextureRegion removeBigTextureRegion;




public TiledTextureRegion validateBigTextureRegion;




public TiledTextureRegion smallOptionsTextureRegion;




public TextureRegion windowFieldTextureRegion;
public TextureRegion ball;




public TextureRegion bloodParticle;





public TiledTextureRegion drawTextureRegion;




public TiledTextureRegion jointTextureRegion;

    public TiledTextureRegion collisionTextureRegion;


public TiledTextureRegion revoluteTextureRegion;




public TiledTextureRegion weldTextureRegion;




public TiledTextureRegion distanceTextureRegion;




public ITiledTextureRegion movePointTextureRegion2;




public ITiledTextureRegion onoffTextureRegion;




public TiledTextureRegion copyTextureRegion;




public TiledTextureRegion prismaticTextureRegion;




public TextureRegion fireParticle;




public Font dungeonFont;


public void loadFont() {


	ITexture fontTexture = new BitmapTextureAtlas(this.activity.getTextureManager(),2*1024,1024

	);

    this.dungeonFont = FontFactory.createFromAsset(this.activity.getFontManager(),fontTexture, this.activity.getAssets(),"font/hellmuth.ttf",12f,true,Color.WHITE_ABGR_PACKED_INT);

    this.dungeonFont.load();


    this.font = FontFactory.create(this.activity.getFontManager(),
        this.activity.getTextureManager(), 256, 256,
Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD), 28,
true, Color.WHITE_ABGR_PACKED_INT);
    this.font.load();
    this.font.prepareLetters("01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.,!?".toCharArray());

    this.font2 = FontFactory.create(this.activity.getFontManager(),
        this.activity.getTextureManager(), 256, 256,
Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD), 14,
true, Color.WHITE_ABGR_PACKED_INT);
    this.font2.load();
    this.font2.prepareLetters("01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.,!?".toCharArray());


    this.font3 = FontFactory.create(this.activity.getFontManager(),
        this.activity.getTextureManager(), 256, 256,
Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD), 12,
true, Color.WHITE_ABGR_PACKED_INT);
    this.font3.load();
    this.font3.prepareLetters("01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.,!?".toCharArray());
}

public void create(GameActivity activity, MyEngine engine, Camera
camera, VertexBufferObjectManager vbom) {
this.activity = activity;
this.engine = engine;
this.camera = camera;
this.vbom = vbom;

}
private ResourceManager() { }
public static ResourceManager getInstance() {
return ResourceManager.INSTANCE;
}
BuildableBitmapTextureAtlas texture;




private BitmapTextureAtlas texturedMeshT;




public TextureRegion imageTextureRegion;



public TiledTextureRegion decorButtonTextureRegion;




public TiledTextureRegion windowCloseTextureRegion;




public TextureRegion slotTextureRegion;




public ITiledTextureRegion messageTextureRegion;




public TiledTextureRegion magnetPointTextureRegion;




public TiledTextureRegion magnetVerTextureRegion;




public TiledTextureRegion magnetHorTextureRegion;

   public Bitmap sketchBitmap;
public void loadImage(Bitmap bitmap){
    this.texture.clearTextureAtlasSources();
	BitmapTextureAtlasSource source = new BitmapTextureAtlasSource(bitmap);

    this.texture.addTextureAtlasSource(source, null);
    this.texture.load();
    sketchTextureRegion = TextureRegionFactory.createFromSource(this.texture, source,0,0);
    sketchBitmap = bitmap;

}

public void loadGameGraphics() {
BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    this.gameTextureAtlas =
new BuildableBitmapTextureAtlas(this.activity.getTextureManager(),2048, 2048, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

    this.texture = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 512, 512);


    this.texturedMeshT = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.REPEATING_BILINEAR);
    this.imageTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.texturedMeshT, this.activity,"pika.jpg", 0, 0);
    this.texturedMeshT.load();


    this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
        this.gameTextureAtlas, this.activity.getAssets(), "controllers/knob.png");


    this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
            this.gameTextureAtlas, this.activity.getAssets(), "base.png");
    this.controlButton = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "controllers/controller.png", 4, 1);
    this.insertPointTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/insert.png", 1, 3);

    this.movePointTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/movepoint.png", 1, 3);
    this.moveImageTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/moveimage.png", 1, 3);
    this.rotateTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/rotate.png", 1, 3);
    this.scaleButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/scale.png", 1, 3);

    this.movePointTextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/movejoint.png", 1, 3);
    this.onoffTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "onoff.png", 2, 1);
    this.createPathTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/path.png", 1, 3);
    this.removePointTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/remove.png", 1, 3);

    this.polygonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/polygon.png", 1, 3);

    this.mirrorTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/mirror.png", 1, 3);
    this.perpendTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/perpend.png", 1, 3);
    this.drawTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "drawoption.png", 2, 1);
    this.jointTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "jointoption.png", 2, 1);
    this.imageButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "imageoption.png", 2, 1);
    this.collisionTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "collisionoption.png", 2, 1);
    this.cutTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/cut.png", 1, 3);
    this.decaleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/decale.png", 1, 2);

    this.dacaleButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/decale.png", 1, 3);

    this.revoluteTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/revolute.png", 1, 3);
    this.weldTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/weld.png", 1, 3);
    this.distanceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/distance.png", 1, 3);
    this.prismaticTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/prismatic.png", 1, 3);

    this.copyTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "copy.png", 1, 2);
    this.messageTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "message/info.png", 1, 2);
    this.prismaticTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/prismatic.png", 1, 3);

    this.acceptvTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "accept1.png", 3, 1);
    this.refusevTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "refuse1.png", 3, 1);
    this.smallButton = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "smallButton.png", 1, 2);
    this.slotTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "slot.png");

    this.textplace = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "textfield.png");
    this.colorSelectorTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "colorcircle.png");
    this.trailTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "scroller/middle.png");
    this.bloodParticle = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "particle/water.png");
    this.fireParticle = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "particle/f4.png");
    this.upperTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "scroller/upper.png");
    this.lowerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "scroller/lower.png");
    this.knobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "scroller/knob.png");
    this.pipeButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "boards/pipebutton.png", 1, 3);
    this.panelButtonTextureRegion1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "panel/panelButton1.png", 3, 1);
    this.panelButtonTextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "panel/panelButton2.png", 3, 1);
    this.ball = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "ball.png");


    this.b1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "b1.png", 2, 1);


    this.bodyButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "bodybutton.png", 1, 2);

    this.decorButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "decorbutton.png", 1, 2);

    this.addTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "add.png", 3, 1);


    this.magnetPointTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "options/point.png", 1, 2);
    this.magnetVerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "options/hor.png", 1, 2);
    this.magnetHorTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "options/ver.png", 1, 2);


    this.renameButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "rename.png", 1, 2);
    this.layerButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "layerbutton.png", 1, 2);


    this.upButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "uparrow.png", 1, 3);
    this.downButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "downarrow.png", 1, 3);


    this.removeTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "removeround.png", 1, 2);
    this.windowFoldTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "windows/Buttons/fold.png", 1, 2);
    this.windowCloseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "windows/Buttons/close.png", 1, 2);


    this.simpleButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "simplefourbutton.png", 1, 2);


    this.removeBigTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "removebig.png", 1, 2);
    this.validateBigTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "validatebig.png", 1, 3);
    this.validateTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "validate.png", 1, 3);
    this.smallOptionsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(),  "smalloptions.png", 1, 2);


    this.window = new ArrayList<ITextureRegion>();
for(int i=0;i<=17;i++)
    this.window.add(BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "windows/w"+i+".png"));
    this.windowFieldTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "windowfield.png");


    this.quantity =  new ArrayList<ArrayList<ITextureRegion>>();

String key = "abgnrtz";
for(int k=0;k<key.length();k++){
    this.quantity.add(new ArrayList<ITextureRegion>());
for(int i=0;i<3;i++)
    this.quantity.get(k).add(BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "quantity/"+key.charAt(k)+i+".png"));
}
    this.lamp =  new ArrayList<ITextureRegion>();
    for(int i=0;i<3;i++)
lamp.add( BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "lamp/l"+i+".png"));

    this.panel =  new ArrayList<ITextureRegion>();
    for(int i=0;i<3;i++)
        panel.add( BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "panel/p"+i+".png"));




    this.keyboardButtons = new ArrayList<TiledTextureRegion>();
    this.keyboardButtons.add(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "keyboard/sbl.png",1,2));
    this.keyboardButtons.add(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "keyboard/sbm.png",1,2));
    this.keyboardButtons.add(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "keyboard/sbr.png",1,2));
    this.keyboardButtons.add(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "keyboard/bb.png",1,2));
    this.keyboardButtons.add(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "keyboard/mb.png",1,2));


    this.messageBoxRegions = new ArrayList<TextureRegion>();
    this.messageBoxRegions.add(BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "message/slot.png"));
    this.messageBoxRegions.add(BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "message/middle.png"));
    this.messageBoxRegions.add(BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameTextureAtlas, this.activity.getAssets(), "message/right.png"));






try {
    this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,BitmapTextureAtlas>(2, 0, 2));
    this.gameTextureAtlas.load();
} catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
throw new RuntimeException("Error while loading game textures", e);
}



}

}