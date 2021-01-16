package com.mygdx.game.desktop;

import com.badlogic.gdx.graphics.Texture;

public class ButtonImg {

    //button dimensions and position
    int width;
    int height;
    int x;
    int y;
    Texture img;

    //constructor
    public ButtonImg(int width, int height, int x, int y, Texture img) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.img = img;
    }

    //sets and gets
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Texture getImg() {
        return img;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setImg(Texture img) {
        this.img = img;
    }
}
