package auxilaries;

import output.Actions;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;


import java.util.ArrayList;

public final class GameTable {
    private static final int ROW_THREE = 3;
    public static final int SUM_FIVE = 5;

    public static final int MAX_MANA = 10;
    private ArrayList<ArrayList<Minion>> minions = new ArrayList<>();

    private ArrayList<Player> players = new ArrayList<>();

    private int turnCounter;

    private int currentPlayer, startingPlayer;
    private int playerOneWins, playerTwoWins, games;
    private Actions actions;

    /**
     * @param playerOne - player one's deck
     * @param playerTwo - player two's  deck
     * @param actions - the array of ActionInputs
     * @param hero1 - the cardInput of the first hero
     * @param hero2 - the cardInput of the second hero
     * @param startingPlayer - number of the starting player
     * @param shuffleSeed - a seed used to shuffle decks
     * @param playerOneWins - how many times the first player has won
     * @param playerTwoWins - how many times the second player has won
     * @param games - number of games played
     * This receives the information needed for each player ( their deck and hero) and the
     * commands thant need to be executed each game which are saved in the Actions class.
     */
    GameTable(final Deck playerOne, final Deck playerTwo, final ArrayList<ActionsInput> actions,
              final CardInput hero1, final CardInput hero2, final int startingPlayer,
              final int shuffleSeed, final int playerOneWins, final int playerTwoWins,
              final int games) {
        Hero h1 = new Hero(hero1);
        Hero h2 = new Hero(hero2);

        Player p1 = new Player(h1, playerOne, shuffleSeed);
        Player p2 = new Player(h2, playerTwo, shuffleSeed);

        this.players.add(p1);
        this.players.add(p2);


        this.currentPlayer = startingPlayer;
        this.startingPlayer = startingPlayer;
        this.turnCounter = 1;
        this.actions = new Actions(actions);
        this.playerOneWins = playerOneWins;
        this.playerTwoWins = playerTwoWins;
        this.games = games;
        this.minions.add(new ArrayList<>());
        this.minions.add(new ArrayList<>());
        this.minions.add(new ArrayList<>());
        this.minions.add(new ArrayList<>());
    }

    /**
     * This method ends the turn for a player, in doing is so is unfreezes any minion that
     * they have on the bord( the isFrozen variable) and also resets the hasAttacked variable
     * so that the minions can attack next turn.
     * If both players have ended their turn, a new round starts and both players are given
     * mana ,incrementing every round up to 10. In this case a card is also "drawn" from their
     * deck and added to their hand parameter.
     */
    public void endTurn() {

        if (this.currentPlayer == 1) {
            this.currentPlayer = 2;
            this.getPlayers().get(0).setHasAttacked(0);
            for (int i = 0; i < this.minions.get(2).size(); i++) {
                this.minions.get(2).get(i).setIsFrozen(0);
                this.minions.get(2).get(i).setHasAttacked(0);
            }
            for (int i = 0; i < this.minions.get(ROW_THREE).size(); i++) {
                this.minions.get(ROW_THREE).get(i).setIsFrozen(0);
                this.minions.get(ROW_THREE).get(i).setHasAttacked(0);
            }
        } else {
            this.getPlayers().get(1).setHasAttacked(0);
            this.currentPlayer = 1;
            for (int i = 0; i < this.minions.get(1).size(); i++) {
                this.minions.get(1).get(i).setIsFrozen(0);
                this.minions.get(1).get(i).setHasAttacked(0);
            }
            for (int i = 0; i < this.minions.get(0).size(); i++) {
                this.minions.get(0).get(i).setIsFrozen(0);
                this.minions.get(0).get(i).setHasAttacked(0);
            }
        }

        if (this.currentPlayer == this.startingPlayer) {
            if (this.getPlayers().get(0).getChosenDeck().getCards().size() > 0) {
                this.getPlayers().get(0).getPlayerHand()
                        .add(this.players.get(0).getChosenDeck().getCards().get(0));
                this.getPlayers().get(0).getChosenDeck().getCards().remove(0);
            }
            if (this.getPlayers().get(1).getChosenDeck().getCards().size() > 0) {
                this.getPlayers().get(1).getPlayerHand()
                        .add(this.players.get(1).getChosenDeck().getCards().get(0));
                this.getPlayers().get(1).getChosenDeck().getCards().remove(0);
            }
            if (this.turnCounter < MAX_MANA) {
                this.turnCounter += 1;
            }

            this.players.get(0).setMana(this.players.get(0).getMana() + this.turnCounter);
            this.players.get(1).setMana(this.players.get(1).getMana() + this.turnCounter);


        }
    }

