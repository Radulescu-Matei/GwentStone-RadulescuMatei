package auxilaries;


import fileio.CardInput;


public final class Environment extends Card {

    Environment() {
    }

    /**
     * @param cInp
     */
    void inputEnvironment(final CardInput cInp) {
        this.setColors(cInp.getColors());
        this.setName(cInp.getName());
        this.setDescription(cInp.getDescription());
        this.setMana(cInp.getMana());
    }
}


