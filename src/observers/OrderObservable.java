package observers;

import java.util.ArrayList;

public interface OrderObservable {

    public void registerOrderObserver(OrderObserver orderobserver);
    public void unregisterOrderObserver(OrderObserver orderobserver);
    public void notifyOrderObservers();
    public ArrayList<String> getOrderList();
}