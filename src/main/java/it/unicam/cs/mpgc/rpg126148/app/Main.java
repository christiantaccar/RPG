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
        Scene scene = new Scene(loader.load(), 500, 400);
        MainMenuController controller = loader.getController();
        controller.setContext(context);
        stage.setTitle("RPG Maledetto");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    public static <T> T cambiaScena(Stage stage, String fxml, int w, int h) throws Exception {
        String path = "/it/unicam/cs/mpgc/rpg126148/" + fxml;
        java.net.URL url = Main.class.getResource(path);
        if (url == null) throw new RuntimeException("FXML non trovato: " + path);
        FXMLLoader loader = new FXMLLoader(url);
        Scene scene = new Scene(loader.load(), w, h);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
        return loader.getController();
    }
}