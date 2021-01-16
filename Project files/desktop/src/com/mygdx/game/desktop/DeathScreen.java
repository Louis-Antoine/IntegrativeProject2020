package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class DeathScreen implements Screen {
    Texture deathText;
    Platformer game;
    Level level;

    public DeathScreen(Platformer game, Level level){
        deathText = new Texture("ui\\death_text.png");
        this.game = game;
        this.level = level;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        game.batch.begin();

        //draw winning splash screen
        game.batch.draw(deathText, Platformer.WIDTH/2 - deathText.getWidth()/2, Platformer.HEIGHT/2 - deathText.getHeight()/2);

        //go back to assigned screen if player presses ENTER
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            game.setScreen(level);
        }

        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {

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

    }
}
