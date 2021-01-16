package com.mygdx.game.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.io.File;

public class Platformer extends Game {

    //resolution of the program window
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;

    //music & sounds variable to switch between music
    public boolean isMusicOn = false;
    public Music menuMusic;
    public Music levelMusic;
    public Music backgroundMusic;
    private Sound sound;
    public float volume = 1.00f;

    Level currentLevel;

    //Batch to draw textures on Menus
    public SpriteBatch batch;

    //Method to play sounds on menus
    public void playSound(String s){
        sound = Gdx.audio.newSound(Gdx.files.internal(s));
        sound.play();
    }

    //Method to specifically play sound when hovering over UI elements
    public void playUiHoverSound(){

        sound = Gdx.audio.newSound(Gdx.files.internal("sounds\\hover.wav"));
        sound.play();

    }

    //method to switch between different music tracks
    public void setMusic(Music track){
        //if statements to ensure two tracks don't start playing at the same time
        if(menuMusic.isPlaying())
            menuMusic.stop();
        if(levelMusic.isPlaying())
            levelMusic.stop();

        //play music
        backgroundMusic = track;
        backgroundMusic.setVolume(volume);
        backgroundMusic.play();
        backgroundMusic.setLooping(true);
        isMusicOn = true;
    }

    public void lowerVolume(){
        volume -= 0.1f;
        backgroundMusic.setVolume(volume);
        if(volume<0.1){ //if statement to overcome float innacuracy
            backgroundMusic.setVolume(0f);
        }
        System.out.println(volume);
    }

    public void increaseVolume(){
        volume += 0.1f;
        backgroundMusic.setVolume(volume);
        System.out.println(volume);
    }

    /**this method is called when first launching the program*/
    @Override
    public void create () {
        //initiate parameters
        batch = new SpriteBatch();

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds\\menu.wav"));
        levelMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds\\level_music.mp3"));

        //set the screen to the main menu
        this.setScreen(new MainMenu(this));
    }

    public void setCurrentLevel(Level level){
        currentLevel = level;
    }

    /** Method to actually draw everything on screen */
    @Override
    public void render () {
        super.render();

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

}