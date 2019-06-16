package observers;

import views.MainMenuView;

public interface MainMenuObservable {
    public void registerObserver(MainMenuView v);

    public void unregisterObserver(MainMenuView v);

    public void notifyObservers();
}
