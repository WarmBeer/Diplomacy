package models;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import views.MainMenu;

public class in_GameMenuModel {

    @FXML
    private VBox MainMenu;

    public void afsluitenModel() {
        System.exit(0);
    }

//    public void openInGameMenuModel() {
//        MainMenu.setVisible(!MainMenu.isVisible());
//    }
}
