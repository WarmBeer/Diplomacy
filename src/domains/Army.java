package domains;


import application.Main;

public class Army extends Unit {

    public Army(Province province) {
        super("sprites/Army-" + province.getOwner().getName() + ".png", province, Main.unitType.ARMY);
    }
}
