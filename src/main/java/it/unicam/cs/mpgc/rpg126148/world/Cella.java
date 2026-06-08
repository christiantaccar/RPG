package it.unicam.cs.mpgc.rpg126148.world;

public class Cella {
    private final int x;
    private final int y;
    private TipoCella tipo;
    private boolean esplorata;

    public Cella(int x, int y, TipoCella tipo) {
        this.x = x;
        this.y = y;
        this.tipo = tipo;
        this.esplorata = false;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public TipoCella getTipo() { return tipo; }
    public boolean isEsplorata() { return esplorata; }

    public void setTipo(TipoCella tipo) { this.tipo = tipo; }
    public void setEsplorata(boolean esplorata) { this.esplorata = esplorata; }
}
