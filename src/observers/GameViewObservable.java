package observers;

import domains.Province;

import java.util.ArrayList;
import java.util.List;

public interface GameViewObservable {

    public void registerGameViewObserver(GameViewObserver gameViewObserver);
    public void unregisterGameViewObserver(GameViewObserver gameViewObserver);
    public void notifyGameViewObservers();
    public List<Province> getProvinces();
}
