package application;

import javafx.application.Application;
import javafx.stage.Stage;
import utilities.KeyHandler;
import views.GameView;


public class Main extends Application {

        private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        private GameView game;
        public static enum unitType {ARMY, FLEET};


        public static void main(String[] args) {
                launch(args);
        }


        @Override
        public void start(Stage primaryStage) throws Exception {

                this.gameModel = new GameModel();
                this.superModel = new SuperModel();

                Parent panel;
                panel = FXMLLoader.load(getClass().getResource(GAME_VIEW));
                Scene scene = new Scene(panel);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Diplomacy v0.1");
                stage.setMaximized(true);
                stage.show();

                setup();
                gameModel.show(stage);

                loadGame();
        }

        public void loadGame() {

                Reader reader = new BufferedReader(new InputStreamReader(
                        this.getClass().getResourceAsStream("/" + "Diplomacy.json")));
                Gson gson = new GsonBuilder().create();
                GameJSON gameJSON = gson.fromJson(reader, GameJSON.class);

                gameModel.initGame(gameJSON);
        }

        public void setup() {

            String jarLocation = KeyHandler.getJarLocation();

            print( jarLocation );

            File file = new File(jarLocation + File.separator + "KEY.txt");

            if (file.exists()) {
                try {
                    this.KEY = new Scanner(file).useDelimiter("\\Z").next();
                    print("Key found!: " + this.KEY);
                    //retrieveSaves
                } catch (FileNotFoundException fnf) {
                    fnf.printStackTrace();
                }
            } else {
                print("Key not found, creating one for you!");
                KeyHandler.createKeyFile(jarLocation);
            }
        }
}