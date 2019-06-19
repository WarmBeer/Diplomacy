package observers;

import java.util.ArrayList;
import java.util.Map;

public interface ChatObservable {

    public void registerChatObserver(ChatObserver chatobserver);
    public void unregisterChatObserver(ChatObserver chatobserver);
    public void notifyChatObservers();
    public ArrayList<String> getArrayListWithMessages();
}