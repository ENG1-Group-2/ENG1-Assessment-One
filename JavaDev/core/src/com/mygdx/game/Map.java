package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Main game screen which enables the user to control the map.
 * Inputs are detected by implementing InputProcessor which explains empty functions.
 * Auto launches this screen in the current prototype.
 */
public class Map extends ScreenAdapter implements InputProcessor{
	ArrayList<Ingredient> pantryInventory;
	TiledMap tiledMap;
	OrthographicCamera camera;
	TiledMapRenderer tiledMapRenderer;
	Rectangle chefOne;
	Rectangle chefTwo;
	Rectangle customerOne;
	Rectangle customerTwo;
	Texture chefImage;
	Texture customerOneImage;
	Texture customerTwoImage;
	SpriteBatch batch;
	//Keep track on what object was clicked last to help with movement.
	Rectangle lastClick;
	//Keep track of whether a relevant object was clicked previously.
	Boolean lastClickObject;
	ArrayList<Rectangle> sprites = new ArrayList<>();
	MapObjects objects;
	Rectangle ingredientsStation;
	Rectangle grill;
	int customerCounter = 5;
	boolean drawCustomerOne;
	boolean drawCustomerTwo;
	long lastRender;
	Recipe hamAndPineapplePizza;
	Recipe chickenSalad;
	Recipe tunaJacketPotato;
	Recipe beefBurger;
	ArrayList<Recipe> recipes;
	ArrayList<Recipe> orders;
	ArrayList<Ingredient> inventory = new ArrayList<>();
	int screenWidth = Gdx.graphics.getWidth();
	int screenHeight = Gdx.graphics.getHeight();
	boolean chefMove = false;
	int keyCode;

	final PiazzaPanicGame game;

	public Map(final PiazzaPanicGame game) {
		this.game = game;
		pantryInventory = new ArrayList<Ingredient>();
		orders = new ArrayList<Recipe>();
	}

	public Map(final PiazzaPanicGame game, ArrayList<Ingredient> pantryInventoryPrev, ArrayList<Recipe> ordersPrev){
			this.game = game;
			pantryInventory = new ArrayList<Ingredient>();
			for (int i=0; i < pantryInventoryPrev.size(); i++){
				pantryInventory.add(pantryInventoryPrev.get(i).copy());
			}
			orders = new ArrayList<Recipe>();
			for (int i=0; i < ordersPrev.size(); i++){
				orders.add(ordersPrev.get(i).copy());
			}
	}

	/**
	 * Loads the map and sprites.
	 * Get the properties for the map.
	 * Creates the camera and sets the initial position of the chefs.
	 */

	@Override
	public void show() {
		chefImage = new Texture(Gdx.files.internal("chef.png"));
		tiledMap = new TmxMapLoader().load("Tiled/map.tmx");
		customerOneImage = new Texture(Gdx.files.internal("person001.png"));
		customerTwoImage = new Texture(Gdx.files.internal("person002.png"));

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
		chefOne.x = Math.round(screenWidth * 0.92);
		chefOne.y = Math.round(screenHeight * 0.73);
		chefOne.width = Math.round(screenWidth * 0.1);
		chefOne.height = Math.round(screenWidth * 0.1);

		chefTwo = new Rectangle();
		chefTwo.x = Math.round(screenWidth * 0.92);
		chefTwo.y = Math.round(screenHeight * 0.83);
		chefTwo.width = Math.round(screenWidth * 0.1);
		chefTwo.height = Math.round(screenWidth * 0.1);

		// Get properties for ingredients stations rectangle stored in the tiled map.
		objects = tiledMap.getLayers().get(0).getObjects();
		MapObject pantryAccess = objects.get("PantryAccess");
		if (pantryAccess instanceof RectangleMapObject) {
			RectangleMapObject pantryRectangle = (RectangleMapObject) pantryAccess;
			this.ingredientsStation = scaleObject(pantryRectangle.getRectangle(), tileSize * mapWidth, tileSize * mapHeight);
		}
		else{
			ingredientsStation = new Rectangle();}

		MapObject grillObject = objects.get("Grill");
		if (grillObject instanceof RectangleMapObject) {
			RectangleMapObject grillRectangle = (RectangleMapObject) grillObject;
			this.grill = scaleObject(grillRectangle.getRectangle(), tileSize * mapWidth, tileSize * mapHeight);
		}
		else{
			grill = new Rectangle();}

		// Keep list of sprites to make checking clicks easier.
		sprites.add(chefOne);
		sprites.add(chefTwo);

		Gdx.input.setInputProcessor(this);

		// Last time a customer has appeared.
		lastRender = System.currentTimeMillis();

		// Reset the customer to the base location.
		resetCustomerOne();
		resetCustomerTwo();
		drawCustomerOne = false;
		drawCustomerTwo = false;

		// Creating pantry and recipes using class defined in package.
		createRecipes();
	}

	private Rectangle scaleObject(Rectangle object, int mapWidth, int mapHeight) {
		object.setX(Math.round((object.getX() / mapWidth) * screenWidth));
		object.setY(Math.round(((mapHeight - object.getY()) / mapHeight) * screenHeight));
		object.setWidth(Math.round((object.getWidth() / mapWidth) * screenWidth));
		object.setHeight(Math.round((object.getHeight() / mapHeight) * screenHeight));
		return object;
	}

	private void createRecipes() {
		// Store all recipes in an array list to randomly choose an index.
		recipes = new ArrayList<>(4);

		hamAndPineapplePizza = new Recipe("Ham and Pineapple Pizza", 3);
		recipes.add(hamAndPineapplePizza);

		tunaJacketPotato = new Recipe("Tuna Jacket Potato", 2);
		recipes.add(tunaJacketPotato);

		beefBurger = new Recipe("Beef Burger", 2);
		recipes.add(beefBurger);

		chickenSalad = new Recipe("Chicken Salad", 2);
		recipes.add(chickenSalad);
	}

