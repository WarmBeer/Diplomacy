package observers;

import domains.Province;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;

public interface GameViewObservable {

    public void registerGameViewObserver(GameViewObserver gameViewObserver);
    public void unregisterGameViewObserver(GameViewObserver gameViewObserver);
    public void notifyGameViewObservers();
    public List<Province> getProvinces();

    public Group getTroopsGroup();
    public Group getPointsGroup();
}
