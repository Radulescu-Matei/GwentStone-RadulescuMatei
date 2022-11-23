package auxilaries;

import java.util.ArrayList;
// This class is used to save the information for the two players playing the current game.
public final class Player {
    private Hero chosenHero;
    private Deck chosenDeck;

    private int mana = 1;

    private int hasAttacked = 0;
    private ArrayList<Card> playerHand = new ArrayList<Card>();

    /**
     *
     * @param chosenHero
     * @param chosenDeck
     * @param shuffleSeed
     * This method saved the hero and deck of a player for the current game, it also uses the given
     * seed to shuffle their deck.
     */
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
