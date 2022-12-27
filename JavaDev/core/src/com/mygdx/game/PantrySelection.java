package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

public class PantrySelection extends ScreenAdapter implements InputProcessor{
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    OrthographicCamera camera;
    MapObjects objects;

    final PiazzaPanicGame game;
    int tileSize;
    int mapWidth;
    int mapHeight;

    public PantrySelection(final PiazzaPanicGame game) {
        this.game = game;
    }

    @Override
    public void show(){
        tiledMap = new TmxMapLoader().load("Tiled/pantry.tmx");

        camera = new OrthographicCamera();

        Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);

        //Gets all properties from imported tiled map.
        MapProperties properties = tiledMap.getProperties();

        int mapWidth = properties.get("width", Integer.class);
        int mapHeight = properties.get("height", Integer.class);
        int tileSize = properties.get("tilewidth", Integer.class);

        //Tiles are square so width and height will be the same.
        camera.setToOrtho(false, mapWidth * tileSize, mapHeight * tileSize);
        camera.update();

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        objects = tiledMap.getLayers().get(0).getObjects();

        Gdx.input.setInputProcessor(this);
    }

    /**
     * Constantly renders frame as this screen is ran
     * by rendering camera and sprites.
     */
    @Override
    public void render(float delta) {
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
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
        for (int i = 0; i < objects.getCount(); i++) {
            MapObject temp = objects.get(i);
            if (temp instanceof RectangleMapObject) {
                RectangleMapObject tempRectangleObject = (RectangleMapObject) temp;
                if (checkClickOnFood(tempRectangleObject.getRectangle(), screenX, screenY)) {
                    System.out.println(objects.get(i).getName());
                }

            }
        }
        return false;
    }


    private boolean checkClickOnFood(Rectangle food, int x, int y){
        // Y is inverted in LibGDX.
        food.setY(tileSize * mapHeight - food.getY());
        return food.contains(x, Gdx.graphics.getHeight() - y);
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
