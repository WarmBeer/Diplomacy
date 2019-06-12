package domains;

import java.util.List;

public class GameJSON {

    public String gameUID;
    public String name;
    public int turnTime;
    public String host;
    public int turn;

    public List<ProvinceJSON> Provinces;
    public List<Player> Players;
}