	private void resetCustomerOne(){
		customerOne = new Rectangle();
		customerOne.x = 25;
		customerOne.y = 25;
		customerOne.width = 100;
		customerOne.height = 100;
	}

	private void resetCustomerTwo(){
		customerTwo = new Rectangle();
		customerTwo.x = 600;
		customerTwo.y = 25;
		customerTwo.width = 100;
		customerTwo.height = 100;
	}

	/**
	 * Constantly renders frame as this screen is ran
	 * by rendering camera and sprites.
	 */
	@Override
	public void render(float delta) {
     	// Set black background and clear screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		if ((screenWidth != Gdx.graphics.getWidth()) || (screenHeight != Gdx.graphics.getHeight())) {
			screenWidth = Gdx.graphics.getWidth();
			screenHeight = Gdx.graphics.getHeight();
		}

		if (chefMove) {
			characterMovement(keyCode);
		}

		//Images needs to be render based upon changes.
		batch.begin();
		batch.draw(chefImage, chefOne.x, chefOne.y, Math.round(screenWidth / 20), Math.round(screenHeight / 20));
		batch.draw(chefImage, chefTwo.x, chefTwo.y, Math.round(screenWidth / 20), Math.round(screenHeight / 20));

		if (customerCounter != 0) {
			if (customerCounter != 0) {
				//If it has been 20 seconds since a customer appeared.
				if (System.currentTimeMillis() - lastRender > 20000) {
					Random random = new Random();
					// Randomly choose between customer one or two.
					if (random.nextBoolean()) {
						drawCustomerOne = true;
					} else {
						drawCustomerTwo = true;
					}
					lastRender = System.currentTimeMillis();
				}

				// Keep on moving customers along per frame.
				if (drawCustomerOne) {
					// If it at the position it needs to be in.
					if (customerOne.x > 240) {
						drawCustomerOne = false;
						randomOrderGeneration();
						resetCustomerOne();

					}
					customerOne.x += 50 * Gdx.graphics.getDeltaTime();
					batch.draw(customerOneImage, customerOne.x, customerOne.y);

				}

				if (drawCustomerTwo) {
					if (customerTwo.x < 340) {
						drawCustomerTwo = false;
						resetCustomerTwo();
						randomOrderGeneration();
					}
					customerTwo.x -= 50 * Gdx.graphics.getDeltaTime();
					batch.draw(customerTwoImage, customerTwo.x, customerTwo.y);
				}
			}
		}
		batch.end();
	}

	private void randomOrderGeneration() {
		// Keep track of how many customers have appeared.
		customerCounter -= 1;

		Random random = new Random();
		int index = random.nextInt(recipes.size());
		orders.add(recipes.get(index));
		// System.out.println(orders);
	}

	/**
	 * Decided what to do with the click input that has been occurred
	 * and runs relevant function.
	 *
	 * @param x X position of the area that has been clicked.
	 * @param y Y position of the area that has been clicked.
	 */
	public void clickEvent (int x, int y){
		/*if (lastClickObject) {
			onClickMove(x, y);
		} else {
			onClickObject(x, y);
		}*/
		onClickObject(x, y);
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
			if (rectangleDetection(sprite, x, y)){
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
/*	private void onClickMove(int x, int y){
		lastClick.x = x;
		lastClick.y = Gdx.graphics.getHeight() - y;
		lastClickObject = false;
	}*/

	/**
	 * Checks whether the users click is on any of the sprites.
	 *
	 * @param sprite The character which we are checking whether the user
	 *               has clicked.
	 * @param x X position of the area that has been clicked.
	 * @param y Y position of the area that has been clicked.
	 * @return [Boolean] Whether the user has clicked on the sprite or not.
	 */
	private boolean rectangleDetection(Rectangle sprite, int x, int y){
		// Y is inverted in LibGDX.
		return x > sprite.getX() && x < sprite.getX() + sprite.getWidth()
				&& Gdx.graphics.getHeight() - y > sprite.getY() && Gdx.graphics.getHeight() - y < sprite.getHeight() + sprite.getY();
	}

	/**
	 * Disposes all assets for cleaner exit.
	 */
	@Override
	public void dispose() {
		tiledMap.dispose();
		chefImage.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.H){
			game.setScreen(new InfoScreen(game, orders, pantryInventory));
		}
		if (lastClickObject == true) {
			/*return false;
		} else {*/
			chefMove = true;
			keyCode = keycode;
		}
			return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if ((keycode == 51) || (keycode == 29) || (keycode == 47) || (keycode == 32)) {
			chefMove = false;
		}
		return false;
	}

	private void characterMovement(int keycode) {
		int pixelPerFrame = Math.round((Gdx.graphics.getWidth() / 3) * Gdx.graphics.getDeltaTime());
		if (keycode == 51){
			lastClick.y += pixelPerFrame;
		}
		else if (keycode == 29){
			lastClick.x -= pixelPerFrame;
		}
		else if (keycode == 47){
			lastClick.y -= pixelPerFrame;
		}
		else if (keycode == 32){
			lastClick.x += pixelPerFrame;
		}
		if (lastClick.overlaps(ingredientsStation)){
			if (pantryInventory == null){
				game.setScreen(new PantrySelection(game, new ArrayList<Ingredient>(), orders));
			}
			else{
				game.setScreen(new PantrySelection(game, pantryInventory, orders));
			}
			dispose();
		}
		if (lastClick.overlaps(grill)){
			System.out.println("DETECTED");
			game.setScreen(new Grill(game, inventory));
			dispose();
		}
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