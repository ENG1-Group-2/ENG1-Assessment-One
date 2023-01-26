package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;

public class Grill{
    Rectangle grill;
    ArrayList<Ingredient> hobs;
    Sound grillSound = Gdx.audio.newSound(Gdx.files.internal("grill_sound.wav"));

    public Grill(Rectangle grill, int hobs){
        this.grill = grill;
        this.hobs = new ArrayList<>(hobs);
        for (int i=0; i < hobs; i++){
            // Use as an alternative to null values.
            this.hobs.add(new Ingredient(null, false, false, false));
        }
        grillSound.loop();
        grillSound.play();
        grillSound.pause();
    }

    public String displayGrillInfo() {
        String temp = "";
        for (Ingredient item : hobs) {
            if (item.getName() != null) {
                long timeDifference = System.currentTimeMillis() - item.getCookingStartTime();
                timeDifference = (timeDifference / 1000) % 60;
                temp += "Grill One Timer:" + timeDifference;
            }
        }
        //System.out.println(temp);
        return temp;
    }

    public void hasGrillEnded(){
        for (int i = 0; hobs.size() > i; i++) {
            if (hobs.get(i).getName() != null &&
                    System.currentTimeMillis() - hobs.get(i).getCookingStartTime() >= hobs.get(i).getCookingTime() * 1000){
                hobs.get(i).endCook();
                hobs.set(i, new Ingredient(null, false, false, false));
                grillSound.pause();
            }
        }
    }

    /**
     * Begins cooking an ingredient
     * @param toCook ingredient to cook
     */
    public void grillItem(Ingredient toCook){
        for (int i = 0; hobs.size() > i; i++){

            if (hobs.get(i).getName() == null){
                hobs.set(i, toCook);
                toCook.startToCook();
                grillSound.play();
                return;
            }
        }
    }

    public void flipItems() {
        for (int i = 0; hobs.size() > i; i++) {
            if (hobs.get(i).getName() != null && hobs.get(i).getFlipped() == false) {
                hobs.get(i).flip();

            }
        }
    }

    public ArrayList<Ingredient> getItems(){
        return hobs;
    }
}