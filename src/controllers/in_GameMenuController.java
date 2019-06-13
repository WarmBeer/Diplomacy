package controllers;


import models.in_GameMenuModel;

public class in_GameMenuController {

    in_GameMenuModel inGameModel;

    private static in_GameMenuController in_gmc;

    /**
     * Singleton Pattern.
     * We can call SpelbordController.getInstance() from everywhere
     * And there is only 1 instance.
     *
     * @return A instance of the class itself (in_GameMenuController).
     * @author Thomas Zijl
     */
    public static in_GameMenuController getInstance() {
        if (in_gmc == null) {
            in_gmc = new in_GameMenuController();
        }
        return in_gmc;
    }

    public in_GameMenuController() {
        inGameModel = new in_GameMenuModel();
    }

    public void afsluitenController() {
        inGameModel.afsluitenModel();
    }

}
