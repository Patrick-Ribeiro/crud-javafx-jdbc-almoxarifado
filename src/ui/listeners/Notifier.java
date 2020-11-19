package ui.listeners;

import java.util.ArrayList;
import java.util.List;

public interface Notifier {

    void subscribeListener(DataChangeListener listener);

    void notifyListeners();
}
