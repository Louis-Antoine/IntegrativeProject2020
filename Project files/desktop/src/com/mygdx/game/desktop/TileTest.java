package com.mygdx.game.desktop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

public class TileTest extends ApplicationAdapter implements Screen {
    OrthographicCamera cam;
    GameMap gameMap;
    TiledMapRenderer tiledMapRenderer;
    TiledMap tiledMap;


    public void create(){
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        cam.update();

        tiledMap = new TmxMapLoader().load("map\\Maptest.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    public void render () {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.update();

        if(Gdx.input.isTouched()){
            cam.translate(-Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
            cam.update();
        }

        if(Gdx.input.justTouched()){
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            TileType type = gameMap.getTileTypeByLocation(1, pos.x, pos.y);

            if(type != null) {
                System.out.println(type.getId() +": " + type.getName());
            }
        }
        tiledMapRenderer.setView(cam);
        tiledMapRenderer.render();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

    }

    @Override
    public void hide() {

    }
}
