package Parsing_Auxiliaries;

import java.util.ArrayList;

public class Player {
    Hero chosenHero;
    Deck chosenDeck;

    int mana = 1;
    ArrayList<Card> playerHand = new ArrayList<Card>();


    Player(Hero chosenHero, Deck chosenDeck, int shuffleSeed) {
        this.chosenHero = chosenHero;
        this.chosenDeck = chosenDeck;

        this.chosenDeck.ShuffleDeck(shuffleSeed);
    }
    public Deck getChosenDeck() {
        return chosenDeck;
    }

    public void setChosenDeck(Deck chosenDeck) {
        this.chosenDeck = chosenDeck;
    }

    public Hero getChosenHero() {
        return chosenHero;
    }

    public void setChosenHero(Hero chosenHero) {
        this.chosenHero = chosenHero;
    }

    public ArrayList<Card> getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(ArrayList<Card> playerHand) {
        this.playerHand = playerHand;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }
}
