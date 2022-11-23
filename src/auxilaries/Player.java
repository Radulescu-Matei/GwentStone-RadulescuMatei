package auxilaries;

import java.util.ArrayList;

public final class Player {
    private Hero chosenHero;
    private Deck chosenDeck;

    private int mana = 1;

    private int hasAttacked = 0;
    private ArrayList<Card> playerHand = new ArrayList<Card>();


    Player(final Hero chosenHero, final Deck chosenDeck, final int shuffleSeed) {
        this.chosenHero = chosenHero;
        this.chosenDeck = chosenDeck;
        this.hasAttacked = 0;
        this.chosenDeck.shuffleDeck(shuffleSeed);
    }

    public Deck getChosenDeck() {
        return chosenDeck;
    }

    public void setChosenDeck(final Deck chosenDeck) {
        this.chosenDeck = chosenDeck;
    }

    public Hero getChosenHero() {
        return chosenHero;
    }

    public void setChosenHero(final Hero chosenHero) {
        this.chosenHero = chosenHero;
    }

    public ArrayList<Card> getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(final ArrayList<Card> playerHand) {
        this.playerHand = playerHand;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public int getHasAttacked() {
        return hasAttacked;
    }

    public void setHasAttacked(final int hasAttacked) {
        this.hasAttacked = hasAttacked;
    }


}
