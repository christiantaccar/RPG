package it.unicam.cs.mpgc.rpg126148.app;
import it.unicam.cs.mpgc.rpg126148.model.*;
import it.unicam.cs.mpgc.rpg126148.combat.CombatSystem;

public class Main {

    public static void main(String[] args) {

        Stregone s = new Stregone("Stregone Protagonista");
        Maledizione m = new Maledizione("Maledizione Minore", 1);

        CombatSystem combat = new CombatSystem();
        combat.combatti(s, m);
    }
}