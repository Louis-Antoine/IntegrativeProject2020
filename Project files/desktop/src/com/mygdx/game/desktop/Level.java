package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;
import org.lwjgl.Sys;

import java.util.ArrayList;

/** Main class for the playable part of the game, somewhat of a mess */

public class Level implements Screen {

    final Platformer game;

    OrthographicCamera cam; //game camera
    ExtendViewport viewport; //related to camera
    TiledGameMap gameMap; //tiled map
    TiledMapTileLayer layer; //to interact in real-time with tiled map
    String level; //to load right level
    ArrayList<Body> groundArray = new ArrayList<>(); //to store all of the ground bodies
    World world; //box2d world object for box2d objects to interact with eachother
    Box2DDebugRenderer debugRenderer; //show hitboxes on box2d bodies
    Texture box; //dumb
    ArrayList<Body> boxArray = new ArrayList<>(); //dumb
    float accumulator = 0;

    Texture winText;

    boolean showDebug = false;
    boolean isCameraOnPlayer = true;
    boolean levelCompleted = false;
    boolean isDead = false;

    Cell platform;
    Cell icyPlatform;
    Cell sky;


    //different bodies
    Player player;
    ArrayList<Entity> entities; //array to store all entities
    IceTool iceTool;
    Door door;
    ToggleSwitch toggleSwitch;
    HoldButton holdButton;
    Boolean isToolTurned;

    //time values
    float stateTime;
    float deltaTime;

    int gravity = -1;

    //box2d variables
    static final float STEP_TIME = 1f / 60f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;
    static final float SCALE = 1f;
    static final int COUNT = 10;
    PhysicsShapeCache physicsBodies;
    int worldGravity = -250; //gravity acting upon bodies in level

    public Level(Platformer game, String level) {

        this.level = level;
        this.game = game;
        Box2D.init(); //start physics

        world = new World(new Vector2(0, worldGravity), false);

        entities = new ArrayList<>();

        //initialize all singular entities at arbitrary positions at first and repositioning them when creating the map
        player = new Player(this, world,60,150);
        entities.add(player);

        iceTool = new IceTool(this, world,40,115,player);
        isToolTurned = player.turned;
        entities.add(iceTool);

        door = new Door(this, world, 100, 100);
        entities.add(door);

        toggleSwitch = new ToggleSwitch(this, world, 100, 100, false);
        entities.add(toggleSwitch);

        holdButton = new HoldButton(this, world, 100, 100, false);
        entities.add(holdButton);


        winText = new Texture("ui\\level_done_text.png");

        //set the music in the game object
        if(!game.levelMusic.isPlaying())
            game.setMusic(game.levelMusic);

        /**Contact listener to check collision between different box2d bodies */

        world.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                /** Contact beween player and ground*/
                if(contact.getFixtureA().getBody().getUserData()== player && contact.getFixtureB().getBody().getType() == BodyDef.BodyType.StaticBody){

                        player.setGrounded(true);
                        System.out.println("contact on");
                }

                /** Sensor Contact*/
                if(contact.getFixtureA().getBody().getUserData()== player && contact.getFixtureB().isSensor()){
                    if(contact.getFixtureA().getBody().getUserData()== player && contact.getFixtureB().getBody().getUserData()== iceTool){
                        System.out.println("gottem");
                        iceTool.inRange = true;
                    }
                    if(contact.getFixtureA().getBody().getUserData()== player && contact.getFixtureB().getBody().getUserData()== toggleSwitch){
                        System.out.println("switch");
                        toggleSwitch.inRange = true;
                    }
                }
                /**Box contact*/
                if(contact.getFixtureA().getBody().getUserData()== player && contact.getFixtureB().getBody().getType() == BodyDef.BodyType.DynamicBody){

                    player.setGrounded(true);
                    player.crate.play();
                    System.out.println("contact on");

                }
                /** Door contact*/
                if(contact.getFixtureA().getBody().getUserData()== player && contact.getFixtureB().getBody().getUserData() == door && door.unlocked){
                    door.levelFinished = true;
                    System.out.println("yay");
                    win();
                }
                /**button contact*/
                if(contact.getFixtureB().getBody().getUserData()== holdButton && ( contact.getFixtureA().getBody().getType() == BodyDef.BodyType.DynamicBody)
                || (contact.getFixtureA().getBody().getUserData()== holdButton && ( contact.getFixtureB().getBody().getType() == BodyDef.BodyType.DynamicBody))){
                    door.unlocked = true;
                    holdButton.pressed = true;
                    holdButton.sound.play();
                    System.out.println("unlocked");
                }

            }

