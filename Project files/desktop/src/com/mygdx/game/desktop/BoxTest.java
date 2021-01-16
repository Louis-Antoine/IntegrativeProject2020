package com.mygdx.game.desktop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class BoxTest extends Body {
    Texture texture;

    protected BoxTest(World world, long addr) {
        super(world, addr);
    }
}
