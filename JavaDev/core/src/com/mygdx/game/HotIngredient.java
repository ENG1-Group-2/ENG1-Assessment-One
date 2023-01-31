package com.mygdx.game;

/**
 * Inherits ingredient class but allow for cooking
 * operations as well.
 */
public class HotIngredient extends Ingredient {
    public long cookingTime;
    long cookingStart;
    long cookingEnd;

    /**
     * Creates an instance of a hot ingredient
     * Extends ingredient
     *
     * @param name name of ingredient
     * @param chopped is ingredient already chopped
     * @param cookingTime time taken to cook ingredient
     */
    public HotIngredient(String name, Boolean chopped, long cookingTime, Boolean flipped) {
        super(name, chopped, false, flipped);
        this.cookingTime = cookingTime;
        cookingStart = 0;
    }

    /**
     * Checks if cooking process has started yet
     *
     * @return truth value of cooking started
     */
    public boolean hasCookStarted(){
        return cookingStart != 0;
    }

    /**
     * Sets the cooking start time of ingredient back to 0
     */
    public void resetCookingStart(){this.cookingStart = 0;}

    /**
     * Gets time taken to cook
     *
     * @return cooking time
     */
    public Long getCookingTime(){
        return cookingTime;
    }

    /**
     * Begins cooking process
     *
     * @return true
     */
    public boolean startToCook() {
        cookingStart = System.currentTimeMillis();
        return true;
    }

    /**
     * Stops cooking process and checks if ingredient is cooked
     *
     * @return truth value of cooked status
     */
    public boolean endCook(){
        cookingEnd = System.currentTimeMillis();
        cooked = correctlyCooked();
        return cooked;
    }

    /**
     * Gets start time of cooking process
     *
     * @return cooking start time
     */
    public long getCookingStartTime() {
        return cookingStart;
    }

    /**
     * Checks if ingredient is cooked properly
     *
     * @return truth value of cooked status
     */
    public Boolean correctlyCooked() {
        Long timeDifference = this.cookingEnd - this.cookingStart;
        timeDifference = (timeDifference/1000) % 60;
        if (timeDifference > cookingTime - cookingTime * 0.2 && timeDifference <  cookingTime + cookingTime * 0.2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Creates copy of a hot ingredient
     *
     * @return new instance of hot ingredient
     */
    public HotIngredient copy(){
        return new HotIngredient(name, chopped, cookingTime, flipped);
    }
}

