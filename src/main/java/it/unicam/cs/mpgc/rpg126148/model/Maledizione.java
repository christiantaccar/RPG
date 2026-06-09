package it.unicam.cs.mpgc.rpg126148.model;

public class Maledizione extends Personaggio {
    private int livelloMinaccia;

    public Maledizione(String nome,int livelloMinaccia) {
        super(nome,
                30+livelloMinaccia*10,
                5+livelloMinaccia*2,
                1+livelloMinaccia);
        this.livelloMinaccia = livelloMinaccia;
    }
    public int getLivelloMinaccia() {
        return livelloMinaccia;
    }
}
