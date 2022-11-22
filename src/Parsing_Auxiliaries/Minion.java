package Parsing_Auxiliaries;

import fileio.CardInput;

public class Minion extends Card {
    int health, attackDamage;

    int hasAttacked = 0, isFrozen = 0;

    public Minion (){

    }
    public Minion(Minion minion){
        this.health = minion.getHealth();
        this.mana = minion.getMana();
        this.name = minion.getName();
        this.description = minion.getDescription();
        this.attackDamage = minion.getAttackDamage();
        this.colors = minion.getColors();
        this.isFrozen = minion.isFrozen;
        this.hasAttacked = minion.hasAttacked;
    }



    public void inputMinion(CardInput cInp) {
        this.health = cInp.getHealth();
        this.mana = cInp.getMana();
        this.name = cInp.getName();
        this.colors = cInp.getColors();
        this.description = cInp.getDescription();
        this.attackDamage = cInp.getAttackDamage();

    }

    public boolean hasTaunt(){
        if(this.name.equals("Goliath") || this.name.equals("Warden")){
            return true;
        }
        return false;
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

    public void setHealth(int health) {
        this.health = health;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public void setHasAttacked(int hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    public void setIsFrozen(int isFrozen) {
        this.isFrozen = isFrozen;
    }
}
