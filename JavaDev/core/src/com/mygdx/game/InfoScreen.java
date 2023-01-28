package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.ArrayList;

public class InfoScreen extends ScreenAdapter implements InputProcessor {

    final PiazzaPanicGame game;
    Map gameState;
    ArrayList<Ingredient> ingredients;
    ArrayList<Recipe> orders;
    ArrayList<Ingredient> shoppingList;
    Music menuMusic;
    BitmapFont font = new BitmapFont();
    SpriteBatch batch = new SpriteBatch();
    int customerCounter;
    TiledMap tiledMenu;
    OrthographicCamera camera;
    TiledMapRenderer tiledMenuRenderer;
    Grill grill;
    Long startTime;

    public InfoScreen(final PiazzaPanicGame game, ArrayList<Recipe> orders, ArrayList<Ingredient> ingredients, int customerCounter, ArrayList<Ingredient> shoppingList, Music menuMusic, Grill grill, Long startTime){
        this.game = game;
        this.orders = orders;
        this.ingredients = ingredients;
        this.customerCounter = customerCounter;
        this.shoppingList = shoppingList;
        this.menuMusic = menuMusic;
        this.grill = grill;
        this.startTime = startTime;
    }

    public InfoScreen(final PiazzaPanicGame game, Map gameState){
        this.game = game;
        this.gameState = gameState;
        this.orders = gameState.orders;
        this.ingredients = gameState.pantryInventory;
    }

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

    @Override
    public void render(float delta){
        // Set black background anc clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        tiledMenuRenderer.setView(camera);
        tiledMenuRenderer.render();

        batch.begin();

        String orderList;
        orderList = "Orders:";
        for (int i=0; i < orders.size(); i++){
            orderList += "\n" + orders.get(i).getName();
        }

        String ingredientList;
        ingredientList = "Ingredients:";

        for (int i=0; i < ingredients.size(); i++){
            ingredientList += "\n" + ingredients.get(i).getName() + " " + ingredients.get(i).getCooked()+ " " + ingredients.get(i).getChopped();
        }
        /*TODO: Change scale of the text so it all fits on screen regardless of resolution
                maybe split ingredient list into two columns
         */
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("karmatic-arcade/ka1.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (Gdx.graphics.getHeight() / 38);
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        font.draw(batch, orderList, Math.round(Gdx.graphics.getWidth() * 0.11), Math.round(Gdx.graphics.getHeight() * 0.84));
        font.draw(batch, ingredientList, Math.round(Gdx.graphics.getWidth() * 0.575), Math.round(Gdx.graphics.getHeight() * 0.84));

        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }
    
    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ESCAPE){
            game.setScreen(gameState);
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
