package com.mygdx.game.desktop;

public class LevelFour extends Level {


    public LevelFour(Platformer game) {
        super(game, "map\\level_4.tmx");
    }

    @Override
    public void death(){
        game.setScreen(new DeathScreen(game, new LevelFour(game)));
    }

    @Override
    public void escape(){
        game.setScreen(new PauseMenu(game, new LevelFour(game)));
    }
}
