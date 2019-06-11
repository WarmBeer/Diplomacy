package domains;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Unit extends ImageView {

    public Unit(String path) {
        super.setImage(new Image(path));
        //super.setScaleX(0.4);
        //super.setScaleY(0.4);
    }

}
