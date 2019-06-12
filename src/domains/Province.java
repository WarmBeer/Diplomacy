package domains;

import java.util.ArrayList;

public class Province {

    private int id;

    public enum ProvinceType {
        COASTAL,
        LAND
    }

    private ProvinceType provinceType;
    private String name;
    private String abbreviation;
    private Boolean isSupplyCenter;
    private Player owner;
    private Unit stationed;
    private ArrayList<Province> borderedProvinces = new ArrayList<Province>();
    private int x, y;

    public Province(String name, String abreviation, int x, int y) {
        this.x = (int) Math.round((x));
        this.y = (int) Math.round((y));
        this.name = name;
        this.abbreviation = abreviation;
        provinceType = ProvinceType.LAND;
    }

    public Province(String name, String abreviation, int x, int y, ProvinceType provinceType) {
        this.x = (int) Math.round((x));
        this.y = (int) Math.round((y));
        this.name = name;
        this.abbreviation = abreviation;
        this.provinceType = provinceType;
    }

    public ProvinceType getProvinceType() {
        return this.provinceType;
    }

    public String getName() {
        return this.name;
    }

    public void addUnit(Unit troop) {
        this.stationed = troop;
    }

    public Unit getUnit() {
        return this.stationed;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setIsSupplyCenter(boolean supplyCenter) {
        this.isSupplyCenter = supplyCenter;
    }

    public void addBorder(Province province) {
        province.getBorders().add(this);
        borderedProvinces.add(province);
    }

    public ArrayList<Province> getBorders() {
        return this.borderedProvinces;
    }
}
