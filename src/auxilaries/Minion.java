package auxilaries;

import fileio.CardInput;

public final class Minion extends Card {
    private int health;
    private int attackDamage;

    private int hasAttacked = 0;
    private int isFrozen = 0;

    public Minion() {

    }

    public Minion(final Minion minion) {
        this.setHealth(minion.getHealth());
        this.setMana(minion.getMana());
        this.setName(minion.getName());
        this.setDescription(minion.getDescription());
        this.setAttackDamage(minion.getAttackDamage());
        this.setColors(minion.getColors());
        this.setIsFrozen(minion.getIsFrozen());
        this.setHasAttacked(minion.getHasAttacked());
    }

    /**
     *
     * @param cInp - input that holds the data for one card
     * This method is used to parse a minion card from input.
     */
    public void inputMinion(final CardInput cInp) {
        this.setHealth(cInp.getHealth());
        this.setMana(cInp.getMana());
        this.setName(cInp.getName());
        this.setColors(cInp.getColors());
        this.setDescription(cInp.getDescription());
        this.setAttackDamage(cInp.getAttackDamage());

    }

    /**
     *
     * @return
     * This method verifies where the minion it's applied to is one of the two that has the tank
     * characteristic.
     */
    public boolean hasTaunt() {
        if (this.getName().equals("Goliath") || this.getName().equals("Warden")) {
            return true;
        }
        return false;
    }

    public int getHealth() {
        return health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getHasAttacked() {
        return hasAttacked;
    }

    public int getIsFrozen() {
        return isFrozen;
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public void setHasAttacked(final int hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    public void setIsFrozen(final int isFrozen) {
        this.isFrozen = isFrozen;
    }


}
