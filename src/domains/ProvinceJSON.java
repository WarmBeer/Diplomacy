package domains;

import application.Main;

import java.util.List;

public class ProvinceJSON {

    public String id;
    public String name;
    public String abbr;
    public Province.ProvinceType provinceType;
    public int owner;
    public Boolean isSupplyCenter;
    public Main.unitType stationed;
    public List<String> borderedProvinces;
    public int x, y;
}
