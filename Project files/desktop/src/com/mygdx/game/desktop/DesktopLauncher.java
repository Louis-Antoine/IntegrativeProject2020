package com.mygdx.game.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS = 60; //set max fps
		//set game resolution
		config.width = Platformer.WIDTH;
		config.height = Platformer.HEIGHT;

		config.resizable = false; //user will not be able to resize window
		config.fullscreen = true; //fullscreen always on
		new LwjglApplication(new Platformer(), config); //start game
	}
}
