package it.unicam.cs.mpgc.rpg126148.controller;

import it.unicam.cs.mpgc.rpg126148.app.GameContext;
import it.unicam.cs.mpgc.rpg126148.app.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreaPersonaggioController {

    @FXML private TextField campoNome;
    @FXML private Label labelErrore;

    private GameContext context;

    public void setContext(GameContext context) {
        this.context = context;
    }

    @FXML
    public void conferma() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            labelErrore.setText("Il nome non può essere vuoto!");
            return;
        }
        if (nome.length() < 2) {
            labelErrore.setText("Il nome deve avere almeno 2 caratteri.");
            return;
        }
        try {
            Stage stage = (Stage) campoNome.getScene().getWindow();
            SceltaSlotController controller = Main.cambiaScena(stage, "scelta-slot.fxml");
            controller.setContext(context);
            controller.inizializza(false, nome);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void tornaMenu() {
        try {
            Stage stage = (Stage) campoNome.getScene().getWindow();
            MainMenuController controller = Main.cambiaScena(stage, "main-menu.fxml");
            controller.setContext(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}