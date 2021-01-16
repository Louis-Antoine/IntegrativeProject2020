package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class HoldButton extends Entity{
    public boolean pressed = false; //boolean that defines if button is pressed

    //Textures
    Texture open;
    Texture closed;

    Vector2 pos; //button position
    Sound sound; //sound on activation

    public HoldButton(Level level, World world, float posX, float posY, boolean reversed){
        this.world = world;
        this.reversed = reversed;
        this.level = level;

        //load sound
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds\\button.wav"));

        //create box2d body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0,0);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(6,1);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setTransform(posX ,posY ,0);

        body.setUserData(this);

        shape.dispose();

        //load textures
        open = new Texture("objects\\pressure_plate_on.png");
        closed = new Texture("objects\\pressure_plate_off.png");

    }

    @Override
    public void update(){
        body.setAwake(true);
        pos = body.getPosition();

        if(pressed)
            level.gameMap.tiledMapRenderer.getBatch().draw(open, pos.x - 8f, pos.y -2f); //draw open texture
        else
            level.gameMap.tiledMapRenderer.getBatch().draw(closed, pos.x - 8f, pos.y-2f); //draw closed texture
    }

    @Override
    public void invert() {
        if(reversed)
            reversed = false;
        else
            reversed = true;
    }

    public void setPos(float x, float y) {
        body.setTransform(x, y, 0);
    }

}
