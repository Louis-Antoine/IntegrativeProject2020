package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class WinScreen implements Screen {
    Texture winText;
    Platformer game;

    public WinScreen(Platformer game){
        winText = new Texture("ui\\level_done_text.png");
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        game.batch.begin();

        //draw winning splash screen
        game.batch.draw(winText, Platformer.WIDTH/2 - winText.getWidth()/2, Platformer.HEIGHT/2 - winText.getHeight()/2);

        //go back to main menu if player presses ENTER
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            game.setScreen(new MainMenu(game));
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
