package auxilaries;

import fileio.CardInput;

public final class Hero extends Card {
    private static final int HERO_HEALTH = 30;
    private int health = HERO_HEALTH;

    Hero() {
        this.health = HERO_HEALTH;
    }

    /**
     *
     * @param cInp - input that holds the data for one card
     * This method is used to parse the hero cards from the input;
     */
    Hero(final CardInput cInp) {
        this.setName(cInp.getName());
        this.setColors(cInp.getColors());
        this.setMana(cInp.getMana());
        this.setDescription(cInp.getDescription());
    }


    public int getHealth() {
        return health;
    }


    public void setHealth(final int health) {
        this.health = health;
    }
}
