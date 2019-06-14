package observers;

import domains.Province;

import java.util.ArrayList;

public interface GameViewObservable {

    public void registerGameViewObserver(GameViewObserver gameViewObserver);
    public void unregisterGameViewObserver(GameViewObserver gameViewObserver);
    public void notifyGameViewObservers();
    public ArrayList<Province> getProvinces();
}
