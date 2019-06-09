package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;


public class in_GameMenuController {
    @FXML
    private VBox MainMenu;

    @FXML
    private Button MenuButton;

    @FXML
    private Button Opslaan;

    @FXML
    private Button Laden;

    @FXML
    private Button Afsluiten;

    @FXML
    private Button Spelregels;

    @FXML
    private Button Opties;

    @FXML
    private void OpenInGameMenu(){
        MainMenu.setVisible(!MainMenu.isVisible());
    }

    @FXML
    private void AfsluitenGame() {
        System.exit(0);
    }

    private void OpenSpelregels() {

    }
}
