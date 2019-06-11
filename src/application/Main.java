package application;

import controllers.MainController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import views.MainMenu;
import javafx.collections.ObservableList;

import javax.print.DocFlavor;
import java.io.IOException;
import java.util.ResourceBundle;

public class Main extends Application {

        public static String GAME_VIEW = "/Resources/views/GameView.fxml";
        public static String MAIN_MENU = "/Resources/views/MainMenu.fxml";
        public static String STYLESHEET_FILE = "/Resources/style.css";
        private Stage stage;

        private GameModel gameModel;
        private SuperModel superModel;

        public static void main(String[] args) {
                launch(args);
        }

        /**
         * Hello World,
         * by calling this, the application will be started.
         */

        @Override
        public void start(Stage primaryStage) throws Exception {

                this.gameModel = new GameModel();
                this.superModel = new SuperModel();

                Parent panel;
                panel = FXMLLoader.load(getClass().getResource(MAIN_MENU));
                Scene scene = new Scene(panel);
                stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Diplomacy v0.1");
                stage.setMaximized(true);
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
=========
        public void start(Stage stage) throws Exception {
                String userName = "Thomas";
                String gameName = "Game4";
                boolean host = true;
                game = new GameView(stage,userName,gameName,host);
        }

>>>>>>>>> Temporary merge branch 2
}