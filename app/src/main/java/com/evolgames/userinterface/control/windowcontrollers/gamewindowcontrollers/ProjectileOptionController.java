package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.activity.GameSound;
import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.persistence.PersistenceException;
import com.evolgames.entities.properties.Explosive;
import com.evolgames.entities.properties.ProjectileProperties;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.control.validators.AlphaNumericValidator;
import com.evolgames.userinterface.model.ItemCategory;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.toolmodels.CasingModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import com.evolgames.userinterface.view.inputs.Keyboard;
import com.evolgames.userinterface.view.inputs.Quantity;
import com.evolgames.userinterface.view.inputs.TextField;
import com.evolgames.userinterface.view.sections.basic.SimplePrimary;
import com.evolgames.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.view.sections.basic.SimpleTertiary;
import com.evolgames.userinterface.view.windows.windowfields.FieldWithError;
import com.evolgames.userinterface.view.windows.windowfields.SecondarySectionField;
import com.evolgames.userinterface.view.windows.windowfields.SectionField;
import com.evolgames.userinterface.view.windows.windowfields.TitledButton;
import com.evolgames.userinterface.view.windows.windowfields.TitledQuantity;
import com.evolgames.userinterface.view.windows.windowfields.TitledTextField;
import com.evolgames.userinterface.view.windows.windowfields.projectieoptionwindow.SoundField;
import com.evolgames.utilities.ToolUtils;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.ParserConfigurationException;

public class ProjectileOptionController extends SettingsWindowController<ProjectileProperties> {
    private final EditorScene editorScene;
    private final AlphaNumericValidator projectileNameValidator = new AlphaNumericValidator(8, 5);
    private final Hashtable<String, ToolModel> missileTable;
    private final Hashtable<String, ButtonWithText<ProjectileOptionController>> missileButtonsTable;
    private TextField<ProjectileOptionController> projectileNameField;
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
                        0, "Projectile:", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(bodyASection);

