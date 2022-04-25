package com.evolgames.physics.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.evolgames.entities.blocks.BlockA;

public class Touch {
    public BlockA block1;
    public BlockA block2;
    public Body body1;
    public Body body2;


    public Touch(BlockA block1, BlockA block2, Body body1, Body body2) {
        this.block1 = block1;
        this.block2 = block2;
        this.body1 = body1;
        this.body2 = body2;
    }
    public boolean isEquivalent(BlockA block1, BlockA block2) {
        return ((this.block1 == block1 && this.block2 == block2) || (this.block1 == block2 && this.block2 == block1));
    }
}
