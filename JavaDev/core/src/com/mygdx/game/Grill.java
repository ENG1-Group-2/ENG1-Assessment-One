package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import sun.util.resources.cldr.ext.TimeZoneNames_ka;

import java.util.ArrayList;

public class Grill extends ScreenAdapter implements InputProcessor {
    final PiazzaPanicGame game;
    ArrayList<Ingredient> pantryInventory;
    Ingredient grillOneItem;
    BitmapFont font = new BitmapFont();
    SpriteBatch batch = new SpriteBatch();
    TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;

    public Grill(final PiazzaPanicGame game, ArrayList<Ingredient> pantryInventory){
        this.game = game;
        this.pantryInventory = pantryInventory;}

    @Override
    public void show(){
        tiledMap = new TmxMapLoader().load("Tiled/grill.tmx");

        camera = new OrthographicCamera();

        //Gets all properties from imported tiled map.
        MapProperties properties = tiledMap.getProperties();

        int mapWidth = properties.get("width", Integer.class);
        int mapHeight = properties.get("height", Integer.class);
        int tileSize = properties.get("tilewidth", Integer.class);

        //Tiles are square so width and height will be the same.
        camera.setToOrtho(false, mapWidth * tileSize, mapHeight * tileSize);
        camera.update();

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta){
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        batch.begin();

        if (grillOneItem != null){
            float timeDifference = System.currentTimeMillis() - grillOneItem.getCookingStartTime();
            font.draw(batch, Float.toString(timeDifference), 10, 10);
        }

        batch.end();
    }
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean found = false;
        int counter = 0;
        while (found == false && counter < pantryInventory.size()){
            if (pantryInventory.get(counter).getName() == "BurgerPatty" ||
                    pantryInventory.get(counter).getName() == "Bun"){
                System.out.println("ran");
                if (pantryInventory.get(counter).hasCookStarted()){
                    pantryInventory.get(counter).endCook();
                }
                else{
                    grillOneItem = pantryInventory.get(counter);
                    pantryInventory.get(counter).startToCook();
                }
            }
            counter++;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