    /**
     * @param aux - objectNode used to send output to arraynode and to json file after
     * @param action - the current action
     * This method places a minion from the current
     * player's hand on the game table.
     */
    public void placeCard(final ObjectNode aux, final ActionsInput action) {
        Card card;

        card = this.players.get(this.currentPlayer - 1).getPlayerHand().get(action.getHandIdx());
        // If the card is an instance of environment it cannot be placed.
        if (card instanceof Environment) {
            aux.put("command", action.getCommand());
            aux.put("handIdx", action.getHandIdx());
            aux.put("error", "Cannot place environment card on table.");

        } else if (this.players.get(currentPlayer - 1).getMana() < card.getMana()) {
            // Checks if the currentPlayer has enough mana
            aux.put("command", action.getCommand());
            aux.put("handIdx", action.getHandIdx());
            aux.put("error", "Not enough mana to place card on table.");
        } else {
            // The next ifs check which player is the current player, and which minion is being
            // played, with these factors it places it on the appropriate row if there is space
            Minion minion = (Minion) card;
            if (this.currentPlayer == 1) {
                if (card.getName().equals("The Ripper") || card.getName().equals("Miraj")
                        || card.getName().equals("Goliath") || card.getName().equals("Warden")) {
                    if (this.minions.get(2).size() == SUM_FIVE) {
                        aux.put("command", action.getCommand());
                        aux.put("handIdx", action.getHandIdx());
                        aux.put("error", "Cannot place card on table since row is full.");
                    } else {
                        this.minions.get(2).add(minion);
                        this.players.get(0)
                                .setMana(this.players.get(0).getMana() - minion.getMana());
                        this.players.get(0).getPlayerHand().remove(action.getHandIdx());
                    }
                } else {
                    if (this.minions.get(ROW_THREE).size() == SUM_FIVE) {
                        aux.put("command", action.getCommand());
                        aux.put("handIdx", action.getHandIdx());
                        aux.put("error", "Cannot place card on table since row is full.");
                    } else {
                        this.minions.get(ROW_THREE).add(minion);
                        this.players.get(0)
                                .setMana(this.players.get(0).getMana() - minion.getMana());
                        this.players.get(0).getPlayerHand().remove(action.getHandIdx());
                    }
                }
            } else {
                // For player two it is seems similar but the minions are placed on his rows.
                if (card.getName().equals("The Ripper") || card.getName().equals("Miraj")
                        || card.getName().equals("Goliath") || card.getName().equals("Warden")) {
                    if (this.minions.get(1).size() == SUM_FIVE) {
                        aux.put("command", action.getCommand());
                        aux.put("handIdx", action.getHandIdx());
                        aux.put("error", "Cannot place card on table since row is full.");
                    } else {
                        this.minions.get(1).add(minion);
                        this.players.get(1)
                                .setMana(this.players.get(1).getMana() - minion.getMana());
                        this.players.get(1).getPlayerHand().remove(action.getHandIdx());
                    }
                } else {
                    if (this.minions.get(0).size() == SUM_FIVE) {
                        aux.put("command", action.getCommand());
                        aux.put("handIdx", action.getHandIdx());
                        aux.put("error", "Cannot place card on table since row is full.");
                    } else {
                        this.minions.get(0).add(minion);
                        this.players.get(1)
                                .setMana(this.players.get(1).getMana() - minion.getMana());
                        this.players.get(1).getPlayerHand().remove(action.getHandIdx());
                    }
                }
            }

        }
    }

