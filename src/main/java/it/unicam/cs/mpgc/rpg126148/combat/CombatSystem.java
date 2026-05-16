package it.unicam.cs.mpgc.rpg126148.combat;
import it.unicam.cs.mpgc.rpg126148.tecniche.*;
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
        System.out.println("1. Attacca base");
        System.out.println("2. Tecnica maledetta");
        System.out.println("3. Cura");
        System.out.print("Scegli azione: ");

        int scelta = Integer.parseInt(scanner.nextLine());

        switch (scelta) {

            case 1 -> {
                player.setTecnica(new AttaccoFisico());
            }

            case 2 -> {
                player.setTecnica(new Magia());
            }

            case 3 -> {
                player.setTecnica(new Cura());
            }

            default -> {
                System.out.println("Azione non valida");
                return;
            }
        }

        Tecnica tecnica = player.getTecnicaAttuale();

        if (!player.haEnergia(tecnica.costoEnergia())) {
            System.out.println("Energia INSUFICIENTE!");
            return;
        }

        player.consumaEnergia(tecnica.costoEnergia());

        int effetto = tecnica.esegui();
        if (effetto >= 0) {

            nemico.subisciDanno(effetto);

            System.out.println(
                    player.getTecnicaAttuale().nome()
                            + " infligge "
                            + effetto
                            + " danni!"
            );

        } else {

            player.cura(-effetto);

            System.out.println(
                    player.getTecnicaAttuale().nome()
                            + " cura "
                            + (-effetto)
                            + " HP!"
            );
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
        System.out.println(player.getNome() + " HP: " + player.getPuntiVita()
        + " Energia: " + player.getEnergiaNera());
        System.out.println(nemico.getNome() + " HP: " + nemico.getPuntiVita());
        System.out.println("----------------\n");
    }

}
