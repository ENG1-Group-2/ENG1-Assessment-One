package com.mygdx.game;

public class Ingredient {
    public String name;
    public Boolean chopped;

    public Ingredient(String name, Boolean chopped){
        this.name = name;
        this.chopped = chopped;
    }
    public String getName(){
        return name;
    }

    public Boolean getChopped(){
        return chopped;
    }

    public Boolean chopIngredient(){
        if (chopped == false) {
            chopped = true;
            return true;
        }
        else{
            return false;
        }
    }

    public Ingredient copy(){
        return new Ingredient(name, chopped);
    }

    public boolean startToCook() {
        return false;
    }

    public boolean hasCookStarted() {
        return false;
    }

    public boolean endCook() {
        return false;
    }

    public float getCookingStartTime() {
        return 0;
    }
}
