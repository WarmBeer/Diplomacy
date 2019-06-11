package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class in_GameMenuController implements Initializable {

    @FXML
    private VBox rootPane;

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
    private void OpenInGameMenu(){

        MainMenu.setVisible(!MainMenu.isVisible());
    }

    @FXML
    private void AfsluitenGame() {

        System.exit(0);
    }



    public VBox createPage(int pageIndex) {
        VBox box = new VBox(5);
        int page = pageIndex;
        for (int i = page; i < page; i++) {
            VBox element = new VBox();
            Label text = new Label("Search results\nfor ");
            element.getChildren().addAll(text);
            box.getChildren().add(element);
        }
        return box;
    }

    @FXML
    public void CreerPagina() throws IOException {
        final ArrayList<Image> imagesRules = new ArrayList<>();
        Pagination p = new Pagination(24);

        p.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                return createPage(pageIndex);
            }
        });


        Parent panel;
        panel = FXMLLoader.load(getClass().getResource("/resources/views/Spelregels.fxml"));
        Scene scene = new Scene(panel);
        Stage stage = new Stage();
        stage.setScene(scene);

        stage.setTitle("Diplomacy v0.1");
        stage.setMaximized(true);
        //stage.setResizable(false);
        stage.show();

        for(int i = 1; i <= 24; i++) {
            imagesRules.add(new Image("/resources/rules/diplomacy_rulebook-" + i + ".png"));


        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void ShowSpelregels(ActionEvent event) throws IOException {
        VBox pane = FXMLLoader.load(getClass().getResource("resources/views/Spelregels.fxml"));
        rootPane.getChildren().setAll(pane);
        Scene scene = new Scene(rootPane);


    }

}