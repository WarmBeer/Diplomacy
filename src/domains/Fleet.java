package domains;

import application.Main;

public class Fleet extends Unit{

    public Fleet(Province province) {
        super("sprites/Fleet-" + province.getOwner().getName() + ".png", province, Main.unitType.FLEET);
        setScaleY(1.1);
        setScaleX(1.1);
    }
}
