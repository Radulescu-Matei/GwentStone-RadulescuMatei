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
     * @param playerOne
     * @param playerTwo
     * @param actions
     * @param hero1
     * @param hero2
     * @param startingPlayer
     * @param shuffleSeed
     * @param playerOneWins
     * @param playerTwoWins
     * @param games
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
     *
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
     * @param aux
     * @param action
     */
    public void placeCard(final ObjectNode aux, final ActionsInput action) {
        Card card;

        card = this.players.get(this.currentPlayer - 1).getPlayerHand().get(action.getHandIdx());

        if (card instanceof Environment) {
            aux.put("command", action.getCommand());
            aux.put("handIdx", action.getHandIdx());
            aux.put("error", "Cannot place environment card on table.");

        } else if (this.players.get(currentPlayer - 1).getMana() < card.getMana()) {
            aux.put("command", action.getCommand());
            aux.put("handIdx", action.getHandIdx());
            aux.put("error", "Not enough mana to place card on table.");
        } else {
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
     * @param aux
     * @param action
     */
    public void useEnvironment(final ObjectNode aux, final ActionsInput action) {
        Card card;
        int oppositeRow = ROW_THREE;
        if (action.getAffectedRow() == ROW_THREE) {
            oppositeRow = 0;
        } else if (action.getAffectedRow() == 2) {
            oppositeRow = 1;
        } else if (action.getAffectedRow() == 1) {
            oppositeRow = 2;
        }


        card = this.players.get(this.currentPlayer - 1).getPlayerHand().get(action.getHandIdx());
        if (card instanceof Minion) {
            aux.put("command", action.getCommand());
            aux.put("handIdx", action.getHandIdx());
            aux.put("affectedRow", action.getAffectedRow());
            aux.put("error", "Chosen card is not of type environment.");

        } else if (this.players.get(this.currentPlayer - 1).getMana() < card.getMana()) {
            aux.put("command", action.getCommand());
            aux.put("handIdx", action.getHandIdx());
            aux.put("affectedRow", action.getAffectedRow());
            aux.put("error", "Not enough mana to use environment card.");
        } else if ((this.currentPlayer == 1 && (action.getAffectedRow() > 1)) || (
                this.currentPlayer == 2 && (action.getAffectedRow() <= 1))) {
            aux.put("command", action.getCommand());
            aux.put("handIdx", action.getHandIdx());
            aux.put("affectedRow", action.getAffectedRow());
            aux.put("error", "Chosen row does not belong to the enemy.");

        } else {
            Environment environment = (Environment) card;
            if (card.getName().equals("Heart Hound")) {
                if (this.getMinions().get(oppositeRow).size() == SUM_FIVE) {
                    aux.put("command", action.getCommand());
                    aux.put("handIdx", action.getHandIdx());
                    aux.put("affectedRow", action.getAffectedRow());
                    aux.put("error",
                            "Cannot steal enemy card since the player's row is" + " full.");
                    return;
                } else {
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
                for (int i = 0; i < this.getMinions().get(action.getAffectedRow()).size(); i++) {
                    this.getMinions().get(action.getAffectedRow()).get(i).setIsFrozen(1);
                }

            } else if (card.getName().equals("Firestorm")) {
                for (int i = 0; i < this.getMinions().get(action.getAffectedRow()).size(); i++) {
                    this.getMinions().get(action.getAffectedRow()).get(i).setHealth(
                            this.getMinions().get(action.getAffectedRow()).get(i).getHealth() - 1);

                    if (this.getMinions().get(action.getAffectedRow()).get(i).getHealth() <= 0) {
                        this.getMinions().get(action.getAffectedRow()).remove(i);
                        i--;
                    }

                }
            }

            this.players.get(this.currentPlayer - 1).setMana(
                    this.players.get(this.currentPlayer - 1).getMana() - environment.getMana());
            this.players.get(this.currentPlayer - 1).getPlayerHand().remove(action.getHandIdx());
        }
    }

    /**
     * @param aux
     * @param action
     */
    public void attackEnemy(final ObjectNode aux, final ActionsInput action) {

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

            this.minions.get(action.getCardAttacker().getX()).get(action.getCardAttacker().getY())
                    .setHasAttacked(1);
            this.minions.get(action.getCardAttacked().getX()).get(action.getCardAttacked().getY())
                    .setHealth(this.minions.get(action.getCardAttacked().getX())
                            .get(action.getCardAttacked().getY()).getHealth() - this.getMinions()
                            .get(action.getCardAttacker().getX())
                            .get(action.getCardAttacker().getY()).getAttackDamage());


            if (this.minions.get(action.getCardAttacked().getX())
                    .get(action.getCardAttacked().getY()).getHealth() <= 0) {
                this.minions.get(action.getCardAttacked().getX())
                        .remove(action.getCardAttacked().getY());
            }
        }
    }

    /**
     * @param aux
     * @param action
     */
    public void cardAbility(final ObjectNode aux, final ActionsInput action) {
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

            this.minions.get(action.getCardAttacked().getX()).get(action.getCardAttacked().getY())
                    .setHealth(this.minions.get(action.getCardAttacked().getX())
                            .get(action.getCardAttacked().getY()).getHealth() + 2);
            this.minions.get(action.getCardAttacker().getX()).get(action.getCardAttacker().getY())
                    .setHasAttacked(1);

        } else {
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


            } else if (at.getName().equals("Miraj")) {
                int swap = attack.getHealth();
                this.minions.get(action.getCardAttacked().getX())
                        .get(action.getCardAttacked().getY()).setHealth(at.getHealth());
                this.minions.get(action.getCardAttacker().getX())
                        .get(action.getCardAttacker().getY()).setHasAttacked(1);
                this.minions.get(action.getCardAttacker().getX())
                        .get(action.getCardAttacker().getY()).setHealth(swap);
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

        }
    }

    /**
     * @param aux
     * @param action
     */
    public void attackHero(final ObjectNode aux, final ActionsInput action) {
        if (this.getMinions().get(action.getCardAttacker().getX())
                .get(action.getCardAttacker().getY()).getIsFrozen() == 1) {
            aux.put("command", action.getCommand());
            ObjectNode attacker = new ObjectNode(JsonNodeFactory.instance);
            attacker.put("x", action.getCardAttacker().getX());
            attacker.put("y", action.getCardAttacker().getY());
            aux.put("cardAttacker", attacker);
            aux.put("error", "Attacker card is frozen.");
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
     * @param aux
     * @param action
     */
    public void useHeroAbility(final ObjectNode aux, final ActionsInput action) {
        if (this.getPlayers().get(this.currentPlayer - 1).getMana() < this.getPlayers()
                .get(this.currentPlayer - 1).getChosenHero().getMana()) {
            aux.put("command", action.getCommand());
            aux.put("affectedRow", action.getAffectedRow());
            aux.put("error", "Not enough mana to use hero's ability.");
        } else if (this.getPlayers().get(this.currentPlayer - 1).getHasAttacked() == 1) {
            aux.put("command", action.getCommand());
            aux.put("affectedRow", action.getAffectedRow());
            aux.put("error", "Hero has already attacked this turn.");
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

                this.getMinions().get(action.getAffectedRow()).get(idx).setIsFrozen(1);
                this.getPlayers().get(this.currentPlayer - 1).setMana(
                        this.getPlayers().get(this.currentPlayer - 1).getMana() - this.getPlayers()
                                .get(currentPlayer - 1)
                                .getChosenHero().getMana());
                this.getPlayers().get(this.currentPlayer - 1).setHasAttacked(1);

            } else {
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
            if (!(((action.getAffectedRow() == 2 || action.getAffectedRow() == ROW_THREE)
                    && currentPlayer == 1)
                    || ((action.getAffectedRow() == 1 || action.getAffectedRow() == 0)
                            && currentPlayer == 2))) {
                aux.put("command", action.getCommand());
                aux.put("affectedRow", action.getAffectedRow());
                aux.put("error", "Selected row does not belong to the current player.");
                return;
            }

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
     * @param finalOut
     */
    void executeGame(final ArrayNode finalOut) {
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





