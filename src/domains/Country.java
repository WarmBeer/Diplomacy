package domains;

import models.GameModel;

import java.util.ArrayList;

public class Country {

    private GameModel.Countries name;
    private Player leader;
    private ArrayList<Province> provinces;

    public Country(GameModel.Countries name) {
        this.name = name;
        this.leader = null;
        this.provinces = new ArrayList<Province>();
    }

    public GameModel.Countries getName() {
        return this.name;
    }
}
