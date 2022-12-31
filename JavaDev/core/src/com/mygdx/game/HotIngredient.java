package com.mygdx.game;

import com.badlogic.gdx.Gdx;

public class HotIngredient extends Ingredient {
    public long cookingTime;
    long cookingStart;
    long cookingEnd;
    boolean isBurnt;

    public HotIngredient(String name, Boolean chopped, long cookingTime) {
        super(name, chopped);
        this.cookingTime = cookingTime * 1000;
        cookingStart = 0;
    }

    public boolean hasCookStarted(){
        return cookingStart != 0;
    }

    public boolean startToCook() {
        cookingStart = System.currentTimeMillis();
        return false;
    }

    public boolean endCook(){
        cookingEnd = System.currentTimeMillis();
        correctlyCooked();
        return isBurnt;
    }

    public long getCookingStartTime() {
        return cookingStart;
    }

    public void correctlyCooked() {
        Long timeDifference = (long) (this.cookingStart);
        timeDifference = (timeDifference/1000) % 60;
        if (timeDifference > cookingTime * 0.2 - cookingTime && timeDifference < cookingTime * 0.2 + cookingTime) {
            isBurnt = false;
        } else {
            isBurnt = true;
        }
    }
    public HotIngredient copy(){
        return new HotIngredient(name, chopped, cookingTime);
    }
}

