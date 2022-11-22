package Parsing_Auxiliaries;

import fileio.CardInput;

public class Hero extends Card {
    int health = 30;

    private Hero() {
        this.health = 30;
    }

    Hero(CardInput cInp) {
        this.name = cInp.getName();
        this.colors = cInp.getColors();
        this.mana = cInp.getMana();
        this.description = cInp.getDescription();
    }
    public int getHealth() {
        return health;
    }
}
