package auxilaries;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.GameInput;
import fileio.Input;

import java.util.ArrayList;

public final class GameInstances {
    private ArrayList<Deck> playerOneDecks = new ArrayList<Deck>();
    private ArrayList<Deck> playerTwoDecks = new ArrayList<Deck>();
    private ArrayList<GameInput> games;
    private int playerOneWins = 0;
    private int playerTwoWins = 0;
    private int gamesPlayed = 0;

    private int shuffleSeed;

    private Input input;

    /**
     * @param input - input class that holds the data from the json file
     */
    public GameInstances(final Input input) {
        this.input = input;
        for (int i = 0; i < input.getPlayerOneDecks().getNrDecks(); i++) {

            Deck deck = new Deck(input.getPlayerOneDecks().getDecks().get(i),
                    input.getPlayerOneDecks().getNrCardsInDeck());
            this.playerOneDecks.add(deck);
        }

        for (int i = 0; i < input.getPlayerTwoDecks().getNrDecks(); i++) {
            Deck deck = new Deck(input.getPlayerTwoDecks().getDecks().get(i),
                    input.getPlayerOneDecks().getNrCardsInDeck());
            this.playerTwoDecks.add(deck);
        }

        this.games = input.getGames();
    }

    /**
     * @param finalOut - ArrayNode used to write output is json file
     * This method goes through the games array and executes all games one by one in order to get
     * the output for each one, it also counts the number of games and each player's wins in order
     * to give statistics in case multiple games are played.
     */
    public void runGames(final ArrayNode finalOut) {
        for (int j = 0; j < this.games.size(); j++) {
            this.playerOneDecks = new ArrayList<>();
            this.playerTwoDecks = new ArrayList<>();
            // The decks are parsed again after each game, this is for running multiple games at
            // once in order to make sure that the decks are not affected.
            for (int i = 0; i < input.getPlayerOneDecks().getNrDecks(); i++) {

                Deck deck = new Deck(input.getPlayerOneDecks().getDecks().get(i),
                        input.getPlayerOneDecks().getNrCardsInDeck());
                this.playerOneDecks.add(deck);
            }

            for (int i = 0; i < input.getPlayerTwoDecks().getNrDecks(); i++) {
                Deck deck = new Deck(input.getPlayerTwoDecks().getDecks().get(i),
                        input.getPlayerOneDecks().getNrCardsInDeck());
                this.playerTwoDecks.add(deck);
            }
            this.gamesPlayed++;
            // A new gameTable is created every game with the required dat stored in it and than
            // the executeGame method is called each time.
            GameTable table = new GameTable(
                    this.playerOneDecks.get(this.games.get(j).getStartGame().getPlayerOneDeckIdx()),
                    this.playerTwoDecks.get(this.games.get(j).getStartGame().getPlayerTwoDeckIdx()),
                    games.get(j).getActions(), this.games.get(j).getStartGame().getPlayerOneHero(),
                    this.games.get(j).getStartGame().getPlayerTwoHero(),
                    this.games.get(j).getStartGame().getStartingPlayer(),
                    this.games.get(j).getStartGame().getShuffleSeed(), this.playerOneWins,
                    this.playerTwoWins, this.gamesPlayed);
            table.executeGame(finalOut);
            this.playerOneWins = table.getPlayerOneWins();
            this.playerTwoWins = table.getPlayerTwoWins();
        }
    }

    public ArrayList<Deck> getPlayerOneDecks() {
        return playerOneDecks;
    }

    public void setPlayerOneDecks(final ArrayList<Deck> playerOneDecks) {
        this.playerOneDecks = playerOneDecks;
    }

    public ArrayList<Deck> getPlayerTwoDecks() {
        return playerTwoDecks;
    }

    public void setPlayerTwoDecks(final ArrayList<Deck> playerTwoDecks) {
        this.playerTwoDecks = playerTwoDecks;
    }

    public ArrayList<GameInput> getGames() {
        return games;
    }

    public void setGames(final ArrayList<GameInput> games) {
        this.games = games;
    }

    public int getPlayerOneWins() {
        return playerOneWins;
    }

    public void setPlayerOneWins(final int playerOneWins) {
        this.playerOneWins = playerOneWins;
    }

    public int getPlayerTwoWins() {
        return playerTwoWins;
    }

    public void setPlayerTwoWins(final int playerTwoWins) {
        this.playerTwoWins = playerTwoWins;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(final int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getShuffleSeed() {
        return shuffleSeed;
    }

    public void setShuffleSeed(final int shuffleSeed) {
        this.shuffleSeed = shuffleSeed;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(final Input input) {
        this.input = input;
    }
}
