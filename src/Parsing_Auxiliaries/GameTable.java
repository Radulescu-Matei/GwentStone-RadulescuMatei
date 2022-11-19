package Parsing_Auxiliaries;

import Output.Actions;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;
import fileio.DecksInput;
import fileio.Input;

import java.util.ArrayList;

public class GameTable {
    ArrayList<ArrayList<Minion>> minions = new ArrayList<>();

    ArrayList<Player> players = new ArrayList<>();

    int turnCounter;

    int currentPlayer, startingPlayer;
    Actions actions;

  GameTable(Deck PlayerOne, Deck PlayerTwo, ArrayList<ActionsInput> actions, CardInput hero1 ,CardInput hero2, int startingPlayer, int shuffleSeed){
    Hero H1 = new Hero(hero1), H2 = new Hero(hero2);

    Player P1 = new Player(H1, PlayerOne, shuffleSeed);
    Player P2 = new Player(H2, PlayerTwo, shuffleSeed);

    this.players.add(P1);
    this.players.add(P2);


    this.currentPlayer = startingPlayer;
    this.startingPlayer = startingPlayer;
    this.turnCounter = 1;
    this.actions = new Actions(actions);

    this.minions.add(new ArrayList<>());
    this.minions.add(new ArrayList<>());
    this.minions.add(new ArrayList<>());
    this.minions.add(new ArrayList<>());
  }
  public void endTurn(){

      if(this.currentPlayer == 1){
          this.currentPlayer = 2;
          for(int i = 0; i < this.minions.get(2).size(); i++){
              this.minions.get(2).get(i).isFrozen = 0;
              this.minions.get(2).get(i).hasAttacked = 0;
          }
          for(int i = 0; i < this.minions.get(3).size(); i++){
              this.minions.get(3).get(i).isFrozen = 0;
              this.minions.get(3).get(i).hasAttacked = 0;
          }
      }else{
          this.currentPlayer = 1;
          for(int i = 0; i < this.minions.get(1).size(); i++){
              this.minions.get(1).get(i).isFrozen = 0;
              this.minions.get(1).get(i).hasAttacked = 0;
          }
          for(int i = 0; i < this.minions.get(0).size(); i++){
              this.minions.get(0).get(i).isFrozen = 0;
              this.minions.get(0).get(i).hasAttacked = 0;
          }
      }

      if(this.currentPlayer == this.startingPlayer){
          if(this.getPlayers().get(0).getChosenDeck().getCards().size() > 0) {
              this.getPlayers().get(0).getPlayerHand().add(this.players.get(0).getChosenDeck().getCards().get(0));
              this.getPlayers().get(0).getChosenDeck().getCards().remove(0);
          }
          if(this.getPlayers().get(1).getChosenDeck().getCards().size() > 0) {
              this.getPlayers().get(1).getPlayerHand().add(this.players.get(1).getChosenDeck().getCards().get(0));
              this.getPlayers().get(1).getChosenDeck().getCards().remove(0);
          }
          if(this.turnCounter < 10)
              this.turnCounter += 1;

          this.players.get(0).mana += this.turnCounter;
          this.players.get(1).mana += this.turnCounter;

      }
  }

  public void placeCard(ObjectNode aux, ActionsInput action){
      Card card;

      card = this.players.get(this.currentPlayer - 1).getPlayerHand().get(action.getHandIdx());

      if(card instanceof Environment){
          aux.put("command", action.getCommand());
          aux.put("handIdx", action.getHandIdx());
          aux.put("error", "Cannot place environment card on table.");

    }else if(this.players.get(currentPlayer - 1).mana < card.mana){
          aux.put("command", action.getCommand());
          aux.put("handIdx", action.getHandIdx());
          aux.put("error", "Not enough mana to place card on table.");
  }else{
          Minion minion =(Minion)card;

          if(card.name.equals("The Ripper") || card.name.equals("Miraj") || card.name.equals("Goliath") || card.name.equals("Warden")){
              if(currentPlayer == 1){
                  if(this.minions.get(2).size() == 5){
                      aux.put("command", action.getCommand());
                      aux.put("handIdx", action.getHandIdx());
                      aux.put("error", "Cannot place card on table since row is full.");
                  }else {
                      this.minions.get(2).add(minion);
                      this.players.get(this.currentPlayer - 1).mana -= minion.mana;
                      this.players.get(this.currentPlayer - 1).getPlayerHand().remove(action.getHandIdx());
                  }
              }else {
                  if(this.minions.get(1).size() == 5){
                      aux.put("command", action.getCommand());
                      aux.put("handIdx", action.getHandIdx());
                      aux.put("error", "Cannot place card on table since row is full.");
                  }else{
                      this.minions.get(1).add(minion);
                      this.players.get(currentPlayer - 1).mana -= minion.mana;
                      this.players.get(currentPlayer - 1).getPlayerHand().remove(action.getHandIdx());
                  }
              }
          }else{
              if(currentPlayer == 1){
                  if(this.minions.get(3).size() == 5){
                      aux.put("command", action.getCommand());
                      aux.put("handIdx", action.getHandIdx());
                      aux.put("error", "Cannot place card on table since row is full.");
                  }else {
                      this.minions.get(3).add(minion);
                      this.players.get(0).mana -= minion.mana;
                      this.players.get(0).getPlayerHand().remove(action.getHandIdx());
                  }
              }else {
                  if(this.minions.get(0).size() == 5){
                      aux.put("command", action.getCommand());
                      aux.put("handIdx", action.getHandIdx());
                      aux.put("error", "Cannot place card on table since row is full.");
                  }else {
                      this.minions.get(0).add(minion);
                      this.players.get(1).mana -= minion.mana;
                      this.players.get(1).getPlayerHand().remove(action.getHandIdx());
                  }
              }

          }
      }
  }

