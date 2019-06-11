package domains;

public class Army extends Unit {

    private String unitType;
    private int province;

    public Army(Province province) {
        super("/France-Army.png", province);
    }
}
