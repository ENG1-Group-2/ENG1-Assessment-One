package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

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
        // Set black background anc clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        batch.begin();

        if (grillOneItem != null){
            Long timeDifference = (long) (System.currentTimeMillis() - grillOneItem.getCookingStartTime());
            timeDifference = (timeDifference/1000) % 60;
            font.draw(batch, Long.toString(timeDifference), 10, 10);
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
        if (grillOneItem != null){
            System.out.println(grillOneItem.endCook());
            grillOneItem = null;
        }
        else{
            boolean found = false;
            int counter = 0;
            while (found == false && counter < pantryInventory.size()){
                if (pantryInventory.get(counter).getName() == "BurgerPatty" ||
                        pantryInventory.get(counter).getName() == "Bun"){
                    grillOneItem = pantryInventory.get(counter);
                    pantryInventory.remove(counter);
                    grillOneItem.startToCook();
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
