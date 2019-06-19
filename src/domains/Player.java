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

    public void setUID(String uid) {
        this.UID = uid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(GameModel.Countries country) {
        this.country = country;
    }

    public Player() {

    }
}
