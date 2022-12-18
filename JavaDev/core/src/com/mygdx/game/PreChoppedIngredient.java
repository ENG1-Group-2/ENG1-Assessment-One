package com.mygdx.game;

public class PreChoppedIngredient extends Ingredient{

    public PreChoppedIngredient(String name) {
        super(name, true);
    }

    public PreChoppedIngredient copy(){
        return new PreChoppedIngredient(name);
    }
}
