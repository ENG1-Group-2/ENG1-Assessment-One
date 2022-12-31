package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class PiazzaPanicGame extends Game{
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    BitmapFont font;

    @Override
    public void create () {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();

        ArrayList<Ingredient> temp = new ArrayList<>();
        temp.add(new HotIngredient("BurgerPatty", true, 30));
        setScreen(new Grill(this, temp));
        //setScreen(new Map(this));
    }

    public void render(){
        super.render();
    }

    @Override
    public void dispose () {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