        List<String> files = new ArrayList<>();
        files.addAll(ToolUtils.getToolNamesByCategory(ResourceManager.getInstance().activity, ItemCategory.PROJECTILE.name.toLowerCase(Locale.ROOT)));
        files.addAll(ToolUtils.getToolNamesByCategory(ResourceManager.getInstance().activity, ItemCategory.ROCKET.name.toLowerCase(Locale.ROOT)));
        AtomicInteger missileCounter = new AtomicInteger();
        files.forEach(f -> createProjectileToolButton(f, missileCounter.getAndIncrement()));
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
                        1, "Shell:", ResourceManager.getInstance().mainButtonTextureRegion, this);
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

    private void createProjectileToolButton(String fileName, int missileId) {
        ButtonWithText<ProjectileOptionController> missileButton =
                new ButtonWithText<>(
                        fileName.split("\\$")[1],
                        2,
                        ResourceManager.getInstance().simpleButtonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        if (fileName.equals(projectileModel.getMissileFile())) {
            missileButton.updateState(Button.State.PRESSED);
        } else {
            missileButton.updateState(Button.State.NORMAL);
        }
        SimpleSecondary<ButtonWithText<ProjectileOptionController>> missileField =
                new SimpleSecondary<>(0, missileId, missileButton);
        window.addSecondary(missileField);
        missileButtonsTable.put(fileName, missileButton);
        missileButton.setBehavior(
                new ButtonBehavior<ProjectileOptionController>(this, missileButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        ToolModel missileModel;
                        if (!missileTable.containsKey(fileName)) {
                            try {
                                missileModel = ToolUtils.getProjectileModel(fileName);
                                missileTable.put(fileName, missileModel);
                                projectileModel.setMissileFile(fileName);
                            } catch (PersistenceException
                                     | SAXException
                                     | ParserConfigurationException
                                     | IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            missileModel = missileTable.get(fileName);
                            projectileModel.setMissileFile(fileName);
                        }

                        onProjectileBodyButtonClicked(missileField);
                    }

                    @Override
                    public void informControllerButtonReleased() {
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
        setBody(projectileModel.getBodyId());
        setFireSound(this.projectileProperties.getFireSound());
        setMuzzleVelocity(this.projectileProperties.getMuzzleVelocity());
        setRecoil(this.projectileProperties.getRecoil());
        setProjectileName(projectileModel.getModelName());
        if (projectileModel.getMissileFile() != null) {
            setMissileName(projectileModel.getMissileFile());
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
    public void onTertiaryButtonClicked(SimpleTertiary<?> simpleTertiary) {
        super.onTertiaryButtonClicked(simpleTertiary);
        int primaryKey = simpleTertiary.getPrimaryKey();
        int secondaryKey = simpleTertiary.getSecondaryKey();
        if (simpleTertiary.getPrimaryKey() == 2 && simpleTertiary.getSecondaryKey() == 1) {
            ResourceManager.getInstance()
                    .gunshotSounds
                    .get(simpleTertiary.getTertiaryKey())
                    .getSoundList()
                    .get(0)
                    .play();
            projectileProperties.setFireSound(simpleTertiary.getTertiaryKey());
            for (int i = 0; i < window.getLayout().getTertiariesSize(primaryKey, secondaryKey); i++) {
                SimpleTertiary<?> element =
                        window.getLayout().getTertiaryByIndex(primaryKey, secondaryKey, i);
                if (element.getTertiaryKey() != simpleTertiary.getTertiaryKey()) {
                    Element main = element.getMain();
                    if (main instanceof ButtonWithText) {
                        ((ButtonWithText<?>) main).updateState(Button.State.NORMAL);
                    }
                }
            }
        }
    }

    @Override
    public void init() {
        super.init();
        SectionField<ProjectileOptionController> sectionField =
                new SectionField<>(
                        2, "General Settings", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(sectionField);
        TitledTextField<ProjectileOptionController> layerNameField =
                new TitledTextField<>("Projectile Name:", 10);
        projectileNameField = layerNameField.getAttachment();

        projectileNameField.setBehavior(
                new TextFieldBehavior<ProjectileOptionController>(
                        this,
                        projectileNameField,
                        Keyboard.KeyboardType.AlphaNumeric,
                        projectileNameValidator) {
                    @Override
                    protected void informControllerTextFieldTapped() {
                        ProjectileOptionController.super.onTextFieldTapped(projectileNameField);
                    }

                    @Override
                    protected void informControllerTextFieldReleased() {
                        ProjectileOptionController.super.onTextFieldReleased(projectileNameField);
                    }
                });
        projectileNameField
                .getBehavior()
                .setReleaseAction(() -> model.setModelName(projectileNameField.getTextString()));
        FieldWithError fieldWithError = new FieldWithError(layerNameField);
        SimpleSecondary<FieldWithError> secondaryElement1 = new SimpleSecondary<>(2, 0, fieldWithError);
        window.addSecondary(secondaryElement1);

        ArrayList<GameSound> sounds = ResourceManager.getInstance().gunshotSounds;
        SecondarySectionField<ProjectileOptionController> projectileShotSoundSection =
                new SecondarySectionField<>(
                        2, 1, "Shot Sound", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addSecondary(projectileShotSoundSection);
        for (int i = 0; i < sounds.size(); i++) {
            GameSound gameSound = sounds.get(i);
            SoundField soundField = new SoundField(gameSound.getTitle(), 2, 1, i, this);
            window.addTertiary(soundField);
        }

        SectionField<ProjectileOptionController> typeSection =
                new SectionField<>(
                        3, "Settings", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(typeSection);

        TitledQuantity<ProjectileOptionController> titleVelocityQuantity =
                new TitledQuantity<>("Velocity:", 16, "r", 5, 50);
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
                new TitledQuantity<>("Recoil:", 10, "b", 5, 50);
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
                        model.getModelName(), projectileModel.getBodyId(), projectileModel.getProjectileId());
    }

    private void setProjectileName(String name) {
        projectileNameField.getBehavior().setTextValidated(name);
    }

    private void setFireSound(int fireSound) {
    }

    @Override
    public void onCancelSettings() {
        super.onCancelSettings();
        this.itemWindowController.onResume();
    }

    private void setMissileName(String fileName) {
        ButtonWithText<ProjectileOptionController> selectedButton = missileButtonsTable.get(fileName + ".Xml");
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
                        4, "Explosive", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(explosiveSection);
        Arrays.stream(Explosive.values()).forEach(this::createCategoryCheckBox);
    }

    private void createCategoryCheckBox(Explosive explosive) {
        TitledButton<ProjectileOptionController> explosiveCheckBox =
                new TitledButton<>(
                        explosive.getName(),
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

    private void createExplosiveSettings() {
        for (SimplePrimary<?> p : window.getLayout().getPrimaries()) {
            if (p.getPrimaryKey() >= 5) {
                window.getLayout().removePrimary(p.getPrimaryKey());
            }
        }

        SectionField<ProjectileOptionController> explosiveSettingsSection =
                new SectionField<>(
                        5, "Explosive Settings", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(explosiveSettingsSection);

        TitledQuantity<ProjectileOptionController> titledFireRatioQuantity =
                new TitledQuantity<>("Fire:", 10, "r", 5, 70);

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
                new TitledQuantity<>("Smoke:", 10, "t", 5, 70);
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
                new TitledQuantity<>("Sparks:", 10, "g", 5, 70);
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
                new TitledQuantity<>("Part. Density:", 10, "b", 5, 70);
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


        // -----
        TitledQuantity<ProjectileOptionController> titledInitialSizeQuantity =
                new TitledQuantity<>("In. Part. Size:", 10, "b", 5, 70);
        inSizeQuantityField = titledInitialSizeQuantity.getAttachment();
        titledInitialSizeQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<ProjectileOptionController>(this, inSizeQuantityField) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                                ProjectileOptionController.this.projectileProperties.setInFirePartSize(
                                        quantity.getRatio());
                            }
                        });

        FieldWithError inSizeFieldWithError = new FieldWithError(titledInitialSizeQuantity);
        SimpleSecondary<FieldWithError> inSizeElement =
                new SimpleSecondary<>(5, 4, inSizeFieldWithError);
        window.addSecondary(inSizeElement);

        // -----
        TitledQuantity<ProjectileOptionController> titledFinalSizeQuantity =
                new TitledQuantity<>("Fin. Part. Size:", 10, "b", 5, 70);
        finSizeQuantityField = titledFinalSizeQuantity.getAttachment();
        titledFinalSizeQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<ProjectileOptionController>(this, finSizeQuantityField) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                                ProjectileOptionController.this.projectileProperties.setFinFirePartSize(
                                        quantity.getRatio());
                            }
                        });

        FieldWithError finSizeFieldWithError = new FieldWithError(titledFinalSizeQuantity);
        SimpleSecondary<FieldWithError> finSizeElement =
                new SimpleSecondary<>(5, 5, finSizeFieldWithError);
        window.addSecondary(finSizeElement);
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
}
