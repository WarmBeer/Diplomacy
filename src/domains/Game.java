package domains;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private String gameUID;
    private String name;
    private int turnTime;
    private String host;
    private int turn;

    private List<Province> Provinces;
    private List<Player> Players;

    public Game(String gameUID, String name, int turnTime, int turn) {
        this.gameUID = gameUID;
        this.name = name;
        this.turnTime = turnTime;
        this.turn = turn;
        Provinces = new ArrayList<Province>();
        Players = new ArrayList<Player>();
    }

    public void addProvince(Province province) {
        this.Provinces.add(province);
    }

    public void addPlayer(Player player) {
        this.Players.add(player);
    }

    public String getName() {
        return this.name;
    }

    public List<Player> getPlayers() {
        return Players;
    }

    public List<Province> getProvinces() {
        return Provinces;
    }
}
