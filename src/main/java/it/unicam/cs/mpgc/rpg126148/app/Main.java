package it.unicam.cs.mpgc.rpg126148.app;

import it.unicam.cs.mpgc.rpg126148.items.Frammento;
import it.unicam.cs.mpgc.rpg126148.items.TipoFrammento;
import it.unicam.cs.mpgc.rpg126148.model.Stregone;
import it.unicam.cs.mpgc.rpg126148.persistence.GestoreSalvataggio;
import it.unicam.cs.mpgc.rpg126148.persistence.SalvataggioStato;
import it.unicam.cs.mpgc.rpg126148.tecniche.RegistroTecniche;
import it.unicam.cs.mpgc.rpg126148.tecniche.Tecnica;
import it.unicam.cs.mpgc.rpg126148.world.*;

public class Main {
    public static void main(String[] args) {

        // carica salvataggio
        GestoreSalvataggio gestoreSalvataggio = new GestoreSalvataggio();
        SalvataggioStato statoCaricato = gestoreSalvataggio.carica();

        String nomeGiocatore = (statoCaricato != null) ? statoCaricato.nome : "Ryomen";
        Stregone giocatore = new Stregone(nomeGiocatore);

        // ripristina frammenti
        if (statoCaricato != null && statoCaricato.frammenti != null) {
            statoCaricato.frammenti.forEach((tipo, quantita) -> {
                for (int i = 0; i < quantita; i++) {
                    giocatore.getInventario().aggiungi(
                            new Frammento(TipoFrammento.valueOf(tipo))
                    );
                }
            });
        }

        // ripristina tecniche sbloccate
        if (statoCaricato != null && statoCaricato.tecnicheSbloccate != null) {
            statoCaricato.tecnicheSbloccate.forEach(nomeTecnica -> {
                Tecnica tecnica = RegistroTecniche.get(nomeTecnica);
                if (tecnica != null) {
                    giocatore.aggiungiTecnicaSbloccata(tecnica);
                }
            });
        }

        // costruisci la mappa
        // costruisci la mappa
        Mappa mappa = new Mappa(20, 20);

    // stanza 1 — angolo sinistra 6x3 (da x=1,y=1)
        mappa.aggiungiStanza(new Stanza("Villaggio Abbandonato", 1, 1, 6, 3));
        mappa.setIngresso(6, 2); // ingresso destra della stanza 1

    // corridoio orizzontale tra le due stanze
        mappa.aggiungiCorridoio(7, 2, 13, 2);

    // stanza 2 — angolo destra 4x4 (da x=14,y=1)
        mappa.aggiungiStanza(new Stanza("Rovine del Tempio", 14, 1, 4, 4));
        mappa.setIngresso(14, 2); // ingresso sinistra della stanza 2

        // nemico davanti alla cassa, cassa nell'angolo
        mappa.posizionaNemico(15, 3);
        mappa.posizionaCassa(16, 3);

        // ripristina posizione
        if (statoCaricato != null) {
            mappa.posizionaGiocatore(statoCaricato.xGiocatore, statoCaricato.yGiocatore);
        } else {
            mappa.posizionaGiocatore(3, 2); // posizione default
        }

        // avvia
        GestoreMovimento gestoreMovimento = new GestoreMovimento(mappa, giocatore);
        gestoreMovimento.avvia();

        // salva
        gestoreSalvataggio.salva(giocatore, mappa);


    }
}