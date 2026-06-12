package it.unicam.cs.mpgc.rpg126148.controller;

import it.unicam.cs.mpgc.rpg126148.app.GameContext;
import it.unicam.cs.mpgc.rpg126148.app.Main;
import it.unicam.cs.mpgc.rpg126148.items.Frammento;
import it.unicam.cs.mpgc.rpg126148.items.TipoFrammento;
import it.unicam.cs.mpgc.rpg126148.model.Maledizione;
import it.unicam.cs.mpgc.rpg126148.model.Stregone;
import it.unicam.cs.mpgc.rpg126148.persistence.GestoreSalvataggio;
import it.unicam.cs.mpgc.rpg126148.persistence.SalvataggioStato;
import it.unicam.cs.mpgc.rpg126148.tecniche.RegistroTecniche;
import it.unicam.cs.mpgc.rpg126148.tecniche.Tecnica;
import it.unicam.cs.mpgc.rpg126148.world.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.Random;

public class MappaController {

    @FXML private GridPane gridMappa;
    @FXML private Label labelNome;
    @FXML private Label labelHP;
    @FXML private Label labelEnergia;
    @FXML private Label labelFrammenti;
    @FXML private Label labelStanza;
    @FXML private TextArea logArea;

    private Stregone giocatore;
    private Mappa mappa;
    private GameContext context;
    private final Random random = new Random();
    private int xPrecedente;
    private int yPrecedente;
    private static final int CELL_SIZE = 22;
    public void inizializza(SalvataggioStato stato) {
        inizializza(stato,"Player1");
    }
    public void inizializza(SalvataggioStato stato, String nome) {
        String nomeFinale = (stato != null) ? stato.nome : nome;
        giocatore = new Stregone(nomeFinale);
        if (stato != null && stato.frammenti != null) {
            stato.frammenti.forEach((tipo, quantita) -> {
                for (int i = 0; i < quantita; i++) {
                    giocatore.getInventario().aggiungi(
                            new Frammento(TipoFrammento.valueOf(tipo))
                    );
                }
            });
        }

        if (stato != null && stato.tecnicheSbloccate != null) {
            stato.tecnicheSbloccate.forEach(nomeTecnica -> {
                Tecnica tecnica = RegistroTecniche.get(nomeTecnica);
                if (tecnica != null) giocatore.aggiungiTecnicaSbloccata(tecnica);
            });
        }

        costruisciMappa();

        if (stato != null) {
            mappa.posizionaGiocatore(stato.xGiocatore, stato.yGiocatore);
        } else {
            mappa.posizionaGiocatore(3, 2);
        }

        aggiornaGriglia();
        aggiornaStats();

        // input tastiera
        javafx.application.Platform.runLater(() -> {
            gridMappa.getScene().setOnKeyPressed(this::gestisciTasto);
            gridMappa.requestFocus();
        });
        gridMappa.setFocusTraversable(true);
    }

    private void costruisciMappa() {
        mappa = new Mappa(25, 25);

        // stanza 1 — partenza (6x3)
        mappa.aggiungiStanza(new Stanza("Villaggio Abbandonato", 1, 1, 6, 3, 1));
        mappa.setIngresso(6, 2);
        mappa.posizionaCassaTipizzata(2, 2, TipoFrammento.FUOCO);
        mappa.posizionaCassaTipizzata(4, 2, TipoFrammento.FUOCO);

        // corridoio orizzontale stanza 1 → stanza 2
        mappa.aggiungiCorridoio(7, 2, 9, 2);

        // stanza 2 — nemico facile (6x3)
        mappa.aggiungiStanza(new Stanza("Accampamento Oscuro", 10, 1, 6, 3, 1));
        mappa.setIngresso(10, 2);
        mappa.setIngresso(15, 2);
        mappa.posizionaNemico(12, 2);
        mappa.posizionaCassaTipizzata(13, 2, TipoFrammento.FUOCO);

        // corridoio orizzontale + verticale stanza 2 → stanza 3
        mappa.aggiungiCorridoio(16, 2, 17, 2);
        mappa.aggiungiCorridoio(17, 2, 17, 5);

        // stanza 3 — livello 2 (8x5)
        mappa.aggiungiStanza(new Stanza("Rovine del Tempio", 14, 6, 8, 5, 2));
        mappa.setIngresso(17, 6);
        mappa.posizionaNemico(16, 8);
        mappa.posizionaNemico(19, 8);
        mappa.posizionaCassaTipizzata(20, 9, TipoFrammento.CURA);
        mappa.setIngresso(17,10);

        // corridoio verticale stanza 3 → stanza 4
        mappa.aggiungiCorridoio(17, 11, 17, 13);

        // stanza 4 — boss (8x4)
        mappa.aggiungiStanza(new Stanza("Abisso Oscuro", 13, 14, 8, 4, 3));
        mappa.setIngresso(17, 14);
        mappa.posizionaNemico(15, 16);
        mappa.posizionaNemico(18, 16);
        mappa.posizionaCassaTipizzata(14, 16, TipoFrammento.OSCURITA);
    }

