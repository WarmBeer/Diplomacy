package domains;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final String[] nations = {
            "France",
            "England",
            "Austria-Hungary",
            "Russia",
            "Germany",
            "Italy",
            "Turkey"
    };

    private ArrayList<Player> players;
    private ArrayList<Country> countries;

    public Game() {

        players = new ArrayList<Player>();
        countries = new ArrayList<Country>();
    }

}
