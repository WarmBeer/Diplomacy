package domains;

import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Province extends ImageView {

    private int id;

    public enum ProvinceType {
        SEA,
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

    public Province(String name, String abbreviation, Boolean isSupplyCenter, int x, int y) {
        super();
        this.setX(x);
        this.setY(y);
        this.name = name;
        this.abbreviation = abbreviation;
        this.isSupplyCenter = isSupplyCenter;
        this.provinceType = provinceType.LAND;
        this.owner = null;
    }

    public Province(String name, String abbreviation, Boolean isSupplyCenter, int x, int y, ProvinceType provinceType) {
        super();
        this.setX(x);
        this.setY(y);
        this.name = name;
        this.abbreviation = abbreviation;
        this.isSupplyCenter = isSupplyCenter;
        this.provinceType = provinceType;
        this.owner = null;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return this.owner;
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
