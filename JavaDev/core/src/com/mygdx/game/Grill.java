package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;

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
        int counter = 0;
        for (Ingredient item : hobs) {
            counter ++;
            if (item.getName() != null) {
                long timeDifference = System.currentTimeMillis() - item.getCookingStartTime();
                timeDifference = (timeDifference / 1000) % 60;
                temp += "Grill" + counter + "Timer:" + timeDifference;
            }
        }
        //System.out.println(temp);
        return temp;
    }

    public Ingredient hasGrillEnded(){
        for (int i = 0; hobs.size() > i; i++) {
            if (hobs.get(i).getName() != null &&
                    System.currentTimeMillis() - hobs.get(i).getCookingStartTime() >= hobs.get(i).getCookingTime() * 1000){
                if (hobs.get(i).endCook() == false){
                    return hobs.get(i);
                }
                hobs.set(i, new Ingredient(null, false, false, false));
                grillSound.pause();
            }
        }
        return null;
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
                grillSound.pause();
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