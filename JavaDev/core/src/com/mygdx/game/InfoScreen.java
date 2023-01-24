package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

/**
 * Screen that displays key information
 * Includes list of current orders and ingredients being held
 */
public class InfoScreen extends ScreenAdapter implements InputProcessor {
    final PiazzaPanicGame game;
    ArrayList<Ingredient> ingredients;
    ArrayList<Recipe> orders;
    ArrayList<Ingredient> shoppingList;
    Music menuMusic;
    BitmapFont font = new BitmapFont();
    SpriteBatch batch = new SpriteBatch();
    int customerCounter;

    /**
     * Gets and assigns variables needed for information screen
     * Also gets variables needing to be saved for Map instantiation
     *
     * @param game instance of game
     * @param orders list of active orders
     * @param ingredients list of ingredients in inventory
     * @param customerCounter number of customers left to arrive
     * @param shoppingList list of needed ingredients
     * @param menuMusic background music being played
     */
    public InfoScreen(final PiazzaPanicGame game, ArrayList<Recipe> orders, ArrayList<Ingredient> ingredients, int customerCounter, ArrayList<Ingredient> shoppingList, Music menuMusic){
        this.game = game;
        this.orders = orders;
        this.ingredients = ingredients;
        this.customerCounter = customerCounter;
        this.shoppingList = shoppingList;
        this.menuMusic = menuMusic;

    }

    /**
     * Creates initial information page
     */
    @Override
    public void show(){
        Gdx.input.setInputProcessor(this);
    }

    /**
     * Runs every frame to draw information to the screen
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta){
        // Set black background anc clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        String orderList;
        orderList = "Orders";
        for (int i=0; i < orders.size(); i++){
            orderList += "\n" + orders.get(i).getName();
        }

        String ingredientList;
        ingredientList = "Ingredients";

        for (int i=0; i < ingredients.size(); i++){
            ingredientList += "\n" + ingredients.get(i).getName() + " " + ingredients.get(i).getCooked() + " " + ingredients.get(i).getChopped();
        }
        /*TODO: Change scale of the text so it all fits on screen regardless of resolution
                maybe split ingredient list into two columns
         */
        font.getData().setScale(Math.round(Gdx.graphics.getHeight() / 300), Math.round(Gdx.graphics.getHeight() / 300));
        font.draw(batch, orderList, Math.round(Gdx.graphics.getWidth() * 0.1), Math.round(Gdx.graphics.getHeight() * 0.9));
        font.draw(batch, ingredientList, Math.round(Gdx.graphics.getWidth() * 0.6), Math.round(Gdx.graphics.getHeight() * 0.9));
        batch.end();


    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    /**
     * Checks if key is pressed, if escape is pressed return to Map screen
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return
     */
    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ESCAPE){
            game.setScreen(new Map(game, ingredients, orders, customerCounter, shoppingList, menuMusic));
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
