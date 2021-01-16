package com.mygdx.game.desktop;

public class LevelTwo extends Level {


    public LevelTwo(Platformer game) {
        super(game, "map\\level_2.tmx");
    }

    @Override
    public void death(){
        game.setScreen(new DeathScreen(game, new LevelTwo(game)));
    }

    @Override
    public void escape(){
        game.setScreen(new PauseMenu(game, new LevelTwo(game)));
    }
}
