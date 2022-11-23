
package auxilaries;

import java.util.ArrayList;

public abstract class Card {
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;

    Card() {

    }

    public final int getMana() {
        return mana;
    }

    public final String getDescription() {
        return description;
    }

    public final ArrayList<String> getColors() {
        return colors;
    }

    public final String getName() {
        return name;
    }

    public final void setMana(final int mana) {
        this.mana = mana;
    }


    public final void setDescription(final String description) {
        this.description = description;
    }


    public final void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    public final void setName(final String name) {
        this.name = name;
    }
}






