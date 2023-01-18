package com.mygdx.game;

import com.badlogic.gdx.Gdx;

public class HotIngredient extends Ingredient {
    public long cookingTime;
    long cookingStart;
    long cookingEnd;

    public HotIngredient(String name, Boolean chopped, long cookingTime) {
        super(name, chopped, false);
        this.cookingTime = cookingTime;
        cookingStart = 0;
    }

    public boolean hasCookStarted(){
        return cookingStart != 0;
    }

    public Long getCookingTime(){
        return cookingTime;
    }

    public boolean startToCook() {
        cookingStart = System.currentTimeMillis();
        return true;
    }

    public boolean endCook(){
        cookingEnd = System.currentTimeMillis();
        cooked = correctlyCooked();
        return cooked;
    }

    public long getCookingStartTime() {
        return cookingStart;
    }

    public Boolean correctlyCooked() {
        Long timeDifference = this.cookingEnd - this.cookingStart;
        timeDifference = (timeDifference/1000) % 60;
        if (timeDifference > cookingTime - cookingTime * 0.2 && timeDifference <  cookingTime + cookingTime * 0.2) {
            return true;
        } else {
            return false;
        }
    }
    public HotIngredient copy(){
        return new HotIngredient(name, chopped, cookingTime);
    }
}