            @Override
            /** Check end of contact between bodies */
            public void endContact(Contact contact) {
                if(contact.getFixtureA().getBody().getUserData()== player && contact.getFixtureB().getBody().getType() == BodyDef.BodyType.StaticBody){

                        player.setGrounded(false);
                        System.out.println("contact off");
                }
                if(contact.getFixtureA().getBody().getUserData()== player && contact.getFixtureB().getBody().getUserData()== iceTool){
                    System.out.println("nvm");
                    iceTool.inRange = false;
                }

                if(contact.getFixtureA().getBody().getUserData()== player && contact.getFixtureB().getBody().getUserData()== toggleSwitch){
                    System.out.println("switch");
                    toggleSwitch.inRange = false;
                }

                if(contact.getFixtureA().getBody().getUserData()== player && contact.getFixtureB().getBody().getType() == BodyDef.BodyType.DynamicBody){

                    player.setGrounded(false);
                    System.out.println("contact on");

                }

                if((contact.getFixtureB().getBody().getUserData()== holdButton && contact.getFixtureA().getBody().getType() == BodyDef.BodyType.DynamicBody)
                        || (contact.getFixtureA().getBody().getUserData()== holdButton && (contact.getFixtureB().getBody().getType() == BodyDef.BodyType.DynamicBody))){
                    door.unlocked = false;
                    holdButton.pressed = false;
                    System.out.println("locked");
                }

            }

            @Override
            public void postSolve(Contact arg0, ContactImpulse arg1) {
            }

