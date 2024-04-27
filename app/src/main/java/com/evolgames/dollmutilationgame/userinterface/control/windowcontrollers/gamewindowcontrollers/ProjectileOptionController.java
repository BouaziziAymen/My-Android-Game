package com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.dollmutilationgame.activity.GameSound;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.ButtonWithText;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Quantity;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SimplePrimary;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.FieldWithError;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.SectionField;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.TitledButton;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.TitledQuantity;
import com.evolgames.dollmutilationgame.entities.persistence.PersistenceCaretaker;
import com.evolgames.dollmutilationgame.entities.persistence.PersistenceException;
import com.evolgames.dollmutilationgame.entities.properties.Explosive;
import com.evolgames.dollmutilationgame.entities.properties.ProjectileProperties;
import com.evolgames.gameengine.R;
import com.evolgames.dollmutilationgame.helpers.ItemMetaData;
import com.evolgames.dollmutilationgame.scenes.EditorScene;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.dollmutilationgame.userinterface.model.ItemCategory;
import com.evolgames.dollmutilationgame.userinterface.model.ProperModel;
import com.evolgames.dollmutilationgame.userinterface.model.ToolModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.CasingModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.projectieoptionwindow.SoundField;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.ParserConfigurationException;

public class ProjectileOptionController extends SettingsWindowController<ProjectileProperties> {
    private final EditorScene editorScene;
    private final Hashtable<String, ToolModel> missileTable;
    private final Hashtable<String, ButtonWithText<ProjectileOptionController>> missileButtonsTable;
    private ProjectileProperties projectileProperties;
    private ProjectileModel projectileModel;
    private Quantity<ProjectileOptionController> velocityQuantity;
    private Quantity<ProjectileOptionController> recoilQuantity;
    private Quantity<ProjectileOptionController> fireRatioQuantityField;
    private Quantity<ProjectileOptionController> smokeRatioQuantityField;
    private Quantity<ProjectileOptionController> sparkRatioQuantityField;
    private Quantity<ProjectileOptionController> intensityQuantityField;
    private ItemWindowController itemWindowController;
    private Quantity<ProjectileOptionController> inSizeQuantityField;
    private Quantity<ProjectileOptionController> finSizeQuantityField;
    private List<GameSound> sounds;
    private ProjectileProperties copy;

    public ProjectileOptionController(EditorScene editorScene) {
        this.editorScene = editorScene;
        this.missileTable = new Hashtable<>();
        this.missileButtonsTable = new Hashtable<>();
    }

    public void setItemWindowController(ItemWindowController itemWindowController) {
        this.itemWindowController = itemWindowController;
    }

