package domains;

import java.util.ArrayList;

public class Country {

    enum colors {
        BLUE,
        CYAN,
        RED,
        ORANGE,
        GREEN,
        WHITE,
        BLACK
    }

    private String name;
    private colors color;
    private ArrayList<Province> provinces = new ArrayList<Province>();

    public Country() {

    }
}
