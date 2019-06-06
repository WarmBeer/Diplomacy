package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.GameModel;
import models.Model;

/**
 * Bevat alle code om het programma te kunnen starten.
 * Dit is een online spel waarvoor internetverbinding vereist is.
 * Het kan met maximaal 7 mensen gespeeld worden.
 *
 * @author Edwin
 */
public class Main extends Application {

        public static String GAME_VIEW = "/resources/views/GameView.fxml";
        public static String MAIN_MENU = "/resources/views/MainMenu.fxml";
        public static String STYLESHEET_FILE = "/resources/style.css";
        private Stage stage;

        private Model gameModel;

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
                Parent panel;
                panel = FXMLLoader.load(getClass().getResource(MAIN_MENU));
                Scene scene = new Scene(panel);
                stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Diplomacy v0.1");
                stage.setMaximized(true);
                stage.show();

                gameModel.show(stage);
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