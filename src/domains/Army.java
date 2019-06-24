package domains;


import application.Main;

/**
 * An Army that extends a Unit. An army can be stationed on a province that is of type Land or Coastal.
 */
public class Army extends Unit {

    public Army(Province province) {
        super("sprites/Army-" + province.getOwner().getName() + ".png", province, Main.unitType.ARMY);
    }
}
