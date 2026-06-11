package it.unicam.cs.mpgc.rpg126148.controller;

import it.unicam.cs.mpgc.rpg126148.app.GameContext;
import it.unicam.cs.mpgc.rpg126148.app.Main;
import it.unicam.cs.mpgc.rpg126148.persistence.SalvataggioStato;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML private Button btnNuovaPartita;

    private GameContext context;

    public void setContext(GameContext context) {
        this.context = context;
    }

    @FXML
    public void nuovaPartita() {
        try {
            Stage stage = (Stage) btnNuovaPartita.getScene().getWindow();
            CreaPersonaggioController controller = Main.cambiaScena(stage, "crea-personaggio.fxml");
            controller.setContext(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void caricaPartita() {
        try {
            Stage stage = (Stage) btnNuovaPartita.getScene().getWindow();
            SceltaSlotController controller = Main.cambiaScena(stage, "scelta-slot.fxml");
            controller.setContext(context);
            controller.inizializza(true, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void apriMappa(SalvataggioStato stato) {
        try {
            Stage stage = (Stage) btnNuovaPartita.getScene().getWindow();
            MappaController controller = Main.cambiaScena(stage, "mappa.fxml");
            controller.setContext(context);
            controller.inizializza(stato);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void esci() {
        System.exit(0);
    }
}