            @Override
            public void preSolve(Contact arg0, Manifold arg1) {
            }
        });

        stateTime = 0f;
    }

    //Method is called right after constructor
    @Override
    public void show() {
        //set map and camera
        gameMap = new TiledGameMap(level);
        cam = new OrthographicCamera();
        viewport = new ExtendViewport(50, 50, cam);
        gameMap.tiledMapRenderer.getBatch().setProjectionMatrix(cam.combined); //put camera on map
        layer = (TiledMapTileLayer)gameMap.tiledMap.getLayers().get(1); //set the layer on 1 to interact with the tiled map

        //each level map has these three cells at the top-left corner. Used for dynamically altering the tiled map i.e when using the ice tool
        platform = ((TiledMapTileLayer) gameMap.tiledMap.getLayers().get(1)).getCell(0, gameMap.getHeight()-1);
        icyPlatform = ((TiledMapTileLayer) gameMap.tiledMap.getLayers().get(1)).getCell(1, gameMap.getHeight()-1);
        sky = ((TiledMapTileLayer) gameMap.tiledMap.getLayers().get(1)).getCell(2, gameMap.getHeight()-1);


        debugRenderer = new Box2DDebugRenderer();
        cam.setToOrtho(false, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        gameMap.tiledMapRenderer.setView(cam);
        cam.update();
        System.out.println("show end");

        createMap(); //call create map method


    }

    //start physics
    private void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();

        accumulator += Math.min(delta, 0.25f);

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;

            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }

    @Override
    //runs every frame
    public void render(float delta) {

        deltaTime = delta;

        drawBackground();

        stepWorld();


        stateTime += Gdx.graphics.getDeltaTime();

        cam.update();
        gameMap.tiledMapRenderer.setView(cam);
        gameMap.tiledMapRenderer.render();


        gameMap.tiledMapRenderer.getBatch().begin();

        /**update entities Array*/
        if(true) {
            for (int i = 0; i < entities.size() - 1; i++) {
                entities.get(i).update();
            }
        }
        /** camera control*/

        Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        //default camera mode
        if(isCameraOnPlayer || !showDebug) {
            cam.position.set(player.getPosX(), player.getPosY(), 0);
            cam.zoom = 0.25f;
        }

        //change camera to map view if debug mode is on
        if(Gdx.input.isKeyJustPressed(Input.Keys.C) && showDebug){
            if(isCameraOnPlayer){
                cam.position.set(gameMap.getWidth()*8, gameMap.getHeight()*8, 0);
                cam.zoom = 1f;
                isCameraOnPlayer = false;
            }
            else
                isCameraOnPlayer = true;
        }

        //boundary check for death
        if(player.getPosX() < -50 || player.getPosX() > gameMap.getWidth()*16 +50 ||
                player.getPosY() < -50 || player.getPosY() > gameMap.getHeight()*16 +50){
            death();
        }

        //drag the screen based on mouse position if map camera is on
        if(Gdx.input.isTouched() && !isCameraOnPlayer){
            cam.translate(-Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
            cam.update();
        }

        /**Escape button / pause screen*/
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            //Stop any active sounds to prevent them playing in subsequent menus
            player.steps.stop();
            iceTool.sound.stop();

            escape(); //call escape method
        }

        /**Spawn box if debug is on*/
        if(Gdx.input.isKeyJustPressed(Input.Keys.P) && showDebug){
            Box tempBox = new Box(this, world, pos.x, pos.y);
            tempBox.update();
            entities.add(tempBox);
        }



        /**Forve change gravity if debug is on**/
        if(Gdx.input.isKeyJustPressed(Input.Keys.G) && showDebug){
            player.invert();
        }
        /**freeze/unfreeze singular tile if debug is on**/
        if(Gdx.input.isKeyJustPressed(Input.Keys.T) && showDebug){

            //get mouse position in terms of tiles
            int x = ((int)(pos.x/16f));
            int y = ((int)(pos.y/16f));


            if(gameMap.getTileTypeByLocation(1, pos.x, pos.y) == TileType.PLATFORM) { //check if clicked tile is a platform
                layer.setCell(x, y, icyPlatform); //change to icy platform
            }

            else if(gameMap.getTileTypeByLocation(1, pos.x, pos.y) == TileType.ICYPLATFORM){//check if clicked tile is a icyplatform
                layer.setCell(x, y, platform); //change to platform
            }
        }


        /** Draw box2d boxes if debug is on */
        if(Gdx.input.isKeyPressed(Input.Keys.B) && showDebug){
            gameMap.tiledMapRenderer.getBatch().draw(box, pos.x,pos.y, 14, 14);
            ground(pos.x, pos.y);
        }

        /**Show Debug*/
        if(Gdx.input.isKeyJustPressed(Input.Keys.F3)){
            if(showDebug)
                showDebug = false;
            else if(!showDebug)
                showDebug = true;
        }
        //take screenshot
        if(Gdx.input.isKeyJustPressed(Input.Keys.F5)){
            byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);

        // This loop makes sure the whole screenshot is opaque and looks exactly like what the user is seeing
            for (int i = 4; i < pixels.length; i += 4) {
                pixels[i - 1] = (byte) 255;
            }

            Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
            BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
            PixmapIO.writePNG(Gdx.files.external("mypixmap.png"), pixmap);
            pixmap.dispose();
        }

        //show hitboxes if debig is on
        if(showDebug)
            debugRenderer.render(world, cam.combined);


        gameMap.tiledMapRenderer.getBatch().end();
    }



    /** Create bodies based on Tiled map */
    public void createMap(){

        //variable to store beginning and end of ground bodies and if the method is currently "drawing" bodies on the map
        float startX = 0;
        float endX = 0;
        boolean isDrawing = false;

        //for loops to analyze each and every single tile on the map
        for(float i =0; i < gameMap.getHeight() *16 ; i+=16){ //vertical layer
            for(float e=0; e<= gameMap.getWidth() *16; e+=16){ //horizontal
                Vector3 currentTile = cam.unproject(new Vector3(e, i, 0)); //current tile being analyzed based on loop values

                TileType tile = gameMap.getTileTypeByLocation(1, e, i); //find tile type based on location of i and e

                if(tile!=null && tile.isCollidable()){ //if tile is collidable store start of tile
                    if(!isDrawing){
                        startX = e;
                        isDrawing = true;
                        //System.out.println("Starting to draw at: " + x + " " + y);
                    }
                    if(isDrawing){ //if tile is adjacent to another collidable tile, store end point
                        endX = Math.round(e+16);
                    }
                }

                if(tile == null && isDrawing){ //if tile adjacent to collidable tile create box2d static body for ground
                    isDrawing = false;
                    BodyDef bodyDef = new BodyDef();
                    bodyDef.type = BodyDef.BodyType.StaticBody;
                    bodyDef.position.set(startX +(endX - startX)/2, i + 7f);


                    PolygonShape shape = new PolygonShape();
                    shape.setAsBox((endX - startX)/2, 14/2);

                    FixtureDef fixtureDef = new FixtureDef();
                    fixtureDef.shape = shape;

                    Body ground;

                    ground = world.createBody(bodyDef);

                    Fixture fixture = ground.createFixture(fixtureDef);
                    //ground.createFixture(fixtureDef);
                    //ground.setTransform(e, i, 0);

                    groundArray.add(ground);

                    shape.dispose();
                }
                if(tile !=null) {

                    //switch case to draw different entities based on tiles in the tiled map
                    switch (tile.getId()) {
                        case (4):
                            entities.add(new Box(this, world, e, i));
                            System.out.println("adding box");
                            layer.setCell((int)(e/16f), (int)(i/16f), sky); //remove tile
                            break;
                        case (8):
                            iceTool.setPos(e, i);
                            System.out.println("adding tool");
                            layer.setCell((int)(e/16f), (int)(i/16f), sky);
                            break;


                        case (7):
                            player.setPos(e, i);
                            System.out.println("adding player");
                            layer.setCell((int)(e/16f), (int)(i/16f), sky);
                            break;

                        case(12):
                            door.setPos(e, i);
                            System.out.println("adding door");
                            layer.setCell((int)(e/16f), (int)(i/16f), sky);
                            break;

                        case(13):
                            holdButton.setPos(e, i);
                            System.out.println("adding button");
                            layer.setCell((int)(e/16f), (int)(i/16f), sky);
                            break;

                        case(14):
                            toggleSwitch.setPos(e, i);
                            System.out.println("adding switch");
                            layer.setCell((int)(e/16f), (int)(i/16f), sky);
                            break;
                    }
                }



            }
        }
    }

    public void ground(float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8, 16/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Body ground;

        ground = world.createBody(bodyDef);

        Fixture fixture = ground.createFixture(fixtureDef);
        //ground.createFixture(fixtureDef);
        //ground.setTransform(e, i, 0);

        groundArray.add(ground);
    }


    /** Draw solid backgrounds based on world gravity */
    public void drawBackground(){
        if(worldGravity >0){ //draw solid white if gravity inverted
            Gdx.graphics.getGL20().glClearColor( 1, 1, 1, 1 );
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        }
        else{ //draw straight black if gravity normal
            Gdx.graphics.getGL20().glClearColor( 0, 0, 0, 1 );
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        }
    }

    //method to be overriden
    public  void death(){
    }

    //win method
    public void win(){
        //stop sounds
        player.steps.stop();
        iceTool.sound.stop();
        //set splashcreen
        game.setScreen(new WinScreen(game));
    }
    //method to be overriden
    public void escape(){}



    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
        boxArray.clear();
        dispose();
    }
}
