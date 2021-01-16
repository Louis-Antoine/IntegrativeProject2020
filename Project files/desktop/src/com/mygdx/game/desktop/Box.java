package com.mygdx.game.desktop;;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Box extends Entity {
    Vector2 pos;
    Sprite sprite; //this entity exclusively uses a sprite instead of a texture to ease sprite rotation

    public Box(Level level, World world, float posX, float posY){
        this.world=world;
        this.level = level;

        //load sprite
        sprite = new Sprite(new Texture("objects\\box.png"));

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0,0);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(7,7);
        fixtureDef.shape = shape;
        fixtureDef.density = 2;
        fixtureDef.friction = 3;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setTransform(posX ,posY ,0);

        body.setUserData(world);

        shape.dispose();

    }

    @Override
    public void update() {
        body.setAwake(true);

        pos = body.getPosition(); //store box postition

        //draw box sprite based on box position and rotation
        sprite.draw(level.gameMap.tiledMapRenderer.getBatch());
        sprite.setCenter(pos.x, pos.y);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));


    }

    @Override
    public void invert() {

    }
}
