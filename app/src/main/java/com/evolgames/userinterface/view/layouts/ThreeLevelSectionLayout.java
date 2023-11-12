package com.evolgames.userinterface.view.layouts;

import com.evolgames.userinterface.sections.OneLevelSection;
import com.evolgames.userinterface.sections.ThreeLevelSection;
import com.evolgames.userinterface.sections.TwoLevelSection;
import com.evolgames.userinterface.sections.ZeroLevelSection;
import com.evolgames.userinterface.view.basics.Element;
import java.util.ArrayList;

public class ThreeLevelSectionLayout<
        Primary extends Element,
        Secondary extends Element,
        Tertiary extends Element,
        Quaternary extends Element>
    extends SectionLayout<ThreeLevelSection<Primary, Secondary, Tertiary, Quaternary>, Primary> {
  private final float[] margins = new float[] {8, 24, 40, 56};

  public ThreeLevelSectionLayout(float pX, float pY, Direction direction) {
    super(pX, pY, direction);
  }

  public ThreeLevelSection<Primary, Secondary, Tertiary, Quaternary> getSectionByKey(
      int sectionKey) {
    for (ThreeLevelSection<Primary, Secondary, Tertiary, Quaternary> section : sectionsList)
      if (sectionKey == section.getSectionKey()) return section;
    return null;
  }

  public Primary getPrimaryByIndex(int index) {
    return sectionsList.get(index).getPrimary();
  }

  public Secondary getSecondaryByIndex(int primaryKey, int index) {
    return getSectionByKey(primaryKey).getSectionByIndex(index).getPrimary();
  }

  public Tertiary getTertiaryByIndex(int primaryKey, int secondaryKey, int index) {
    return getSectionByKey(primaryKey)
        .getSectionByKey(secondaryKey)
        .getChildren()
        .get(index)
        .getPrimary();
  }

  public Quaternary getQuaternaryByIndex(
      int primaryKey, int secondaryKey, int tertiaryKey, int index) {
    return getSectionByKey(primaryKey)
        .getSectionByKey(secondaryKey)
        .getSectionByKey(tertiaryKey)
        .getChildren()
        .get(index)
        .getPrimary();
  }

  public int getPrimariesSize() {
    return sectionsList.size();
  }

  public int getSecondariesSize(int primaryKey) {
    return getSectionByKey(primaryKey).getChildren().size();
  }

  public int getTertiariesSize(int primaryKey, int secondaryKey) {
    return getSectionByKey(primaryKey).getSectionByKey(secondaryKey).getChildren().size();
  }

  public int getQuaternatiesSize(int primaryKey, int secondaryKey, int tertiaryKey) {
    return getSectionByKey(primaryKey)
        .getSectionByKey(secondaryKey)
        .getSectionByKey(tertiaryKey)
        .getChildren()
        .size();
  }

  public Secondary getSecondary(int primaryKey, int secondaryKey) {
    return getSectionByKey(primaryKey).getSectionByKey(secondaryKey).getPrimary();
  }

  public Tertiary getTertiary(int primaryKey, int secondaryKey, int tertiaryKey) {
    return getSectionByKey(primaryKey)
        .getSectionByKey(secondaryKey)
        .getSectionByKey(tertiaryKey)
        .getPrimary();
  }

  public Quaternary getQuaternary(
      int primaryKey, int secondaryKey, int tertiaryKey, int quaternaryKey) {
    return getSectionByKey(primaryKey)
        .getSectionByKey(secondaryKey)
        .getSectionByKey(tertiaryKey)
        .getSectionByKey(quaternaryKey)
        .getPrimary();
  }

  @Override
  protected ThreeLevelSection<Primary, Secondary, Tertiary, Quaternary> createSection(
      int key, Primary p, boolean isActive) {
    return new ThreeLevelSection<>(key, p, isActive);
  }

  @Override
  protected ThreeLevelSection<Primary, Secondary, Tertiary, Quaternary> createDummySection(
      Element e) {
    return new ThreeLevelSection<>(e);
  }

  public void addSecondary(int primaryKey, int secondaryKey, Secondary s, boolean isActive) {
    ThreeLevelSection<Primary, Secondary, Tertiary, Quaternary> section =
        getSectionByKey(primaryKey);
    section.addSecondary(secondaryKey, s, isActive);
  }

  public void addSecondary(int primaryKey, int secondaryKey, Secondary s) {
    addSecondary(primaryKey, secondaryKey, s, false);
  }

  public void addTertiary(int primaryKey, int secondaryKey, int tertiaryKey, Tertiary t) {
    ThreeLevelSection<Primary, Secondary, Tertiary, Quaternary> section =
        getSectionByKey(primaryKey);
    section.addTertiary(secondaryKey, tertiaryKey, t, false);
  }

  public void addQuaternary(
      int primaryKey, int secondaryKey, int tertiaryKey, int quaternaryKey, Quaternary q) {
    ThreeLevelSection<Primary, Secondary, Tertiary, Quaternary> section =
        getSectionByKey(primaryKey);
    section.addQuaternary(secondaryKey, tertiaryKey, quaternaryKey, q);
  }

  @Override
  public void updateLayout() {
    super.updateLayout();

    for (int i = 0; i < sectionsList.size(); i++) {
      ThreeLevelSection<Primary, Secondary, Tertiary, Quaternary> section = sectionsList.get(i);
      if (section.isDummy()) {
        addToLayout(section.getDummyElement());
        continue;
      } else addToLayout(section.getPrimary());
      section.getPrimary().setLowerBottomX(margins[0]);
      if (!section.isActive()) continue;
      ArrayList<TwoLevelSection<Secondary, Tertiary, Quaternary>> secondarySections =
          section.getChildren();
      for (int j = 0; j < secondarySections.size(); j++) {
        TwoLevelSection<Secondary, Tertiary, Quaternary> secondarySection =
            secondarySections.get(j);
        addToLayout(secondarySection.getPrimary());

        secondarySection.getPrimary().setLowerBottomX(margins[1]);
        if (!secondarySection.isActive()) continue;
        ArrayList<OneLevelSection<Tertiary, Quaternary>> tertiarySections =
            secondarySection.getChildren();
        for (int k = 0; k < tertiarySections.size(); k++) {
          OneLevelSection<Tertiary, Quaternary> tertiarySection = tertiarySections.get(k);
          addToLayout(tertiarySection.getPrimary());
          tertiarySection.getPrimary().setLowerBottomX(margins[2]);
          if (!tertiarySection.isActive()) continue;
          ArrayList<ZeroLevelSection<Quaternary>> quaternarySections =
              tertiarySection.getChildren();
          for (int l = 0; l < quaternarySections.size(); l++) {
            ZeroLevelSection<Quaternary> quaternarySection = quaternarySections.get(l);
            addToLayout(quaternarySection.getPrimary());
            quaternarySection.getPrimary().setLowerBottomX(margins[3]);
          }
        }
      }
    }
  }

  public void removePrimary(int primaryKey) {
    sectionsList.remove(getSectionByKey(primaryKey));
  }

  public void removeSecondary(int primaryKey, int secondaryKey) {
    getSectionByKey(primaryKey).removeSection(secondaryKey);
  }

  public void removeTertiary(int primaryKey, int secondaryKey, int tertiaryKey) {
    getSectionByKey(primaryKey).getSectionByKey(secondaryKey).removeSection(tertiaryKey);
  }

  public void removeQuaternary(
      int primaryKey, int secondaryKey, int tertiaryKey, int quaternaryKey) {
    getSectionByKey(primaryKey)
        .getSectionByKey(secondaryKey)
        .getSectionByKey(tertiaryKey)
        .removeSection(quaternaryKey);
  }

  public ArrayList<Primary> getPrimaries() {
    ArrayList<Primary> primaries = new ArrayList<>();
    for (ThreeLevelSection<Primary, Secondary, Tertiary, Quaternary> section : sectionsList) {
      primaries.add(section.getPrimary());
    }
    return primaries;
  }
}