    /**
     * @param aux - objectNode used to send output to arraynode and to json file after
     * @param action - the current action
     * This method uses an environment card
     */
    public void useEnvironment(final ObjectNode aux, final ActionsInput action) {
        Card card;
        int oppositeRow = ROW_THREE;
        // The row opposite the affected one is found for the Hearth Hound card.
        if (action.getAffectedRow() == ROW_THREE) {
            oppositeRow = 0;
        } else if (action.getAffectedRow() == 2) {
            oppositeRow = 1;
        } else if (action.getAffectedRow() == 1) {
            oppositeRow = 2;
        }


        card = this.players.get(this.currentPlayer - 1).getPlayerHand().get(action.getHandIdx());
        // Checks if the card is a minion and sends the error message to the object node if it is.
        if (card instanceof Minion) {
            aux.put("command", action.getCommand());
            aux.put("handIdx", action.getHandIdx());
            aux.put("affectedRow", action.getAffectedRow());
            aux.put("error", "Chosen card is not of type environment.");
        // Checks if the player has enough mana to play the card.
        } else if (this.players.get(this.currentPlayer - 1).getMana() < card.getMana()) {
            aux.put("command", action.getCommand());
            aux.put("handIdx", action.getHandIdx());
            aux.put("affectedRow", action.getAffectedRow());
            aux.put("error", "Not enough mana to use environment card.");
            // If the affected row belongs to the current player the card cannot be used.
        } else if ((this.currentPlayer == 1 && (action.getAffectedRow() > 1)) || (
                this.currentPlayer == 2 && (action.getAffectedRow() <= 1))) {
            aux.put("command", action.getCommand());
            aux.put("handIdx", action.getHandIdx());
            aux.put("affectedRow", action.getAffectedRow());
            aux.put("error", "Chosen row does not belong to the enemy.");

        } else {
            // Checks which of the three cards is being played
            Environment environment = (Environment) card;
            // Heart Hound checks if the opposite row is full if not it steals a minion.
            if (card.getName().equals("Heart Hound")) {
                if (this.getMinions().get(oppositeRow).size() == SUM_FIVE) {
                    aux.put("command", action.getCommand());
                    aux.put("handIdx", action.getHandIdx());
                    aux.put("affectedRow", action.getAffectedRow());
                    aux.put("error",
                            "Cannot steal enemy card since the player's row is" + " full.");
                    return;
                } else {
                    // Simple check to find the highest health minion on the row so it can be
                    // stolen.
                    int maxHealth = 0;
                    int maxIndex = -1;
                    for (int i = 0; i < this.getMinions().get(action.getAffectedRow()).size();
                         i++) {
                        if (this.getMinions().get(action.getAffectedRow()).get(i).getHealth()
                                > maxHealth) {
                            maxHealth = this.getMinions().get(action.getAffectedRow()).get(i)
                                    .getHealth();
                            maxIndex = i;
                        }
                    }
                    Minion stolen = this.minions.get(action.getAffectedRow()).get(maxIndex);
                    this.minions.get(action.getAffectedRow()).remove(maxIndex);

                    this.minions.get(oppositeRow).add(stolen);
                }

            } else if (card.getName().equals("Winterfell")) {
                // Sets the isFrozen parameter of an entire row to one, so it can be checked later.
                for (int i = 0; i < this.getMinions().get(action.getAffectedRow()).size(); i++) {
                    this.getMinions().get(action.getAffectedRow()).get(i).setIsFrozen(1);
                }

            } else if (card.getName().equals("Firestorm")) {
                // Reduces the health of an entire row by one.
                for (int i = 0; i < this.getMinions().get(action.getAffectedRow()).size(); i++) {
                    this.getMinions().get(action.getAffectedRow()).get(i).setHealth(
                            this.getMinions().get(action.getAffectedRow()).get(i).getHealth() - 1);

                    if (this.getMinions().get(action.getAffectedRow()).get(i).getHealth() <= 0) {
                        this.getMinions().get(action.getAffectedRow()).remove(i);
                        i--;
                    }

                }
            }
            // The cost of the card is deducted from the player's mana.
            this.players.get(this.currentPlayer - 1).setMana(
                    this.players.get(this.currentPlayer - 1).getMana() - environment.getMana());
            this.players.get(this.currentPlayer - 1).getPlayerHand().remove(action.getHandIdx());
        }
    }

