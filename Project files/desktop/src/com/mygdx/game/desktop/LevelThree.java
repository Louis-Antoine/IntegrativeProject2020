package com.mygdx.game.desktop;

public class LevelThree extends Level {


    public LevelThree(Platformer game) {
        super(game, "map\\level_3.tmx");
    }

    @Override
    public void death(){
        game.setScreen(new DeathScreen(game, new LevelThree(game)));
    }

    @Override
    public void escape(){
        game.setScreen(new PauseMenu(game, new LevelThree(game)));
    }
}
