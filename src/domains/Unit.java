package domains;

import application.Main;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class Unit extends ImageView {

    public enum orderType {
        HOLD,
        MOVE,
        SUPPORT
    }

    private HashMap<String, Object> currentOrder;
    private ArrayList<Unit> supporters;
    private Country owner;
    public Province province;
    private Main.unitType unitType;

    public Unit(String path, Province province, Main.unitType unitType) {
        super.setImage(new Image(path));
        currentOrder = new HashMap<>();
        currentOrder.put("orderType", "Hold");
        currentOrder.put("orderTarget", "");
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

    public void addOrder(orderType order, String target) {
        currentOrder.put("orderType", order);
        currentOrder.put("orderTarget", target);
    }

    public HashMap<String, Object> getCurrentOrder() {
        return currentOrder;
    }

    public orderType getOrderType() {
        switch (currentOrder.get("orderType").toString()) {
            case "Move":
                return orderType.MOVE;

            case "Support":
                return orderType.SUPPORT;

            default:
            case "Hold":
                return orderType.HOLD;
        }
    }
}
