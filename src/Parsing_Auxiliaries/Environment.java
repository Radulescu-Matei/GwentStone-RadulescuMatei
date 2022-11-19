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
}


