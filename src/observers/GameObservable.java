package observers;

import java.util.ArrayList;

public interface GameObservable {
    public void register(GameObserver observer);
    public void notifyAllObservers();
    public ArrayList<String> getArrayListWithMessages();
}

