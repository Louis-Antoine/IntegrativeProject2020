package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class OptionsScreen implements Screen {

    //sounds +/- dimensions
    private static final int PLUS_MINUS_SIDE = 32;
    private static final int MINUS_BUTTON_Y = 450;
    private static final int PLUS_BUTTON_Y = 450;

    //volume bar max dimensions
    private static final int VOLUME_BAR_WIDTH = 400;
    private static final int VOLUME_BAR_HEIGHT = 32;
    private static final int VOLUME_BAR_Y = 450;
    private static final int VOLUME_BAR_X = Platformer.WIDTH / 2 - VOLUME_BAR_WIDTH / 2;

    //back button dimensions
    private static final int BACK_BUTTON_WIDTH = 100;
    private static final int BACK_BUTTON_HEIGHT = 60;
    private static final int BACK_BUTTON_Y = 950;
    private static final int BACK_BUTTON_X = 50;

    //controls button dimensions
    private static final int CONTROLS_BUTTON_WIDTH = 463;
    private static final int CONTROLS_BUTTON_HEIGHT = 81;
    private static final int CONTROLS_BUTTON_Y = 230;

    //ui booleans
    boolean isControls = false;

    final Platformer game;

    Screen previousScreen; //store screen options menu was accessed from

    //button textures
    Texture minusButtonActive;
    Texture minusButtonInactive;
    Texture plusButtonActive;
    Texture plusButtonInactive;
    Texture volumeBar;
    Texture controlsButtonActive;
    Texture controlsButtonInactive;

    Texture backButtonActive;
    Texture backButtonInactive;
    Texture background;

    public OptionsScreen (final Platformer game, Screen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;

        //load textures
        minusButtonActive = new Texture("ui\\Volume-Activated.png");
        minusButtonInactive = new Texture("ui\\Volume-Deactivated.png");
        plusButtonActive = new Texture("ui\\Volume+Activated.png");
        plusButtonInactive = new Texture("ui\\Volume+Deactivated.png");
        background = new Texture("ui\\options_screen_background.png");
        controlsButtonActive = new Texture("ui\\controls_active.png");
        controlsButtonInactive = new Texture("ui\\controls_inactive.png");
        backButtonActive = new Texture("ui\\back_active.png");
        backButtonInactive = new Texture("ui\\back_inactive.png");
        volumeBar = new Texture("ui\\volume_bar.png");
    }

    @Override
    public void show () {

    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();


        game.batch.draw(background, 0, 0); //draw background

        int x = BACK_BUTTON_X; //set x-position at screen center

        //back button
        if(Gdx.input.getX() < x + BACK_BUTTON_WIDTH && Gdx.input.getX() > x && Platformer.HEIGHT - Gdx.input.getY() < BACK_BUTTON_Y + BACK_BUTTON_HEIGHT && Platformer.HEIGHT - Gdx.input.getY() > BACK_BUTTON_Y) {
            game.batch.draw(backButtonActive, x, BACK_BUTTON_Y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);

            //detect mouse input
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                game.setScreen(previousScreen);
                game.playSound("sounds\\click.wav");
            }
        }
        else{
            //draw inactive texture if mouse is not on the button
            game.batch.draw(backButtonInactive, x, BACK_BUTTON_Y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
        }

        //minus

        x = VOLUME_BAR_X - 44;
        if(Gdx.input.getX() < x + PLUS_MINUS_SIDE && Gdx.input.getX() > x && Platformer.HEIGHT - Gdx.input.getY() < MINUS_BUTTON_Y + PLUS_MINUS_SIDE && Platformer.HEIGHT - Gdx.input.getY() > MINUS_BUTTON_Y) {

            game.batch.draw(minusButtonActive, x, MINUS_BUTTON_Y, PLUS_MINUS_SIDE, PLUS_MINUS_SIDE);
            //detect mouse input
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && game.volume > 0){
                game.playSound("sounds\\click.wav");
                game.lowerVolume();
            }
        }
        else{
            game.batch.draw(minusButtonInactive, x, MINUS_BUTTON_Y, PLUS_MINUS_SIDE, PLUS_MINUS_SIDE);
        }

        //plus
        x = VOLUME_BAR_X + 412;
        if(Gdx.input.getX() < x + PLUS_MINUS_SIDE && Gdx.input.getX() > x && Platformer.HEIGHT - Gdx.input.getY() < PLUS_BUTTON_Y + PLUS_MINUS_SIDE && Platformer.HEIGHT - Gdx.input.getY() > PLUS_BUTTON_Y) {

            game.batch.draw(plusButtonActive, x, PLUS_BUTTON_Y, PLUS_MINUS_SIDE, PLUS_MINUS_SIDE);
            //detect mouse input
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && game.volume < 1){
                game.playSound("sounds\\click.wav");
                game.increaseVolume();
            }
        }
        else{
            game.batch.draw(plusButtonInactive, x, PLUS_BUTTON_Y, PLUS_MINUS_SIDE, PLUS_MINUS_SIDE);
        }

        //volume bar

        //draw volume in proportion to the Platform object's volume
        game.batch.draw(volumeBar, VOLUME_BAR_X, VOLUME_BAR_Y, (VOLUME_BAR_WIDTH * game.volume), VOLUME_BAR_HEIGHT, VOLUME_BAR_X, VOLUME_BAR_Y, VOLUME_BAR_WIDTH, VOLUME_BAR_HEIGHT, false, false);

        //controls

        x = Platformer.WIDTH / 2 - CONTROLS_BUTTON_WIDTH / 2;
        if(Gdx.input.getX() < x + CONTROLS_BUTTON_WIDTH && Gdx.input.getX() > x && Platformer.HEIGHT - Gdx.input.getY() < CONTROLS_BUTTON_Y + CONTROLS_BUTTON_HEIGHT && Platformer.HEIGHT - Gdx.input.getY() > CONTROLS_BUTTON_Y) {
            if(!isControls){
                isControls =true;
                game.playUiHoverSound();
            }
            //detect mouse input
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                game.playSound("sounds\\click.wav");
                game.setScreen(new ControlsMenu(game, this));
            }
            game.batch.draw(controlsButtonActive, x, CONTROLS_BUTTON_Y, CONTROLS_BUTTON_WIDTH, CONTROLS_BUTTON_HEIGHT);
        }
        else{
            isControls = false;
            game.batch.draw(controlsButtonInactive, x, CONTROLS_BUTTON_Y, CONTROLS_BUTTON_WIDTH, CONTROLS_BUTTON_HEIGHT);
        }

        game.batch.end();
    }

    @Override
    public void resize (int width, int height) {

    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }

}