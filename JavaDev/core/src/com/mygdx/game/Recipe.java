package com.mygdx.game;

import java.util.ArrayList;

public class Recipe {
    ArrayList<Ingredient> ingredients;
    String name;
    Boolean assembled;

    /**
     * Creates a unique recipe
     *
     * @param name name of recipe
     * @param ingredients list of ingredients needed to make item
     */
    public Recipe(String name, ArrayList<Ingredient> ingredients){
        this.name = name;
        this.ingredients = ingredients;
        this.assembled = false;
    }

    /**
     * Checks if the recipe is complete
     *
     * @return truth value of completion
     */
    public Boolean verifyCompletion(){
        for (Ingredient ingredient: ingredients){
            if (ingredient.getChopped() == false || ingredient.getCooked() == false){
                assembled = false;
                return false;
            }
        }
        assembled = true;
        return true;
    }

    /**
     * Gets the name of the recipe
     *
     * @return name of recipe as string
     */
    public String getName(){
        return name;
    }

    /**
     * Creates a deep copy of a recipe
     *
     * @return new instance of the recipe
     */
    public Recipe copy(){
        // Need a deep copy.
        ArrayList<Ingredient> newMemoryIngredients = new ArrayList<>();
        for (Ingredient ingredient: ingredients){
            newMemoryIngredients.add(ingredient.copy());
        }
        return new Recipe(name, newMemoryIngredients);
    }

    /**
     * Gets list of ingredients needed for recipe
     *
     * @return list of ingredients
     */
    public ArrayList<Ingredient> getIngredients(){
        return ingredients;
    }

    /**
     * Checks if recipe is assembled and returns value
     *
     * @return truth value of recipe's assembly status
     */
    public Boolean getAssembled() {
        return assembled;
    }
}


