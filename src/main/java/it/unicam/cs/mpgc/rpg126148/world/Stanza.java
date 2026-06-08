package it.unicam.cs.mpgc.rpg126148.world;

public class Stanza {
    private final String nome;
    private final int xInizio;
    private final int yInizio;
    private final int larghezza;
    private final int altezza;

    public Stanza(String nome, int xInizio, int yInizio, int larghezza, int altezza) {
        this.nome = nome;
        this.xInizio = xInizio;
        this.yInizio = yInizio;
        this.larghezza = larghezza;
        this.altezza = altezza;
    }

    public boolean contiene(int x, int y) {
        return x >= xInizio && x < xInizio + larghezza
                && y >= yInizio && y < yInizio + altezza;
    }

    public String getNome() { return nome; }
    public int getXInizio() { return xInizio; }
    public int getYInizio() { return yInizio; }
    public int getLarghezza() { return larghezza; }
    public int getAltezza() { return altezza; }
}
