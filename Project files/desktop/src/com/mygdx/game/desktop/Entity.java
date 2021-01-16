package com.mygdx.game.desktop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Entity {

    //protected EntityType type;
    public Body body;
    public World world;
    public Level level;
    public boolean reversed = false;

    public Entity() {

    }

    public abstract void update();

    public abstract void invert();
}
