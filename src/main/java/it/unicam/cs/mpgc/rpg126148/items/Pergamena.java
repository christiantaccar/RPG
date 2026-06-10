package it.unicam.cs.mpgc.rpg126148.items;

import it.unicam.cs.mpgc.rpg126148.tecniche.Tecnica;

public class Pergamena extends Oggetto implements Usabile {
    private final Tecnica tecnicaContenuta;
    private boolean usata;

    public Pergamena(String nome, String descrizione, Tecnica tecnicaContenuta) {
        super(nome, descrizione);
        this.tecnicaContenuta = tecnicaContenuta;
        this.usata = false;
    }

    public Tecnica getTecnicaContenuta() { return tecnicaContenuta; }
    public boolean isUsata() { return usata; }

    @Override
    public void usa() {
        usata = true;
    }
}