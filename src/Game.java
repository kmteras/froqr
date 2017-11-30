import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Game implements EventHandler<Event> {
    private Input input;
    private AnimationTimer animationTimer;
    private Frog frog;
    private GraphicsContext gc;

    public Game(Canvas canvas) {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                gameLoopIncrement();
            }
        };

        gc = canvas.getGraphicsContext2D();

        frog = new Frog();
        //input = new Input();
        canvas.addEventHandler(KeyEvent.KEY_PRESSED, this);
        canvas.addEventHandler(MouseEvent.ANY, this);
    }

    public void start() {
        animationTimer.start();
    }

    public void gameLoopIncrement() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 400, 400);
        gc.setFill(Color.BLUE);
        frog.draw(gc);
    }

    @Override
    public void handle(Event event) {
        System.out.println(event.getEventType());
        if(event.getEventType() == KeyEvent.KEY_PRESSED) {
            KeyEvent keyEvent = (KeyEvent)event;
            if(keyEvent.getCode() == KeyCode.D) {
                frog.move(32, 0);
            }
            else if(keyEvent.getCode() == KeyCode.A) {
                frog.move(-32,  0);
            }
        }

    }
}
