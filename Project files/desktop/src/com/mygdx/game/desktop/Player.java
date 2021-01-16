package com.mygdx.game.desktop;;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


public class Player extends Entity{
    //speed and sound maximum values
    public static final float SPEED = 50f;
    public static float JUMP = 1000f;

    //booleans to check if player is grounded or turned around
    private boolean grounded = false;
    public boolean turned = false;

    //keep track of player's position in level and player's velocity
    private Vector2 pos;
    private Vector2 vel;

    public PickUp pickUp;
    boolean isWalking = false;

    public Sound steps; //footstep sounds
    public Sound jump; //jump sound
    public Sound crate; //hit box sound

    private static final int FRAME_COLS = 4, FRAME_ROWS = 4; //dimension of texture region (animation is 16 frames long = 4x4)

    //animation variables for normal and inverted gravity
    Animation<TextureRegion> walkAnimation;
    Texture walkSheet;

    Animation<TextureRegion> idleAnimation;
    Texture idleSheet;

    Animation<TextureRegion> walkAnimationInv;
    Texture walkSheetInv;

    Animation<TextureRegion> idleAnimationInv;
    Texture idleSheetInv;




    public Player(Level level, World world, float posX, float posY){
        this.level = level;

        this.world=world;

        //load sounds
        steps = Gdx.audio.newSound(Gdx.files.internal("sounds/step.wav"));
        jump = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.wav"));
        crate = Gdx.audio.newSound(Gdx.files.internal("sounds/crate.wav"));

        //create box2d player Dynamic body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0,0);
        bodyDef.fixedRotation=true;

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(6,15/2);
        fixtureDef.shape = shape;
        fixtureDef.density = 10;
        fixtureDef.friction = 3;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setTransform(posX ,posY ,0);

        shape.dispose();

        body.setUserData(this);

        /**Initializing animations*/

        walkSheet = new Texture(Gdx.files.internal("entity\\john.png")); //load texture
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);//split iceTool picture into 16 distinct, singular images
        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        //for loops to assign each index of the texture region to a frame of animation
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        //create animation with the texture region
        walkAnimation = new Animation<TextureRegion>(0.15f, walkFrames);

        //repeat three other times for other animations
        //I tried making a method to speed things up and avoid copying the same code four times, but it broke everything

