import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.ArrayList;

public class Input {
    private ArrayList<KeyCode> pressedKeys;
    private EventHandler<KeyEvent> keyEventHandler;

    public Input() {
        pressedKeys = new ArrayList<>();
        keyEventHandler = new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                handleKeyEvent(keyEvent);
            }
        };
    }

    public boolean isPressed(KeyCode key) {
        return pressedKeys.contains(key);
    }

    public void addKey(KeyCode key) {
        pressedKeys.add(key);
    }

    public void removeKey(KeyCode key) {
        pressedKeys.remove(key);
    }

    private void handleKeyEvent(KeyEvent keyEvent) {
        if(keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
            addKey(keyEvent.getCode());
        }
        else if(keyEvent.getEventType() == KeyEvent.KEY_RELEASED) {
            removeKey(keyEvent.getCode());
        }
    }

    public EventHandler<KeyEvent> getKeyEventHandler() {
        return keyEventHandler;
    }
}