    /**
     * @param aux - objectNode used to send output to arraynode and to json file after
     * @param action - the current action
     * This method implements the command where a minion attacks another.
     */
    public void attackEnemy(final ObjectNode aux, final ActionsInput action) {
        // Checks if the cards are on the same side, if they are equal, or
        // the sum of their 2 indexes is 5 or 1 ( because player 1 has rows 2,3
        // and player 1 has row 1 and 0)
        if ((action.getCardAttacker().getX() == action.getCardAttacked().getX()) || (
                action.getCardAttacked().getX() + action.getCardAttacker().getX() == SUM_FIVE) || (
                action.getCardAttacked().getX() + action.getCardAttacker().getX() == 1)) {
            aux.put("command", action.getCommand());
            ObjectNode attacker = new ObjectNode(JsonNodeFactory.instance);
            ObjectNode attacked = new ObjectNode(JsonNodeFactory.instance);
            attacked.put("x", action.getCardAttacked().getX());
            attacked.put("y", action.getCardAttacked().getY());
            aux.put("cardAttacked", attacked);
            attacker.put("x", action.getCardAttacker().getX());
            attacker.put("y", action.getCardAttacker().getY());
            aux.put("cardAttacker", attacker);
            aux.put("error", "Attacked card does not belong to the enemy.");
        } else if (this.getMinions().get(action.getCardAttacker().getX())
                .get(action.getCardAttacker().getY()).getHasAttacked() == 1) {
            // Checks the hasAttacked parameter, because multiple attacks by the same
            // minion in one turn are not allowed.
            aux.put("command", action.getCommand());
            ObjectNode attacker = new ObjectNode(JsonNodeFactory.instance);
            attacker.put("x", action.getCardAttacker().getX());
            attacker.put("y", action.getCardAttacker().getY());
            aux.put("cardAttacker", attacker);
            ObjectNode attacked = new ObjectNode(JsonNodeFactory.instance);
            attacked.put("x", action.getCardAttacked().getX());
            attacked.put("y", action.getCardAttacked().getY());
            aux.put("cardAttacked", attacked);
            aux.put("error", "Attacker card has already attacked this turn.");
        } else if (this.getMinions().get(action.getCardAttacker().getX())
                .get(action.getCardAttacker().getY()).getIsFrozen() == 1) {
            // Checks the isFrozen variable to see if the minion can attack
            // as freeze stops it from doing so.
            aux.put("command", action.getCommand());
            ObjectNode attacker = new ObjectNode(JsonNodeFactory.instance);
            attacker.put("x", action.getCardAttacker().getX());
            attacker.put("y", action.getCardAttacker().getY());
            aux.put("cardAttacker", attacker);
            ObjectNode attacked = new ObjectNode(JsonNodeFactory.instance);
            attacked.put("x", action.getCardAttacked().getX());
            attacked.put("y", action.getCardAttacked().getY());
            aux.put("cardAttacked", attacked);
            aux.put("error", "Attacker card is frozen.");
        } else {
            int taunt = 0;
            Minion attack = this.minions.get(action.getCardAttacked().getX())
                    .get(action.getCardAttacked().getY());

            if (!attack.hasTaunt()) {
                // If the attacked card does not possesses the tank characteristic
                // we scan the front row of the enemy player for a tank minion.
                // Because they have to be attacked first.
                if (this.currentPlayer == 1) {
                    for (int i = 0; i < this.minions.get(1).size(); i++) {
                        if (this.minions.get(1).get(i).hasTaunt()) {
                            taunt = 1;
                        }
                    }
                }
                if (this.currentPlayer == 2) {
                    for (int i = 0; i < this.minions.get(2).size(); i++) {
                        if (this.minions.get(2).get(i).hasTaunt()) {
                            taunt = 1;
                        }
                    }
                }
            }
            if (taunt == 1) {
                aux.put("command", action.getCommand());
                ObjectNode attacker = new ObjectNode(JsonNodeFactory.instance);
                attacker.put("x", action.getCardAttacker().getX());
                attacker.put("y", action.getCardAttacker().getY());
                aux.put("cardAttacker", attacker);
                ObjectNode attacked = new ObjectNode(JsonNodeFactory.instance);
                attacked.put("x", action.getCardAttacked().getX());
                attacked.put("y", action.getCardAttacked().getY());
                aux.put("cardAttacked", attacked);
                aux.put("error", "Attacked card is not of type 'Tank'.");

                return;
            }
            // Sets the hasAttacked variable to 1, and deducts the damage of the attacker from the
            // attacked minion's health.
            this.minions.get(action.getCardAttacker().getX()).get(action.getCardAttacker().getY())
                    .setHasAttacked(1);
            this.minions.get(action.getCardAttacked().getX()).get(action.getCardAttacked().getY())
                    .setHealth(this.minions.get(action.getCardAttacked().getX())
                            .get(action.getCardAttacked().getY()).getHealth() - this.getMinions()
                            .get(action.getCardAttacker().getX())
                            .get(action.getCardAttacker().getY()).getAttackDamage());

            // If the enemies has reached 0 health it dies, so it is removed from the table.
            if (this.minions.get(action.getCardAttacked().getX())
                    .get(action.getCardAttacked().getY()).getHealth() <= 0) {
                this.minions.get(action.getCardAttacked().getX())
                        .remove(action.getCardAttacked().getY());
            }
        }
    }

