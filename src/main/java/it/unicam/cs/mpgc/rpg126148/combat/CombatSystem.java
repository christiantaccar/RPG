package it.unicam.cs.mpgc.rpg126148.combat;
import it.unicam.cs.mpgc.rpg126148.model.Stregone;
import it.unicam.cs.mpgc.rpg126148.model.Maledizione;

import java.util.Scanner;

public class CombatSystem {
    private final Scanner scanner = new Scanner(System.in);

    public void combatti(Stregone player, Maledizione nemico) {

        System.out.println("⚔ Inizio combattimento!");

        while (player.eVivo() && nemico.eVivo()) {

            turnoGiocatore(player, nemico);

            if (nemico.eVivo()) {
                turnoNemico(player, nemico);
            }

            stampaStato(player, nemico);
        }

        if (player.eVivo()) {
            System.out.println("🏆 Hai vinto!");
        } else {
            System.out.println("☠ Sei stato sconfitto!");
        }
    }
    private void turnoGiocatore(Stregone player, Maledizione nemico) {

        System.out.println("\n--- TURNO GIOCATORE ---");
        System.out.println("1. Attacca");
        System.out.println("2. Cura (energia semplice)");
        System.out.print("Scegli azione: ");

        int scelta = scanner.nextInt();

        switch (scelta) {
            case 1 -> {
                int danno = player.attacca();
                nemico.subisciDanno(danno);
                System.out.println("Hai attaccato il nemico!");
            }

            case 2 -> {
                player.cura(10);
                System.out.println("Ti sei curato!");
            }

            default -> System.out.println("Azione non valida!");
        }
    }
    private void turnoNemico(Stregone player, Maledizione nemico) {

        System.out.println("\n--- TURNO NEMICO ---");

        int danno = nemico.attacca();
        player.subisciDanno(danno);

        System.out.println("Il nemico ti ha attaccato!");
    }
    private void stampaStato(Stregone player, Maledizione nemico) {

        System.out.println("\n--- STATO ---");
        System.out.println(player.getNome() + " HP: " + player.getPuntiVita());
        System.out.println(nemico.getNome() + " HP: " + nemico.getPuntiVita());
        System.out.println("----------------\n");
    }

}
