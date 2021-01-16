package com.mygdx.game.desktop;;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class MapObj extends Entity {

    public MapObj(World world, float posX, float posY, float sizeX, float sizeY){
        this.world=world;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0,0);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sizeX,sizeY);
        fixtureDef.shape = shape;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setTransform(posX ,posY ,0);

        body.setUserData(world);

        shape.dispose();
    }

    @Override
    public void update() {
    }

    @Override
    public void invert() {

    }
}
