package Parsing_Auxiliaries;

import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;

import java.util.ArrayList;

public class GameInstances {
    ArrayList<Deck> PlayerOneDecks;
    ArrayList<Deck> PlayerTwoDecks;
    ArrayList<GameInput> games;



    public GameInstances(Input input) {
        for(int i = 0; i < input.getPlayerOneDecks().getNrDecks(); i++) {
            this.PlayerOneDecks.get(i).inputDeck(input.getPlayerOneDecks().getDecks().get(i), input.getPlayerOneDecks().getNrCardsInDeck());
        }

        for(int i = 0; i < input.getPlayerTwoDecks().getNrDecks(); i++) {
            this.PlayerTwoDecks.get(i).inputDeck(input.getPlayerTwoDecks().getDecks().get(i), input.getPlayerOneDecks().getNrCardsInDeck());
        }

        this.games = input.getGames();
    }

    public void runGames() {
        for (int i = 0; i < games.size(); i++) {
            GameTable table = new GameTable(this.PlayerOneDecks.get(this.games.get(i).getStartGame().getPlayerOneDeckIdx()), this.PlayerTwoDecks.get(this.games.get(i).getStartGame().getPlayerOneDeckIdx()), games.get(i).getActions(), this.games.get(i).getStartGame().getPlayerOneHero(), this.games.get(i).getStartGame().getPlayerTwoHero(), this.games.get(i).getStartGame().getStartingPlayer());
            table.executeGame();
        }
    }




}
