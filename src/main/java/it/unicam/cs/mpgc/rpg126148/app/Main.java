package it.unicam.cs.mpgc.rpg126148.app;

import it.unicam.cs.mpgc.rpg126148.controller.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private final GameContext context = new GameContext();

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/it/unicam/cs/mpgc/rpg126148/main-menu.fxml")
        );
        Scene scene = new Scene(loader.load());
        stage.setTitle("RPG Maledetto");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        MainMenuController controller = loader.getController();
        controller.setContext(context);
    }

    public static void main(String[] args) {
        launch(args);
    }
    public static <T> T cambiaScena(Stage stage, String fxml) throws Exception {
        String path = "/it/unicam/cs/mpgc/rpg126148/" + fxml;
        java.net.URL url = Main.class.getResource(path);
        if (url == null) throw new RuntimeException("FXML non trovato: " + path);

        boolean eraMaximized = stage.isMaximized();
        double w = stage.getWidth();
        double h = stage.getHeight();

        FXMLLoader loader = new FXMLLoader(url);
        stage.setScene(new Scene(loader.load(), w, h));

        if (eraMaximized) {
            stage.setMaximized(true);
        }

        javafx.application.Platform.runLater(() -> {
            stage.getScene().getRoot().requestLayout();
        });

        return loader.getController();
    }
}