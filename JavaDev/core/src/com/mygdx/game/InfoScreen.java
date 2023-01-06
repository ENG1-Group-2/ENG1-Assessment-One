package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class InfoScreen extends ScreenAdapter implements InputProcessor {
    final PiazzaPanicGame game;
    ArrayList<Recipe> ingredients;
    ArrayList<Recipe> orders;
    BitmapFont font = new BitmapFont();
    SpriteBatch batch = new SpriteBatch();

    public InfoScreen(final PiazzaPanicGame game, ArrayList<Recipe> orders, ArrayList<Ingredient> ingredients){
        this.game = game;
        this.orders = orders;
        this.ingredients = orders;}

    @Override
    public void render(float delta){
        // Set black background anc clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        String output;
        output = "Orders";
        for (int i=0; i > orders.size(); i++){
            output += "\n" + orders.get(i).getName();
        }
        output += "\nIngredients";
        for (int i=0; i > ingredients.size(); i++){
            output += "\n" + ingredients.get(i).getName();
        }

        font.draw(batch, output, 200, 200);

        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ESCAPE){
            game.setScreen(new Map(game));
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
