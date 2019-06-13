package observers;

import views.GameView;

public interface GameObservable {

    public void registerObserver(GameView v);

    public void unregisterObserver(GameView v);

    public void notifyObservers();
}