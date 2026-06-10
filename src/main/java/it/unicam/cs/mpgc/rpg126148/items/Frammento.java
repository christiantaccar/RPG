package it.unicam.cs.mpgc.rpg126148.items;

public class Frammento extends Oggetto {
    private final TipoFrammento tipo;

    public Frammento(TipoFrammento tipo) {
        super("Frammento di " + tipo.name(),tipo.name());
        this.tipo = tipo;
    }

    public TipoFrammento getTipo() { return tipo; }

}