    private void aggiornaGriglia() {
        gridMappa.getChildren().clear();
        Stanza stanzaGiocatore = mappa.getStanzaCorrente();

        for (int y = 0; y < mappa.getAltezza(); y++) {
            for (int x = 0; x < mappa.getLarghezza(); x++) {
                StackPane cella = creaCella(x, y, stanzaGiocatore);
                gridMappa.add(cella, x, y);
            }
        }
    }

    private StackPane creaCella(int x, int y, Stanza stanzaGiocatore) {
        StackPane pane = new StackPane();
        Rectangle rect = new Rectangle(CELL_SIZE, CELL_SIZE);
        Text testo = new Text();

        boolean isGiocatore = x == mappa.getXGiocatore() && y == mappa.getYGiocatore();

        if (isGiocatore) {
            rect.setFill(Color.web("#1a1a2e"));
            testo.setText("@");
            testo.setFill(Color.web("#4ecca3"));
        } else {
            TipoCella tipo = mappa.getCella(x, y).getTipo();

            // fog of war
            boolean dentroAltraStanza = mappa.isInternoStanza(x, y)
                    && (stanzaGiocatore == null || !stanzaGiocatore.contiene(x, y));

            if (dentroAltraStanza) {
                rect.setFill(Color.web("#0d0d1a"));
                testo.setText("?");
                testo.setFill(Color.web("#333355"));
            } else {
                switch (tipo) {
                    case MURO -> { rect.setFill(Color.web("#0d0d1a")); testo.setText(""); }
                    case BORDO -> { rect.setFill(Color.web("#0f3460")); testo.setText("+"); testo.setFill(Color.web("#4a90d9")); }
                    case INGRESSO -> { rect.setFill(Color.web("#1a1a2e")); testo.setText("O"); testo.setFill(Color.web("#f5a623")); }
                    case VUOTA -> { rect.setFill(Color.web("#1a1a2e")); testo.setText("."); testo.setFill(Color.web("#333355")); }
                    case NEMICO -> { rect.setFill(Color.web("#3d0000")); testo.setText("N"); testo.setFill(Color.web("#e94560")); }
                    case CASSA -> { rect.setFill(Color.web("#1a2d00")); testo.setText("C"); testo.setFill(Color.web("#4ecca3")); }
                }
            }
        }

        pane.getChildren().addAll(rect, testo);
        return pane;
    }

    private void gestisciTasto(KeyEvent event) {
        int dx = 0, dy = 0;
        switch (event.getCode()) {
            case W, UP -> dy = -1;
            case S, DOWN -> dy = 1;
            case A, LEFT -> dx = -1;
            case D, RIGHT -> dx = 1;
            case I->apriInventario();
            default -> { return; }
        }
        muovi(dx, dy);
    }

    private void muovi(int dx, int dy) {
        int nuovoX = mappa.getXGiocatore() + dx;
        int nuovoY = mappa.getYGiocatore() + dy;

        if (nuovoX < 0 || nuovoX >= mappa.getLarghezza()
                || nuovoY < 0 || nuovoY >= mappa.getAltezza()) return;

        Cella cella = mappa.getCella(nuovoX, nuovoY);

        if (cella.getTipo() == TipoCella.MURO || cella.getTipo() == TipoCella.BORDO) return;

        // salva posizione precedente
        xPrecedente = mappa.getXGiocatore();
        yPrecedente = mappa.getYGiocatore();

        mappa.posizionaGiocatore(nuovoX, nuovoY);
        cella.setEsplorata(true);

        switch (cella.getTipo()) {
            case NEMICO -> gestisciNemico(cella);
            case CASSA -> gestisciCassa(cella);
            default -> {}
        }

        aggiornaGriglia();
        aggiornaStats();
    }

    private void gestisciNemico(Cella cella) {
        Stanza stanzaCorrente = mappa.getStanzaCorrente();
        int livello = (stanzaCorrente != null) ? stanzaCorrente.getLivelloNemici() : 1;
        Maledizione nemico = new Maledizione("Spirito Maledetto", livello);

        // anteprima nemico
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.CONFIRMATION
        );
        alert.setTitle("⚠ Nemico Rilevato");
        alert.setHeaderText("Spirito Maledetto — Livello " + livello);
        alert.setContentText(
                "HP: " + nemico.getPuntiVitaMassimi() + "\n" +
                        "Attacco: " + nemico.getAttacco() + "\n" +
                        "Difesa: " + nemico.getDifesa() + "\n\n" +
                        "Cosa vuoi fare?"
        );

