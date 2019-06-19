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

    private List<Country> Countries;
    private List<Province> Provinces;
    private List<Player> Players;

    public Game(String gameUID, String name, int turnTime, int turn) {
        this.gameUID = gameUID;
        this.name = name;
        this.turnTime = turnTime;
        this.turn = turn;
        Countries = new ArrayList<>();
        Provinces = new ArrayList<>();
        Players = new ArrayList<>();
    }

    public void nextTurn() {
        turn++;
    }

    public void addCountry(Country country) {
        this.Countries.add(country);
    }

    public void addProvince(Province province) {
        this.Provinces.add(province);
    }

    public void addPlayer(Player player) {
        this.Players.add(player);
    }

    public void resetProvinces() {
        this.Provinces = new ArrayList<>();
    }

    public String getGameUID() {
        return  this.gameUID;
    }

    public int getTurnTime() {
        return  this.turnTime;
    }

    public int getTurn() {
        return turn;
    }

    public String getName() {
        return this.name;
    }

    public List<Country> getCountries() {
        return this.Countries;
    }

    public List<Player> getPlayers() {
        return Players;
    }

    public List<Province> getProvinces() {
        return Provinces;
    }

    public List<Unit> getUnits() {
        List<Unit> units = new ArrayList<>();

        for (Province province : Provinces) {
            if (province.getUnit() != null) {
                units.add(province.getUnit());
            }
        }
        return units;
    }
}
