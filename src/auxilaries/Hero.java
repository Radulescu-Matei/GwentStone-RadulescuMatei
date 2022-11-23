package auxilaries;

import fileio.CardInput;

public final class Hero extends Card {
    private static final int HERO_HEALTH = 30;
    private int health = HERO_HEALTH;

    Hero() {
        this.health = HERO_HEALTH;
    }

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
