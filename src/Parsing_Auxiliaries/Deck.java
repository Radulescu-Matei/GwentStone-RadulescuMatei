package Parsing_Auxiliaries;

import Parsing_Auxiliaries.Card;
import fileio.CardInput;
import fileio.DecksInput;
import fileio.Input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

class Deck {
    int size;

    ArrayList<Card> cards;
    void inputDeck(ArrayList<CardInput> deckInput, int size) {
        this.size = size;
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
}
