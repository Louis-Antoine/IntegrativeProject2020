package com.mygdx.game.desktop;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.HashMap;

import static com.badlogic.gdx.maps.tiled.TiledMapTileLayer.*;

public enum TileType {


    //all tiletypes catogorised by ID, if they're collidable and their names
    PLATFORM(1, true, "Platform"),
    ICYPLATFORM(2, true, "Icy_Platform"),
    SKY(3, false, "sky"),
    BOX(4, false, "box"),
    ICE_TOOL(8, false, "ice_tool"),
    PLAYER(7, false, "player"),
    DOOR(12, false, "door"),
    BUTTON(13, false, "button"),
    SWITCH(14, false, "switch");

    public static final int TILE_SIZE = 16; //pixel size of a tile

    private int id;
    private boolean collidable;
    private String name;

    //contructor
    private TileType (int id, boolean collidable, String name){
        this.id = id;
        this.collidable = collidable;
        this.name = name;
    }

    //sets and gets
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static HashMap<Integer, TileType> tileMap;

    static {
        tileMap = new HashMap<Integer, TileType>();
        for(TileType tileType : TileType.values()){
            tileMap.put(tileType.getId(), tileType);
        }
    }

    public static TileType getTileTypeById (int id){
        return tileMap.get(id);
    }
}
