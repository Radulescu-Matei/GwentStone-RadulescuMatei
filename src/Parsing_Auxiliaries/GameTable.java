package Parsing_Auxiliaries;

import Output.Actions;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;
import fileio.CardInput;
import fileio.DecksInput;
import fileio.Input;

import java.util.ArrayList;

public class GameTable {
    ArrayList<Minion>[] minions;

    ArrayList<Player> players = new ArrayList<Player>();

    int turnCounter;

    int currentPlayer;
    Actions actions;

  GameTable(Deck PlayerOne, Deck PlayerTwo, ArrayList<ActionsInput> actions, CardInput hero1 ,CardInput hero2, int startingPlayer, int shuffleSeed){
    Hero H1 = new Hero(hero1), H2 = new Hero(hero2);

    Player P1 = new Player(H1, PlayerOne, shuffleSeed);
    Player P2 = new Player(H2, PlayerTwo, shuffleSeed);

    this.players.add(P1);
    this.players.add(P2);

    this.currentPlayer = startingPlayer;
    this.turnCounter = 1;
    this.actions = new Actions(actions);
  }

  void executeGame(ArrayNode finalOut) {
        this.players.get(0).playerHand.add(this.players.get(0).chosenDeck.getCards().get(0));
        this.players.get(0).chosenDeck.getCards().remove(0);
        this.players.get(1).playerHand.add(this.players.get(1).chosenDeck.getCards().get(0));
        this.players.get(1).chosenDeck.getCards().remove(0);

      this.actions.addOutput(finalOut, this);


  }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}





