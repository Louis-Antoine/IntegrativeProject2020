package com.mygdx.game.desktop;


public class LevelOne extends Level {


    public LevelOne(Platformer game) {
        //load Tiled map
        super(game, "map\\level_1.tmx");
    }

    /**Override these methods from the Level class to ensure that in case of dying or resetting the level, the right level is rendered  */

    @Override
    public void death(){
        game.setScreen(new DeathScreen(game, new LevelOne(game)));
    }

    @Override
    public void escape(){
        game.setScreen(new PauseMenu(game, new LevelOne(game)));
    }
}
