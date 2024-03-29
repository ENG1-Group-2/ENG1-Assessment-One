package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Designed for any station that can be used to grill items.
 */
public class Grill{
    Rectangle grill;
    ArrayList<Ingredient> hobs;
    Sound grillSound = Gdx.audio.newSound(Gdx.files.internal("grill_sound.wav"));

    /**
     * Constructor method.
     * @param grill location of grill on the map.
     * @param hobs number of hobs within the map.
     */
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

    /**
     * Display timer for anything on the grill.
     * @return How long each item has been on the grill.
     */
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
        return temp;
    }

    /**
     * Looks through each ingredient on the hob and checks
     * whether it has been there for enough time.
     * @return Ingredient If any items have been burnt it returns true;
     */
    public ArrayList<Ingredient> hasGrillEnded(){
        ArrayList<Ingredient> burntItem = new ArrayList<>();
        for (int i = 0; hobs.size() > i; i++) {
            if (hobs.get(i).getName() != null &&
                    System.currentTimeMillis() - hobs.get(i).getCookingStartTime() >= hobs.get(i).getCookingTime() * 1000){
                if (hobs.get(i).endCook() == false){
                    hobs.get(i).resetCookingStart();
                    burntItem.add(hobs.get(i));
                }
                hobs.set(i, new Ingredient(null, false, false, false));
                grillSound.pause();
            }
        }
        return burntItem;
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

    /**
     * Flips any items that haven't already been flipped on the hob.
     *
     * @return String A message displaying what has happened.
     */
    public String flipItems() {
        String message = "";
        for (int i = 0; hobs.size() > i; i++) {
            if (hobs.get(i).getName() != null && hobs.get(i).getFlipped() == false) {
                hobs.get(i).flip();
                message = "Flipped";
            }
        }
        return message;
    }

    /**
     * Getter method.
     * @return class variable hobs.
     */
    public ArrayList<Ingredient> getItems(){
        return hobs;
    }
}