package it.unicam.cs.mpgc.rpg126148.app;
import it.unicam.cs.mpgc.rpg126148.combat.CombatSystem;
import it.unicam.cs.mpgc.rpg126148.items.Frammento;
import it.unicam.cs.mpgc.rpg126148.model.Maledizione;
import it.unicam.cs.mpgc.rpg126148.model.Stregone;
import it.unicam.cs.mpgc.rpg126148.world.GestoreFrammenti;
import it.unicam.cs.mpgc.rpg126148.world.GestoreRicompense;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {

        Stregone giocatore = new Stregone("Ryomen");
        GestoreRicompense gestoreRicompense = new GestoreRicompense();
        GestoreFrammenti gestoreFrammenti = new GestoreFrammenti();
        CombatSystem cs = new CombatSystem();

        // simuliamo 3 scontri contro nemici livello 1
        for (int i = 0; i < 3; i++) {
            System.out.println("\n=== SCONTRO " + (i + 1) + " ===");
            Maledizione nemico = new Maledizione("Spirito Maledetto", 1);

            // reset HP dopo ogni scontro
            boolean vittoria = cs.combatti(giocatore, nemico);

            if (vittoria) {
                Optional<Frammento> ricompensa = gestoreRicompense.genera(nemico);
                ricompensa.ifPresent(f -> gestoreFrammenti.aggiungiFrammento(giocatore, f));
            }
        }

        System.out.println("\n=== TECNICHE SBLOCCATE ===");
        giocatore.getTecnicheSbloccate()
                .forEach(t -> System.out.println("  - " + t.nome()));
    }
}