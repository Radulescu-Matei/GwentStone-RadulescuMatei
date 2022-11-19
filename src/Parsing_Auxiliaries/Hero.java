package Parsing_Auxiliaries;

import fileio.CardInput;

public class Hero extends Card {
    int health = 30;

    private Hero() {
        this.health = 30;
    }
    public void UseAbility(int row) {

        if(this.name.equals("General Kociorow")) {
            BloodThirst(row);
        }else if(this.name.equals("King Mudface")) {
            EarthBorn(row);
        }else if(this.name.equals("Empress Thorina")) {
            LowBlow(row);
        }else if(this.name.equals("Lord Royce")) {
            SubZero(row);
        }
    }

    Hero(CardInput cInp) {
        this.name = cInp.getName();
        this.colors = cInp.getColors();
        this.mana = cInp.getMana();
        this.description = cInp.getDescription();
    }
    private void SubZero(int row) {

    }
    private void LowBlow(int row) {

    }

    private void EarthBorn(int row) {

    }

    private void BloodThirst(int row) {

    }

    public int getHealth() {
        return health;
    }
}
