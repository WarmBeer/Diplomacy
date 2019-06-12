package views;

import controllers.MainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu {
    private Stage stage;
    private Scene scene;

    private static String VIEW_FILE = "/resources/MainMenu.fxml";
    private static String STYLESHEET_FILE = "/resources/MainMenu.css";
    private MainController mainController;

    public MainMenu(Stage stage, MainController mainController) throws IOException {
        this.mainController = mainController;
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_FILE));
        Parent root = loader.load();
        this.scene = SceneBuilder.create().root(root).width(1280).height(720)
                .fill(Color.GRAY).build();
        scene.getStylesheets().add(STYLESHEET_FILE);

    }

    public void show() {
        stage.hide();
        //stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }
}
