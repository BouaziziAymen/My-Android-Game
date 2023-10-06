package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.evolgames.entities.GameEntity;
import com.evolgames.physics.WorldFacade;
import com.evolgames.physics.entities.Data;

import java.util.List;

public  interface Penetrating {
float getAvailableEnergy(float collisionEnergy);
void onEnergyConsumed(WorldFacade worldFacade, Contact contact, Vector2 point, Vector2 normal, float actualAdvance, GameEntity penetrator, GameEntity penetrated, List<Data> envData, List<Data> penData, float consumedEnergy);
void onFree(WorldFacade worldFacade, Contact contact, Vector2 point, Vector2 normal, float actualAdvance, GameEntity penetrator, GameEntity penetrated, List<Data> envData, List<Data> penData, float consumedEnergy, float collisionEnergy);
void onCancel();
}
