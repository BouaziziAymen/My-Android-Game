package com.evolgames.entities.contact;

public class PhysicalConnection {
  Connection_Type type;

  public PhysicalConnection(Connection_Type type, ContactWrapper cw) {
    this.type = type;
    cWrapper = cw;
  }

  public ContactWrapper getWrapper() {
    return cWrapper;
  }

  public Connection_Type getType() {
    return type;
  }

  public enum Connection_Type {
    TOUCHINGTOGETHER,
    SLIDINGTOGETHER,
    IMPACTAONB,
    IMPACTBONA
  }
  ContactWrapper cWrapper;
}
