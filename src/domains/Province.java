package domains;

import javafx.scene.image.ImageView;
import models.GameModel;

import java.util.ArrayList;

/**
 * Defines a province in the game.
 * It can hold information about neighbouring provinces, stationed Units, the country it belongs to and other basic info.
 *
 * @author Stefan Damen
 */
public class Province extends ImageView {

    private int id;

    public enum ProvinceType {
        SEA,
        COASTAL,
        LAND
    }
    private GameModel.Countries country;
    private ProvinceType provinceType;
    private String name;
    private String abbreviation;
    private Boolean isSupplyCenter;
    private Country owner;
    private Unit stationed;
    private ArrayList<Province> borderedProvinces = new ArrayList<Province>();

    /**
     * Overflow constructor for the province,
     * country is Countries.INDEPENDENT standard, provinceType is provinceType.LAND standard
     *
     * @author Stefan Damen
     *
     * @param name: String
     * @param abbreviation: String
     * @param isSupplyCenter: boolean
     * @param x: int
     * @param y: int
     */
    public Province(String name, String abbreviation, Boolean isSupplyCenter, int x, int y) {
        this(name, abbreviation, isSupplyCenter, x, y, GameModel.Countries.INDEPENDENT);
    }
    /**
     * Overflow constructor for the province, country is Countries.INDEPENDENT standard
     *
     * @author Stefan Damen
     *
     * @param name: String
     * @param abbreviation: String
     * @param isSupplyCenter: boolean
     * @param x: int
     * @param y: int
     * @param provinceType: ProvinceType
     */
    public Province(String name, String abbreviation, Boolean isSupplyCenter, int x, int y, ProvinceType provinceType) {
        this(name, abbreviation, isSupplyCenter, x, y, GameModel.Countries.INDEPENDENT, provinceType);
    }


    /**
     * Overflow constructor for the province, provinceType is provinceType.LAND standard
     *
     * @author Stefan Damen
     *
     * @param name: String
     * @param abbreviation: String
     * @param isSupplyCenter: boolean
     * @param x: int
     * @param y: int
     * @param country: GameModel.Countries
     */
    public Province(String name, String abbreviation, Boolean isSupplyCenter, int x, int y, GameModel.Countries country) {
        super();
        this.setX(x);
        this.setY(y);
        this.name = name;
        this.abbreviation = abbreviation;
        this.isSupplyCenter = isSupplyCenter;
        this.provinceType = provinceType.LAND;
        this.owner = null;
        this.stationed = null;
        this.country = country;
        checkCoastal();
    }

    /**
     * Main constructor for the province, abbreviation must be unique
     *
     * @author Stefan Damen
     *
     * @param name: String
     * @param abbreviation: String
     * @param isSupplyCenter: boolean
     * @param x: int
     * @param y: int
     * @param country: GameModel.Countries
     * @param provinceType: ProvinceType
     */
    public Province(String name, String abbreviation, Boolean isSupplyCenter, int x, int y, GameModel.Countries country, ProvinceType provinceType) {
        super();
        this.setX(x);
        this.setY(y);
        this.name = name;
        this.abbreviation = abbreviation;
        this.isSupplyCenter = isSupplyCenter;
        this.provinceType = provinceType;
        this.owner = null;
        this.country = country;
        checkCoastal();
    }

    public boolean isSupplyCenter() {
        return this.isSupplyCenter;
    }

    public GameModel.Countries getCountry() {
        return this.country;
    }

    private void checkCoastal() {
        if(provinceType != ProvinceType.SEA &&
                doesBorderType(ProvinceType.SEA)) {
            this.provinceType = ProvinceType.COASTAL;
        }
    }

    /**
     * Check if province borders any sea type province and isn't a sea type province itself
     *
     * @author Stefan Damen
     */
    public boolean isCoastal() {
        return this.provinceType == ProvinceType.COASTAL;
    }

    private boolean doesBorderType(ProvinceType type) {
        for(Province province : borderedProvinces) {
            if(province.provinceType == type)
                return true;
        }
        return false;
    }

    public void setOwner(Country owner) {
        this.owner = owner;
    }

    public Country getOwner() {
        return this.owner;
    }

    public ProvinceType getProvinceType() {
        return this.provinceType;
    }

    public String getAbbreviation() {
        return this.abbreviation;
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

    /**
     * Adds a border connection between the two provinces
     *
     * @author Stefan Damen
     *
     * @param province: Province
     */
    public void addBorder(Province province) {
        province.getBorders().add(this);
        borderedProvinces.add(province);
    }

    public void removeUnit() {
        this.stationed = null;
    }

    public ArrayList<Province> getBorders() {
        return this.borderedProvinces;
    }

    /**
     * Spawns an Unit on the province if it is a supply center, is not independent and hasn't got an Unit already.
     * depending on the type province type it spawn an Unit or a fleet
     *
     * @author Mick Dijke
     */
    public void spawnUnit() {
        if (this.stationed == null && isSupplyCenter && getOwner().getName() != GameModel.Countries.INDEPENDENT) {
            switch (provinceType) {
                case LAND:
                    this.stationed = new Army(this);
                    break;
                case COASTAL:
                    this.stationed = new Army(this);
                    break;
                case SEA:
                    this.stationed = new Fleet(this);
                    break;
            }
        }
    }
}
