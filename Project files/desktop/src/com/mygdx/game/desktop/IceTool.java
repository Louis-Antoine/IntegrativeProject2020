package com.mygdx.game.desktop;;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class IceTool extends Entity implements PickUp{
    public boolean inRange; //if player is in range of the iceTool
    boolean picked; //if the icetool was picked by the player

    Body sensor;
    Player player;
    Vector2 pos;

    //two particle effects for flipping the ice particles along the Y-axis
    ParticleEffect iceRay;
    ParticleEffect iceRayInv;

    Sound sound; //ice sound

    //animation variables
    private static final int FRAME_COLS = 2, FRAME_ROWS = 2; //size of texture region
    Animation<TextureRegion> toolAnimation;
    Texture toolSheet;

    public IceTool(Level level, World world, float posX, float posY, Player player){
        this.level = level;
        this.world = world;
        this.player = player;

        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/ice_tool.wav"));


        //create box2d body to interact with Player object
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(0,0);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setTransform(posX, posY, 0);

        body.setUserData(this);

        FixtureDef sensorFixtureDef = new FixtureDef();
        PolygonShape shapeSensor = new PolygonShape();
        shapeSensor.setAsBox(15/2,5/2);
        sensorFixtureDef.shape = shapeSensor;
        sensorFixtureDef.isSensor = true;

        sensor = world.createBody(bodyDef);
        sensor.createFixture(sensorFixtureDef);
        sensor.setTransform(posX, posY, 0);

        //initialize animation
        toolSheet = new Texture(Gdx.files.internal("objects\\ice_tool.png")); //load texture
        TextureRegion[][] tmp = TextureRegion.split(toolSheet,
                toolSheet.getWidth() / FRAME_COLS,
                toolSheet.getHeight() / FRAME_ROWS); //split iceTool picture into four distinct, singular images
        TextureRegion[] toolFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        //for loops to assign each index of the texture region to a frame of animation
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                toolFrames[index++] = tmp[i][j];
            }
        }
        //create animation with the texture region
        toolAnimation = new Animation<TextureRegion>(0.25f, toolFrames);

        //create, load and start particle effects
        iceRay = new ParticleEffect();
        iceRay.load(Gdx.files.internal("particles\\ice_gun_test1"), Gdx.files.internal("particles\\"));
        iceRay.start();

        iceRayInv = new ParticleEffect();
        iceRayInv.load(Gdx.files.internal("particles\\ice_gun_inv"), Gdx.files.internal("particles\\"));
        iceRayInv.start();

        shape.dispose();
    }

    @Override
    public void update() {
        //start object
        body.setAwake(true);

        pos = body.getPosition(); //variable to keep track of tool's position

        //describes the frame the animation should play based on the level's time
        TextureRegion currentFrame = toolAnimation.getKeyFrame(player.level.stateTime, true);

        //check if player is in range the tool and changes the picked boolean value is player presses E to pick it up
        if(inRange){
            if(picked && Gdx.input.isKeyJustPressed(Input.Keys.E)){
                picked = false;
            }
            else if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
                picked = true;
            }
        }

        //if/else statements to draw & render animation and body flipped on the right side depending on player's gravity and orientation

        if(picked && player.turned == false && player.reversed == false){
            body.setTransform(player.getPosX(), player.getPosY(),0);
            sensor.setTransform(player.getPosX() , player.getPosY() , 0);
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                use();
            }
            player.level.gameMap.tiledMapRenderer.getBatch().draw(currentFrame,pos.x -15/2f, pos.y -5/2f);

        }

        else if(picked && player.turned && player.reversed == false){
            body.setTransform(player.getPosX(), player.getPosY(),0);
            sensor.setTransform(player.getPosX() , player.getPosY(), 0);
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                use();
            }
            player.level.gameMap.tiledMapRenderer.getBatch().draw(currentFrame,pos.x + 15/2f, pos.y -5/2f, currentFrame.getRegionWidth() * -1, currentFrame.getRegionHeight());
        }

        else if(picked && player.turned == false && player.reversed){
            body.setTransform(player.getPosX(), player.getPosY(),0);
            sensor.setTransform(player.getPosX(), player.getPosY(), 0);
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                use();
            }
            player.level.gameMap.tiledMapRenderer.getBatch().draw(currentFrame,pos.x -15/2f, pos.y +5/2f, currentFrame.getRegionWidth() , currentFrame.getRegionHeight()* -1);
        }

        else if(picked && player.turned && player.reversed){
            body.setTransform(player.getPosX(), player.getPosY(),0);
            sensor.setTransform(player.getPosX(), player.getPosY(), 0);
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                use();
            }
            player.level.gameMap.tiledMapRenderer.getBatch().draw(currentFrame,pos.x + 15/2f, pos.y +5/2f, currentFrame.getRegionWidth() * -1, currentFrame.getRegionHeight()* -1);
        }

        else{
            player.level.gameMap.tiledMapRenderer.getBatch().draw(currentFrame,pos.x -15/2f, pos.y -5/2f);
        }

        /** Use IceTool */
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && picked){ //check if user presses the spacebar to use tool if tool is picked up


            //if/else statement to draw correct Particle effect based on player orientation
            if(!player.turned) {
                iceRay.update(Gdx.graphics.getDeltaTime());
                iceRay.setPosition(pos.x + 7f, pos.y + 0.5f);
                iceRay.draw(level.gameMap.tiledMapRenderer.getBatch(), level.deltaTime);
            }
            else{
                iceRayInv.update(Gdx.graphics.getDeltaTime());
                iceRayInv.setPosition(pos.x - 7f, pos.y + 0.5f);
                iceRayInv.draw(level.gameMap.tiledMapRenderer.getBatch(), level.deltaTime);
            }


            //restart particle effect it it's complete
            if(iceRay.isComplete())
                iceRay.reset();

            if(iceRayInv.isComplete())
                iceRayInv.reset();

            //loops to analyze tiles around Icetool's position
            for(int y =level.gravity; y<= 3 && y>=-3; y+=level.gravity){
                //the level.gravity is an int either equal to -1 when gravity is normal or 1 when gravity is inverted
                //the first loop cycles through the 3 first layers of tiles below or above the Icetool (checks below when gravity is normal, above when inverted)
                for(int x =0; x<=3 && x>=-3; x+=((!player.turned)? 1 : -1)){
                    //this loop checks four tiles from the tool
                    //it checks to the right if the player is not turned and to the left if it is turned

                    //exact cell locations the loop is currently analyzing.
                    //The game runs in pixel and a tile is 16x16 pixels, so dividing by 16 is necessary to get location based on the tile map which is 100x100 tiles big
                    int cellX = (int) (pos.x /16f +x);
                    int cellY = (int)(pos.y /16f +y);

                    if(level.gameMap.getTileTypeByLocation(1, pos.x + 16*x, pos.y +16*y) == TileType.PLATFORM) { //check if the tile currently analyzed is a regular platform
                        level.layer.setCell(cellX, cellY, level.icyPlatform); //change the tile to an icy platform

                    }

                }
            }
        }

        //play and loop the icetool sound when space bar is pressed
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            sound.play();
            sound.setVolume(sound.play(), 0.35f);
            sound.loop();
        }

        //stop the icetool sound when space bar is not pressed
        if(!Gdx.input.isKeyPressed(Input.Keys.SPACE))
            sound.stop();



    }

    //setposition method
    public void setPos(float x, float y) {
        body.setTransform(x, y, 0);
        sensor.setTransform(x, y, 0);
    }

    @Override
    public void invert() {

    }

    @Override
    public void interact() {
        picked = true;
    }

    @Override
    public void use() {
        System.out.println("haha icetool goes brrrrrrrrrrrr");
    }
}
