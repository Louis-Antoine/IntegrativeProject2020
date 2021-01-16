package com.mygdx.game.desktop;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Door extends Entity{
    //booleans to define if level is finished and it the door is unlocked
    public boolean levelFinished = false;
    public boolean unlocked = false;

    //textures
    Texture open;
    Texture closed;
    //position
    Vector2 pos;


    public Door(Level level, World world, float posX, float posY){
        this.level = level;
        this.world = world;

        //create body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0,0);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(6,8);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setTransform(posX ,posY ,0);

        body.setUserData(this);

        shape.dispose();

        //load textures
        open = new Texture("objects\\door_open.png");
        closed = new Texture("objects\\door_closed.png");
    }

    public void update(){
        body.setAwake(true);
        pos = body.getPosition();

        //draw right texture if door is unlocked or not
        if(unlocked)
            level.gameMap.tiledMapRenderer.getBatch().draw(open, pos.x-6f, pos.y-8f);
        else
            level.gameMap.tiledMapRenderer.getBatch().draw(closed, pos.x-6f, pos.y-8f);
    }

    @Override
    public void invert() {
        if(reversed)
            reversed = false;
        else
            reversed = true;
    }

    //set position method
    public void setPos(float x, float y) {
        body.setTransform(x+6f, y+8f, 0);
    }
}
