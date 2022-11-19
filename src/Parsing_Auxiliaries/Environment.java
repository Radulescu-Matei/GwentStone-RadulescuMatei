package Parsing_Auxiliaries;


import fileio.CardInput;

import java.util.ArrayList;


public class Environment extends Card {

    Environment(){}
    void inputEnvironment(CardInput cInp) {
        this.colors = cInp.getColors();
        this.name = cInp.getName();
        this.description = cInp.getDescription();
        this.mana = cInp.getMana();
    }
    void playCard(int row){

        if(this.name.equals("Firestorm")) {
            this.Firestorm(row);
        }else if(this.name.equals("Winterfell")) {
            this.Winterfell(row);
        }else if(this.name.equals("HeartHound")) {
            this.HearthHound(row);
        }
    }
    private void Firestorm(int row) {

    }

    private void Winterfell(int row) {

    }

    private void HearthHound(int row) {

    }
}


