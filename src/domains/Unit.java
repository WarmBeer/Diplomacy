package domains;

import application.Main;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Unit extends ImageView {

    public enum orderType {
        HOLD,
        MOVE,
        SUPPORT
    }

    //private HashMap<String, Object> currentOrder;
    private orderType currentOrder;
    private Province targetProvince;
    private ArrayList<Unit> supporters;
    private Country owner;
    public Province province;
    private Main.unitType unitType;
    private boolean dead = false;

    public Unit(String path, Province province, Main.unitType unitType) {
        super.setImage(new Image(path));
        currentOrder = orderType.HOLD;
        targetProvince = province;
        supporters = new ArrayList<>();
        this.province = province;
        this.owner = province.getOwner();
        this.unitType = unitType;
    }

    public Main.unitType getUnitType() {
        return unitType;
    }

    public void addSupporter(Unit unit) {
        this.supporters.add(unit);
    }

    public void addOrder(orderType order, Province target) {
        this.currentOrder = order;
        this.targetProvince = target;
    }

    public Province getProvince() {
        return province;
    }

    public Province getTargetProvince() {return this.targetProvince;}

    public void doOrder() {
        if(targetProvince == null) {
            targetProvince = this.province;
        }
        if (!dead) {
            System.out.println("yeeeeeeet");
            switch (currentOrder) {
                case MOVE:
                    if(targetProvince.getUnit() == null) {
                        targetProvince.addUnit(this);
                        province.removeUnit();
                        this.province = targetProvince;
                    } else if (targetProvince.getUnit().getSupporters().size() < this.supporters.size()) {
                        targetProvince.getUnit().destroy();
                        targetProvince.addUnit(this);
                        province.removeUnit();
                        this.province = targetProvince;
                    }
                    break;
                case SUPPORT:
                    targetProvince.getUnit().addSupporter(this);
                    break;
            }
        }
    }

    public void destroy() {
        this.dead = true;
    }

    public orderType getCurrentOrder() {
        return currentOrder;
    }

    public List<Unit> getSupporters() {
        return  this.supporters;
    }
}
