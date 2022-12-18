package com.mygdx.game;

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
	int customerCounter = 5;
	boolean drawCustomerOne;
	boolean drawCustomerTwo;
	long lastRender;
	Ingredient onion;
	Ingredient pepper;
	Ingredient lettuce;
	Ingredient cookedChicken;
	Ingredient saladDressing;
	Ingredient pineapple;
	Ingredient pizzaSauce;
	Ingredient pizzaBase;
	Ingredient burgerPatty;
	Ingredient tuna;
	Ingredient mayo;
	Ingredient jacketPotato;
	Ingredient breadBuns;
	Ingredient ham;
	Recipe hamAndPineapplePizza;
	Recipe chickenSalad;
	Recipe tunaJacketPotato;
	Recipe beefBurger;
	ArrayList<Recipe> recipes;

	private PiazzaPanicGame piazzaPanicGame;

	public Map(PiazzaPanicGame piazzaPanicGame) {
		piazzaPanicGame = piazzaPanicGame;
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

		// Get properties for ingredients stations rectangle stored in the tiled map.
		objects = tiledMap.getLayers().get(0).getObjects();
		MapObject pantryAccess = objects.get("PantryAccess");
		if (pantryAccess instanceof RectangleMapObject) {
			RectangleMapObject pantryRectangle = (RectangleMapObject) pantryAccess;
			this.ingredientsStation = pantryRectangle.getRectangle();}
		else{
			ingredientsStation = new Rectangle();}

		Gdx.input.setInputProcessor(this);

		// Last time a customer has appeared.
		lastRender = System.currentTimeMillis();

		// Reset the customer to the base location.
		resetCustomerOne();
		resetCustomerTwo();
		drawCustomerOne = false;
		drawCustomerTwo = false;

		// Creating pantry and recipes using class defined in package.
		createPantryItem();
		createRecipes();
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

	private void createPantryItem() {
		onion = new Ingredient("Onion", false);
		pepper = new Ingredient("Peppers", false);
		lettuce = new Ingredient("Lettuce", false);
		cookedChicken = new PreChoppedIngredient("Chicken");
		saladDressing = new PreChoppedIngredient("Salad Dressing");
		pineapple = new Ingredient("Pineapple", false);
		pizzaSauce = new PreChoppedIngredient("Pizza Sauce");
		pizzaBase = new HotIngredient("Base", true, 5);
		burgerPatty = new HotIngredient("BurgerPatty", true, 5);
		mayo = new PreChoppedIngredient("Mayo");
		tuna = new PreChoppedIngredient("Tuna");
		jacketPotato = new HotIngredient("JacketPotato", false, 5);
		breadBuns = new HotIngredient("BreadBuns", false, 5);
		ham = new PreChoppedIngredient("Ham");
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
		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		//Images needs to be render based upon changes.
		batch.begin();
		batch.draw(chefImage, chefOne.x, chefOne.y);
		batch.draw(chefImage, chefTwo.x, chefTwo.y);

		//If it has been 20 seconds since a customer appeared.
		if (System.currentTimeMillis() - lastRender > 20000) {
			Random random = new Random();
			// Randomly choose between customer one or two.
			if (random.nextBoolean()){
				drawCustomerOne = true;
			}
			else {
				drawCustomerTwo = true;
			}
			lastRender = System.currentTimeMillis();
		}

		// Keep on moving customers along per frame.
		if (drawCustomerOne){
			// If it at the position it needs to be in.
			if (customerOne.x > 240){
				drawCustomerOne = false;
				randomOrderGeneration();
				resetCustomerOne();

			}
			customerOne.x += 50 * Gdx.graphics.getDeltaTime();
			batch.draw(customerOneImage, customerOne.x, customerOne.y);

		}

		if (drawCustomerTwo){
			if (customerTwo.x < 340){
				drawCustomerTwo = false;
				resetCustomerTwo();
				randomOrderGeneration();
			}
			customerTwo.x -= 50 * Gdx.graphics.getDeltaTime();
			batch.draw(customerTwoImage, customerTwo.x, customerTwo.y);
		}

		batch.end();
	}

	private void randomOrderGeneration() {
		// Keep track of how many customers have appeared.
		customerCounter -= 1;

		// TODO: Add this to some form of data structure - currently just prints out.
		Random random = new Random();
		int index = random.nextInt(recipes.size());
		System.out.println(recipes.get(index).getName());
	}

	/**
	 * Decided what to do with the click input that has been occurred
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
	private boolean rectangleDetection(Rectangle sprite, int x, int y){
		// Y is inverted in LibGDX.
		return x > sprite.getX() && x < sprite.getX() + sprite.getWidth()
				&& Gdx.graphics.getHeight() - y > sprite.getY() && Gdx.graphics.getHeight() - y < sprite.getHeight() + sprite.getY();
	}

	private boolean rectangleDetection(Rectangle sprite, float x, float y){
		// Y is inverted in LibGDX.
		return x > sprite.getX() && x < sprite.getX() + sprite.getWidth()
				&& Gdx.graphics.getHeight() - y > sprite.getY() && Gdx.graphics.getHeight() - y < sprite.getHeight() + sprite.getY();
	}

	private boolean enterAreaCheck(Rectangle area, Rectangle sprite){
		// TODO: Get pantry detection working.
		// area.getY() - area.getHeight() < sprite.getY()
		// area.getX() - area.getWidth() > sprite.getX()
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
		if (lastClickObject == true) {
			return false;
		} else {
			charactorMovement(keycode);
			return true;
		}
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	private void charactorMovement(int keycode) {
		if (enterAreaCheck(ingredientsStation, lastClick)){
			// TODO: Change screen to ingredients screen.
		}
		if (keycode == 51){
			lastClick.y += 400 * Gdx.graphics.getDeltaTime();
		}
		else if (keycode == 29){
			lastClick.x -= 400 * Gdx.graphics.getDeltaTime();
		}
		else if (keycode == 47){
			lastClick.y -= 400 * Gdx.graphics.getDeltaTime();
		}
		else if (keycode == 32){
			lastClick.x += 400 * Gdx.graphics.getDeltaTime();
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
