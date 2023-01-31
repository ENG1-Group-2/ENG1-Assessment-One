package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Base class for all operating systems.
 */
public class PiazzaPanicGame extends Game{
    /**
     * Runs MainMenu Screen on startup.
     */
    @Override
    public void create () {
        setScreen(new MainMenu(this));
    }
}
