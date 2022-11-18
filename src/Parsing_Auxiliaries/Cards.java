package Parsing_Auxiliaries;

import fileio.CardInput;
import fileio.Input;

import java.util.ArrayList;

abstract class Card {
    int mana;
    String description;
    ArrayList<String> colors;
    String name;

}

class Minion extends Card {
    int health, attackDamage;

    int hasAttacked = 0, isFrozen = 0;
    public void UseAbility(int row, int index){
        if(this.name.equals("The Ripper")) {
            WeakKnees(row, index);
        }else if(this.name.equals("Mirage")) {
            Skyjack(row, index);
        }else if(this.name.equals("The Cursed One")) {
            Shapeshift(row, index);
        }else if(this.name.equals("Disciple")) {
            GodsPlan(row, index);
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

    public void WeakKnees(int row, int index) {

    }

    public void Skyjack(int row, int index) {
    }

    public void Shapeshift(int row, int index) {

    }

    public void GodsPlan(int row, int index) {

    }
    public void Place() {

    }
}

class Hero extends Card {
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
}

class Environment extends Card {

 Environment(){}
 void inputEnvironment(CardInput cInp) {
     this.colors = cInp.getColors();
     this.name = cInp.getName();
     this.description = cInp.getDescription();
     this.mana = cInp.getMana();
 }
 private void Firestorm() {

    }

 private void Winterfell(int row) {

 }

 private void HearthHound(int row) {

 }
}