    public void updateMissileSelectionFields() {
        if (window.getLayout().getPrimariesSize() >= 1) {
            window.getLayout().removePrimary(0);
        }
        SectionField<ProjectileOptionController> bodyASection =
                new SectionField<>(
                        0, ResourceManager.getInstance().getString(R.string.projectile_title), ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(bodyASection);

        List<ItemMetaData> files = new ArrayList<>();
        List<ItemMetaData> bullets = new ArrayList<>(Objects.requireNonNull(ResourceManager.getInstance().getItemsMap().get(ItemCategory.BULLET)));
        List<ItemMetaData> rockets = new ArrayList<>(Objects.requireNonNull(ResourceManager.getInstance().getItemsMap().get(ItemCategory.ROCKET)));
        List<ItemMetaData> arrows = new ArrayList<>(Objects.requireNonNull(ResourceManager.getInstance().getItemsMap().get(ItemCategory.ARROW)));
        List<ItemMetaData> shells = new ArrayList<>(Objects.requireNonNull(ResourceManager.getInstance().getItemsMap().get(ItemCategory.SHELL)));
        files.addAll(bullets);
        files.addAll(rockets);
        files.addAll(arrows);
        files.addAll(shells);
        AtomicInteger missileCounter = new AtomicInteger();
        files.forEach(itemMetaData -> createProjectileToolButton(itemMetaData, missileCounter.getAndIncrement()));
        this.onUpdated();
    }

    public void updateCasingSelectionFields() {
        if (model == null) {
            return;
        }
        if (window.getLayout().getPrimariesSize() >= 2) {
            window.getLayout().removePrimary(1);
        }
        SectionField<ProjectileOptionController> shellSection =
                new SectionField<>(
                        1, ResourceManager.getInstance().getString(R.string.casing_title), ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(shellSection);

        List<CasingModel> ammoModels =
                editorScene
                        .getUserInterface()
                        .getToolModel()
                        .getBodyModelById(((ProjectileModel) model).getBodyId())
                        .getCasingModels();

        ammoModels.forEach(this::createShellToolButton);
        this.onUpdated();
    }

    private void createShellToolButton(CasingModel ammoModel) {

        ButtonWithText<ProjectileOptionController> shellButton =
                new ButtonWithText<>(
                        ammoModel.getModelName(),
                        2,
                        ResourceManager.getInstance().simpleButtonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        SimpleSecondary<ButtonWithText<ProjectileOptionController>> shellField =
                new SimpleSecondary<>(1, ammoModel.getCasingId(), shellButton);
        window.addSecondary(shellField);
        shellButton.setBehavior(
                new ButtonBehavior<ProjectileOptionController>(this, shellButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        projectileModel.setCasingModel(ammoModel);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                    }
                });
        if (projectileModel.getCasingModel() == ammoModel) {
            shellButton.updateState(Button.State.PRESSED);
        }
    }

    private void createProjectileToolButton(ItemMetaData itemMetaData, int missileId) {
        int translatedNameStringId = ResourceManager.getInstance().getTranslatedItemStringId(itemMetaData.getName());
        ButtonWithText<ProjectileOptionController> missileButton =
                new ButtonWithText<>(
                        translatedNameStringId!=-1?ResourceManager.getInstance().getString(translatedNameStringId):itemMetaData.getName(),
                        2,
                        ResourceManager.getInstance().simpleButtonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        if (itemMetaData.getFileName().equals(projectileModel.getProperties().getMissileFile())) {
            missileButton.updateState(Button.State.PRESSED);
        } else {
            missileButton.updateState(Button.State.NORMAL);
        }
        SimpleSecondary<ButtonWithText<ProjectileOptionController>> missileField =
                new SimpleSecondary<>(0, missileId, missileButton);
        window.addSecondary(missileField);
        missileButtonsTable.put(itemMetaData.getFileName(), missileButton);
        missileButton.setBehavior(
                new ButtonBehavior<ProjectileOptionController>(this, missileButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        ToolModel missileModel;
                        if (!missileTable.containsKey(itemMetaData.getFileName())) {
                            try {
                                missileModel = PersistenceCaretaker.getInstance().loadToolModel(itemMetaData.getFileName(), false, !itemMetaData.isUserCreated());
                                missileTable.put(itemMetaData.getFileName(), missileModel);
                                projectileModel.getProperties().setMissileFile(itemMetaData.getFileName());
                                projectileModel.getProperties().setAssetsMissile(!itemMetaData.isUserCreated());
                            } catch (PersistenceException
                                     | SAXException
                                     | ParserConfigurationException
                                     | IOException e) {
                                e.printStackTrace();
                                projectileModel.getProperties().setMissileFile("");
                            }
                        } else {
                            projectileModel.getProperties().setMissileFile(itemMetaData.getFileName());
                            projectileModel.getProperties().setAssetsMissile(!itemMetaData.isUserCreated());
                        }

                        onProjectileBodyButtonClicked(missileField);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        if(projectileModel.getProperties().getMissileFile().equals(itemMetaData.getFileName())) {
                            projectileModel.getProperties().setMissileFile("");
                        }
                    }
                });
    }

    void onProjectileBodyButtonClicked(SimpleSecondary<?> projectileButton) {
        int primaryKey = projectileButton.getPrimaryKey();
        int secondaryKey = projectileButton.getSecondaryKey();
        for (int i = 0; i < window.getLayout().getSecondariesSize(primaryKey); i++) {
            SimpleSecondary<?> element = window.getLayout().getSecondaryByIndex(primaryKey, i);
            if (element.getSecondaryKey() != secondaryKey) {
                Element main = element.getMain();
                if (main instanceof ButtonWithText) {
                    ((ButtonWithText<?>) main).updateState(Button.State.NORMAL);
                }
            }
        }
    }

    public void onUpdated() {
        window.getLayout().updateLayout();
        onLayoutChanged();
    }

    @Override
    void onModelUpdated(ProperModel<ProjectileProperties> model) {
        super.onModelUpdated(model);
        if (model == null) {
            return;
        }
        this.projectileModel = (ProjectileModel) model;
        updateMissileSelectionFields();
        updateCasingSelectionFields();
        this.projectileProperties = model.getProperties();
        this.copy = (ProjectileProperties) projectileProperties.clone();
        setBody(projectileModel.getBodyId());
        setFireSound(this.projectileProperties.getFireSound());
        setMuzzleVelocity(this.projectileProperties.getMuzzleVelocity());
        setRecoil(this.projectileProperties.getRecoil());
        if (projectileModel.getProperties().getMissileFile() != null) {
            setMissileName(projectileModel.getProperties().getMissileFile());
        }
        setIntensity(this.projectileProperties.getParticles());
        setFireRatio(this.projectileProperties.getFireRatio());
        setSmokeRatio(this.projectileProperties.getSmokeRatio());
        setSparkRatio(this.projectileProperties.getSparkRatio());
        checkExplosive();
    }

    private void setRecoil(float recoil) {
        this.recoilQuantity.updateRatio(recoil);
    }

    private void setMuzzleVelocity(float muzzleVelocity) {
        this.velocityQuantity.updateRatio(muzzleVelocity);
    }

    private void setBody(int bodyId) {
    }


    @Override
    public void init() {
        super.init();
        SectionField<ProjectileOptionController> sectionField =
                new SectionField<>(
                        2, ResourceManager.getInstance().getString(R.string.shot_sound_title), ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(sectionField);

 //TODO wire with the new sound map
        sounds =  ResourceManager.getInstance().getProjectileSounds();

        for (int i = 0; i < sounds.size(); i++) {
            GameSound gameSound = sounds.get(i);
            SoundField soundField = new SoundField(gameSound.getTitle(), 2,  i, this);
            window.addSecondary(soundField);
        }

        SectionField<ProjectileOptionController> typeSection =
                new SectionField<>(
                        3, ResourceManager.getInstance().getString(R.string.velocity_title), ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(typeSection);

        TitledQuantity<ProjectileOptionController> titleVelocityQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.lin_velocity_title), 16, "r", 5, true);
        velocityQuantity = titleVelocityQuantity.getAttachment();
        titleVelocityQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<ProjectileOptionController>(this, velocityQuantity) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });

        FieldWithError muzzleVelocityFieldWithError = new FieldWithError(titleVelocityQuantity);
        SimpleSecondary<FieldWithError> muzzleVelocityElement =
                new SimpleSecondary<>(3, 1, muzzleVelocityFieldWithError);
        window.addSecondary(muzzleVelocityElement);
        velocityQuantity
                .getBehavior()
                .setChangeAction(
                        () -> this.projectileProperties.setMuzzleVelocity(velocityQuantity.getRatio()));

        TitledQuantity<ProjectileOptionController> titledRecoilQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.recoil_title), 10, "b", 5, true);
        recoilQuantity = titledRecoilQuantity.getAttachment();
        titledRecoilQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<ProjectileOptionController>(this, recoilQuantity) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });
        SimpleSecondary<TitledQuantity<?>> recoilElement =
                new SimpleSecondary<>(3, 2, titledRecoilQuantity);
        window.addSecondary(recoilElement);
        recoilQuantity
                .getBehavior()
                .setChangeAction(() -> this.projectileProperties.setRecoil(recoilQuantity.getRatio()));

        this.createExplosiveSection();
        this.createExplosiveSettings();

        window.createScroller();
        updateLayout();
        window.setVisible(false);
    }

    @Override
    public void onSubmitSettings() {
        super.onSubmitSettings();
        ProjectileModel projectileModel = (ProjectileModel) model;
        this.itemWindowController.onResume();
        editorUserInterface
                .getItemWindowController()
                .changeItemName(
                        model.getModelName(), projectileModel.getProjectileField().getPrimaryKey(), projectileModel.getProjectileField().getSecondaryKey());
    }


    private void setFireSound(String sound) {
       int n = window.getLayout().getSecondariesSize(2);
       for(int i=0;i<n;i++){
          SoundField soundField = (SoundField) window.getLayout().getSecondaryByIndex(2, i);
         String name =  soundField.getSoundFieldControl().getTitle();
         if(name.equals(sound)) {
             soundField.getSoundFieldControl().updateState(Button.State.PRESSED);
         } else {
             soundField.getSoundFieldControl().updateState(Button.State.NORMAL);
         }
       }
    }

    @Override
    public void onCancelSettings() {
        super.onCancelSettings();
        this.projectileModel.setProperties(copy);
        this.itemWindowController.onResume();
    }

    private void setMissileName(String fileName) {
        ButtonWithText<ProjectileOptionController> selectedButton = missileButtonsTable.get(fileName + ".xml");
        if (selectedButton != null) {
            missileButtonsTable.forEach((key, value) -> value.release());
            selectedButton.click();
        }
    }

    private void checkExplosive() {
        final int size = window.getLayout().getSecondariesSize(4);
        for (int i = 0; i < size; i++) {
            SimpleSecondary<TitledButton<ProjectileOptionController>> secondary =
                    (SimpleSecondary<TitledButton<ProjectileOptionController>>)
                            window.getLayout().getSecondaryByIndex(4, i);
            if (secondary.getSecondaryKey() == this.projectileProperties.getExplosive().ordinal()) {
                secondary.getMain().getAttachment().updateState(Button.State.PRESSED);
            } else {
                secondary.getMain().getAttachment().updateState(Button.State.NORMAL);
            }
        }
    }

    private void createExplosiveSection() {

        if (window.getLayout().getPrimaries().size() >= 4) {
            window.getLayout().removePrimary(4);
        }
        SectionField<ProjectileOptionController> explosiveSection =
                new SectionField<>(
                        4, ResourceManager.getInstance().getString(R.string.explosive_title), ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(explosiveSection);
        Arrays.stream(Explosive.values()).forEach(this::createCategoryCheckBox);
    }

    private void createCategoryCheckBox(Explosive explosive) {
        TitledButton<ProjectileOptionController> explosiveCheckBox =
                new TitledButton<>(
                        ResourceManager.getInstance().getString(explosive.stringId),
                        ResourceManager.getInstance().checkBoxTextureRegion,
                        Button.ButtonType.Selector,
                        5f,
                        true);

        SimpleSecondary<TitledButton<ProjectileOptionController>> explosiveField =
                new SimpleSecondary<>(4, explosive.ordinal(), explosiveCheckBox);
        window.addSecondary(explosiveField);
        explosiveCheckBox
                .getAttachment()
                .setBehavior(
                        new ButtonBehavior<ProjectileOptionController>(
                                this, explosiveCheckBox.getAttachment()) {
                            @Override
                            public void informControllerButtonClicked() {
                                ProjectileOptionController.this.onExplosiveButtonPressed(
                                        explosiveField, explosive, true);
                            }

                            @Override
                            public void informControllerButtonReleased() {
                                ProjectileOptionController.this.onExplosiveButtonReleased(explosiveField);
                            }
                        });
    }

    @Override
    public void onSecondaryButtonClicked(SimpleSecondary<?> simpleSecondary) {
        super.onSecondaryButtonClicked(simpleSecondary);
        int primaryKey = simpleSecondary.getPrimaryKey();
        int secondaryKey = simpleSecondary.getSecondaryKey();

        if (simpleSecondary.getPrimaryKey() == 2) {
            GameSound gameSound =   this.sounds
                    .get(secondaryKey);
            ResourceManager.getInstance().tryPlaySound(gameSound.getSound(),1f,1);
            projectileProperties.setFireSound(gameSound.getTitle());
            for (int i = 0; i < window.getLayout().getSecondariesSize(primaryKey); i++) {
                SimpleSecondary<?> element =
                        window.getLayout().getSecondaryByIndex(primaryKey, i);
                if (element.getSecondaryKey() != secondaryKey) {
                    Element main = element.getMain();
                    if (main instanceof ButtonWithText) {
                        ((ButtonWithText<?>) main).updateState(Button.State.NORMAL);
                    }
                }
            }
        }
    }

    private void onExplosiveButtonPressed(
            SimpleSecondary<?> explosiveField, Explosive e, boolean changeRatios) {
        super.onSecondaryButtonClicked(explosiveField);
        int size = window.getLayout().getSecondariesSize(4);
        this.projectileProperties.setExplosive(e);
        for (int i = 0; i < size; i++) {
            SimpleSecondary<?> other = window.getLayout().getSecondaryByIndex(4, i);
            if (explosiveField != other) {
                Element main = other.getMain();
                if (main instanceof TitledButton) {
                    ((TitledButton<?>) main).getAttachment().updateState(Button.State.NORMAL);
                }
            }
        }
        if (changeRatios) {
            this.projectileProperties.setFireRatio(e.getFireRatio());
            setFireRatio(e.getFireRatio());
            this.projectileProperties.setSmokeRatio(e.getSmokeRatio());
            setSmokeRatio(e.getSmokeRatio());
            this.projectileProperties.setSparkRatio(e.getSparkRatio());
            setSparkRatio(e.getSparkRatio());
            this.projectileProperties.setParticles(0.5f);
        }

        setIntensity(0.5f);
    }

    private void onExplosiveButtonReleased(SimpleSecondary<?> categoryField) {
        super.onSecondaryButtonReleased(categoryField);
    }

    @Override
    public void onSecondaryButtonReleased(SimpleSecondary<?> simpleSecondary) {
        super.onSecondaryButtonReleased(simpleSecondary);

        if (simpleSecondary.getPrimaryKey() == 2) {
            GameSound gameSound = this.sounds
                    .get(simpleSecondary.getSecondaryKey());
          if(projectileProperties.getFireSound().equals(gameSound.getTitle())) {
              projectileProperties.setFireSound("");
          }
        }
    }

    private void createExplosiveSettings() {
        for (SimplePrimary<?> p : window.getLayout().getPrimaries()) {
            if (p.getPrimaryKey() >= 5) {
                window.getLayout().removePrimary(p.getPrimaryKey());
            }
        }

        SectionField<ProjectileOptionController> explosiveSettingsSection =
                new SectionField<>(
                        5, ResourceManager.getInstance().getString(R.string.explosive_settings_title), ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(explosiveSettingsSection);

        TitledQuantity<ProjectileOptionController> titledFireRatioQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.fire_title), 10, "r", 5, true);

        fireRatioQuantityField = titledFireRatioQuantity.getAttachment();
        titledFireRatioQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<ProjectileOptionController>(this, fireRatioQuantityField) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                                ProjectileOptionController.this.projectileProperties.setFireRatio(
                                        quantity.getRatio());
                                ProjectileOptionController.this.onExplosiveOptionsChanged();
                            }
                        });

        FieldWithError fireRatioFieldWithError = new FieldWithError(titledFireRatioQuantity);
        SimpleSecondary<FieldWithError> fireRatioElement =
                new SimpleSecondary<>(5, 0, fireRatioFieldWithError);
        window.addSecondary(fireRatioElement);

        // -----
        TitledQuantity<ProjectileOptionController> titledSmokeRatioQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.smoke_title), 10, "t", 5, true);
        smokeRatioQuantityField = titledSmokeRatioQuantity.getAttachment();
        titledSmokeRatioQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<ProjectileOptionController>(this, smokeRatioQuantityField) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                                ProjectileOptionController.this.projectileProperties.setSmokeRatio(
                                        quantity.getRatio());
                            }
                        });

        FieldWithError smokeRatioFieldWithError = new FieldWithError(titledSmokeRatioQuantity);
        SimpleSecondary<FieldWithError> smokeRatioElement =
                new SimpleSecondary<>(5, 1, smokeRatioFieldWithError);
        window.addSecondary(smokeRatioElement);
        // -----
        TitledQuantity<ProjectileOptionController> titledSparkRatioQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.sparks_title), 10, "g", 5, true);
        sparkRatioQuantityField = titledSparkRatioQuantity.getAttachment();
        titledSparkRatioQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<ProjectileOptionController>(this, sparkRatioQuantityField) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                                ProjectileOptionController.this.projectileProperties.setSparkRatio(
                                        quantity.getRatio());
                            }
                        });

        FieldWithError sparkRatioFieldWithError = new FieldWithError(titledSparkRatioQuantity);
        SimpleSecondary<FieldWithError> sparkRatioElement =
                new SimpleSecondary<>(5, 2, sparkRatioFieldWithError);
        window.addSecondary(sparkRatioElement);

        // -----
        TitledQuantity<ProjectileOptionController> titledIntensityQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.particle_density_title), 10, "b", 5, true);
        intensityQuantityField = titledIntensityQuantity.getAttachment();
        titledIntensityQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<ProjectileOptionController>(this, intensityQuantityField) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                                ProjectileOptionController.this.projectileProperties.setParticles(
                                        quantity.getRatio());
                            }
                        });

        FieldWithError intensityFieldWithError = new FieldWithError(titledIntensityQuantity);
        SimpleSecondary<FieldWithError> intensityElement =
                new SimpleSecondary<>(5, 3, intensityFieldWithError);
        window.addSecondary(intensityElement);
    }

    private void onExplosiveOptionsChanged() {
        if (!projectileProperties.getExplosive().equals(Explosive.OTHER)) {
            SimpleSecondary<?> secondary = window.getLayout().getSecondary(4, 0);
            Element main = secondary.getMain();
            ((TitledButton<?>) main).getAttachment().updateState(Button.State.PRESSED);
            projectileProperties.setExplosive(Explosive.OTHER);
            onExplosiveButtonPressed(secondary, projectileProperties.getExplosive(), false);
        }
    }

    @Override
    public void onPrimaryButtonReleased(SimplePrimary<?> simplePrimary) {
        super.onPrimaryButtonReleased(simplePrimary);
    }

    @Override
    public void onPrimaryButtonClicked(SimplePrimary<?> simplePrimary) {
        super.onPrimaryButtonClicked(simplePrimary);
    }

    private void setFireRatio(float fireRatio) {
        fireRatioQuantityField.updateRatio(fireRatio);
    }

    private void setSmokeRatio(float smokeRatio) {
        smokeRatioQuantityField.updateRatio(smokeRatio);
    }

    private void setSparkRatio(float sparkRatio) {
        sparkRatioQuantityField.updateRatio(sparkRatio);
    }

    private void setIntensity(float intensity) {
        intensityQuantityField.updateRatio(intensity);
    }

    private void setPartInitSize(float partInitSize) {
        inSizeQuantityField.updateRatio(partInitSize);
    }

    private void setPartFinSize(float partFinSize) {
        finSizeQuantityField.updateRatio(partFinSize);
    }
}
