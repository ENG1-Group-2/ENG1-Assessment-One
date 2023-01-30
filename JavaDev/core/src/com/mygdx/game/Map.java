package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.lwjgl.Sys;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

/**
 * Main game screen which enables the user to control the map.
 * Inputs are detected by implementing InputProcessor which explains empty functions.
 * Auto launches this screen in the current prototype.
 */
public class Map extends ScreenAdapter implements InputProcessor{
	// Stores any items that have been collected.
	ArrayList<Ingredient> pantryInventory;
	// Stores any items that are yet to be collected.
	ArrayList<Ingredient> shoppingList;
	// Put all sprites in this list.
	ArrayList<Rectangle> sprites = new ArrayList<>();
	//Keep track of whether a relevant object was clicked previously.
	Boolean lastClickObject;
	// Default for timed mode.
	// TODO: Change variable if in endless mode.
	int customerCounter = 5;
	boolean drawCustomerTwo, drawCustomerOne;
	// Time of the last customer appearance.
	long lastRender;
	// TODO: Define more recipes.
	Recipe chickenSalad, beefBurger;
	// TODO: Add ingredients for new recipes.
	Ingredient saladDressing, burgerPatty, breadBuns, lettuce, pepper, cookedChicken;
	ArrayList<Recipe> recipes;
	ArrayList<Recipe> orders;
	int finishedOrders = customerCounter;

	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;
	OrthographicCamera camera;
	SpriteBatch batch;

	// Placements of customers placing orders.
	Rectangle customerOne, customerTwo;
	// All stations that can be interacted with.
	Rectangle chiller, grill, choppingStation, assemblyStation, ingredientsForSalad;
	Texture chefImage, customerOneImage, customerTwoImage, staffImage;
	//Keep track on what object was clicked last to help with movement.
	Rectangle lastClick;
	int screenWidth = Gdx.graphics.getWidth();
	int screenHeight = Gdx.graphics.getHeight();
	boolean chefMove = false;
	// The last key pressed.
	int keyCode;
	int mapWidth, mapHeight, tileSize;
    boolean orderAnimation = false;

	final PiazzaPanicGame game;
    Music menuMusic;
    Iterator<Ingredient> iterator;
    Boolean choppingStaff = false;
    Long choppingStationTime = 0L;
    Grill burgerGrill;
    Rectangle hob1, hob2;
    Texture burgerCookImagePost, burgerCookImagePre;
	Texture knifeImage;
    Long startTime = 0L;
    BitmapFont font;
    String message;
    Long lastMessage;
	Rectangle chefOne = new Rectangle(Math.round(screenWidth * 0.92), Math.round(screenHeight * 0.60), Math.round(screenWidth * 0.1), Math.round(screenWidth * 0.1) );
	Rectangle chefTwo = new Rectangle(Math.round(screenWidth * 0.92), Math.round(screenHeight * 0.70), Math.round(screenWidth * 0.1), Math.round(screenWidth * 0.1));
    Rectangle staffDeliver = new Rectangle(Math.round(screenWidth * 0.45), Math.round(screenHeight * 0.45), Math.round(screenWidth * 0.1), Math.round(screenWidth * 0.1));


    /**
	 * Creates new game map
	 *
	 * @param game instance of the game
	 */
    public Map(final PiazzaPanicGame game) {
		this.game = game;
		pantryInventory = new ArrayList<>();
		orders = new ArrayList<>();
        shoppingList = new ArrayList<>();
        // TODO: Add any new grill stations.
        burgerGrill = new Grill(grill, 2);
        startTime = System.currentTimeMillis();
	}

