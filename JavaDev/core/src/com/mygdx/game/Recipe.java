package com.mygdx.game;

import java.util.ArrayList;

public class Recipe {
    ArrayList<ArrayList<String>> instructions;
    int steps;
    boolean followingProcedure;
    boolean finished;
    String name;
    public Recipe(String name, int steps){
        this.name = name;
        this.steps = steps;
        // Instructions in the following format: machine needed to use
        // and ingredient.
        instructions = new ArrayList<>(steps);
        this.followingProcedure = false;
        this.finished = false;
    }

    public void addStep(ArrayList<String> step){
        this.instructions.add(step);
    }

    public Boolean verifyCompletion(){
        if (instructions.isEmpty()){
            return true;
        }
        else {
            return false;
        }
    }
    public String getName(){
        return name;
    }

}
