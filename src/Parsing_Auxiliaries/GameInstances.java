package Parsing_Auxiliaries;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;

import java.util.ArrayList;

public class GameInstances {
    ArrayList<Deck> PlayerOneDecks = new ArrayList<Deck>();
    ArrayList<Deck> PlayerTwoDecks = new ArrayList<Deck>();;
    ArrayList<GameInput> games;
    int playerOneWins = 0;
    int playerTwoWins = 0;
    int gamesPlayed = 0;

    int shuffleSeed;



    public GameInstances(Input input) {
        for(int i = 0; i < input.getPlayerOneDecks().getNrDecks(); i++) {

        Deck deck = new Deck(input.getPlayerOneDecks().getDecks().get(i), input.getPlayerOneDecks().getNrCardsInDeck());
            this.PlayerOneDecks.add(deck);
        }

        for(int i = 0; i < input.getPlayerTwoDecks().getNrDecks(); i++) {
            Deck deck =  new Deck(input.getPlayerTwoDecks().getDecks().get(i), input.getPlayerOneDecks().getNrCardsInDeck());
            this.PlayerTwoDecks.add(deck);
        }

        this.games = input.getGames();
    }

    public void runGames(ArrayNode finalOut) {
        for (int i = 0; i < this.games.size(); i++) {
            GameTable table = new GameTable(this.PlayerOneDecks.get(this.games.get(i).getStartGame().getPlayerOneDeckIdx()), this.PlayerTwoDecks.get(this.games.get(i).getStartGame().getPlayerTwoDeckIdx()), games.get(i).getActions(), this.games.get(i).getStartGame().getPlayerOneHero(), this.games.get(i).getStartGame().getPlayerTwoHero(), this.games.get(i).getStartGame().getStartingPlayer(), this.games.get(i).getStartGame().getShuffleSeed(), this.playerOneWins, this.playerTwoWins, this.gamesPlayed);
            this.gamesPlayed++;
            table.executeGame(finalOut);
            this.playerOneWins = table.playerOneWins;
            this.playerTwoWins = table.playerTwoWins;
        }
    }



}