	/**
	 * Loads given rectangle from the Tiled map.
	 *
	 * @param objectName Name property of the object layer from tile map.
	 * @param objectLayer The layer of the map which the rectangle is located in.
	 * @return Rectangle from the tiled map.
	 */
	public Rectangle loadRectangle(String objectName, int objectLayer) {
		// Get properties for ingredients stations rectangle stored in the tiled map.
		MapObjects objects = tiledMap.getLayers().get(objectLayer).getObjects();
		MapObject object = objects.get(objectName);
		if (object instanceof RectangleMapObject) {
			RectangleMapObject grillRectangle = (RectangleMapObject) object;
			return scaleObject(grillRectangle.getRectangle(), tileSize * mapWidth, tileSize * mapHeight);
		} else {
			throw new IllegalArgumentException("This object does not exist in the object layer");
		}
	}

	/**
	 * Loads the map and sprites.
	 * Get the properties for the map.
	 * Creates the camera and sets the initial position of the chefs.
	 */
	@Override
	public void show() {
        message = "";
        lastMessage = 0L;

        // TODO: If you don't like the font, find a ttf and change here for it to be consistent throughout.
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("karmatic-arcade/ka1.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = (Gdx.graphics.getHeight() / 38);
        parameter.color = Color.BLUE;
        font = generator.generateFont(parameter);

        hob1 = new Rectangle((float) (screenWidth * 0.65), (float) (screenHeight * 0.85), 5,5 );
		hob2 = new Rectangle((float) (screenWidth * 0.6), (float) (screenHeight * 0.85), 5,5 );

        // Plays music if it is not playing yet.
		if (menuMusic == null || !menuMusic.isPlaying()) {
			menuMusic = Gdx.audio.newMusic(Gdx.files.internal("background.wav"));
			menuMusic.setLooping(true);
			menuMusic.play();
		}

        // TODO: Load any new images related to assessment two.
		chefImage = new Texture(Gdx.files.internal("chef.png"));
		tiledMap = new TmxMapLoader().load("Tiled/map.tmx");
		customerOneImage = new Texture(Gdx.files.internal("person001.png"));
		customerTwoImage = new Texture(Gdx.files.internal("person002.png"));
        staffImage = new Texture(Gdx.files.internal("chef2.png"));
        burgerCookImagePre = new Texture(Gdx.files.internal("raw_burger.png"));
        burgerCookImagePost = new Texture(Gdx.files.internal("cooked_burger.png"));
		knifeImage = new Texture(Gdx.files.internal("knives.png"));

		//Gets all properties from imported tiled map.
		MapProperties properties = tiledMap.getProperties();

		// Just initialises a new rectangle so code can be run without error.
		lastClick = new Rectangle();
		// Start game with nothing being clicked.
		lastClickObject = false;
		batch = new SpriteBatch();

		camera = new OrthographicCamera();

		mapWidth = properties.get("width", Integer.class);
		mapHeight = properties.get("height", Integer.class);
		tileSize = properties.get("tilewidth", Integer.class);

		//Tiles are square so width and height will be the same.
		camera.setToOrtho(false, mapWidth * tileSize, mapHeight * tileSize);
		camera.update();

		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        //TODO: Load any new hitboxes for new stations.
		ingredientsForSalad = loadRectangle("IngredientsForSalad", 0);
		choppingStation = loadRectangle("ChoppingStation", 0);
		assemblyStation = loadRectangle("Assembly", 0);
		chiller = loadRectangle("Chiller", 0);
		grill = loadRectangle("Grill", 0);


		createIngredients();

        // TODO: Add chef to sprite list.
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

	/**
	 * Creates a base instance of every ingredient being used
	 */
	public void createIngredients(){
        // TODO: Load new ingredients for new recipes.
        // Ingredients created in the format: name, chopped, cooked, flipped.
		lettuce = new Ingredient("Lettuce", false, true, true);
		pepper = new Ingredient("Peppers", false, true, true);
		cookedChicken = new Ingredient("Chicken", true, true, true);
		saladDressing = new Ingredient("SaladDressing", true, true, true);
		burgerPatty = new HotIngredient("BurgerPatty", true, 15, false);
		breadBuns = new HotIngredient("Bun", true, 5, true);
	}

	/**
	 * Adjusts the (X,Y) coordinates, and dimensions of a Rectangle object
	 *
	 * @param object Rectangle object in need of scaling
	 * @param mapWidth number of pixels wide the Tiled map is
	 * @param mapHeight number of pixels tall the Tiled map is
	 * @return
	 */
	public Rectangle scaleObject(Rectangle object, int mapWidth, int mapHeight) {
		object.setX(Math.round((object.getX() / mapWidth) * screenWidth));
		object.setY(Math.round(((mapHeight - object.getY() - object.getHeight()) / mapHeight) * screenHeight));
		object.setWidth(Math.round((object.getWidth() / mapWidth) * screenWidth));
		object.setHeight(Math.round((object.getHeight() / mapHeight) * screenHeight));
		return object;
	}

	/**
	 * Creates a base instance of every recipe being used
	 */
	private void createRecipes() {
		// Store all recipes in an array list to randomly choose an index.
		recipes = new ArrayList<>(4);

		//TODO: NEW RECIPE
		//hamAndPineapplePizza = new Recipe("Ham and Pineapple Pizza", 3);
		//recipes.add(hamAndPineapplePizza);

		//TODO: NEW RECIPE
		//tunaJacketPotato = new Recipe("Tuna Jacket Potato", 2);
		//recipes.add(tunaJacketPotato);

		beefBurger = new Recipe("Beef Burger",
                new ArrayList<>(Arrays.asList(breadBuns.copy(), burgerPatty.copy(), lettuce.copy())));
		recipes.add(beefBurger);

		chickenSalad = new Recipe("Chicken Salad",
                new ArrayList<>(Arrays.asList(saladDressing.copy(), cookedChicken.copy(), lettuce.copy(), pepper.copy())));
		recipes.add(chickenSalad);
	}

	/**
	 * Returns first customer object to original location
	 */
	private void resetCustomerOne(){
		customerOne = new Rectangle();
		customerOne.x = Math.round(screenWidth * 0.04);
		customerOne.y = Math.round(screenWidth * 0.03);
		customerOne.width = Math.round(screenWidth / 20);
		customerOne.height = Math.round(screenHeight / 20);
	}

	/**
	 * Returns second customer object to original location
	 */
	private void resetCustomerTwo(){
		customerTwo = new Rectangle();
		customerTwo.x = Math.round(screenWidth * 0.94);
		customerTwo.y = Math.round(screenWidth * 0.03);
		customerTwo.width = Math.round(screenWidth / 20);
		customerTwo.height = Math.round(screenHeight / 20);
	}

	/**
	 * Runs every frame (1/60th of a second) to draw images to screen
	 * Calls any functions that need constant checks
	 */
	@Override
	public void render(float delta) {
     	// Set black background and clear screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

        // Update screen size if it has changed.
		if ((screenWidth != Gdx.graphics.getWidth()) || (screenHeight != Gdx.graphics.getHeight())) {
			screenWidth = Gdx.graphics.getWidth();
			screenHeight = Gdx.graphics.getHeight();
		}

        // If key is down, move chef using WASD input.
		if (chefMove) {
			characterMovement(keyCode);
		}

		//Images needs to be render based upon changes.
		batch.begin();
		batch.draw(chefImage, chefOne.x, chefOne.y, Math.round(screenWidth / 20), Math.round(screenHeight / 20));
		batch.draw(chefImage, chefTwo.x, chefTwo.y, Math.round(screenWidth / 20), Math.round(screenHeight / 20));

        /* If any of the grills contain burger patties, for 2 seconds show it as uncooked and for the rest show it as
        cooked. Changes from pink to brown.
         */
        ArrayList<Ingredient> hobs = burgerGrill.getItems();
        if (hobs.get(0).getName() == "BurgerPatty") {
            Ingredient item = hobs.get(0);
            if (System.currentTimeMillis() - item.getCookingStartTime() > 2000 && item.getCookingStartTime() != 0) {
                batch.draw(burgerCookImagePost, hob1.x, hob1.y, Math.round(screenWidth / 20), Math.round(screenHeight / 20));
            } else if (System.currentTimeMillis() - item.getCookingStartTime() < 2000 && item.getCookingStartTime() != 0) {
                batch.draw(burgerCookImagePre, hob1.x, hob1.y, Math.round(screenWidth / 20), Math.round(screenHeight / 20));
            }
        }

        if (hobs.get(1).getName() == "BurgerPatty") {
            Ingredient item = hobs.get(1);
            if (System.currentTimeMillis() - item.getCookingStartTime() > 2000 && item.getCookingStartTime() != 0) {
                batch.draw(burgerCookImagePost, hob2.x, hob2.y, Math.round(screenWidth / 20), Math.round(screenHeight / 20));
            } else if (System.currentTimeMillis() - item.getCookingStartTime() < 2000 && item.getCookingStartTime() != 0) {
                batch.draw(burgerCookImagePre, hob2.x, hob2.y, Math.round(screenWidth / 20), Math.round(screenHeight / 20));
            }
        }

        // If any ingredients need chopping display the knife.
		for (Ingredient ingredient: pantryInventory){
			if (ingredient.getChopped() == false){
				batch.draw(knifeImage, Math.round(screenWidth * 0.43), Math.round(screenHeight * 0.83), Math.round(screenWidth / 20), Math.round(screenHeight / 20));
				break;
			}
		}

        // If an order has just been done, a chef appears and takes it towards to exit to indicate the order being
        // given out.
        if (orderAnimation){
            batch.draw(chefImage, staffDeliver.x, staffDeliver.y, Math.round(screenWidth / 20), Math.round(screenHeight / 20));
            int pixelPerFrame = Math.round((Gdx.graphics.getWidth() / 10) * Gdx.graphics.getDeltaTime());
            staffDeliver.y -= pixelPerFrame;
            if (staffDeliver.y < 0.2 * screenHeight){
                orderAnimation = false;
            }

        }

        /* If the maximum number of customer allowed in this game has not been reached.

        And it has been 5 seconds since the last time a customer started walking. Choose a customer to appear
        either left or right. Set corresponding boolean value to true, so it moves at every frame, until it
        gets near the centre where it disappeared. Once complete, the corrosponding boolean value returns to
        false.
         */
		if (customerCounter != 0) {
			int pixelPerFrame = Math.round((Gdx.graphics.getWidth() / 10) * Gdx.graphics.getDeltaTime());
				//If it has been 20 seconds since a customer appeared.
				if (System.currentTimeMillis() - lastRender > 500) {
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
					if (customerOne.x > screenWidth * 0.42) {
						drawCustomerOne = false;
						randomOrderGeneration();
						resetCustomerOne();

					}
					customerOne.x += pixelPerFrame;
					batch.draw(customerOneImage, customerOne.x, customerOne.y, Math.round(screenWidth / 40), Math.round(screenWidth / 40));

				}

				if (drawCustomerTwo) {
					if (customerTwo.x < screenWidth * 0.531) {
						drawCustomerTwo = false;
						resetCustomerTwo();
						randomOrderGeneration();
					}
					customerTwo.x -= pixelPerFrame;
					batch.draw(customerTwoImage, customerTwo.x, customerTwo.y, Math.round(screenWidth / 40), Math.round(screenWidth / 40));
			}
		}

        String grillInfo = burgerGrill.displayGrillInfo();
		font.draw(batch, grillInfo, (float) (screenWidth * 0.1), (float) (screenHeight * 0.1));

        if (rectangleDetection(grill, chefOne.getX(), chefOne.getY()) ||
                rectangleDetection(grill, chefTwo.getX(), chefTwo.getY())) {
            Ingredient temp = burgerGrill.hasGrillEnded();
            // Indicates that the cook has failed so he will need to be able to collect it again.
            if (temp != null){
                shoppingList.add(temp);
            }
		}

		font.draw(batch, message, Math.round(screenWidth * 0.47), Math.round(screenHeight * 0.625));

		batch.end();
	}

	/**
	 * Randomly chooses a recipe and adds it to the order list
	 */
	private void randomOrderGeneration() {
		// Keep track of how many customers have appeared.
		customerCounter -= 1;

		Random random = new Random();
		int index = random.nextInt(recipes.size());
		Recipe newOrder = recipes.get(index).copy();
		orders.add(newOrder);

        shoppingList.addAll(newOrder.getIngredients());
	}

	/**
	 * Decided what to do with the click input that has been occurred
	 * and runs relevant function.
	 *
	 * @param x X position of the area that has been clicked.
	 * @param y Y position of the area that has been clicked.
	 */
	public void clickEvent (int x, int y){
		if (rectangleDetection(grill, x, Gdx.graphics.getHeight() - y) &&
                ((rectangleDetection(grill, chefOne.getX(), chefOne.getY())) ||
                (rectangleDetection(grill, chefTwo.getX(), chefTwo.getY())))){
			message = burgerGrill.flipItems();
		}
		if (rectangleDetection(choppingStation, x, Gdx.graphics.getHeight() - y) &&
				((rectangleDetection(choppingStation, chefOne.getX(), chefOne.getY())) ||
						(rectangleDetection(choppingStation, chefTwo.getX(), chefTwo.getY())))){
			for (Ingredient ingredient: pantryInventory){
				if (ingredient.getChopped() == false){
					ingredient.chopIngredient();
					message = String.format("%s Chopped!", ingredient.getName());
					break;
				}
			}
		}
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
	 * Checks whether the users click is on any of the sprites.
	 *
	 * @param sprite The character which we are checking whether the user
	 *               has clicked.
	 * @param x X position of the area that has been clicked.
	 * @param y Y position of the area that has been clicked.
	 * @return [Boolean] Whether the user has clicked on the sprite or not.
	 */
	private boolean rectangleDetection(Rectangle sprite, float x, float y){
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

	/**
	 * Detects key press
	 * If H pressed display info screen
	 * If a chef is selected, enable movement of the chef
	 *
	 * @param keycode one of the constants in {@link Input.Keys}
	 * @return true
	 */
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.H){
            // Info screen contains all orders and ingredients.
			game.setScreen(new InfoScreen(game, this));
		} else if (keycode == Input.Keys.I){
            // Instruction screen describing game.
			game.setScreen(new InstructionScreen(game, this));
		} else if (keycode == Input.Keys.BACKSPACE){
			Gdx.app.exit();
		}
		if (lastClickObject == true) {
			chefMove = true;
			keyCode = keycode;
		}
		return true;
	}

	/**
	 * If WASD key is lifted, disable chef movement
	 *
	 * @param keycode one of the constants in {@link Input.Keys}
	 * @return false
	 */
	@Override
	public boolean keyUp(int keycode) {
		if ((keycode == 51) || (keycode == 29) || (keycode == 47) || (keycode == 32)) {
			chefMove = false;
		}
		return false;
	}

    /**
     *
     * @param x X coordinate that the chef would move to if move takes place.
     * @param y Y coordinate that the chef would move to if move takes place.
     * @return Boolean : Can move take place.
     */
    public Boolean collisionDetection(float x, float y){
		if (y > screenHeight * 0.75){
			return false;
		}
		if (y < screenHeight * 0.25){
			return false;
		}
		if (y < screenHeight * 0.4 && x > screenWidth * 0.5){
			return false;
		}
		if (x < (screenWidth * 0.015)){
			return false;
		}
		if (x > (screenWidth * 0.925)){
			return false;
		}
        return true;

    }

	/**
	 * Moves chef based on WASD input
	 * Checks for collision of chef with work station objects
	 *
	 * @param keycode ID of key being pressed
	 */
	private void characterMovement(int keycode) {
		int pixelPerFrame = Math.round((Gdx.graphics.getWidth() / 3) * Gdx.graphics.getDeltaTime());
        if (keycode == 51 && collisionDetection(lastClick.getX(), lastClick.getY() + pixelPerFrame)) {
			lastClick.y += pixelPerFrame;
        } else if (keycode == 29 && collisionDetection(lastClick.getX() - pixelPerFrame, lastClick.getY())) {
            lastClick.x -= pixelPerFrame;
        } else if (keycode == 47 && collisionDetection(lastClick.getX(), lastClick.getY() - pixelPerFrame)) {
            lastClick.y -= pixelPerFrame;
        } else if (keycode == 32 && collisionDetection(lastClick.getX() + pixelPerFrame, lastClick.getY())) {
            lastClick.x += pixelPerFrame;
        }
		detectCollision();
	}

	/**
	 * Detects any collisions between selected chef and work stations
	 */
	private void detectCollision(){
        // TODO: Add any new hitboxes with a corresponding function.
		if (rectangleDetection(ingredientsForSalad, lastClick.getX(), lastClick.getY())){
			addSaladIngredients();
		}
		if (rectangleDetection(choppingStation, lastClick.getX(), lastClick.getY())) {
				chopIngredients();
				choppingStationTime = System.currentTimeMillis();
		}
		if (rectangleDetection(assemblyStation, lastClick.getX(), lastClick.getY())) {
			assembly();
		}
		if (rectangleDetection(chiller, lastClick.getX(), lastClick.getY())) {
			getChillerItems();
		}
		if (rectangleDetection(grill, lastClick.getX(), lastClick.getY())) {
			chooseGrillItems();
		}
	}

	/**
	 * Checks order list and adds needed chiller ingredients to inventory
	 */
	public void getChillerItems(){
        if (shoppingList.isEmpty()){
            return;
        }
        ArrayList<Integer> counter = new ArrayList<>();
        for (int i=0; i < 2; i++){
            counter.add(0);
        }
        iterator = shoppingList.iterator();
        while (iterator.hasNext()){
			Ingredient tempObject = iterator.next();
            switch (tempObject.getName()){
                case "Bun":
                    //tempObject = moveToNextObject(tempObject, true);
					iterator.remove();
                    counter.set(0, counter.get(0) + 1);
					pantryInventory.add(tempObject);
					break;
                case "BurgerPatty":
                    //tempObject = moveToNextObject(tempObject, true);
					iterator.remove();
                    counter.set(1, counter.get(1) + 1);
					pantryInventory.add(tempObject);
					break;
            }
        }
        if (changeMessage(counter)) {
            message = String.format("Burger Patty %d %n Burger Bun %d %n",
                    counter.get(0), counter.get(1));
            lastMessage = System.currentTimeMillis();
        }
	}

    /**
     *
     * @param counter Contains the number of each unique ingredient/recipe operated on.
     * @return True if something has been done, false otherwie.
     */
    public Boolean changeMessage(ArrayList<Integer> counter){
        for (int count: counter){
            if (count != 0){
                return true;
            }
        }
        return false;
    }

	/**
	 * Checks order list and adds needed salad station ingredients to inventory
	 */
	public void addSaladIngredients(){
        if (shoppingList.isEmpty()){
            return;
        }
        // Counter for each item organised via list.
        ArrayList<Integer> counter = new ArrayList<>();
        for (int x=0;x < 4;x++){
            counter.add(0);
        }
        iterator = shoppingList.iterator();
        while (iterator.hasNext()){
			Ingredient tempObject = iterator.next();
			switch (tempObject.getName()){
                case "SaladDressing":
                    //tempObject = moveToNextObject(tempObject, true);
					iterator.remove();
					pantryInventory.add(tempObject);
                    counter.set(0, counter.get(0) + 1);
					break;
				case "Chicken":
                    //tempObject = moveToNextObject(tempObject, true);
					iterator.remove();
					pantryInventory.add(tempObject);
                    counter.set(1, counter.get(1) + 1);
					break;
				case "Lettuce":
                    //tempObject = moveToNextObject(tempObject, true);
					iterator.remove();
					pantryInventory.add(tempObject);
                    counter.set(2, counter.get(2) + 1);
					break;
				case "Peppers":
                    //tempObject = moveToNextObject(tempObject, true);
					iterator.remove();
					pantryInventory.add(tempObject);
                    counter.set(3, counter.get(3) + 1);
					break;
				default:
                    //tempObject = moveToNextObject(tempObject, false);
            }
        }
		if (changeMessage(counter)) {
            message = String.format("Salad Dressing %d %n Chicken %d %n Lettuce %d %n Peppers %d %n",
                    counter.get(0), counter.get(1), counter.get(2), counter.get(3));
            lastMessage = System.currentTimeMillis();
        }
	}

	/**
	 * Chops any ingredients that can be chopped
	 */
	public void chopIngredients(){
        choppingStaff = true;
	}

	/**
	 * Checks held ingredients and assembles complete orders
	 */
	public void assembly() {
		Iterator<Recipe> iterator = orders.iterator();
		ArrayList<Integer> counter = new ArrayList<>();
		for (int i=0; i<recipes.size(); i++){
			counter.add(0);
		}
		while (iterator.hasNext()) {
			Recipe tempObject = iterator.next();
			if (tempObject.verifyCompletion()) {
				removeIngredients(tempObject);
                Sound assemblySound = Gdx.audio.newSound(Gdx.files.internal("assembly station sound.wav"));
                assemblySound.play();
                if (tempObject.getName() == "Beef Burger"){
                    counter.set(0, counter.get(0) + 1);
                } else {
                    counter.set(1, counter.get(1) + 1);
                }
				iterator.remove();
				finishedOrders --;
                orderAnimation = true;
			}
		}

        if (changeMessage(counter)){
            lastMessage = System.currentTimeMillis();
            message = String.format("Assembled %n Beef Burger %d %n Chicken Salad %d", counter.get(0), counter.get(1));
        }

		if (finishedOrders == 0){
			Long finishTime = ((System.currentTimeMillis() - startTime) / 1000) % 60;
			lastMessage = System.currentTimeMillis();
			message = String.format("Game Complete in %d seconds", finishTime);
		}

	}

	/**
	 * Removes all used ingredients from inventory
	 * @param order order being assembled
	 */
	public void removeIngredients(Recipe order) {
		for (Ingredient ingredient: order.getIngredients()) {
			if (pantryInventory.isEmpty()){
				return;
			}
			switch (ingredient.getName()) {
				case "SaladDressing":
					pantryInventory.remove(ingredient);
					break;
				case "Chicken":
					pantryInventory.remove(ingredient);
					break;
				case "Lettuce":
					pantryInventory.remove(ingredient);
					break;
				case "Peppers":
					pantryInventory.remove(ingredient);
					break;
				case "Bun":
					pantryInventory.remove(ingredient);
					break;
				case "BurgerPatty":
					pantryInventory.remove(ingredient);
					break;
			}
		}
	}

	/**
	 * Checks inventory and adds ingredients that need cooking to the grill
	 */
	public void chooseGrillItems(){
		for (Ingredient rawItem: pantryInventory) {
			if (rawItem instanceof HotIngredient && ((HotIngredient) rawItem).hasCookStarted() == false){
				switch (rawItem.getName()) {
					case "Bun":
                        burgerGrill.grillItem(rawItem);
                        break;
					case "BurgerPatty":
                        burgerGrill.grillItem(rawItem);
                        break;
				}
			}
		}
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	/**
	 * When mouse is clicked, gets coordinates and checks for any click events
	 *
	 * @param screenX The x coordinate, origin is in the upper left corner
	 * @param screenY The y coordinate, origin is in the upper left corner
	 * @param pointer the pointer for the event.
	 * @param button the button
	 * @return false
	 */
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