    /**
     * @param aux - objectNode used to send output to arraynode and to json file after
     * @param action - the current action
     * This method implements the use of a minion's ability.
     */
    public void cardAbility(final ObjectNode aux, final ActionsInput action) {
        // If the minion is frozen it cannot attack.
        if (this.getMinions().get(action.getCardAttacker().getX())
                .get(action.getCardAttacker().getY()).getIsFrozen() == 1) {
            aux.put("command", action.getCommand());
            ObjectNode attacker = new ObjectNode(JsonNodeFactory.instance);
            attacker.put("x", action.getCardAttacker().getX());
            attacker.put("y", action.getCardAttacker().getY());
            aux.put("cardAttacker", attacker);
            ObjectNode attacked = new ObjectNode(JsonNodeFactory.instance);
            attacked.put("x", action.getCardAttacked().getX());
            attacked.put("y", action.getCardAttacked().getY());
            aux.put("cardAttacked", attacked);
            aux.put("error", "Attacker card is frozen.");
        } else if (this.getMinions().get(action.getCardAttacker().getX())
                .get(action.getCardAttacker().getY()).getHasAttacked() == 1) {
            // If the minion has already attacked or uses it's ability this turn,
            // it cannot attack.
            aux.put("command", action.getCommand());
            ObjectNode attacker = new ObjectNode(JsonNodeFactory.instance);
            attacker.put("x", action.getCardAttacker().getX());
            attacker.put("y", action.getCardAttacker().getY());
            aux.put("cardAttacker", attacker);
            ObjectNode attacked = new ObjectNode(JsonNodeFactory.instance);
            attacked.put("x", action.getCardAttacked().getX());
            attacked.put("y", action.getCardAttacked().getY());
            aux.put("cardAttacked", attacked);
            aux.put("error", "Attacker card has already attacked this turn.");
        } else if (this.minions.get(action.getCardAttacker().getX())
                .get(action.getCardAttacker().getY()).getName().equals("Disciple")) {
            // The Disciple minion is the only one that targets it's allies so it is checked
            // whether the attack card is not on it's side of the board. The condition is explained
            // in the attackEnemy method, but negated this time.
            if (!((action.getCardAttacked().getX() + action.getCardAttacker().getX() == 1) || (
                    action.getCardAttacked().getX() + action.getCardAttacker().getX() == SUM_FIVE)
                    || (action.getCardAttacked().getX() == action.getCardAttacker().getX()))) {
                aux.put("command", action.getCommand());
                ObjectNode attacker = new ObjectNode(JsonNodeFactory.instance);
                attacker.put("x", action.getCardAttacker().getX());
                attacker.put("y", action.getCardAttacker().getY());
                aux.put("cardAttacker", attacker);
                ObjectNode attacked = new ObjectNode(JsonNodeFactory.instance);
                attacked.put("x", action.getCardAttacked().getX());
                attacked.put("y", action.getCardAttacked().getY());
                aux.put("cardAttacked", attacked);
                aux.put("error", "Attacked card does not belong to the current player.");
                return;
            }
            // Two health is added to the attacked minion and the hasAttacked variable is changed
            // to 1.
            this.minions.get(action.getCardAttacked().getX()).get(action.getCardAttacked().getY())
                    .setHealth(this.minions.get(action.getCardAttacked().getX())
                            .get(action.getCardAttacked().getY()).getHealth() + 2);
            this.minions.get(action.getCardAttacker().getX()).get(action.getCardAttacker().getY())
                    .setHasAttacked(1);

        } else {
            // The same condition as above but not negated, so the error message is sent when
            // the attacked card is not on the currentPlayer's side.
            if ((action.getCardAttacked().getX() + action.getCardAttacker().getX() == 1) || (
                    action.getCardAttacked().getX() + action.getCardAttacker().getX() == SUM_FIVE)
                    || (action.getCardAttacked().getX() == action.getCardAttacker().getX())) {
                aux.put("command", action.getCommand());
                ObjectNode attacker = new ObjectNode(JsonNodeFactory.instance);
                attacker.put("x", action.getCardAttacker().getX());
                attacker.put("y", action.getCardAttacker().getY());
                aux.put("cardAttacker", attacker);
                ObjectNode attacked = new ObjectNode(JsonNodeFactory.instance);
                attacked.put("x", action.getCardAttacked().getX());
                attacked.put("y", action.getCardAttacked().getY());
                aux.put("cardAttacked", attacked);
                aux.put("error", "Attacked card does not belong to the enemy.");
                return;
            }

            int taunt = 0;

            Minion attack = this.minions.get(action.getCardAttacked().getX())
                    .get(action.getCardAttacked().getY());
            // Same condition as in the attack method checks for taunt minions.
            if (!attack.hasTaunt()) {
                if (this.currentPlayer == 1) {
                    for (int i = 0; i < this.minions.get(1).size(); i++) {
                        if (this.minions.get(1).get(i).hasTaunt()) {
                            taunt = 1;
                        }
                    }
                }
                if (this.currentPlayer == 2) {
                    for (int i = 0; i < this.minions.get(2).size(); i++) {
                        if (this.minions.get(2).get(i).hasTaunt()) {
                            taunt = 1;
                        }
                    }
                }
            }

            if (taunt == 1) {
                aux.put("command", action.getCommand());
                ObjectNode attacker = new ObjectNode(JsonNodeFactory.instance);
                attacker.put("x", action.getCardAttacker().getX());
                attacker.put("y", action.getCardAttacker().getY());
                aux.put("cardAttacker", attacker);
                ObjectNode attacked = new ObjectNode(JsonNodeFactory.instance);
                attacked.put("x", action.getCardAttacked().getX());
                attacked.put("y", action.getCardAttacked().getY());
                aux.put("cardAttacked", attacked);
                aux.put("error", "Attacked card is not of type 'Tank'.");

                return;
            }

            Minion at = this.minions.get(action.getCardAttacker().getX())
                    .get(action.getCardAttacker().getY());
            // The Ripper deducts two attackDamage from a minion from the other side.
            if (at.getName().equals("The Ripper")) {
                this.minions.get(action.getCardAttacked().getX())
                        .get(action.getCardAttacked().getY()).setAttackDamage(
                                this.minions.get(action.getCardAttacked().getX()).get(action
                                        .getCardAttacked().getY()).getAttackDamage() - 2);
                if (this.minions.get(action.getCardAttacked().getX())
                        .get(action.getCardAttacked().getY()).getAttackDamage() < 0) {
                    this.minions.get(action.getCardAttacked().getX())
                            .get(action.getCardAttacked().getY()).setAttackDamage(0);

                }

            // The Miraj card swaps it's health with an enemies. The implementation uses
            // an auxiliary variable to do so.
            } else if (at.getName().equals("Miraj")) {
                int swap = attack.getHealth();
                this.minions.get(action.getCardAttacked().getX())
                        .get(action.getCardAttacked().getY()).setHealth(at.getHealth());
                this.minions.get(action.getCardAttacker().getX())
                        .get(action.getCardAttacker().getY()).setHasAttacked(1);
                this.minions.get(action.getCardAttacker().getX())
                        .get(action.getCardAttacker().getY()).setHealth(swap);
                // The Cursed One swaps and enemy's health with it's own damage
                // if the enemy has 0 damage, it is destroyed and removed from the board.
            } else if (at.getName().equals("The Cursed One")) {

                if (this.minions.get(action.getCardAttacked().getX())
                        .get(action.getCardAttacked().getY()).getAttackDamage() == 0) {
                    this.minions.get(action.getCardAttacked().getX())
                            .remove(action.getCardAttacked().getY());
                    this.minions.get(action.getCardAttacker().getX())
                            .get(action.getCardAttacker().getY()).setHasAttacked(1);
                    return;

                }
                int swap = attack.getHealth();
                this.minions.get(action.getCardAttacked().getX())
                        .get(action.getCardAttacked().getY()).setHealth(attack.getAttackDamage());
                this.minions.get(action.getCardAttacked().getX())
                        .get(action.getCardAttacked().getY()).setAttackDamage(swap);
                this.minions.get(action.getCardAttacker().getX())
                        .get(action.getCardAttacker().getY()).setHasAttacked(1);


            }
            // The hasAttacked parameter is set to 1 in each case.
        }
    }

