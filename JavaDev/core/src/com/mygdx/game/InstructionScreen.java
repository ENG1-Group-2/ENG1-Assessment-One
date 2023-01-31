package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Loads a jpeg which acts as the instruction - not an
 * interactable screen.
 */
public class InstructionScreen extends ScreenAdapter implements InputProcessor {
    final PiazzaPanicGame game;
    Map gameState;
    TiledMap tiledMenu;
    OrthographicCamera camera;
    TiledMapRenderer tiledMenuRenderer;
    SpriteBatch batch;
    Texture instructionsImage;

    /**
     * Constructor method.
     * @param game The instance of the PiazzaPanicGame.
     * @param gameState A map class that the user has just come from.
     */
    public InstructionScreen(final PiazzaPanicGame game, Map gameState) {
        this.game = game;
        this.gameState = gameState;
    }

    /**
     * Runs on creation of the class.
     */
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
        instructionsImage = new Texture(Gdx.files.internal("Instruction.png"));
        batch = new SpriteBatch();

    }

    @Override
    public void render(float delta) {
        // Set black background anc clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        tiledMenuRenderer.setView(camera);
        tiledMenuRenderer.render();
        batch.begin();
        batch.draw(instructionsImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
     * If escape, return back to the game. If backspace, close game.
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
