package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
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
import com.badlogic.gdx.graphics.GL20;

import java.util.ArrayList;

public class PantrySelection extends ScreenAdapter implements InputProcessor{
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    OrthographicCamera camera;
    MapObjects objects;

    final PiazzaPanicGame game;
    int tileSize;
    int mapWidth;
    int mapHeight;
    Ingredient onion;
    Ingredient pepper;
    Ingredient lettuce;
    Ingredient cookedChicken;
    Ingredient saladDressing;
    Ingredient pineapple;
    Ingredient pizzaSauce;
    Ingredient pizzaBase;
    Ingredient burgerPatty;
    Ingredient tuna;
    Ingredient mayo;
    Ingredient jacketPotato;
    Ingredient breadBuns;
    Ingredient ham;
    ArrayList<Ingredient> shop = new ArrayList<>();
    ArrayList<Ingredient> selection = new ArrayList<>();

    public PantrySelection(final PiazzaPanicGame game) {
        this.game = game;
    }

    private void createPantryItem() {
        pepper = new Ingredient("Peppers", false);
        lettuce = new Ingredient("Lettuce", false);
        cookedChicken = new PreChoppedIngredient("Chicken");
        saladDressing = new PreChoppedIngredient("SaladDressing");
        pineapple = new Ingredient("Pineapple", false);
        pizzaSauce = new PreChoppedIngredient("PizzaSauce");
        pizzaBase = new HotIngredient("Base", true, 5);
        burgerPatty = new HotIngredient("BurgerPatty", true, 5);
        mayo = new PreChoppedIngredient("Mayo");
        // TODO: Add on tiled map.
        tuna = new PreChoppedIngredient("Tuna");
        jacketPotato = new HotIngredient("JacketPotato", false, 5);
        breadBuns = new HotIngredient("Bun", false, 5);
        ham = new PreChoppedIngredient("Ham");
        selection.add(lettuce);
        selection.add(cookedChicken);
        selection.add(saladDressing);
        selection.add(pineapple);
        selection.add(pizzaSauce);
        selection.add(pizzaSauce);
        selection.add(burgerPatty);
        selection.add(mayo);
        selection.add(tuna);
        selection.add(jacketPotato);
        selection.add(breadBuns);
        selection.add(ham);
    }


    @Override
    public void show(){
        tiledMap = new TmxMapLoader().load("Tiled/pantry.tmx");

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

        objects = tiledMap.getLayers().get(0).getObjects();

        Gdx.input.setInputProcessor(this);

        createPantryItem();
    }

    /**
     * Constantly renders frame as this screen is ran
     * by rendering camera and sprites.
     */
    @Override
    public void render(float delta) {
        // Set black background anc clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE){
            game.setScreen(new Map(game, shop));
        }
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
                    boolean found = false;
                    int counter = 0;
                    while (found == false && counter < selection.size())
                        if (selection.get(counter).getName() == tempRectangleObject.getName()){
                            shop.add(selection.get(counter).copy());
                            found = true;
                        }
                        counter++;
                    }
                }

            }
        return false;
        }
    }


    private boolean checkClickOnFood(Rectangle food, int x, int y){
        // Y is inverted in LibGDX.
        food.setX(tileSize * mapHeight - food.getX());
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