    public void useEnvironment(ObjectNode aux, ActionsInput action){
        Card card;
        int oppositeRow = 3;
        if(action.getAffectedRow() == 3){
            oppositeRow = 0;
        }else if(action.getAffectedRow() == 2){
            oppositeRow = 1;
        }else if(action.getAffectedRow() == 1){
            oppositeRow = 2;
        }


        card = this.players.get(this.currentPlayer - 1).getPlayerHand().get(action.getHandIdx());
        if(card instanceof Minion){
            aux.put("command", action.getCommand());
            aux.put("handIdx", action.getHandIdx());
            aux.put("affectedRow", action.getAffectedRow());
            aux.put("error", "Chosen card is not of type environment.");

        }else if(this.players.get(this.currentPlayer - 1).mana < card.mana){
            aux.put("command", action.getCommand());
            aux.put("handIdx", action.getHandIdx());
            aux.put("affectedRow", action.getAffectedRow());
            aux.put("error", "Not enough mana to use environment card.");
        }else if((this.currentPlayer == 1 && (action.getAffectedRow() > 1)) || (this.currentPlayer == 2 && (action.getAffectedRow() <= 1))) {
            aux.put("command", action.getCommand());
            aux.put("handIdx", action.getHandIdx());
            aux.put("affectedRow", action.getAffectedRow());
            aux.put("error", "Chosen row does not belong to the enemy.");

        }else {
            Environment environment = (Environment)card;
            if(card.name.equals("Heart Hound")){
                if(this.getMinions().get(oppositeRow).size() == 5){
                    aux.put("command", action.getCommand());
                    aux.put("handIdx", action.getHandIdx());
                    aux.put("affectedRow", action.getAffectedRow());
                    aux.put("error", "Cannot steal enemy card since the player's row is full.");
                    return;
                }else{
                    int maxHealth = 0;
                    int maxIndex = -1;
                    for(int i = 0; i < this.getMinions().get(action.getAffectedRow()).size(); i++){
                        if(this.getMinions().get(action.getAffectedRow()).get(i).getHealth() > maxHealth){
                            maxHealth = this.getMinions().get(action.getAffectedRow()).get(i).getHealth();
                            maxIndex = i;
                        }
                    }
                    Minion stolen = this.minions.get(action.getAffectedRow()).get(maxIndex);
                    this.minions.get(action.getAffectedRow()).remove(maxIndex);

                    this.minions.get(oppositeRow).add(stolen);
                }

            }else if(card.name.equals("Winterfell")){
                for(int i = 0; i < this.getMinions().get(action.getAffectedRow()).size(); i++){
                    this.getMinions().get(action.getAffectedRow()).get(i).isFrozen = 1;
                }

            }else if(card.name.equals("Firestorm")){
                for(int i = 0; i < this.getMinions().get(action.getAffectedRow()).size(); i++){
                    this.getMinions().get(action.getAffectedRow()).get(i).health -= 1;

                    if(this.getMinions().get(action.getAffectedRow()).get(i).health <= 0){
                        this.getMinions().get(action.getAffectedRow()).remove(i);
                        i--;
                    }

                }
            }

            this.players.get(this.currentPlayer - 1).mana -= environment.mana;
            this.players.get(this.currentPlayer - 1).getPlayerHand().remove(action.getHandIdx());
        }
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

    public ArrayList<ArrayList<Minion>> getMinions() {
        return minions;
    }

    public void setMinions(ArrayList<ArrayList<Minion>> minions) {
        this.minions = minions;
    }
}





