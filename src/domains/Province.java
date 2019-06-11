package domains;

import java.util.ArrayList;

public class Province {

    enum provinceType {
        COASTAL,
        LAND
    }

    private String name;
    private String abreviation;
    private Boolean isSupplyCenter;
    private Player owner;
    private Unit stationed;
    private ArrayList<Province> borderedProvinces = new ArrayList<Province>();

    public Province() {

    }
}
