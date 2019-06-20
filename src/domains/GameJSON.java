package domains;

import models.GameModel;

import java.util.List;

public class GameJSON {

    public String gameUID;
    public String name;
    public int turnTime;
    public String host;
    public int turn;
    public boolean inLobby;
    public List<GameModel.Countries> freeCountries;

    public List<ProvinceJSON> Provinces;
    public List<Player> Players;
}
