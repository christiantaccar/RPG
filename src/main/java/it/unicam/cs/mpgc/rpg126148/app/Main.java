package it.unicam.cs.mpgc.rpg126148.app;
import it.unicam.cs.mpgc.rpg126148.combat.CombatSystem;
import it.unicam.cs.mpgc.rpg126148.items.Pergamena;
import it.unicam.cs.mpgc.rpg126148.model.Maledizione;
import it.unicam.cs.mpgc.rpg126148.model.Stregone;
import it.unicam.cs.mpgc.rpg126148.tecniche.PallaDiFuoco;

public class Main {

    public static void main(String[] args) {

        Stregone giocatore = new Stregone("Ryomen");
        Maledizione nemico = new Maledizione("Spirito Maledetto", 1);

        // dopo aver vinto un combattimento, sblocchi una pergamena:
        Pergamena pergamena = new Pergamena(
                "Pergamena del Fuoco",
                "Contiene la tecnica Palla di Fuoco",
                new PallaDiFuoco()
        );
        giocatore.sbloccaTecnica(pergamena);

        CombatSystem cs = new CombatSystem();
        cs.combatti(giocatore, nemico);
    }
}