    /**
     * @param aux - objectNode used to send output to arraynode and to json file after
     * @param action - the current action
     * This method is almost the same as the previous attack one, but it is onlu used to
     * decrease the health of the enemy hero.
     */
    public void attackHero(final ObjectNode aux, final ActionsInput action) {
        // Checks if attacker is frozen.
        if (this.getMinions().get(action.getCardAttacker().getX())
                .get(action.getCardAttacker().getY()).getIsFrozen() == 1) {
            aux.put("command", action.getCommand());
            ObjectNode attacker = new ObjectNode(JsonNodeFactory.instance);
            attacker.put("x", action.getCardAttacker().getX());
            attacker.put("y", action.getCardAttacker().getY());
            aux.put("cardAttacker", attacker);
            aux.put("error", "Attacker card is frozen.");
            // Checks if the minion hasAttacked already.
        } else if (this.getMinions().get(action.getCardAttacker().getX())
                .get(action.getCardAttacker().getY()).getHasAttacked() == 1) {
            aux.put("command", action.getCommand());
            ObjectNode attacker = new ObjectNode(JsonNodeFactory.instance);
            attacker.put("x", action.getCardAttacker().getX());
            attacker.put("y", action.getCardAttacker().getY());
            aux.put("cardAttacker", attacker);
            aux.put("error", "Attacker card has already attacked this turn.");
        } else {
            int taunt = 0;
        // Checks for taunt minions same as before without the check for the attacked
        // as heroes cannot have the tank characteristic.
            if (this.currentPlayer == 1) {
                for (int i = 0; i < this.minions.get(1).size(); i++) {
                    if (this.minions.get(1).get(i).hasTaunt()) {
                        taunt = 1;
                    }
                }
            }
            if (this.currentPlayer == 2) {
                for (int i = 0; i < this.minions.get(2).size(); i++) {
                    if (this.minions.get(2).get(i).hasTaunt()) {
                        taunt = 1;
                    }
                }
            }

            if (taunt == 1) {
                aux.put("command", action.getCommand());
                ObjectNode attacker = new ObjectNode(JsonNodeFactory.instance);
                attacker.put("x", action.getCardAttacker().getX());
                attacker.put("y", action.getCardAttacker().getY());
                aux.put("cardAttacker", attacker);
                aux.put("error", "Attacked card is not of type 'Tank'.");

                return;
            }

            int damage = this.getMinions().get(action.getCardAttacker().getX())
                    .get(action.getCardAttacker().getY()).getAttackDamage();

            this.getMinions().get(action.getCardAttacker().getX())
                    .get(action.getCardAttacker().getY()).setHasAttacked(1);
            // Deducts the damage of the attacker minion from the enemy's hero health
            // if the hero is killed a winner is announced.
            if (this.currentPlayer == 1) {
                this.getPlayers().get(1).getChosenHero()
                        .setHealth(this.getPlayers().get(1).getChosenHero().getHealth() - damage);
                if (this.getPlayers().get(1).getChosenHero().getHealth() <= 0) {
                    this.playerOneWins++;

                    aux.put("gameEnded", "Player one killed the enemy hero.");
                }
            } else {
                this.getPlayers().get(0).getChosenHero()
                        .setHealth(this.getPlayers().get(0).getChosenHero().getHealth() - damage);


                if (this.getPlayers().get(0).getChosenHero().getHealth() <= 0) {
                    this.playerTwoWins++;
                    aux.put("gameEnded", "Player two killed the enemy hero.");
                }
            }

        }
    }

