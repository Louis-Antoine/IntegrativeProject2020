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

public class LevelsMenu implements Screen {

    //create level buttons
    ButtonImg lvl1img = new ButtonImg(512, 300, 96, 480, new Texture("ui\\lvl1img.png"));
    ButtonImg lvl2img = new ButtonImg(512, 300, 704, 480, new Texture("ui\\lvl2img.png"));
    ButtonImg lvl3img = new ButtonImg(512, 300, 1312, 480, new Texture("ui\\lvl3img.png"));
    ButtonImg lvl4img = new ButtonImg(512, 300, 96, 90, new Texture("ui\\lvl4img.png"));
    ButtonImg lvl5img = new ButtonImg(512, 300, 704, 90, new Texture("ui\\lvl5img.png"));
    ButtonImg lvl6img = new ButtonImg(512, 300, 1312,90, new Texture("ui\\lvl6img.png"));

    //dimension of back button
    private static final int BACK_BUTTON_WIDTH = 100;
    private static final int BACK_BUTTON_HEIGHT = 60;
    private static final int BACK_BUTTON_Y = 950;
    private static final int BACK_BUTTON_X = 50;

    //variables
    final Platformer game;

    Texture backButtonActive;
    Texture backButtonInactive;
    Texture background;
    Texture highlight;


    public LevelsMenu (final Platformer game) {
        this.game = game;
        //load textures
        backButtonActive = new Texture("ui\\back_active.png");
        backButtonInactive = new Texture("ui\\back_inactive.png");
        background = new Texture("ui\\levels_screen_background.png");
        highlight  = new Texture("ui\\highlight_Level_5wider.png");
    }

    @Override
    public void show () {

    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();


        game.batch.draw(background, 0, 0); //draw background image

        //x-position of the button
        int x = BACK_BUTTON_X;

        //back button

        //draw button at specific location, with specific width and height if mouse is on the button
        if(Gdx.input.getX() < x + BACK_BUTTON_WIDTH && Gdx.input.getX() > x && Platformer.HEIGHT - Gdx.input.getY() < BACK_BUTTON_Y + BACK_BUTTON_HEIGHT && Platformer.HEIGHT - Gdx.input.getY() > BACK_BUTTON_Y) {
            game.batch.draw(backButtonActive, x, BACK_BUTTON_Y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);

            //detect mouse input
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                game.setScreen(new MainMenu(game));
                game.playSound("sounds\\click.wav");
            }
        }
        else{
            //draw inactive texture if mouse is not on the button
            game.batch.draw(backButtonInactive, x, BACK_BUTTON_Y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
        }

        //levels

        //lvl1

        x = lvl1img.x;
        if(Gdx.input.getX() < x + lvl1img.width && Gdx.input.getX() > x && Platformer.HEIGHT - Gdx.input.getY() < lvl1img.y + lvl1img.height && Platformer.HEIGHT - Gdx.input.getY() > lvl1img.y) {
            game.batch.draw(lvl1img.img, x, lvl1img.y, lvl1img.width, lvl1img.height);
            game.batch.draw(highlight, x-5, lvl1img.y -5, lvl1img.width +10, lvl1img.height +10);
            //detect mouse input
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                game.setScreen(new LevelOne(game));
                game.playSound("sounds\\click.wav");
            }
        }
        else{
            game.batch.draw(lvl1img.img, x, lvl1img.y, lvl1img.width, lvl1img.height);
        }

        //lvl2

        x = lvl2img.x;
        if(Gdx.input.getX() < x + lvl2img.width && Gdx.input.getX() > x && Platformer.HEIGHT - Gdx.input.getY() < lvl2img.y + lvl2img.height && Platformer.HEIGHT - Gdx.input.getY() > lvl2img.y) {
            game.batch.draw(lvl2img.img, x, lvl2img.y, lvl2img.width, lvl2img.height);
            game.batch.draw(highlight, x-5, lvl2img.y -5, lvl2img.width +10, lvl2img.height +10);
            //detect mouse input
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                game.setScreen(new LevelTwo(game));
                game.playSound("sounds\\click.wav");
            }
        }
        else{
            game.batch.draw(lvl2img.img, x, lvl2img.y, lvl2img.width, lvl2img.height);
        }

        //lvl3

        x = lvl3img.x;
        if(Gdx.input.getX() < x + lvl3img.width && Gdx.input.getX() > x && Platformer.HEIGHT - Gdx.input.getY() < lvl3img.y + lvl3img.height && Platformer.HEIGHT - Gdx.input.getY() > lvl3img.y) {
            game.batch.draw(lvl3img.img, x, lvl3img.y, lvl3img.width, lvl3img.height);
            game.batch.draw(highlight, x-5, lvl3img.y -5, lvl3img.width +10, lvl3img.height +10);
            //detect mouse input
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                game.setScreen(new LevelThree(game));
                game.playSound("sounds\\click.wav");
            }
        }
        else{
            game.batch.draw(lvl3img.img, x, lvl3img.y, lvl3img.width, lvl3img.height);
        }

        //lvl4

        x = lvl4img.x;
        if(Gdx.input.getX() < x + lvl4img.width && Gdx.input.getX() > x && Platformer.HEIGHT - Gdx.input.getY() < lvl4img.y + lvl4img.height && Platformer.HEIGHT - Gdx.input.getY() > lvl4img.y) {
            game.batch.draw(lvl4img.img, x, lvl4img.y, lvl4img.width, lvl4img.height);
            game.batch.draw(highlight, x-5, lvl4img.y -5, lvl4img.width +10, lvl4img.height +10);
            //detect mouse input
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                game.setScreen(new LevelFour(game));
                game.playSound("sounds\\click.wav");
            }
        }
        else{
            game.batch.draw(lvl4img.img, x, lvl4img.y, lvl4img.width, lvl4img.height);
        }

        //lvl5

        x = lvl5img.x;
        if(Gdx.input.getX() < x + lvl5img.width && Gdx.input.getX() > x && Platformer.HEIGHT - Gdx.input.getY() < lvl5img.y + lvl5img.height && Platformer.HEIGHT - Gdx.input.getY() > lvl5img.y) {
            game.batch.draw(lvl5img.img, x, lvl5img.y, lvl5img.width, lvl5img.height);
            game.batch.draw(highlight, x-5, lvl5img.y -5, lvl5img.width +10, lvl5img.height +10);
            //detect mouse input
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                game.setScreen(new LevelFive(game));
                game.playSound("sounds\\click.wav");
            }
        }
        else{
            game.batch.draw(lvl5img.img, x, lvl5img.y, lvl5img.width, lvl5img.height);
        }

        //lvl6

        x = lvl6img.x;
        if(Gdx.input.getX() < x + lvl6img.width && Gdx.input.getX() > x && Platformer.HEIGHT - Gdx.input.getY() < lvl6img.y + lvl6img.height && Platformer.HEIGHT - Gdx.input.getY() > lvl6img.y) {
            game.batch.draw(lvl6img.img, x, lvl6img.y, lvl6img.width, lvl6img.height);
            game.batch.draw(highlight, x-5, lvl6img.y -5, lvl6img.width +10, lvl6img.height +10);
            //detect mouse input
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                game.setScreen(new LevelSix(game));
                game.playSound("sounds\\click.wav");
            }
        }
        else{
            game.batch.draw(lvl6img.img, x, lvl6img.y, lvl6img.width, lvl6img.height);
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