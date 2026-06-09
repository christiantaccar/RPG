package it.unicam.cs.mpgc.rpg126148.combat;

import it.unicam.cs.mpgc.rpg126148.model.Maledizione;
import it.unicam.cs.mpgc.rpg126148.model.Stregone;
import it.unicam.cs.mpgc.rpg126148.tecniche.Tecnica;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CombatSystem {
    private final Scanner scanner = new Scanner(System.in);

    public boolean combatti(Stregone player, Maledizione nemico) {
        System.out.println("Inizio combattimento!");

        while (player.eVivo() && nemico.eVivo()) {
            turnoGiocatore(player, nemico);
            if (nemico.eVivo()) turnoNemico(player, nemico);
            stampaStato(player, nemico);
        }

        if (player.eVivo()) {
            System.out.println(" Hai vinto!");
            player.resetHP();
            return true;
        }
        else{
            System.out.println("☠ Sei stato sconfitto!");
            return false;
        }
    }

    private void turnoGiocatore(Stregone player, Maledizione nemico) {
        System.out.println("\n--- TURNO GIOCATORE ---");

        // costruiamo il menu dinamico
        List<Tecnica> menuTecniche = new ArrayList<>();

        System.out.println(" ATTACCHI FISICI:");
        for (Tecnica t : player.getTecnicheFisiche()) {
            menuTecniche.add(t);
            System.out.println("  " + menuTecniche.size() + ". " + t.nome());
        }

        if (!player.getTecnicheSbloccate().isEmpty()) {
            System.out.println(" MAGIE SBLOCCATE:");
            for (Tecnica t : player.getTecnicheSbloccate()) {
                menuTecniche.add(t);
                System.out.println("  " + menuTecniche.size() + ". "
                        + t.nome() + "  [" + t.costoEnergia() + " EN]");
            }
        }

        System.out.print("Scegli azione: ");
        int scelta;
        try {
            scelta = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Scelta non valida.");
            return;
        }

        if (scelta < 1 || scelta > menuTecniche.size()) {
            System.out.println("Azione non valida.");
            return;
        }

        Tecnica tecnica = menuTecniche.get(scelta - 1);

        if (!player.haEnergia(tecnica.costoEnergia())) {
            System.out.println("⚡ Energia insufficiente!");
            return;
        }

        player.consumaEnergia(tecnica.costoEnergia());
        int effetto = tecnica.esegui();

        if (effetto >= 0) {
            nemico.subisciDanno(effetto);
            System.out.println(tecnica.nome() + " infligge " + effetto + " danni!");
        } else {
            player.cura(-effetto);
            System.out.println(tecnica.nome() + " cura " + (-effetto) + " HP!");
        }
    }

    private void turnoNemico(Stregone player, Maledizione nemico) {
        System.out.println("\n--- TURNO NEMICO ---");
        int danno = nemico.attacca();
        player.subisciDanno(danno);
        System.out.println(nemico.getNome() + " ti attacca per " + danno + " danni!");
    }

    private void stampaStato(Stregone player, Maledizione nemico) {
        System.out.println("\n--- STATO ---");
        System.out.println(player.getNome()
                + " | HP: " + player.getPuntiVita()
                + " | EN: " + player.getEnergiaNera() + "/" + player.getEnergiaNeraMassima());
        System.out.println(nemico.getNome()
                + " | HP: " + nemico.getPuntiVita());
        System.out.println("-------------------\n");
    }
}