    /**
     * @param aux - objectNode used to send output to arraynode and to json file after
     * @param action - the current action
     * This method implements the use of a hero's ability. Each of the four
     * heroes has a different one.
     */
    public void useHeroAbility(final ObjectNode aux, final ActionsInput action) {
        // Checks whether the player has enough mana to use it's hero's ability.
        if (this.getPlayers().get(this.currentPlayer - 1).getMana() < this.getPlayers()
                .get(this.currentPlayer - 1).getChosenHero().getMana()) {
            aux.put("command", action.getCommand());
            aux.put("affectedRow", action.getAffectedRow());
            aux.put("error", "Not enough mana to use hero's ability.");
            // Checks whether the hero has already used it's ability this turn.
        } else if (this.getPlayers().get(this.currentPlayer - 1).getHasAttacked() == 1) {
            aux.put("command", action.getCommand());
            aux.put("affectedRow", action.getAffectedRow());
            aux.put("error", "Hero has already attacked this turn.");
            // There are two cases for the heroes that target enemies and allies, each
            // have a different error.
            //Case for the heroes that target enemies.
        } else if (this.getPlayers().get(this.currentPlayer - 1).getChosenHero().getName()
                .equals("Lord Royce")
                || this.getPlayers().get(this.currentPlayer - 1).getChosenHero().getName()
                .equals("Empress Thorina")) {
            if (((action.getAffectedRow() == 2 || action.getAffectedRow() == ROW_THREE)
                    && this.currentPlayer == 1) || (
                    (action.getAffectedRow() == 1 || action.getAffectedRow() == 0)
                            && this.currentPlayer == 2)) {
                aux.put("command", action.getCommand());
                aux.put("affectedRow", action.getAffectedRow());
                aux.put("error", "Selected row does not belong to the enemy.");
                return;
            }
            // If the hero is Lord Royce a loop is used to find the minion on the given
            // enemy row with the maxium attack in order to freeze it.
            if (this.getPlayers().get(this.currentPlayer - 1).getChosenHero().getName()
                    .equals("Lord Royce")) {
                int maxAttack = -1;
                int idx = -1;

                for (int i = 0; i < this.getMinions().get(action.getAffectedRow()).size();
                     i++) {
                    if (this.getMinions().get(action.getAffectedRow()).get(i).getAttackDamage()
                            > maxAttack) {
                        maxAttack = this.getMinions().get(action.getAffectedRow()).get(i)
                                .getAttackDamage();
                        idx = i;
                    }
                }
                // The minion is frozen, mana is deducted from total, and the hero is set
                // as having attacked.
                this.getMinions().get(action.getAffectedRow()).get(idx).setIsFrozen(1);
                this.getPlayers().get(this.currentPlayer - 1).setMana(
                        this.getPlayers().get(this.currentPlayer - 1).getMana() - this.getPlayers()
                                .get(currentPlayer - 1)
                                .getChosenHero().getMana());
                this.getPlayers().get(this.currentPlayer - 1).setHasAttacked(1);

            } else {
                // For Empress Thorina, in a similiar matter the highest health minion
                // on the row is found and removed from the board.
                int maxHealth = -1;
                int idx = -1;

                for (int i = 0; i < this.getMinions().get(action.getAffectedRow()).size();
                     i++) {
                    if (this.getMinions().get(action.getAffectedRow()).get(i).getHealth()
                            > maxHealth) {
                        maxHealth = this.getMinions().get(action.getAffectedRow()).get(i)
                                .getHealth();
                        idx = i;
                    }
                }

                this.getMinions().get(action.getAffectedRow()).remove(idx);
                this.getPlayers().get(this.currentPlayer - 1).setMana(
                        this.getPlayers().get(this.currentPlayer - 1).getMana() - this.getPlayers()
                                .get(currentPlayer - 1)
                                .getChosenHero().getMana());
                this.getPlayers().get(this.currentPlayer - 1).setHasAttacked(1);
            }
        } else {
            // For the other two heroes it is checked whether the row is on the player's side.
            if (!(((action.getAffectedRow() == 2 || action.getAffectedRow() == ROW_THREE)
                    && currentPlayer == 1)
                    || ((action.getAffectedRow() == 1 || action.getAffectedRow() == 0)
                            && currentPlayer == 2))) {
                aux.put("command", action.getCommand());
                aux.put("affectedRow", action.getAffectedRow());
                aux.put("error", "Selected row does not belong to the current player.");
                return;
            }
            // If the hero is King Mudface, one health is added to all minions on the row.
            if (this.getPlayers().get(this.currentPlayer - 1).getChosenHero().getName()
                    .equals("King Mudface")) {

                for (int i = 0; i < this.getMinions().get(action.getAffectedRow()).size();
                     i++) {
                    this.getMinions().get(action.getAffectedRow()).get(i).setHealth(
                            this.getMinions().get(action.getAffectedRow()).get(i).getHealth()
                                    + 1);
                }

                this.getPlayers().get(this.currentPlayer - 1).setMana(
                        this.getPlayers().get(this.currentPlayer - 1).getMana() - this.getPlayers()
                                .get(currentPlayer - 1)
                                .getChosenHero().getMana());
                this.getPlayers().get(this.currentPlayer - 1).setHasAttacked(1);
            } else {
                // If the hero is General Kocioraw, one attack is instead added to the
                // minions on one of it's rows.
                for (int i = 0; i < this.getMinions().get(action.getAffectedRow()).size();
                     i++) {
                    this.getMinions().get(action.getAffectedRow()).get(i).setAttackDamage(
                            this.getMinions().get(action.getAffectedRow()).get(i)
                                    .getAttackDamage() + 1);
                }

                this.getPlayers().get(this.currentPlayer - 1).setMana(
                        this.getPlayers().get(this.currentPlayer - 1).getMana() - this.getPlayers()
                                .get(currentPlayer - 1)
                                .getChosenHero().getMana());
                this.getPlayers().get(this.currentPlayer - 1).setHasAttacked(1);
            }
        }

    }

