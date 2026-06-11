package it.unicam.cs.mpgc.rpg126148.controller;

import it.unicam.cs.mpgc.rpg126148.app.GameContext;
import it.unicam.cs.mpgc.rpg126148.app.Main;
import it.unicam.cs.mpgc.rpg126148.persistence.SalvataggioStato;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Optional;

public class SceltaSlotController {

    @FXML private VBox boxSlot;
    @FXML private Text titolo;

    private GameContext context;
    private boolean modalitaCarica; // true=carica, false=nuova partita
    private String nomeGiocatore;

    public void setContext(GameContext context) {
        this.context = context;
    }

    public void inizializza(boolean modalitaCarica, String nomeGiocatore) {
        this.modalitaCarica = modalitaCarica;
        this.nomeGiocatore = nomeGiocatore;
        titolo.setText(modalitaCarica ? "CARICA PARTITA" : "SALVA IN SLOT");
        costruisciSlot();
    }

    private void costruisciSlot() {
        boxSlot.getChildren().clear();
        SalvataggioStato[] slots = context.getGestoreSalvataggio().tuttiSlot();

        for (int i = 0; i < slots.length; i++) {
            final int slotNum = i + 1;
            SalvataggioStato stato = slots[i];

            HBox riga = new HBox(10);
            riga.setAlignment(javafx.geometry.Pos.CENTER);

            // bottone principale slot
            Button btnSlot = new Button();
            btnSlot.setPrefWidth(300);
            if (stato == null) {
                btnSlot.setText("Slot " + slotNum + " — Vuoto");
                btnSlot.setStyle("-fx-background-color: #16213e; -fx-text-fill: #a8a8b3; " +
                        "-fx-font-size: 13; -fx-background-radius: 6; -fx-cursor: hand;");
                if (modalitaCarica) btnSlot.setDisable(true);
            } else {
                btnSlot.setText("Slot " + slotNum + " — " + stato.nome +
                        "\n" + stato.dataOra);
                btnSlot.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; " +
                        "-fx-font-size: 13; -fx-background-radius: 6; -fx-cursor: hand;");
            }
            btnSlot.setOnAction(e -> selezionaSlot(slotNum, stato));

            riga.getChildren().add(btnSlot);

            // bottone elimina (solo slot occupati)
            if (stato != null) {
                Button btnElimina = new Button("🗑");
                btnElimina.setStyle("-fx-background-color: #3d0000; -fx-text-fill: #e94560; " +
                        "-fx-font-size: 13; -fx-background-radius: 6; -fx-cursor: hand;");
                btnElimina.setOnAction(e -> eliminaSlot(slotNum));
                riga.getChildren().add(btnElimina);
            }

            boxSlot.getChildren().add(riga);
        }
    }

    private void selezionaSlot(int slot, SalvataggioStato stato) {
        if (modalitaCarica) {
            // carica partita
            try {
                Stage stage = (Stage) boxSlot.getScene().getWindow();
                context.setSlotCorrente(slot);
                MappaController controller = Main.cambiaScena(stage, "mappa.fxml");
                controller.setContext(context);
                controller.inizializza(stato);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // nuova partita — chiedi conferma se slot occupato
            if (stato != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Sovrascrivere?");
                alert.setHeaderText("Slot " + slot + " contiene già un salvataggio.");
                alert.setContentText("Vuoi sovrascriverlo?");
                Optional<ButtonType> risposta = alert.showAndWait();
                if (risposta.isEmpty() || risposta.get() != ButtonType.OK) return;
            }
            try {
                Stage stage = (Stage) boxSlot.getScene().getWindow();
                MappaController controller = Main.cambiaScena(stage, "mappa.fxml");
                controller.setContext(context);
                context.setSlotCorrente(slot);
                controller.inizializza(null, nomeGiocatore);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void eliminaSlot(int slot) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminare salvataggio?");
        alert.setHeaderText("Slot " + slot);
        alert.setContentText("Sei sicuro di voler eliminare questo salvataggio?");
        Optional<ButtonType> risposta = alert.showAndWait();
        if (risposta.isPresent() && risposta.get() == ButtonType.OK) {
            context.getGestoreSalvataggio().elimina(slot);
            costruisciSlot(); // aggiorna la lista
        }
    }

    @FXML
    public void indietro() {
        try {
            Stage stage = (Stage) boxSlot.getScene().getWindow();
            MainMenuController controller = Main.cambiaScena(stage, "main-menu.fxml");
            controller.setContext(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}