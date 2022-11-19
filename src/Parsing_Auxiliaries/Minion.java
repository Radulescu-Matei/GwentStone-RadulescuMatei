package Parsing_Auxiliaries;

import fileio.CardInput;

public class Minion extends Card {
    int health, attackDamage;

    int hasAttacked = 0, isFrozen = 0;
    public void UseAbility(int row, int index, GameTable table){
        if(this.name.equals("The Ripper")) {
            WeakKnees(row, index, table);
        }else if(this.name.equals("Mirage")) {
            Skyjack(row, index, table);
        }else if(this.name.equals("The Cursed One")) {
            Shapeshift(row, index, table);
        }else if(this.name.equals("Disciple")) {
            GodsPlan(row, index, table);
        }
    }

    public void inputMinion(CardInput cInp) {
        this.health = cInp.getHealth();
        this.mana = cInp.getMana();
        this.name = cInp.getName();
        this.colors = cInp.getColors();
        this.description = cInp.getDescription();
        this.attackDamage = cInp.getAttackDamage();

    }



    public void WeakKnees(int row, int index, GameTable table) {

    }

    public void Skyjack(int row, int index, GameTable table) {
    }

    public void Shapeshift(int row, int index, GameTable table) {

    }

    public void GodsPlan(int row, int index, GameTable table) {

    }
    public void Place() {

    }

    public int getHealth() {
        return health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getHasAttacked() {
        return hasAttacked;
    }

    public int getIsFrozen() {
        return isFrozen;
    }
}
