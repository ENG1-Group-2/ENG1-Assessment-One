package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User can see the ingredients and recipes they have.
 */
public class InfoScreen extends ScreenAdapter implements InputProcessor {
    final PiazzaPanicGame game;
    Map gameState;
    ArrayList<Ingredient> ingredients;
    ArrayList<Recipe> orders;
    BitmapFont font = new BitmapFont();
    SpriteBatch batch = new SpriteBatch();
    TiledMap tiledMenu;
    OrthographicCamera camera;
    TiledMapRenderer tiledMenuRenderer;
    Texture burger = new Texture(Gdx.files.internal("Beef Burger.png"));
    Texture salad = new Texture(Gdx.files.internal("Chicken Salad.png"));
    HashMap<String, Texture> orderTextures = new HashMap<String, Texture>(2);
    HashMap<String, Integer> orderNo = new HashMap<String, Integer>(2);

    /**
     * Constructor method.
     * @param game The instance of the PiazzaPanicGame.
     * @param gameState A map class that the user has just come from.
     */
    public InfoScreen(final PiazzaPanicGame game, Map gameState){
        this.game = game;
        this.gameState = gameState;
        this.orders = gameState.orders;
        this.ingredients = gameState.pantryInventory;
    }

    /**
     * Runs upon startup of the class.
     */
    @Override
    public void show(){
        tiledMenu = new TmxMapLoader().load("Tiled/infoScreen.tmx");

        camera = new OrthographicCamera();

        //Gets all properties from imported tiled map.
        MapProperties properties = tiledMenu.getProperties();

        int mapWidth = properties.get("width", Integer.class);
        int mapHeight = properties.get("height", Integer.class);
        int tileSize = properties.get("tilewidth", Integer.class);

        //Tiles are square so width and height will be the same.
        camera.setToOrtho(false, mapWidth * tileSize, mapHeight * tileSize);
        camera.update();

        tiledMenuRenderer = new OrthogonalTiledMapRenderer(tiledMenu);

        Gdx.input.setInputProcessor(this);
    }

    /**
     * Runs for every frame of the game.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta){
        // Set black background anc clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        tiledMenuRenderer.setView(camera);
        tiledMenuRenderer.render();

        batch.begin();

        // Puts the picture in a Hash Map alongside
        // a counter.
        orderTextures.put("Beef Burger", burger);
        orderTextures.put("Chicken Salad", salad);
        orderNo.put("Beef Burger", 0);
        orderNo.put("Chicken Salad", 0);

        // Iterates through and counts.
        String orderList;
        orderList = "Orders:";
        for (int i=0; i < orders.size(); i++){
            orderNo.put(orders.get(i).getName(), orderNo.get(orders.get(i).getName())+1);
        }

        String ingredientList1 = "Ingredients:\n";
        String ingredientList2 = "\n";

        // Display two columns.
        if (ingredients.size() % 2 == 1){
            for (int i=0; i < ingredients.size()-1; i = i+2){
                ingredientList1 += "\n" + ingredients.get(i).getName();
                ingredientList2 += "\n"+ ingredients.get(i+1).getName();
            }   ingredientList1 += "\n" + ingredients.get(ingredients.size()-1).getName();
        }else{
            for (int i=0; i < ingredients.size(); i = i+2){
                ingredientList1 += "\n" + ingredients.get(i).getName();
                ingredientList2 += "\n" + ingredients.get(i+1).getName();
            }
        }

        long imagePointX = Math.round(Gdx.graphics.getWidth() * 0.11);
        long imagePointY = Math.round(Gdx.graphics.getHeight() * 0.70);
        long width = Math.round(Gdx.graphics.getWidth() * 0.10);
        long height = Math.round(Gdx.graphics.getHeight() * 0.10);
        int count = 1;

        // Using the hash maps generated, it draws the images for the order column.
        for(java.util.Map.Entry<String, Integer> entry : orderNo.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            for (int i=0; i < value; i++){
                batch.draw(orderTextures.get(key), imagePointX, imagePointY, width, height);
                imagePointX += width;
                if (count % 3 == 0 && count != 0){
                    imagePointY -= height;
                    imagePointX = Math.round(Gdx.graphics.getWidth() * 0.11);
                }
                count += 1;
            }
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("karmatic-arcade/ka1.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (Gdx.graphics.getHeight() / 50);
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        font.draw(batch, orderList, Math.round(Gdx.graphics.getWidth() * 0.11), Math.round(Gdx.graphics.getHeight() * 0.84));
        font.draw(batch, ingredientList1, Math.round(Gdx.graphics.getWidth() * 0.575), Math.round(Gdx.graphics.getHeight() * 0.84));
        font.draw(batch, ingredientList2, Math.round(Gdx.graphics.getWidth() * 0.74), Math.round(Gdx.graphics.getHeight() * 0.84));

        batch.end();
    }

    /**
     * Unused method
     */
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    /**
     * When a user enters a key on their keyboard.
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return false
     */
    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ESCAPE){
            game.setScreen(gameState);
        } else if (keycode == Input.Keys.BACKSPACE){
            Gdx.app.exit();
        }
        return false;
    }

    /**
     * Unused method
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Unused method
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Unused method
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Unused method
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Unused method
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Unused method
     */
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
