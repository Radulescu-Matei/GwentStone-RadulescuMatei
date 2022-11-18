package Parsing_Auxiliaries;

import fileio.ActionsInput;
import fileio.CardInput;
import fileio.DecksInput;
import fileio.Input;

import java.util.ArrayList;

class GameTable {
    ArrayList<Minion>[] minions = new ArrayList[4];
    ArrayList<Player> players;

    int turnCounter = 1;
    int startingPlayer;
    ArrayList<ActionsInput> actions;
  GameTable(Deck PlayerOne, Deck PlayerTwo, ArrayList<ActionsInput> actions, CardInput hero1 ,CardInput hero2, int StartingPlayer){
    Hero H1 = new Hero(hero1), H2 = new Hero(hero2);

    Player P1 = new Player(H1, PlayerOne);
    Player P2 = new Player(H2, PlayerTwo);

    this.players.add(P1);
    this.players.add(P2);

    this.startingPlayer = startingPlayer;
    this.turnCounter = 1;
  }

  void executeGame() {

  }
}


class Player {
    Hero chosenHero;
    Deck chosenDeck;


    Player(Hero chosenHero, Deck chosenDeck) {
        this.chosenHero = chosenHero;
        this.chosenDeck = chosenDeck;
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
}


