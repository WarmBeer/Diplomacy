package observers;

import java.util.ArrayList;

public interface GameObservable {

    public void registerObserver(GameObserver gameobserver);
    public void unregisterObserver(GameObserver gameobserver);
    public void notifyObservers();
    public ArrayList<String> getOrderList();
}