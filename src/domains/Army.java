package domains;


import application.Main;

public class Army extends Unit {

    public Army(Province province) {
        super("/Army-" + province.getCountry().toString() + ".png", province, Main.unitType.ARMY);
    }
}
