package it.unicam.cs.mpgc.rpg126148.world;

import it.unicam.cs.mpgc.rpg126148.combat.CombatSystem;
import it.unicam.cs.mpgc.rpg126148.items.Frammento;
import it.unicam.cs.mpgc.rpg126148.items.TipoFrammento;
import it.unicam.cs.mpgc.rpg126148.model.Maledizione;
import it.unicam.cs.mpgc.rpg126148.model.Stregone;

import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

public class GestoreMovimento {
    private final Mappa mappa;
    private final Stregone giocatore;
    private final CombatSystem combatSystem;
    private final GestoreRicompense gestoreRicompense;
    private final GestoreFrammenti gestoreFrammenti;
    private final Scanner scanner;
    private final Random random = new Random();

    public GestoreMovimento(Mappa mappa, Stregone giocatore) {
        this.mappa = mappa;
        this.giocatore = giocatore;
        this.combatSystem = new CombatSystem();
        this.gestoreRicompense = new GestoreRicompense();
        this.gestoreFrammenti = new GestoreFrammenti();
        this.scanner = new Scanner(System.in);
    }

    public void avvia() {
        System.out.println("=== BENVENUTO NEL GIOCO ===");
        System.out.println("W=Su A=Sinistra S=Giù D=Destra Q=Esci");

        while (true) {
            mappa.stampa();
            Stanza stanzaCorrente = mappa.getStanzaCorrente();
            if (stanzaCorrente != null) {
                System.out.println("Stanza: " + stanzaCorrente.getNome());
            }
            System.out.println("HP: " + giocatore.getPuntiVita()
                    + " EN: " + giocatore.getEnergiaNera());
            System.out.print("Movimento: ");

            String input = scanner.nextLine().trim().toUpperCase();

            switch (input) {
                case "W" -> muovi(0, -1);
                case "S" -> muovi(0, 1);
                case "A" -> muovi(-1, 0);
                case "D" -> muovi(1, 0);
                case "Q" -> {
                    System.out.println("Uscita dal gioco.");
                    return;
                }
                default -> System.out.println("Comando non valido.");
            }
        }
    }

    private void muovi(int dx, int dy) {
        int nuovoX = mappa.getXGiocatore() + dx;
        int nuovoY = mappa.getYGiocatore() + dy;

        if (nuovoX < 0 || nuovoX >= mappa.getLarghezza()
                || nuovoY < 0 || nuovoY >= mappa.getAltezza()) {
            System.out.println("Non puoi andare oltre i confini.");
            return;
        }

        Cella cella = mappa.getCella(nuovoX, nuovoY);

        if (cella.getTipo() == TipoCella.MURO
                || cella.getTipo() == TipoCella.BORDO) {
            System.out.println("C'è un muro.");
            return;
        }

        mappa.posizionaGiocatore(nuovoX, nuovoY);
        cella.setEsplorata(true);

        switch (cella.getTipo()) {
            case NEMICO -> gestisciNemico(cella);
            case CASSA -> gestisciCassa(cella);
            default -> {}
        }
    }

    private void gestisciNemico(Cella cella) {
        System.out.println("⚔ Un nemico!");
        int livello = 1 + random.nextInt(3);
        Maledizione nemico = new Maledizione("Spirito Maledetto", livello);
        boolean vittoria = combatSystem.combatti(giocatore, nemico);

        if (vittoria) {
            cella.setTipo(TipoCella.VUOTA); // nemico sconfitto
            Optional<Frammento> ricompensa = gestoreRicompense.genera(nemico);
            ricompensa.ifPresent(f -> gestoreFrammenti.aggiungiFrammento(giocatore, f));
        } else {
            System.out.println("Game Over.");
            System.exit(0);
        }
    }

    private void gestisciCassa(Cella cella) {
        System.out.println("📦 Hai trovato una cassa!");
        TipoFrammento[] tipi = TipoFrammento.values();
        Frammento frammento = new Frammento(tipi[random.nextInt(tipi.length)]);
        gestoreFrammenti.aggiungiFrammento(giocatore, frammento);
        cella.setTipo(TipoCella.VUOTA); // cassa aperta
    }
}