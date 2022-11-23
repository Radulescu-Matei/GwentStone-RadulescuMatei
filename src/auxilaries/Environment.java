package auxilaries;


import fileio.CardInput;


public final class Environment extends Card {

    Environment() {
    }

    /**
     * @param cInp - input that holds the data for one card
     * This method parses an environment card.
     */
    void inputEnvironment(final CardInput cInp) {
        this.setColors(cInp.getColors());
        this.setName(cInp.getName());
        this.setDescription(cInp.getDescription());
        this.setMana(cInp.getMana());
    }
}


