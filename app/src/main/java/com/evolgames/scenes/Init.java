package com.evolgames.scenes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.evolgames.entities.init.AngularVelocityInit;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.init.BodyInitImpl;
import com.evolgames.entities.init.BulletInit;
import com.evolgames.entities.init.LinearVelocityInit;
import com.evolgames.entities.init.TransformInit;
import com.evolgames.physics.CollisionUtils;

public class Init {
    private final float x;
    private final float y;
    private final float angle;
    private final Vector2 linearVelocity;
    private final float angularVelocity;
    private final boolean isBullet;
    private final Body muzzleBody;
    private final Vector2 muzzleVelocity;
    private final Vector2 point;
    private final float recoil;
    private final Filter filter;

    public static class Builder {
        // Required parameters
        private float x;
        private float y;

        // Optional parameters - initialized to default values
        private float angle = 0;
        private Vector2 linearVelocity = new Vector2(0, 0);
        private float angularVelocity = 0;
        private boolean isBullet = false;
        private boolean withRecoil = false;
        private Body muzzleBody = null;
        private Vector2 muzzleVelocity = new Vector2(0, 0);
        private Vector2 point = new Vector2(0, 0);
        private float recoil = 0;
        private Filter filter = null;

        public Builder(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Builder angle(float angle) {
            this.angle = angle;
            return this;
        }

        public Builder linearVelocity(Vector2 linearVelocity) {
            this.linearVelocity = linearVelocity;
            return this;
        }

        public Builder angularVelocity(float angularVelocity) {
            this.angularVelocity = angularVelocity;
            return this;
        }

        public Builder isBullet(boolean isBullet) {
            this.isBullet = isBullet;
            return this;
        }


        public Builder muzzleVelocity(Vector2 muzzleVelocity) {
            this.muzzleVelocity = muzzleVelocity;
            return this;
        }

        public Builder point(Vector2 point) {
            this.point = point;
            return this;
        }

        public Builder recoil(Body muzzleBody,Vector2 muzzleVelocity, Vector2 point, float recoil) {
            this.recoil = recoil;
            this.muzzleBody = muzzleBody;
            this.muzzleVelocity  = muzzleVelocity;
            this.point = point;
            return this;
        }

        public Builder filter(short categoryBits, short maskBits) {
            this.filter = new Filter();
            this.filter.categoryBits = categoryBits;
            this.filter.maskBits = maskBits;
            this.filter.groupIndex = CollisionUtils.groupIndex();
            return this;
        }
        public Builder filter(short categoryBits, short maskBits, short groupIndex) {
            this.filter = new Filter();
            this.filter.categoryBits = categoryBits;
            this.filter.maskBits = maskBits;
            this.filter.groupIndex = groupIndex;
            return this;
        }

        public Init build() {
            return new Init(this);
        }
    }

    private Init(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.angle = builder.angle;
        this.linearVelocity = builder.linearVelocity;
        this.angularVelocity = builder.angularVelocity;
        this.isBullet = builder.isBullet;
        this.muzzleBody = builder.muzzleBody;
        this.muzzleVelocity = builder.muzzleVelocity;
        this.point = builder.point;
        this.recoil = builder.recoil;
        this.filter = builder.filter;
    }

    public BodyInit getBodyInit(){
            return new AngularVelocityInit(new LinearVelocityInit(new BulletInit(
                    new TransformInit(new BodyInitImpl(this.filter), x / 32f, y / 32f, angle), isBullet), linearVelocity), this.angularVelocity);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getAngle() {
        return angle;
    }

    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public boolean isBullet() {
        return isBullet;
    }

    public Body getMuzzleBody() {
        return muzzleBody;
    }

    public Vector2 getMuzzleVelocity() {
        return muzzleVelocity;
    }

    public Vector2 getPoint() {
        return point;
    }

    public float getRecoil() {
        return recoil;
    }

    public Filter getFilter() {
        return filter;
    }
}
