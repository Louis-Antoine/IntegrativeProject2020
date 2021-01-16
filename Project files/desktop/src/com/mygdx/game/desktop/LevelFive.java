package com.mygdx.game.desktop;

public class LevelFive extends Level {


    public LevelFive(Platformer game) {
        super(game, "map\\level_5.tmx");
    }

    @Override
    public void death(){
        game.setScreen(new DeathScreen(game, new LevelFive(game)));
    }

    @Override
    public void escape(){
        game.setScreen(new PauseMenu(game, new LevelFive(game)));
    }
}
