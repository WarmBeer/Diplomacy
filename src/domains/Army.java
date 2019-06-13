package domains;


public class Army extends Unit {

    public Army(Province province) {
        super("/Army-" + province.getOwner().getName().toString() + ".png", province);
    }
}
