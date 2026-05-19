package it.unicam.cs.mpgc.rpg126148.app;

import it.unicam.cs.mpgc.rpg126148.combat.CombatSystem;
import it.unicam.cs.mpgc.rpg126148.items.Frammento;
import it.unicam.cs.mpgc.rpg126148.model.Maledizione;
import it.unicam.cs.mpgc.rpg126148.model.Stregone;
import it.unicam.cs.mpgc.rpg126148.persistence.GestoreSalvataggio;
import it.unicam.cs.mpgc.rpg126148.persistence.SalvataggioStato;
import it.unicam.cs.mpgc.rpg126148.tecniche.RegistroTecniche;
import it.unicam.cs.mpgc.rpg126148.tecniche.Tecnica;
import it.unicam.cs.mpgc.rpg126148.world.GestoreFrammenti;
import it.unicam.cs.mpgc.rpg126148.world.GestoreRicompense;
import it.unicam.cs.mpgc.rpg126148.items.TipoFrammento;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {

        GestoreSalvataggio gestoreSalvataggio = new GestoreSalvataggio();
        SalvataggioStato statoCaricato = gestoreSalvataggio.carica();

        // nome del giocatore dal salvataggio o default
        String nomeGiocatore = (statoCaricato != null) ? statoCaricato.nome : "Ryomen";
        Stregone giocatore = new Stregone(nomeGiocatore);

// ripristina frammenti dal salvataggio
        if (statoCaricato != null && statoCaricato.frammenti != null) {
            statoCaricato.frammenti.forEach((tipo, quantita) -> {
                for (int i = 0; i < quantita; i++) {
                    giocatore.getInventario().aggiungi(
                            new Frammento(TipoFrammento.valueOf(tipo))
                    );
                }
            });
        }
        if (statoCaricato != null && statoCaricato.tecnicheSbloccate != null) {
            statoCaricato.tecnicheSbloccate.forEach(nomeTecnica -> {
                Tecnica tecnica = RegistroTecniche.get(nomeTecnica);
                if (tecnica != null) {
                    giocatore.aggiungiTecnicaSbloccata(tecnica);
                }
            });
        }

        GestoreRicompense gestoreRicompense = new GestoreRicompense();
        GestoreFrammenti gestoreFrammenti = new GestoreFrammenti();
        CombatSystem cs = new CombatSystem();

        Maledizione nemico = new Maledizione("Spirito Maledetto", 1);
        boolean vittoria = cs.combatti(giocatore, nemico);

        if (vittoria) {
            Optional<Frammento> ricompensa = gestoreRicompense.genera(nemico);
            ricompensa.ifPresent(f -> gestoreFrammenti.aggiungiFrammento(giocatore, f));
        }

        // salva dopo ogni scontro
        gestoreSalvataggio.salva(giocatore);

        System.out.println("\nTecniche sbloccate:");
        giocatore.getTecnicheSbloccate()
                .forEach(t -> System.out.println("  - " + t.nome()));
    }
}