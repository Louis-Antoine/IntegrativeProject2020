package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class PauseMenu implements Screen {


    //button y-positions
    private static final int MAINMENU_BUTTON_Y = 220;
    private static final int RESUME_BUTTON_Y = 540;
    private static final int OPTIONS_BUTTON_Y = 380;

    //ui booleans
    boolean isResume = false;
    boolean isOptions = false;
    boolean isMain = false;

    Level level;

    final Platformer game;

    //button textures
    Texture resumeButtonActive;
    Texture resumeButtonInactive;
    Texture mainButtonActive;
    Texture mainButtonInactive;
    Texture optionsButtonActive;
    Texture optionsButtonInactive;
    Texture background;

    public PauseMenu (final Platformer game, Level level) {
        this.game = game;
        this.level = level;

        //load textures
        resumeButtonActive = new Texture("ui\\Resume_active.png");
        resumeButtonInactive = new Texture("ui\\resume_inactive.png");
        mainButtonActive = new Texture("ui\\MainMenu_active.png");
        mainButtonInactive = new Texture("ui\\MainMenu_inactive.png");
        optionsButtonActive = new Texture("ui\\options_selected.png");
        optionsButtonInactive = new Texture("ui\\options_unselected.png");
        background = new Texture("ui\\pause_background.png");
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

        //set button x-position in the centre of the scrren
        int x = Platformer.WIDTH / 2 - mainButtonActive.getWidth() / 2;

        //mainmenu button

        if(Gdx.input.getX() < x + mainButtonActive.getWidth() && Gdx.input.getX() > x && Platformer.HEIGHT - Gdx.input.getY() < MAINMENU_BUTTON_Y + mainButtonActive.getHeight() && Platformer.HEIGHT - Gdx.input.getY() > MAINMENU_BUTTON_Y) {
            game.batch.draw(mainButtonActive, x, MAINMENU_BUTTON_Y, mainButtonActive.getWidth(), mainButtonActive.getHeight());
            if(!isMain){
                isMain =true;
                game.playUiHoverSound();
            }
            //detect mouse input
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
                game.playSound("sounds\\click.wav");
                game.setScreen(new MainMenu(game));
            }
        }
        else{
            isMain = false;
            game.batch.draw(mainButtonInactive, x, MAINMENU_BUTTON_Y, mainButtonActive.getWidth(), mainButtonActive.getHeight());
        }

        //resume

        x = Platformer.WIDTH / 2 - resumeButtonActive.getWidth() / 2;
        if(Gdx.input.getX() < x + resumeButtonActive.getWidth()&& Gdx.input.getX() > x && Platformer.HEIGHT - Gdx.input.getY() < RESUME_BUTTON_Y + resumeButtonActive.getHeight() && Platformer.HEIGHT - Gdx.input.getY() > RESUME_BUTTON_Y) {
            if(!isResume){
                isResume =true;
                game.playUiHoverSound();
            }
            game.batch.draw(resumeButtonActive, x, RESUME_BUTTON_Y, resumeButtonActive.getWidth(), resumeButtonActive.getHeight());
            //detect mouse input
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
                game.playSound("sounds\\click.wav");
                game.setScreen(level);
                dispose();
            }
        }
        else{
            isResume = false;
            game.batch.draw(resumeButtonInactive, x, RESUME_BUTTON_Y, resumeButtonActive.getWidth(), resumeButtonActive.getHeight());
        }

        //options

        x = Platformer.WIDTH / 2 - optionsButtonActive.getWidth() / 2;
        if(Gdx.input.getX() < x + resumeButtonActive.getWidth() && Gdx.input.getX() > x && Platformer.HEIGHT - Gdx.input.getY() < OPTIONS_BUTTON_Y + resumeButtonActive.getHeight() && Platformer.HEIGHT - Gdx.input.getY() > OPTIONS_BUTTON_Y) {
            if(!isOptions){
                isOptions =true;
                game.playUiHoverSound();
            }
            //detect mouse input
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                game.playSound("sounds\\click.wav");
                game.setScreen(new OptionsScreen(game, new PauseMenu(game, level)));
            }
            game.batch.draw(optionsButtonActive, x, OPTIONS_BUTTON_Y, resumeButtonActive.getWidth(), resumeButtonActive.getHeight());
        }
        else{
            isOptions = false;
            game.batch.draw(optionsButtonInactive, x, OPTIONS_BUTTON_Y, resumeButtonActive.getWidth(), resumeButtonActive.getHeight());
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