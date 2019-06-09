package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    /**
     * When this method is called, a new scene with the in_game menu will open.
     */
//    @FXML
//    public void openInGameMenu(ActionEvent event) throws IOException {
//        Parent menuViewParent = FXMLLoader.load(getClass().getResource("in_GameMenu.fxml"));
//        Scene panel = new Scene(menuViewParent);
//
//        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//
//        window.setScene(panel);
//        window.show();
//    }

    @FXML
    private Button MenuButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
