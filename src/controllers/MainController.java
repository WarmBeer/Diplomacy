package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import models.SuperModel;

public class MainController {

    private SuperModel superModel;
    public GameController gameController;

    public MainController(SuperModel superModel) {
        this.superModel = superModel;
    }

    public void clickJoinedGame(Stage primaryStage) {
        try {
            this.gameController.showGame(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickedHostGame() {

    }

    public void clickedOptions() {

    }

}
