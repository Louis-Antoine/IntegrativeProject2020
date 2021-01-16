package com.mygdx.game.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MainMenu implements Screen {

    //dimension of buttons

    private static final int EXIT_BUTTON_WIDTH = 250;
    private static final int EXIT_BUTTON_HEIGHT = 120;
    private static final int OPTIONS_BUTTON_WIDTH = 350;
    private static final int OPTIONS_BUTTON_HEIGHT = 120;;
    private static final int LEVELS_BUTTON_WIDTH = 300;
    private static final int LEVELS_BUTTON_HEIGHT = 120;

    //y-position of buttons
    private static final int EXIT_BUTTON_Y = 100;
    private static final int LEVELS_BUTTON_Y = 360;
    private static final int OPTIONS_BUTTON_Y = 230;

    //ui booleans
    boolean isLevel = false;
    boolean isOptions = false;
    boolean isExit = false;

    final Platformer game;

    //Textures for buttons
    Texture levelsButtonActive;
    Texture levelsButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture optionsButtonActive;
    Texture optionsButtonInactive;
    Texture background;

    public MainMenu (final Platformer game) {
        this.game = game;

        //load textures
        levelsButtonActive = new Texture("ui\\levels_selected.png");
        levelsButtonInactive = new Texture("ui\\levels_unselected.png");
        exitButtonActive = new Texture("ui\\exit_selected.png");
        exitButtonInactive = new Texture("ui\\exit_unselected.png");
        optionsButtonActive = new Texture("ui\\options_selected.png");
        optionsButtonInactive = new Texture("ui\\options_unselected.png");
        background = new Texture("ui\\background.png");

        //set music to main menu music
        if(!game.menuMusic.isPlaying())
            this.game.setMusic(game.menuMusic);
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

        //set x-position to be centered on the screen
        int x = Platformer.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2;

        //exit button

        //draw button at specific location, with specific width and height if mouse is on the button
        if(Gdx.input.getX() < x + EXIT_BUTTON_WIDTH && Gdx.input.getX() > x && Platformer.HEIGHT - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && Platformer.HEIGHT - Gdx.input.getY() > EXIT_BUTTON_Y) {
            game.batch.draw(exitButtonActive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
            if(!isExit){
                isExit =true;
                game.playUiHoverSound();
            }
            //detect mouse input
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
                game.playSound("sounds\\click.wav");
                Gdx.app.exit();
                System.exit(0);
            }
        }
        else{
            //draw inactive texture if mouse is not on the button
            isExit = false;
            game.batch.draw(exitButtonInactive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        }

        //levels

        x = Platformer.WIDTH / 2 - LEVELS_BUTTON_WIDTH / 2;
        if(Gdx.input.getX() < x + LEVELS_BUTTON_WIDTH && Gdx.input.getX() > x && Platformer.HEIGHT - Gdx.input.getY() < LEVELS_BUTTON_Y + LEVELS_BUTTON_HEIGHT && Platformer.HEIGHT - Gdx.input.getY() > LEVELS_BUTTON_Y) {
            if(!isLevel){
                isLevel =true;
                game.playUiHoverSound();
            }
            game.batch.draw(levelsButtonActive, x, LEVELS_BUTTON_Y, LEVELS_BUTTON_WIDTH, LEVELS_BUTTON_HEIGHT);
            //detect mouse input
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
                game.playSound("sounds\\click.wav");
                game.setScreen(new LevelsMenu(game));
            }
        }
        else{
            isLevel = false;
            game.batch.draw(levelsButtonInactive, x, LEVELS_BUTTON_Y, LEVELS_BUTTON_WIDTH, LEVELS_BUTTON_HEIGHT);
        }

        //options

        x = Platformer.WIDTH / 2 - OPTIONS_BUTTON_WIDTH / 2;
        if(Gdx.input.getX() < x + OPTIONS_BUTTON_WIDTH && Gdx.input.getX() > x && Platformer.HEIGHT - Gdx.input.getY() < OPTIONS_BUTTON_Y + OPTIONS_BUTTON_HEIGHT && Platformer.HEIGHT - Gdx.input.getY() > OPTIONS_BUTTON_Y) {
            if(!isOptions){
                isOptions =true;
                game.playUiHoverSound();
            }
            //detect mouse input
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                game.playSound("sounds\\click.wav");
                game.setScreen(new OptionsScreen(game, new MainMenu(game)));
            }
            game.batch.draw(optionsButtonActive, x, OPTIONS_BUTTON_Y, OPTIONS_BUTTON_WIDTH, OPTIONS_BUTTON_HEIGHT);
        }
        else{
            isOptions = false;
            game.batch.draw(optionsButtonInactive, x, OPTIONS_BUTTON_Y, OPTIONS_BUTTON_WIDTH, OPTIONS_BUTTON_HEIGHT);
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