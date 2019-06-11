package models;

import domains.Army;
import domains.Fleet;
import domains.Unit;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static application.Main.GAME_VIEW;

public class GameModel implements Model {

    private enum unitType {
        Army,
        Fleet
    }

    Group root; //Kaart en UI render groep
    Group troops; //Troepen render groep
    Group points; //Provincie punt render groep

    @FXML
    public void show(Stage stage) throws Exception{

        root = new Group();
        troops = new Group();
        points = new Group();

        Parent pane = FXMLLoader.load(
                getClass().getResource(GAME_VIEW));

        Scene scene = new Scene( root, 1920, 1080 );
        root.getChildren().addAll(pane, troops, points);

        stage.setScene(scene);
    }

    @FXML
    public void init() {
        //Maak troepen aan
        createUnit(unitType.Army, 400d, 650d);
        createUnit(unitType.Army, 480d, 740d);
        createUnit(unitType.Army, 330d, 630d);
        createUnit(unitType.Fleet, 10d, 650d);
    }

    public void createUnit(unitType unit, double x, double y) {
        Unit troop = null;

        switch (unit) {
            case Army:
                troop = new Army();
                break;
            case Fleet:
                troop = new Fleet();
                break;
        }

        moveUnit(troop, x, y);

        //Render troepen
        troops.getChildren().add(troop);
    }

    public void moveUnit(Unit unit, double x, double y) {
        unit.setX(x);
        unit.setY(y);
    }

}
