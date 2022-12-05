package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PiazzaPanicGame extends ApplicationAdapter{
	TiledMap tiledMap;
	PerspectiveCamera camera;
	TiledMapRenderer tiledMapRenderer;
	FitViewport viewport;


	@Override
	public void create () {
		tiledMap = new TmxMapLoader().load("Tiled/map.tmx");
		MapProperties properties = tiledMap.getProperties();

		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();


		camera = new PerspectiveCamera();
		viewport = new FitViewport(width, height, camera);


		camera.update();

		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
	}

	@Override
	public void render () {
		camera.update();
		tiledMapRenderer.render();
	}

	@Override
	public void dispose () {
		tiledMap.dispose();
	}

	public void resize(int width, int height) {
		viewport.update(width, height);
	}
}
