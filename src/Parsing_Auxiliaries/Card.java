
package Parsing_Auxiliaries;

import fileio.CardInput;

import java.util.ArrayList;

public abstract class Card {
    int mana;
    String description;
    ArrayList<String> colors;
    String name;

    Card(){

    }
    public int getMana() {
        return mana;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public String getName() {
        return name;
    }
}






