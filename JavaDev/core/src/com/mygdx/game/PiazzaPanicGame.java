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


public class PiazzaPanicGame extends ApplicationAdapter implements InputProcessor{
	TiledMap tiledMap;
	OrthographicCamera camera;
	TiledMapRenderer tiledMapRenderer;
	Rectangle chefOne;
	Rectangle chefTwo;
	Texture chefImage;
	SpriteBatch batch;
	Rectangle lastClick;
	Boolean lastClickObject = false;


	@Override
	public void create (){
		chefImage = new Texture(Gdx.files.internal("chef.png"));

		tiledMap = new TmxMapLoader().load("Tiled/map.tmx");
		MapProperties properties = tiledMap.getProperties();

		lastClick = new Rectangle();
		lastClickObject = false;
		batch = new SpriteBatch();

		camera = new OrthographicCamera();

		int mapWidth = properties.get("width", Integer.class);
		int mapHeight = properties.get("height", Integer.class);
		int tileSize = properties.get("tilewidth", Integer.class);

		camera.setToOrtho(false, mapWidth * tileSize, mapHeight * tileSize);
		camera.update();

		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

		//Position chef at staff room door.
		chefOne = new Rectangle();
		chefOne.x = 600;
		chefOne.y = 350;
		chefOne.width = 64;
		chefOne.height = 64;

		//Position chef at staff room door.
		chefTwo = new Rectangle();
		chefTwo.x = 600;
		chefTwo.y = 400;
		chefTwo.width = 64;
		chefTwo.height = 64;

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		//Raindrops need to be rendered.
		batch.begin();
		batch.draw(chefImage, chefOne.x, chefOne.y);
		batch.draw(chefImage, chefTwo.x, chefTwo.y);
		batch.end();
	}

	public void clickEvent (int x, int y){
		if (Gdx.input.isTouched()) {
			if (lastClickObject) {
				onClickMove(Gdx.input.getX(), Gdx.input.getY());
			} else {
				onClickObject(Gdx.input.getX(), Gdx.input.getY());
			}
		}
	}

	private void onClickObject(int x, int y) {
		ArrayList<Rectangle> sprites = new ArrayList<>();
		sprites.add(chefOne);
		sprites.add(chefTwo);
		for (Rectangle sprite: sprites){
			if (checkClickOnSprite(sprite, x, y)){
				lastClick = sprite;
				lastClickObject = true;
			}
		}
	}

	private void onClickMove(int x, int y){
		lastClick.x = x;
		lastClick.y = Gdx.graphics.getHeight() - y;
		lastClickObject = false;
	}

	private boolean checkClickOnSprite(Rectangle sprite, int x, int y){
		return x > sprite.getX() && x < sprite.getX() + sprite.getWidth()
				&& Gdx.graphics.getHeight() - y > sprite.getY() && Gdx.graphics.getHeight() - y < sprite.getHeight() + sprite.getY();
	}


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
