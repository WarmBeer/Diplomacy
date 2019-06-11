package domains;

import java.util.ArrayList;

public class Player {

    private int id;
    private String name;
    private Boolean isHost;
    private Country country;
    private ArrayList<Unit> units = new ArrayList<Unit>();

    public Player() {

    }
}
