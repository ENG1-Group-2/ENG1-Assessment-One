package com.mygdx.game;

import com.badlogic.gdx.Gdx;

public class HotIngredient extends Ingredient {
    public long cookingTime;
    float cookingStart;
    public HotIngredient(String name, Boolean chopped, long cookingTime) {
        super(name, chopped);
        this.cookingTime = cookingTime * 1000;
        cookingStart = 0;
    }

    public boolean hasCookStarted(){
        return cookingStart != 0;
    }

    public boolean startToCook() {
        cookingStart = Gdx.graphics.getDeltaTime();
        return false;
    }

    public boolean endCook(){
        if (cookingStart == 0){
            return false;
        }
        if (System.currentTimeMillis() - cookingStart > cookingTime){
            return false;
        }
        else{
            return true;
        }
    }

    public float getCookingStart() {
        return cookingStart;
    }

    public HotIngredient copy(){
        return new HotIngredient(name, chopped, cookingTime);
    }
}

