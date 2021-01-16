package com.mygdx.game.desktop;

public class LevelSix extends Level {


    public LevelSix(Platformer game) {
        super(game, "map\\Maptest.tmx");
        super.showDebug = true;
    }

    @Override
    public void death(){
        game.setScreen(new DeathScreen(game, new LevelSix(game)));
    }

    @Override
    public void escape(){
        game.setScreen(new PauseMenu(game, new LevelSix(game)));
    }
}
