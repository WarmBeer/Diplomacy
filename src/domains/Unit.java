package domains;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class Unit extends ImageView {

    private HashMap<String, Object> currentOrder;
    private ArrayList<Unit> supporters;
    private Player owner;
    private Province province;

    public Unit(String path) {
        super.setImage(new Image(path));
        currentOrder = new HashMap<String, Object>();
        supporters = new ArrayList<Unit>();
    }

}
