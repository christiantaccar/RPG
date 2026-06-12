package it.unicam.cs.mpgc.rpg126148.controller;

import it.unicam.cs.mpgc.rpg126148.app.GameContext;
import it.unicam.cs.mpgc.rpg126148.app.Main;
import it.unicam.cs.mpgc.rpg126148.model.Maledizione;
import it.unicam.cs.mpgc.rpg126148.model.Stregone;
import it.unicam.cs.mpgc.rpg126148.tecniche.Tecnica;
import it.unicam.cs.mpgc.rpg126148.world.Cella;
import it.unicam.cs.mpgc.rpg126148.world.Mappa;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CombattimentoController {

    @FXML private Label labelNomeGiocatore;
    @FXML private Label labelHPGiocatore;
    @FXML private Label labelEnergiaGiocatore;
    @FXML private Label labelNomeNemico;
    @FXML private Label labelHPNemico;
    @FXML private TextArea logCombattimento;
    @FXML private HBox boxTecniche;

    private Stregone giocatore;
    private Maledizione nemico;
    private Mappa mappa;
    private Cella cellaCorrente;
    private GameContext context;

    public void inizializza(Stregone giocatore, Maledizione nemico,
                            Mappa mappa, Cella cella) {
        this.giocatore = giocatore;
        this.nemico = nemico;
        this.mappa = mappa;
        this.cellaCorrente = cella;

        aggiornaStats();
        costruisciBottoniTecniche();
        log("⚔ Inizio combattimento contro " + nemico.getNome() + "!");
    }

    private void costruisciBottoniTecniche() {
        boxTecniche.getChildren().clear();

        // attacchi fisici
        for (Tecnica t : giocatore.getTecnicheFisiche()) {
            boxTecniche.getChildren().add(creaBottone(t));
        }

        // magie sbloccate
        for (Tecnica t : giocatore.getTecnicheSbloccate()) {
            boxTecniche.getChildren().add(creaBottone(t));
        }
    }

    private Button creaBottone(Tecnica tecnica) {
        Button btn = new Button(tecnica.nome()
                + (tecnica.costoEnergia() > 0 ? " [" + tecnica.costoEnergia() + " EN]" : ""));
        btn.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; " +
                "-fx-font-size: 12; -fx-background-radius: 6; -fx-cursor: hand;");
        btn.setOnAction(e -> eseguiTurno(tecnica));
        return btn;
    }

    private void eseguiTurno(Tecnica tecnica) {
        if (!giocatore.haEnergia(tecnica.costoEnergia())) {
            log("⚡ Energia insufficiente!");
            return;
        }

        // turno giocatore
        giocatore.consumaEnergia(tecnica.costoEnergia());
        int effetto = tecnica.esegui();
        if (effetto >= 0) {
            nemico.subisciDanno(effetto);
            log("▶ " + tecnica.nome() + " infligge " + effetto + " danni!");
        } else {
            giocatore.cura(-effetto);
            log("▶ " + tecnica.nome() + " cura " + (-effetto) + " HP!");
        }

        aggiornaStats();

        if (!nemico.eVivo()) {
            log("Hai vinto!");
            giocatore.resetHP();
            giocatore.recuperaEnergia(30);
            tornaAllaMappa(true);
            return;
        }

        // turno nemico
        int danno = nemico.attacca();
        giocatore.subisciDanno(danno);
        log("◀ " + nemico.getNome() + " attacca per " + danno + " danni!");

        aggiornaStats();

        if (!giocatore.eVivo()) {
            log("☠ Sei stato sconfitto!");
            tornaAllaMappa(false);
        }
    }

    private void aggiornaStats() {
        labelNomeGiocatore.setText(giocatore.getNome());
        labelHPGiocatore.setText("HP: " + giocatore.getPuntiVita() + "/100");
        labelEnergiaGiocatore.setText("EN: " + giocatore.getEnergiaNera() + "/100");
        labelNomeNemico.setText(nemico.getNome());
        labelHPNemico.setText("HP: " + nemico.getPuntiVita());
    }

    private void tornaAllaMappa(boolean vittoria) {
        try {
            Stage stage = (Stage) logCombattimento.getScene().getWindow();
            if (vittoria) {
                MappaController controller = Main.cambiaScena(stage, "mappa.fxml");
                controller.setContext(context);
                controller.inizializzaDaEsistente(giocatore, mappa);
                controller.ritornaDopoScontro(true, cellaCorrente);
                if(!mappa.hasNemici()){
                    VittoriaController win = Main.cambiaScena(stage, "vittoria.fxml");
                    win.setContext(context);
                }
            } else {
                // game over (non salva)
                GameOverController controller = Main.cambiaScena(stage, "game-over.fxml");
                controller.setContext(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void log(String msg) {
        logCombattimento.appendText(msg + "\n");
    }
    public void setContext(GameContext context) {
        this.context = context;
    }

}