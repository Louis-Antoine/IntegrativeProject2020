package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class ControlsMenu implements Screen {

    //button dimension
    private static final int BACK_BUTTON_WIDTH = 100;
    private static final int BACK_BUTTON_HEIGHT = 60;
    private static final int BACK_BUTTON_Y = 950;
    private static final int BACK_BUTTON_X = 50;

    final Platformer game;
    Screen previousScreen;

    //button textures
    Texture backButtonActive;
    Texture backButtonInactive;
    Texture background;


    public ControlsMenu (final Platformer game, Screen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;

        //load textures
        backButtonActive = new Texture("ui\\back_active.png");
        backButtonInactive = new Texture("ui\\back_inactive.png");
        background = new Texture("ui\\controls_screen_background.png");
    }

    @Override
    public void show () {

    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        //draw background
        game.batch.draw(background, 0, 0);

        int x = BACK_BUTTON_X;

        //back button

        if(Gdx.input.getX() < x + BACK_BUTTON_WIDTH && Gdx.input.getX() > x && Platformer.HEIGHT - Gdx.input.getY() < BACK_BUTTON_Y + BACK_BUTTON_HEIGHT && Platformer.HEIGHT - Gdx.input.getY() > BACK_BUTTON_Y) {
            game.batch.draw(backButtonActive, x, BACK_BUTTON_Y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);

            //detect mouse input
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                game.setScreen(new OptionsScreen(game, previousScreen));
                game.playSound("sounds\\click.wav");
            }
        }
        else{
            game.batch.draw(backButtonInactive, x, BACK_BUTTON_Y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
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