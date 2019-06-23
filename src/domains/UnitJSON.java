package domains;

import application.Main;

/**
 * A serialized object of the Unit class that  can be stored in Firestore.
 * @author Mick van Dijke
 */

public class UnitJSON {

    public Main.unitType unitType;
    public Unit.orderType orderType;
    public String orderTarget;

}
