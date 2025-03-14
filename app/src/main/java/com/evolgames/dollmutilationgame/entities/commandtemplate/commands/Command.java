package com.evolgames.dollmutilationgame.entities.commandtemplate.commands;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.dollmutilationgame.entities.commandtemplate.Invoker;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Command {

    private boolean aborted;

    public boolean isAborted() {
        return aborted;
    }

    public void setAborted(boolean aborted) {
        this.aborted = aborted;
    }

    public void execute() {
        if (!Invoker.scene.getPhysicsWorld().isLocked() && this.isReady() && !isAborted()) {
            this.run();
            this.setAborted(true);
        }
    }

    protected abstract void run();

    protected boolean isReady() {
        return true;
    }

    protected static boolean isBodyAlive(Body body) {
        if (body == null) {
            return false;
        }
        AtomicBoolean result = new AtomicBoolean(false);
        Invoker.scene
                .getPhysicsWorld()
                .getBodies()
                .forEachRemaining(
                        body1 -> {
                            if (body1 == body) {
                                result.set(true);
                            }
                        });
        return result.get();
    }
}
