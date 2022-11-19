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
            aux.put("playerIdx", action.getPlayerIdx());

            writeDeck = table.getPlayers().get(action.getPlayerIdx() - 1).getChosenDeck();



            for(int i = 0; i < writeDeck.getCards().size(); i++){
                ObjectNode card = new ObjectNode(JsonNodeFactory.instance);
                if(writeDeck.getCards().get(i) instanceof  Minion){
                Minion writeMinion = (Minion) writeDeck.getCards().get(i);
                card.put("mana", writeMinion.getMana());
                card.put("attackDamage", writeMinion.getAttackDamage());
                card.put("health", writeMinion.getHealth());
                card.put("description", writeMinion.getDescription());
                for(int j = 0; j < writeMinion.getColors().size(); j++) {
                    card.withArray("colors").add(writeMinion.getColors().get(j));
                }
                card.put("name", writeMinion.getName());
                aux.withArray("output").add(card);
            }else {
                    Environment writeEnvironment = (Environment)writeDeck.getCards().get(i);
                    card.put("mana", writeEnvironment.getMana());
                    card.put("description", writeEnvironment.getDescription());
                    for(int j = 0; j < writeEnvironment.getColors().size(); j++) {
                        card.withArray("colors").add(writeEnvironment.getColors().get(j));
                    }
                    card.put("name", writeEnvironment.getName());
                    aux.withArray("output").add(card);
                }
            }

        }else return;
    }
}