    /**
     * @param finalOut - arrayNode used to write output into the json output file
     * This method runs the actions for a game.
     */
    void executeGame(final ArrayNode finalOut) {
        // The players draw their first card and the addOutput method is called
        // so that the given commands are checked and executed.
        this.players.get(0).getPlayerHand()
                .add(this.players.get(0).getChosenDeck().getCards().get(0));
        this.players.get(0).getChosenDeck().getCards().remove(0);
        this.players.get(1).getPlayerHand()
                .add(this.players.get(1).getChosenDeck().getCards().get(0));
        this.players.get(1).getChosenDeck().getCards().remove(0);

        this.actions.addOutput(finalOut, this);
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(final int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(final ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<ArrayList<Minion>> getMinions() {
        return minions;
    }

    public void setMinions(final ArrayList<ArrayList<Minion>> minions) {
        this.minions = minions;
    }

    public int getPlayerOneWins() {
        return playerOneWins;
    }

    public void setPlayerOneWins(final int playerOneWins) {
        this.playerOneWins = playerOneWins;
    }

    public int getGames() {
        return games;
    }

    public void setGames(final int games) {
        this.games = games;
    }

    public int getPlayerTwoWins() {
        return playerTwoWins;
    }

    public void setPlayerTwoWins(final int playerTwoWins) {
        this.playerTwoWins = playerTwoWins;
    }
}





