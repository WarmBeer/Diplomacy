package domains;

/**
 *
 */
public class Orders {

    public Unit unit;
    public Province target;
    public Province origin;
    public Unit.orderType type;

    public Orders(Unit unit) {
        this.unit = unit;
        this.origin = unit.getProvince();
        this.target = unit.getTargetProvince();
        this.type = unit.getCurrentOrder();
    }
}
