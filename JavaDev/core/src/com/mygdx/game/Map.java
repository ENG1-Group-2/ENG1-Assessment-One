package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

/**
 * Main game screen which enables the user to control the map.
 * Inputs are detected by implementing InputProcessor which explains empty functions.
 * Auto launches this screen in the current prototype.
 */
public class PiazzaPanicGame extends ApplicationAdapter implements InputProcessor{
	TiledMap tiledMap;
	OrthographicCamera camera;
	TiledMapRenderer tiledMapRenderer;
	Rectangle chefOne;
	Rectangle chefTwo;
	Texture chefImage;
	SpriteBatch batch;
	//Keep track on what object was clicked last to help with movement.
	Rectangle lastClick;
	//Keep track of whether a relevant object was clicked previously.
	Boolean lastClickObject;
	ArrayList<Rectangle> sprites = new ArrayList<>();

	/**
	 * Loads the map and sprites.
	 * Get the properties for the map.
	 * Creates the camera and sets the initial position of the chefs.
	 */
	@Override
	public void create (){
		chefImage = new Texture(Gdx.files.internal("chef.png"));
		tiledMap = new TmxMapLoader().load("Tiled/map.tmx");

		//Gets all properties from imported tiled map.
		MapProperties properties = tiledMap.getProperties();

		// Just initialises a new rectangle so code can be run without error.
		lastClick = new Rectangle();
		// Start game with nothing being clicked.
		lastClickObject = false;
		batch = new SpriteBatch();

		camera = new OrthographicCamera();

		int mapWidth = properties.get("width", Integer.class);
		int mapHeight = properties.get("height", Integer.class);
		int tileSize = properties.get("tilewidth", Integer.class);

		//Tiles are square so width and height will be the same.
		camera.setToOrtho(false, mapWidth * tileSize, mapHeight * tileSize);
		camera.update();

		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

		//Position chefs near staff door.
		chefOne = new Rectangle();
		chefOne.x = 600;
		chefOne.y = 350;
		chefOne.width = 64;
		chefOne.height = 64;

		chefTwo = new Rectangle();
		chefTwo.x = 600;
		chefTwo.y = 400;
		chefTwo.width = 64;
		chefTwo.height = 64;

		// Keep list of sprites to make checking clicks easier.
		sprites.add(chefOne);
		sprites.add(chefTwo);

		Gdx.input.setInputProcessor(this);
	}

	/**
	 * Constantly renders frame as this screen is ran
	 * by rendering camera and sprites.
	 */
	@Override
	public void render () {
		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		//Images needs to be render based upon changes.
		batch.begin();
		batch.draw(chefImage, chefOne.x, chefOne.y);
		batch.draw(chefImage, chefTwo.x, chefTwo.y);
		batch.end();
	}

	/**
	 * Decided what to do with the click input that has been occured
	 * and runs relevant function.
	 *
	 * @param x X position of the area that has been clicked.
	 * @param y Y position of the area that has been clicked.
	 */
	public void clickEvent (int x, int y){
		if (lastClickObject) {
			onClickMove(x, y);
		} else {
			onClickObject(x, y);
		}
	}

	/**
	 * Iterates through list of sprites that can be moved by the user
	 * and updates variable, so it can be moved to the position on the next
	 * click.
	 *
	 * @param x X position of the area that has been clicked.
	 * @param y Y position of the area that has been clicked.
	 */
	private void onClickObject(int x, int y) {
		for (Rectangle sprite: sprites){
			if (checkClickOnSprite(sprite, x, y)){
				lastClick = sprite;
				lastClickObject = true;
			}
		}
	}

	/**
	 * Moves the previously clicked sprite to the position
	 * clicked on now.
	 *
	 * @param x X position of the area that has been clicked.
	 * @param y Y position of the area that has been clicked.
	 */
	private void onClickMove(int x, int y){
		lastClick.x = x;
		lastClick.y = Gdx.graphics.getHeight() - y;
		lastClickObject = false;
	}

	/**
	 * Checks whether the users click is on any of the sprites.
	 *
	 * @param sprite The character which we are checking whether the user
	 *               has clicked.
	 * @param x X position of the area that has been clicked.
	 * @param y Y position of the area that has been clicked.
	 * @return [Boolean] Whether the user has clicked on the sprite or not.
	 */
	private boolean checkClickOnSprite(Rectangle sprite, int x, int y){
		// Y is inverted in LibGDX.
		return x > sprite.getX() && x < sprite.getX() + sprite.getWidth()
				&& Gdx.graphics.getHeight() - y > sprite.getY() && Gdx.graphics.getHeight() - y < sprite.getHeight() + sprite.getY();
	}

	/**
	 * Disposes all assets for cleaner exit.
	 */
	@Override
	public void dispose () {
		tiledMap.dispose();
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
		clickEvent(screenX, screenY);
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
