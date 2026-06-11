package it.unicam.cs.mpgc.rpg126148.controller;

import it.unicam.cs.mpgc.rpg126148.app.GameContext;
import it.unicam.cs.mpgc.rpg126148.app.Main;
import it.unicam.cs.mpgc.rpg126148.persistence.SalvataggioStato;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GameOverController {

    @FXML private Button btnRiprendi;
    @FXML private Label labelInfo;

    private GameContext context;

    public void setContext(GameContext context) {
        this.context = context;
        verificaSalvataggio();
    }

    private void verificaSalvataggio() {
        SalvataggioStato stato = context.getGestoreSalvataggio().carica(context.getSlotCorrente());
        if (stato == null) {
            btnRiprendi.setDisable(true);
            labelInfo.setText("Nessun salvataggio disponibile.");
        } else {
            labelInfo.setText("Ultimo salvataggio: " + stato.nome);
        }
    }

    @FXML
    public void riprendi() {
        try {
            SalvataggioStato stato = context.getGestoreSalvataggio().carica(context.getSlotCorrente());
            Stage stage = (Stage) btnRiprendi.getScene().getWindow();
            MappaController controller = Main.cambiaScena(stage, "mappa.fxml");
            controller.setContext(context);
            controller.inizializza(stato);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void tornaMenu() {
        try {
            Stage stage = (Stage) btnRiprendi.getScene().getWindow();
            MainMenuController controller = Main.cambiaScena(stage, "main-menu.fxml");
            controller.setContext(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void esci() {
        System.exit(0);
    }
}