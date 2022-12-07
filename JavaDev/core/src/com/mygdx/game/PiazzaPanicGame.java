package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class PiazzaPanicGame extends ApplicationAdapter{
	TiledMap tiledMap;
	OrthographicCamera camera;
	TiledMapRenderer tiledMapRenderer;
	Rectangle chefOne;
	Texture chefImage;
	private SpriteBatch batch;
	Rectangle lastClick;


	@Override
	public void create (){
		chefImage = new Texture(Gdx.files.internal("chef.png"));

		tiledMap = new TmxMapLoader().load("Tiled/map.tmx");
		MapProperties properties = tiledMap.getProperties();

		lastClick = new Rectangle();
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
	}

	@Override
	public void render () {
		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		//Raindrops need to be rendered.
		batch.begin();
		batch.draw(chefImage, chefOne.x, chefOne.y);
		batch.end();

		if (Gdx.input.isTouched()){
			onClick(Gdx.input.getX(), Gdx.input.getY());
		}
	}

	private void onClick(int x, int y) {

		if (x > chefOne.getX() && x < chefOne.getX() + chefOne.width){
			System.out.println("IG");
		}
		if (Gdx.graphics.getHeight() - y > chefOne.getY() && Gdx.graphics.getHeight() - y < chefOne.height + chefOne.getY()){
			System.out.println("Y");
		}

		System.out.println(Gdx.graphics.getHeight() - y);
		System.out.println(chefOne.getY());

	}

	@Override
	public void dispose () {
		tiledMap.dispose();
	}
}
