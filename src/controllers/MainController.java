package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import models.SuperModel;

public class MainController {
    private SuperModel superModel;
    public GameController gameController;

    public MainController(Stage primaryStage) {
        superModel = new SuperModel(primaryStage, this);
    }

    public void clickedOptions() {

    }

    public void clickedJoinGame() {
        gameController.show();
    }

    public void clickedHostGame() {

    }

    public void dontShowMainMenu() {
        this.superModel.dontShowMainMenu();
    }

    public void showMainMenu() {
        superModel.show();
    }

}
