package observers;


import domains.GameJSON;

import java.util.List;

public interface MainMenuViewObservable {

    public void registerMainMenuViewObserver(MainMenuViewObserver mainMenuViewObserver);
    public void unregisterMainMenuViewObserver(MainMenuViewObserver mainMenuViewObserver);
    public void notifyMainMenuViewObservers();

    public boolean doShowMainMenu();
}
