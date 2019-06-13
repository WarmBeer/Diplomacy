package resources.views;

import controllers.in_GameMenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class in_GameMenuView {

    private Parent content;
    in_GameMenuController in_gamecontroller = new in_GameMenuController();

    public in_GameMenuView(Stage primaryStage) {
        maakView(primaryStage);
    }

    private void maakView(Stage primaryStage) {
        String sceneFile = "../resources/views/in_GameMenu.fxml";
        String spelregelFile = "../resources/views/Spelregels.fxml";

        try {
            URL url = getClass().getResource(spelregelFile);
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(this);
            content = loader.load();

            primaryStage.setTitle("Spelregels");
            primaryStage.setScene(new Scene(content, 300, 275));
            primaryStage.setFullScreen(true);
            primaryStage.show();
        } catch (IOException IOE) {
            IOE.printStackTrace();
        }
    }

    @FXML
    private Pagination paginationrules;

    @FXML
    private VBox MainMenu;

    @FXML
    private Button MenuButton;

    @FXML
    private Button OpslaanKnop;

    @FXML
    private Button LadenKnop;

    @FXML
    private Button AfsluitenKnop;

    @FXML
    private Button SpelregelsKnop;

    @FXML
    private Button OptiesKnop;

    @FXML
    private void AfsluitenView() {
        in_gamecontroller.afsluitenController();
    }
    @FXML
    private VBox createPage(int pageIndex) {
        final ArrayList<String> imagesRules = new ArrayList<>();
        for (int i = 1; i <= 24; i++) {
            imagesRules.add("/resources/rules/rulebook-" + i + ".png");
        }
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        ImageView iv = new ImageView(String.valueOf(imagesRules.get(pageIndex)));
        box.getChildren().addAll(iv);
        return box;
    }
}
