package it.unicam.cs.mpgc.rpg126148.model;

import it.unicam.cs.mpgc.rpg126148.items.Inventario;
import it.unicam.cs.mpgc.rpg126148.items.Pergamena;
import it.unicam.cs.mpgc.rpg126148.tecniche.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stregone extends Personaggio {
    private int energiaNera;
    private final int energiaNeraMassima;
    private final List<Tecnica> tecnicheFisiche;
    private final List<Tecnica> tecnicheSbloccate;
    private final Inventario inventario;

    public Stregone(String nome) {
        super(nome, 100, 15, 5);
        this.energiaNeraMassima = 100;
        this.energiaNera = energiaNeraMassima;
        this.inventario = new Inventario();

        // tecniche fisiche sempre disponibili
        this.tecnicheFisiche = new ArrayList<>();
        tecnicheFisiche.add(new Pugno());
        tecnicheFisiche.add(new Calcio());

        // magie sbloccate tramite pergamene
        this.tecnicheSbloccate = new ArrayList<>();
    }

    // --- Pergamene ---

    public void sbloccaTecnica(Pergamena pergamena) {
        if (pergamena.isUsata()) {
            System.out.println("Questa pergamena è già stata usata.");
            return;
        }
        boolean giaPresente = tecnicheSbloccate.stream()
                .anyMatch(t -> t.nome().equals(pergamena.getTecnicaContenuta().nome()));
        if (giaPresente) {
            System.out.println("Tecnica già sbloccata.");
            return;
        }
        tecnicheSbloccate.add(pergamena.getTecnicaContenuta());
        pergamena.usa();
        System.out.println("Tecnica sbloccata: " + pergamena.getTecnicaContenuta().nome());

    }

    // --- Energia ---

    public boolean haEnergia(int costo) { return energiaNera >= costo; }

    public void consumaEnergia(int valore) {
        energiaNera = Math.max(0, energiaNera - valore);
    }

    // --- Getter ---

    public List<Tecnica> getTecnicheFisiche() {
        return Collections.unmodifiableList(tecnicheFisiche);
    }

    public List<Tecnica> getTecnicheSbloccate() {
        return Collections.unmodifiableList(tecnicheSbloccate);
    }

    public Inventario getInventario() { return inventario; }

    public int getEnergiaNera() { return energiaNera; }
    public int getEnergiaNeraMassima() { return energiaNeraMassima; }
    //Aggiungere alle tecniche in combattimento quelle presenti nel file di salvataggio
    public void aggiungiTecnicaSbloccata(Tecnica tecnica) {
        boolean giaPresente = tecnicheSbloccate.stream()
                .anyMatch(t -> t.nome().equals(tecnica.nome()));
        if (!giaPresente) {
            tecnicheSbloccate.add(tecnica);
        }
    }
    public void recuperaEnergia(int valore) {
        energiaNera = Math.min(energiaNeraMassima, energiaNera + valore);
    }
    // cura rimane in Personaggio, qui esponiamo solo ciò che serve
}