package it.unicam.cs.mpgc.rpg126148.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventario {
    private final List<Oggetto> oggetti;

    public Inventario() {
        this.oggetti = new ArrayList<>();
    }

    public void aggiungi(Oggetto oggetto) {
        oggetti.add(oggetto);
    }

    public void rimuovi(Oggetto oggetto) {
        oggetti.remove(oggetto);
    }

    public List<Oggetto> getOggetti() {
        return Collections.unmodifiableList(oggetti);
    }

    public boolean contiene(String nome) {
        return oggetti.stream().anyMatch(o -> o.getNome().equals(nome));
    }

    public void stampa() {
        if (oggetti.isEmpty()) {
            System.out.println("Inventario vuoto.");
            return;
        }
        oggetti.forEach(o ->
                System.out.println("- " + o.getNome() + ": " + o.getDescrizione())
        );
    }
}