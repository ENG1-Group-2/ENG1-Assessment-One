package com.mygdx.game;

public class Ingredient {
    public String name;
    public Boolean chopped;
    public Boolean cooked;

    /**
     * Creates new instance of an ingredient
     *
     * @param name name of ingredient
     * @param chopped is ingredient already chopped
     * @param cooked time taken to cook ingredient
     */
    public Ingredient(String name, Boolean chopped, Boolean cooked){
        this.name = name;
        this.chopped = chopped;
        this.cooked = cooked;
    }

    /**
     * Gets the name of the ingredient
     *
     * @return name of ingredient
     */
    public String getName(){
        return name;
    }

    /**
     * Gets chopped value of ingredient
     *
     * @return truth value of chopped status
     */
    public Boolean getChopped(){
        return chopped;
    }

    /**
     * Chops ingredient
     * Sets copped value to true if previously false
     *
     * @return truth value of if function changed anything
     */
    public Boolean chopIngredient(){
        if (chopped == false) {
            chopped = true;
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Creates copy of an ingredient
     *
     * @return new instance of ingredient
     */
    public Ingredient copy(){
        return new Ingredient(name, chopped, cooked);
    }

    /**
     * Begins cooking process
     *
     * @return false
     */
    public boolean startToCook() {
        return false;
    }

    /**
     * Gets cooked value of ingredient
     *
     * @return truth value of cooked status
     */
    public boolean getCooked(){
        return cooked;
    }

    /**
     * Ends cooking process
     *
     * @return false
     */
    public boolean endCook() {
        return false;
    }

    /**
     * Gets start time of cooking process
     *
     * @return 0
     */
    public long getCookingStartTime() {
        return 0;
    }

    /**
     * Gets time taken to cook ingredient
     *
     * @return null
     */
    public Long getCookingTime() {
        return null;
    }
}
