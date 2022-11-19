package Parsing_Auxiliaries;

import Parsing_Auxiliaries.Card;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;
import fileio.DecksInput;
import fileio.Input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {

    ArrayList<Card> cards = new ArrayList<Card>();

    public Deck(ArrayList<Card> cards){

        for(int i = 0; i < cards.size(); i++){
            this.cards.add(cards.get(i));
        }
    }
    Deck(ArrayList<CardInput> deckInput, int size) {
        for(int i = 0; i < size; i++) {
            if(deckInput.get(i).getName().equals("Heart Hound") || deckInput.get(i).getName().equals("Firestorm") || deckInput.get(i).getName().equals("Winterfell")){
                Environment env = new Environment();
                env.inputEnvironment(deckInput.get(i));
                this.cards.add(env);
            }else {
                Minion min = new Minion();
                min.inputMinion(deckInput.get(i));
                this.cards.add(min);
            }
        }
    }
    void ShuffleDeck(int rndSeed) {
        Random rnd = new Random(rndSeed);
        Collections.shuffle(this.cards, rnd);
    }

    public void writeDeck(ObjectNode aux) {
        aux.putArray("output");
        for(int i = 0; i < this.getCards().size(); i++){
            ObjectNode card = new ObjectNode(JsonNodeFactory.instance);
            if(this.getCards().get(i) instanceof  Minion){
                Minion writeMinion = (Minion) this.getCards().get(i);
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
                Environment writeEnvironment = (Environment)this.getCards().get(i);
                card.put("mana", writeEnvironment.getMana());
                card.put("description", writeEnvironment.getDescription());
                for(int j = 0; j < writeEnvironment.getColors().size(); j++) {
                    card.withArray("colors").add(writeEnvironment.getColors().get(j));
                }
                card.put("name", writeEnvironment.getName());
                aux.withArray("output").add(card);
            }
        }
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
