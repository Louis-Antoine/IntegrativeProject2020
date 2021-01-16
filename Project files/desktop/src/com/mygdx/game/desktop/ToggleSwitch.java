package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;


public class ToggleSwitch extends Entity{
    public boolean inRange = false; // if switch is in range of the player
    public boolean toggled = false; //it switch is toggled

    //textures
    Texture open;
    Texture closed;

    Vector2 pos; //switch postition

    Sound sound; //toggle sound

    public ToggleSwitch(Level level, World world, float posX, float posY, boolean reversed){
        this.world = world;
        this.reversed = reversed;
        this.level = level;

        sound = Gdx.audio.newSound(Gdx.files.internal("sounds\\switch.wav")); //load sound

        //create box2d body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(0,0);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(4);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setTransform(posX, posY, 0);

        body.setUserData(this);

        shape.dispose();

        //load textures
        open = new Texture("objects\\switch_on.png");
        closed = new Texture("objects\\switch_off.png");
    }

    public void update() {
        body.setAwake(true);
        pos = body.getPosition();

        //draw proper texture is switch is toggled or not
        if(toggled)
            level.gameMap.tiledMapRenderer.getBatch().draw(open, pos.x-4f, pos.y-4f);
        else
            level.gameMap.tiledMapRenderer.getBatch().draw(closed, pos.x-4f, pos.y-4f);

        //activate swtitch, play toggle sound and unlock level door if player is in range and presses Q
        if(inRange){
            if(toggled && Gdx.input.isKeyJustPressed(Input.Keys.Q)){
                toggled = false;
                level.door.unlocked = false;
                sound.play();
            }
            else if(Gdx.input.isKeyJustPressed(Input.Keys.Q)){
                toggled = true;
                level.door.unlocked = true;
                sound.play();
            }
        }
    }

    @Override
    public void invert() {
        if(reversed)
            reversed = false;
        else
            reversed = true;
    }
    public void setPos(float x, float y) {
        body.setTransform(x+6, y+3, 0);
    }


}