        idleSheet = new Texture(Gdx.files.internal("entity\\john_idle.png"));
        TextureRegion[][] tmp2 = TextureRegion.split(idleSheet,
                idleSheet.getWidth() / FRAME_COLS,
                idleSheet.getHeight() / FRAME_ROWS);
        TextureRegion[] idleFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index2 = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                idleFrames[index2++] = tmp2[i][j];
            }
        }
        idleAnimation = new Animation<TextureRegion>(0.15f, idleFrames);


        walkSheetInv = new Texture(Gdx.files.internal("entity\\john_inv.png"));
        TextureRegion[][] tmp3 = TextureRegion.split(walkSheetInv,
                walkSheetInv.getWidth() / FRAME_COLS,
                walkSheetInv.getHeight() / FRAME_ROWS);
        TextureRegion[] walkFramesInv = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFramesInv[index++] = tmp3[i][j];
            }
        }
        walkAnimationInv = new Animation<TextureRegion>(0.15f, walkFramesInv);


        idleSheetInv = new Texture(Gdx.files.internal("entity\\john_idle_inv.png"));
        TextureRegion[][] tmp4 = TextureRegion.split(idleSheetInv,
                idleSheetInv.getWidth() / FRAME_COLS,
                idleSheetInv.getHeight() / FRAME_ROWS);
        TextureRegion[] idleFramesInv = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                idleFramesInv[index++] = tmp4[i][j];
            }
        }
        idleAnimationInv = new Animation<TextureRegion>(0.15f, idleFramesInv);
    }

    @Override
    public void update() {
        pos = body.getPosition(); //player's position
        vel = body.getLinearVelocity(); //player's velocity

        //move left if velocity
        if(Gdx.input.isKeyPressed(Input.Keys.A) && vel.x > -SPEED){ //check if velocity is below maximum velocity
            turned = true; //set turned boolean to true
            body.setLinearVelocity(vel.x - 50f,vel.y); //remove 50f to the velocity in x
        }

        //move right
        if(Gdx.input.isKeyPressed(Input.Keys.D) && vel.x < SPEED){
            turned = false;
            body.setLinearVelocity(vel.x + 50f,vel.y);

        }

        //jump up
        if(Gdx.input.isKeyPressed(Input.Keys.W) && vel.y < JUMP  && grounded){ //only jump is player is grounded and y-velocity is below the maximum
            body.setLinearVelocity(vel.x,vel.y + 250f); //add 250f to y-velocity
            System.out.println("grounded: " + grounded);
        }

        //jump when gravity is inverted
        if(Gdx.input.isKeyPressed(Input.Keys.S) && vel.y > -JUMP && grounded){
            body.setLinearVelocity(vel.x,vel.y - 250f);
        }

        //play jump sounds
        if(Gdx.input.isKeyJustPressed(Input.Keys.W) && vel.y < JUMP  && grounded && !reversed)
            jump.play();

        if(Gdx.input.isKeyJustPressed(Input.Keys.S) && vel.y > -JUMP && grounded && reversed)
            jump.play();


        //invert the gravity
        if(Gdx.input.isKeyJustPressed(Input.Keys.F) && grounded){
            invert();
        }

        /** Friction control */

        //check if tile below the player. If the tile is an IcyPlatform (id:1), change the linear dampening on player, which acts as friction
        if(((level.gameMap.getTileTypeByLocation(1, pos.x, pos.y -16f) == TileType.getTileTypeById(2) && !reversed)
        || (level.gameMap.getTileTypeByLocation(1, pos.x, pos.y +16f) == TileType.getTileTypeById(2) && reversed)) && grounded){
            body.setLinearDamping(0f);
        }

        else if(((level.gameMap.getTileTypeByLocation(1, pos.x, pos.y -16f) == TileType.getTileTypeById(1) && !reversed)
                || (level.gameMap.getTileTypeByLocation(1, pos.x, pos.y +16f) == TileType.getTileTypeById(1) && reversed)) && grounded){
            body.setLinearDamping(60f);
        }
        else if(!grounded)
            body.setLinearDamping(0f);

        /** Animation*/

        //if player is between these velocities draw walking animation
        if(vel.x > 10f || vel.x < -10f){
            step(); //play step sounds
            isWalking = true;
            //draw inverted animation if gravity is inverted
            if(reversed) {
                TextureRegion currentFrame = walkAnimationInv.getKeyFrame(level.stateTime, true);
                level.gameMap.tiledMapRenderer.getBatch().draw(currentFrame, pos.x - 6, pos.y + 15 / 2, currentFrame.getRegionWidth(), currentFrame.getRegionHeight() *-1);
            }
            else{
                TextureRegion currentFrame = walkAnimation.getKeyFrame(level.stateTime, true);
                level.gameMap.tiledMapRenderer.getBatch().draw(currentFrame,pos.x -6, pos.y -15/2);
            }



        }
        else{ //idle animation
            isWalking = false;
            steps.stop();
            if(reversed) {
                TextureRegion currentFrame = idleAnimationInv.getKeyFrame(level.stateTime, true);
                level.gameMap.tiledMapRenderer.getBatch().draw(currentFrame, pos.x - 6, pos.y + 15 / 2, currentFrame.getRegionWidth(), currentFrame.getRegionHeight() *-1);
            }
            else{
                TextureRegion currentFrame = idleAnimation.getKeyFrame(level.stateTime, true);
                level.gameMap.tiledMapRenderer.getBatch().draw(currentFrame,pos.x -6, pos.y -15/2);
            }
        }

    }

    @Override
    public void invert(){
        level.gravity*=-1; //invert the level.gravity value for the iceTool
        level.worldGravity*=-1; //invert the gravity in the level's world object
        level.world.setGravity(new Vector2(0, level.worldGravity)); //set the new gravity
        //set the player's reversed boolean
        if(reversed)
            reversed = false;
        else
            reversed = true;
    }

    public void setGrounded(boolean grounded){
        this.grounded = grounded;
    }

    public float getPosX(){
        return pos.x;
    }

    public float getPosY(){
        return pos.y;
    }

    public void setPos(float x, float y) {
        body.setTransform(x, y, 0);
    }

    //step sound method
    public void step(){
        if(grounded && !isWalking){
            steps.play();
            steps.loop();
            steps.setPitch(steps.play(), (float) (Math.random() * 1.6));
        }
    }

    public void setPickUp(PickUp pickUp){
        this.pickUp = pickUp;
    }
}