        javafx.scene.control.ButtonType btnCombatti =
                new javafx.scene.control.ButtonType("⚔ Combatti");
        javafx.scene.control.ButtonType btnFuggi =
                new javafx.scene.control.ButtonType("🏃 Fuggi");

        alert.getButtonTypes().setAll(btnCombatti, btnFuggi);

        alert.showAndWait().ifPresent(risposta -> {
            if (risposta == btnCombatti) {
                avviaCombattimento(nemico, cella);
            } else {
                mappa.posizionaGiocatore(xPrecedente, yPrecedente);
                log("Sei fuggito!");
                aggiornaGriglia();
            }
        });
    }

    private void avviaCombattimento(Maledizione nemico, Cella cella) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/it/unicam/cs/mpgc/rpg126148/combattimento.fxml")
            );
            Stage stage = (Stage) gridMappa.getScene().getWindow();
            CombattimentoController controller = Main.cambiaScena(stage, "combattimento.fxml");
            controller.setContext(context);
            controller.inizializza(giocatore, nemico, mappa, cella);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gestisciCassa(Cella cella) {
        TipoFrammento tipo = cella.getContenuto();
        if (tipo == null) {
            TipoFrammento[] tipi = TipoFrammento.values();
            tipo = tipi[random.nextInt(tipi.length)];
        }
        Frammento frammento = new Frammento(tipo);
        context.getGestoreFrammenti().aggiungiFrammento(giocatore, frammento);

        // +20 energia bonus
        giocatore.recuperaEnergia(20);

        cella.setTipo(TipoCella.VUOTA);
        log("📦 Hai trovato: " + frammento.getNome() + " +20 EN");
        aggiornaStats();
    }

    public void ritornaDopoScontro(boolean vittoria, Cella cella) {
        if (vittoria) {
            cella.setTipo(TipoCella.VUOTA);
            Optional<Frammento> ricompensa = context.getGestoreRicompense().genera(
                    new Maledizione("", 1 + random.nextInt(3))
            );
            ricompensa.ifPresent(f -> {
                context.getGestoreFrammenti().aggiungiFrammento(giocatore, f);
                log("🔮 Hai ottenuto: " + f.getNome());
            });
        } else {
            log("☠ Sei stato sconfitto!");
        }
        aggiornaGriglia();
        aggiornaStats();
        gridMappa.requestFocus();
    }

    private void aggiornaStats() {
        labelNome.setText(giocatore.getNome());
        labelHP.setText("HP: " + giocatore.getPuntiVita() + "/100");
        labelEnergia.setText("EN: " + giocatore.getEnergiaNera() + "/100");

        long fuoco = contaFrammenti(TipoFrammento.FUOCO);
        long cura = contaFrammenti(TipoFrammento.CURA);
        long osc = contaFrammenti(TipoFrammento.OSCURITA);
        labelFrammenti.setText("F:" + fuoco + " C:" + cura + " O:" + osc);

        Stanza s = mappa.getStanzaCorrente();
        labelStanza.setText("Stanza: " + (s != null ? s.getNome() : "Corridoio"));
    }

    private long contaFrammenti(TipoFrammento tipo) {
        return giocatore.getInventario().getOggetti().stream()
                .filter(o -> o instanceof Frammento)
                .map(o -> (Frammento) o)
                .filter(f -> f.getTipo() == tipo)
                .count();
    }

    public void log(String messaggio) {
        logArea.appendText(messaggio + "\n");
    }

    @FXML
    public void salvaEsci() {
        context.getGestoreSalvataggio().salva(giocatore, mappa, context.getSlotCorrente());
        System.exit(0);
    }
    public void inizializzaDaEsistente(Stregone giocatore, Mappa mappa) {
        this.giocatore = giocatore;
        this.mappa = mappa;

        aggiornaGriglia();
        aggiornaStats();

        javafx.application.Platform.runLater(() -> {
            gridMappa.getScene().setOnKeyPressed(this::gestisciTasto);
            gridMappa.requestFocus();
        });
        gridMappa.setFocusTraversable(true);
    }
    public void setContext(GameContext context) {
        this.context = context;
    }
    private void apriInventario() {
        try {
            Stage stage = (Stage) gridMappa.getScene().getWindow();
            InventarioController controller = Main.cambiaScena(stage, "inventario.fxml");
            controller.setContext(context);
            controller.inizializza(giocatore, mappa);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}