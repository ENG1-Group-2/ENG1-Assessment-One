package com.mygdx.game;

import java.util.ArrayList;

public class Recipe {
    ArrayList<Ingredient> ingredients;
    String name;
    Boolean assembled;

    public Recipe(String name, ArrayList<Ingredient> ingredients){
        this.name = name;
        this.ingredients = ingredients;
        this.assembled = false;
    }

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


    public String getName(){
        return name;
    }

    public Recipe copy(){
        ArrayList<Ingredient> newMemoryIngredients = (ArrayList<Ingredient>) ingredients.clone();
        return new Recipe(name, newMemoryIngredients);
    }

    public ArrayList<Ingredient> getIngredients(){
        return ingredients;
    }

    public Boolean getAssembled() {
        return assembled;
    }
}


