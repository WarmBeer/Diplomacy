package controllers;


import models.in_GameMenuModel;
import views.MainMenu;

public class in_GameMenuController {

    in_GameMenuModel inGameModel;

    public in_GameMenuController() {
        inGameModel = new in_GameMenuModel();
    }

    public void afsluitenController() {
        inGameModel.afsluitenModel();
    }

//    public void openInGameMenuController() {
//        inGameModel.openInGameMenuModel();
//    }





















//    @FXML
//    private VBox VboxSpelregels;
//
//    @FXML
//    private Pagination paginationrules;
//
//    @FXML
//    private VBox MainMenu;
//
//    @FXML
//    private Button MenuButton;
//
//    @FXML
//    private Button OpslaanKnop;
//
//    @FXML
//    private Button LadenKnop;
//
//    @FXML
//    private Button AfsluitenKnop;
//
//    @FXML
//    private Button SpelregelsKnop;
//
//    @FXML
//    private Button OptiesKnop;
//
//    @FXML
//    private void OpenInGameMenu(){
//
//        MainMenu.setVisible(!MainMenu.isVisible());
//    }
//
//    @FXML
//    private void AfsluitenGame() {
//
//        System.exit(0);
//    }
//
//    @FXML
//    private VBox createPage(int pageIndex) {
//        final ArrayList<String> imagesRules = new ArrayList<>();
//        for (int i = 1; i <= 24; i++) {
//            imagesRules.add("/resources/rules/rulebook-" + i + ".png");
//        }
//        VBox box = new VBox();
//        box.setAlignment(Pos.CENTER);
//        ImageView iv = new ImageView(String.valueOf(imagesRules.get(pageIndex)));
//        box.getChildren().addAll(iv);
//        return box;
//    }
//
////    @FXML
////    public void CreerPagina() throws IOException {
////    }
////        final ArrayList<String> imagesRules = new ArrayList<>();
////        for (int i = 1; i <= 24; i++) {
////            imagesRules.add("/resources/rules/rulebook-" + i + ".png");
////        }
//
//
////        Parent panel;
////        panel = FXMLLoader.load(getClass().getResource("/resources/views/Spelregels.fxml"));
////        Scene scene = new Scene(panel);
////        Stage stage = new Stage();
////        stage.setScene(scene);
////
////        stage.setTitle("Diplomacy v0.1");
////        stage.setMaximized(true);
////        //stage.setResizable(false);
////        stage.show();
//
////    }
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        paginationrules.setPageCount(24);
//        paginationrules.setPageFactory((Integer pageIndex) -> createPage(pageIndex));
//    }
}
