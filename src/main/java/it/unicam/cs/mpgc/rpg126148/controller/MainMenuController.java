package it.unicam.cs.mpgc.rpg126148.controller;

import it.unicam.cs.mpgc.rpg126148.persistence.GestoreSalvataggio;
import it.unicam.cs.mpgc.rpg126148.persistence.SalvataggioStato;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML private Button btnNuovaPartita;

    private final GestoreSalvataggio gestoreSalvataggio = new GestoreSalvataggio();

    @FXML
    public void nuovaPartita() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/it/unicam/cs/mpgc/rpg126148/mappa.fxml")
            );
            Stage stage = (Stage) btnNuovaPartita.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 900, 600));
            MappaController controller = loader.getController();
            controller.inizializza(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void caricaPartita() {
        try {
            SalvataggioStato stato = gestoreSalvataggio.carica();
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/it/unicam/cs/mpgc/rpg126148/mappa.fxml")
            );
            Stage stage = (Stage) btnNuovaPartita.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 900, 600));
            MappaController controller = loader.getController();
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