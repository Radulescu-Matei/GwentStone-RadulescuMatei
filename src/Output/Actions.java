package Output;

import Parsing_Auxiliaries.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;

import java.util.ArrayList;

public class Actions {
    ArrayList<ActionsInput> actions;

    public Actions(ArrayList<ActionsInput> actions) {
        this.actions = actions;
    }
    public void addOutput(ArrayNode finalOut, GameTable table){
        for(int i = 0; i < this.actions.size(); i++) {
            ObjectNode aux = new ObjectNode(JsonNodeFactory.instance);
            checkCommand(this.actions.get(i), aux, table);
            if(!aux.isEmpty())
                finalOut.add(aux);
        }
    }

    void checkCommand(ActionsInput action, ObjectNode aux, GameTable table) {

        if(action.getCommand().equals("getPlayerTurn")){
            aux.put("command", action.getCommand());
            aux.put("output", table.getCurrentPlayer());
        }else if(action.getCommand().equals("getPlayerHero")){
            aux.put("command", action.getCommand());
            aux.put("playerIdx", action.getPlayerIdx());
            Hero writePlayer = table.getPlayers().get(action.getPlayerIdx() - 1).getChosenHero();

            ObjectNode hero = new ObjectNode(JsonNodeFactory.instance);
            hero.put("mana", writePlayer.getMana());
            hero.put("description", writePlayer.getDescription());
            for(int i = 0; i < writePlayer.getColors().size(); i++) {
                hero.withArray("colors").add(writePlayer.getColors().get(i));
            }
            hero.put("name", writePlayer.getName());
            hero.put("health", writePlayer.getHealth());
            aux.put("output", hero);

        }else if(action.getCommand().equals("getPlayerDeck")){
           Deck writeDeck;
            aux.put("command", action.getCommand());


            writeDeck = table.getPlayers().get(action.getPlayerIdx() - 1).getChosenDeck();
            ObjectNode helper = new ObjectNode(JsonNodeFactory.instance);
            ObjectNode playerIdx = new ObjectNode(JsonNodeFactory.instance);
            playerIdx.put("playerIdx", action.getPlayerIdx());
            writeDeck.writeDeck(aux);
            aux.put("playerIdx", action.getPlayerIdx());


        }else if(action.getCommand().equals("endPlayerTurn")){
                table.endTurn();
        }else if(action.getCommand().equals("getCardsInHand")){
            Deck writeDeck = new Deck(table.getPlayers().get(action.getPlayerIdx() - 1).getPlayerHand());
            aux.put("command", action.getCommand());
            aux.put("playerIdx", action.getPlayerIdx());
            writeDeck.writeDeck(aux);
        }else if(action.getCommand().equals("getPlayerMana")) {
            aux.put("command", action.getCommand());
            aux.put("playerIdx", action.getPlayerIdx());
            aux.put("output", table.getPlayers().get(action.getPlayerIdx() - 1).getMana());
        }else if(action.getCommand().equals("placeCard")){
            table.placeCard(aux, action);
        }else if(action.getCommand().equals("getCardsOnTable")){
            aux.put("command", action.getCommand());
            ArrayNode finalobj = new ArrayNode(JsonNodeFactory.instance);
            for(int k = 0; k < 4; k++){
                ArrayList<Card> cards = new ArrayList<>();
                for(int j = 0; j < table.getMinions().get(k).size(); j++){
                    cards.add(table.getMinions().get(k).get(j));
                }
                Deck minions = new Deck(cards);
                ArrayNode rows = new ArrayNode(JsonNodeFactory.instance);

                for(int i = 0; i < minions.getCards().size(); i++){
                    ObjectNode card = new ObjectNode(JsonNodeFactory.instance);

                    Minion writeMinion = (Minion) minions.getCards().get(i);
                    card.put("mana", writeMinion.getMana());
                    card.put("attackDamage", writeMinion.getAttackDamage());
                    card.put("health", writeMinion.getHealth());
                    card.put("description", writeMinion.getDescription());
                    for(int j = 0; j < writeMinion.getColors().size(); j++) {
                        card.withArray("colors").add(writeMinion.getColors().get(j));
                        }
                    card.put("name", writeMinion.getName());
                    rows.add(card);

                }
                finalobj.add(rows);
            }
            aux.put("output", finalobj);
        }else if(action.getCommand().equals("getEnvironmentCardsInHand")){
           Deck writeDeck = new Deck(table.getPlayers().get(action.getPlayerIdx() - 1).getPlayerHand());
            aux.put("command", action.getCommand());
            aux.put("playerIdx", action.getPlayerIdx());
            for(int i = 0; i < writeDeck.getCards().size(); i++){
                if(writeDeck.getCards().get(i) instanceof Minion){
                    writeDeck.getCards().remove(i);
                    i--;
                }
            }
            writeDeck.writeDeck(aux);

        }else if(action.getCommand().equals("getCardAtPosition")){
            aux.put("command", action.getCommand());
            aux.put("x", action.getX());
            aux.put("y", action.getY());
            if(table.getMinions().size() <= action.getX()){
                aux.put("output", "No card available at that position.");

            }else if(table.getMinions().get(action.getX()).size() <= action.getY()){
                aux.put("output", "No card available at that position.");
            }else {
                Minion writeMinion = table.getMinions().get(action.getX()).get(action.getY());
                ObjectNode card = new ObjectNode(JsonNodeFactory.instance);

                card.put("mana", writeMinion.getMana());
                card.put("attackDamage", writeMinion.getAttackDamage());
                card.put("health", writeMinion.getHealth());
                card.put("description", writeMinion.getDescription());
                for(int j = 0; j < writeMinion.getColors().size(); j++) {
                    card.withArray("colors").add(writeMinion.getColors().get(j));
                }
                card.put("name", writeMinion.getName());

                aux.put("output", card);
            }
        }else if(action.getCommand().equals("useEnvironmentCard")){
            table.useEnvironment(aux, action);
        }else if(action.getCommand().equals("getFrozenCardsOnTable")){
            aux.put("command", action.getCommand());
            ArrayList<Card> frozen = new ArrayList<>();

            for(int i = 0; i < table.getMinions().size(); i++){
                for(int j = 0; j < table.getMinions().get(i).size(); j++){
                    if(table.getMinions().get(i).get(j).getIsFrozen() == 1){
                        frozen.add(table.getMinions().get(i).get(j));
                    }
                }
            }

            Deck writeDeck = new Deck(frozen);
            writeDeck.writeDeck(aux);
        }else if(action.getCommand().equals("cardUsesAttack")){
            table.attackEnemy(aux, action);
        }else if(action.getCommand().equals("cardUsesAbility")){
            table.cardAbility(aux,action);
        }else if(action.getCommand().equals("useAttackHero")){
            table.attackHero(aux, action);
        }else if(action.getCommand().equals("useHeroAbility")){
            table.useHeroAbility(aux, action);
        }else if(action.getCommand().equals("getTotalGamesPlayed")){
            aux.put("command", action.getCommand());
            aux.put("output", table.getGames());
        }else if(action.getCommand().equals("getPlayerOneWins")){
            aux.put("command", action.getCommand());
            aux.put("output", table.getPlayerOneWins());
        }else if(action.getCommand().equals("getPlayerTwoWins")){
            aux.put("command", action.getCommand());
            aux.put("output", table.getPlayerTwoWins());
        }
    }
}
