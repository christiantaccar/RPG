package it.unicam.cs.mpgc.rpg126148.items;

public abstract class Oggetto {
    private final String nome;
    private final String descrizione;

    public Oggetto(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public String getNome() { return nome; }
    public String getDescrizione() { return descrizione; }

    public abstract void usa();
}