package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

import javax.print.DocFlavor;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Bevat alle code om het programma te kunnen starten.
 * Dit is een online spel waarvoor internetverbinding vereist is.
 * Het kan met maximaal 7 mensen gespeeld worden.
 *
 * @author Edwin
 */
public class Main extends Application {

        private static String GAME_VIEW = "/resources/Diplomacy.fxml";
        private static String STYLESHEET_FILE = "/resources/style.css";

        public static void main(String[] args) {
                launch(args);
        }

        /**
         * Hello World,
         * by calling this, the application will be started.
         */
        @Override
        public void start(Stage primaryStage) throws Exception {
                String fxmlResource = GAME_VIEW;
                Parent panel;
                panel = FXMLLoader.load(getClass().getResource(fxmlResource));
                Scene scene = new Scene(panel);
                Stage stage = new Stage();
                stage.setHeight(1040);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setAlwaysOnTop(true);
                stage.show();
        }

//        @FXML
//        private ComboBox<String> dbtypeCbx;
//
//        private ObservableList<String> dbTypeList = FXCollections.observableArrayList("SQLite");
//
//        //@Override
//        public void initialize(DocFlavor.URL location, ResourceBundle resources) {
//                dbtypeCbx.setItems(dbTypeList);
//        }
}