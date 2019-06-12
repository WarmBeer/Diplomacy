package domains;

import models.GameModel;

public class Player {

    private String UID;
    private int id;
    private String name;
    private GameModel.Countries country;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public GameModel.Countries getCountry() {
        return country;
    }

    public Player() {

    }
}
