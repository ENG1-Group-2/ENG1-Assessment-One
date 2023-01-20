package com.mygdx.game;

public class Ingredient {
    public String name;
    public Boolean chopped;
    public Boolean cooked;

    public Ingredient(String name, Boolean chopped, Boolean cooked){
        this.name = name;
        this.chopped = chopped;
        this.cooked = cooked;
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
        return new Ingredient(name, chopped, false);
    }

    public boolean startToCook() {
        return false;
    }

    public boolean getCooked(){
        return cooked;
    }

    public boolean endCook() {
        return false;
    }

    public long getCookingStartTime() {
        return 0;
    }

    public Long getCookingTime() {
        return null;
    }
}
