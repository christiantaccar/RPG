package it.unicam.cs.mpgc.rpg126148.model;

public class Maledizione extends Personaggio {
    private int livelloMinaccia;

    public Maledizione(String nome,int livelloMinaccia) {
        super(nome,
                50+livelloMinaccia*10,
                10+livelloMinaccia*2,
                2+livelloMinaccia);
        this.livelloMinaccia = livelloMinaccia;
    }
    public int getLivelloMinaccia() {
        return livelloMinaccia;
    }
}
