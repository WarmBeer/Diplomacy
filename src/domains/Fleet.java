package domains;

import application.Main;

/**
 * A Fleet that extends a Unit. A Fleet can be stationed on a province that is of type Sea.
 */
public class Fleet extends Unit{

    public Fleet(Province province) {
        super("sprites/Fleet-" + province.getOwner().getName() + ".png", province, Main.unitType.FLEET);
        setScaleY(1.1);
        setScaleX(1.1);
    }
}
