package it.unicam.cs.mpgc.rpg126148.controller;

import it.unicam.cs.mpgc.rpg126148.app.GameContext;
import it.unicam.cs.mpgc.rpg126148.app.Main;
import it.unicam.cs.mpgc.rpg126148.items.Frammento;
import it.unicam.cs.mpgc.rpg126148.items.Pergamena;
import it.unicam.cs.mpgc.rpg126148.items.TipoFrammento;
import it.unicam.cs.mpgc.rpg126148.model.Stregone;
import it.unicam.cs.mpgc.rpg126148.tecniche.Magia;
import it.unicam.cs.mpgc.rpg126148.tecniche.Cura;
import it.unicam.cs.mpgc.rpg126148.tecniche.PallaDiFuoco;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public class InventarioController {

    @FXML private Label labelNome;
    @FXML private VBox boxFrammenti;
    @FXML private VBox boxTecniche;

    private Stregone giocatore;
    private GameContext context;
    private Stregone stregone;
    private it.unicam.cs.mpgc.rpg126148.world.Mappa mappa;

    // soglie sblocco
    private static final Map<TipoFrammento, Integer> SOGLIE = Map.of(
            TipoFrammento.FUOCO, 3,
            TipoFrammento.CURA, 3,
            TipoFrammento.OSCURITA, 5
    );

    // nomi tecniche per tipo
    private static final Map<TipoFrammento, String> NOMI_TECNICHE = Map.of(
            TipoFrammento.FUOCO, "Palla di Fuoco",
            TipoFrammento.CURA, "Cura",
            TipoFrammento.OSCURITA, "Maledizione Nera"
    );

    public void setContext(GameContext context) {
        this.context = context;
    }

    public void inizializza(Stregone giocatore, it.unicam.cs.mpgc.rpg126148.world.Mappa mappa) {
        this.giocatore = giocatore;
        this.mappa = mappa;
        labelNome.setText(giocatore.getNome());
        aggiornaFrammenti();
        aggiornaTecniche();
    }

    private void aggiornaFrammenti() {
        boxFrammenti.getChildren().clear();
        for (TipoFrammento tipo : TipoFrammento.values()) {
            long count = contaFrammenti(tipo);
            int soglia = SOGLIE.get(tipo);
            String nomeTecnica = NOMI_TECNICHE.get(tipo);

            Label label = new Label(tipo.name() + ": " + count + "/" + soglia
                    + "  → " + nomeTecnica);
            label.setStyle("-fx-text-fill: " + coloreFragmento(tipo)
                    + "; -fx-font-size: 13;");
            boxFrammenti.getChildren().add(label);
        }
    }

    private void aggiornaTecniche() {
        boxTecniche.getChildren().clear();
        for (TipoFrammento tipo : TipoFrammento.values()) {
            long count = contaFrammenti(tipo);
            int soglia = SOGLIE.get(tipo);
            String nomeTecnica = NOMI_TECNICHE.get(tipo);

            boolean giaSbloccata = giocatore.getTecnicheSbloccate().stream()
                    .anyMatch(t -> t.nome().equals(nomeTecnica));

            HBox riga = new HBox(15);
            riga.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

            Label labelTecnica = new Label(nomeTecnica);
            labelTecnica.setPrefWidth(180);
            labelTecnica.setStyle("-fx-text-fill: white; -fx-font-size: 13;");

            if (giaSbloccata) {
                Label labelSbloccata = new Label("✔ Già sbloccata");
                labelSbloccata.setStyle("-fx-text-fill: #4ecca3; -fx-font-size: 12;");
                riga.getChildren().addAll(labelTecnica, labelSbloccata);
            } else if (count >= soglia) {
                Button btnSblocca = new Button("Sblocca (" + soglia + " frammenti)");
                btnSblocca.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; " +
                        "-fx-font-size: 12; -fx-background-radius: 6; -fx-cursor: hand;");
                btnSblocca.setOnAction(e -> sblocca(tipo, soglia, nomeTecnica));
                riga.getChildren().addAll(labelTecnica, btnSblocca);
            } else {
                Label labelManca = new Label("Mancano " + (soglia - count) + " frammenti");
                labelManca.setStyle("-fx-text-fill: #a8a8b3; -fx-font-size: 12;");
                riga.getChildren().addAll(labelTecnica, labelManca);
            }

            boxTecniche.getChildren().add(riga);
        }
    }

    private void sblocca(TipoFrammento tipo, int soglia, String nomeTecnica) {
        // rimuove i frammenti usati
        long rimossi = 0;
        for (var oggetto : giocatore.getInventario().getOggetti().stream()
                .filter(o -> o instanceof Frammento)
                .map(o -> (Frammento) o)
                .filter(f -> f.getTipo() == tipo)
                .limit(soglia)
                .toList()) {
            giocatore.getInventario().rimuovi(oggetto);
            rimossi++;
        }

        // sblocca la tecnica
        Pergamena pergamena = switch (tipo) {
            case FUOCO -> new Pergamena("Pergamena del Fuoco", "", new PallaDiFuoco());
            case CURA -> new Pergamena("Pergamena della Cura", "", new Cura());
            case OSCURITA -> new Pergamena("Pergamena dell'Oscurità", "", new Magia());
        };
        giocatore.sbloccaTecnica(pergamena);

        // aggiorna UI
        aggiornaFrammenti();
        aggiornaTecniche();
    }

    private long contaFrammenti(TipoFrammento tipo) {
        return giocatore.getInventario().getOggetti().stream()
                .filter(o -> o instanceof Frammento)
                .map(o -> (Frammento) o)
                .filter(f -> f.getTipo() == tipo)
                .count();
    }

    private String coloreFragmento(TipoFrammento tipo) {
        return switch (tipo) {
            case FUOCO -> "#f5a623";
            case CURA -> "#4ecca3";
            case OSCURITA -> "#9b59b6";
        };
    }

    @FXML
    public void chiudi() {
        try {
            Stage stage = (Stage) boxFrammenti.getScene().getWindow();
            MappaController controller = Main.cambiaScena(stage, "mappa.fxml");
            controller.setContext(context);
            controller.inizializzaDaEsistente(giocatore, mappa);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}