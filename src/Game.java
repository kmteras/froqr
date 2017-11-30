import com.sun.javafx.geom.Vec2d;
import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Game implements EventHandler<Event> {
    private AnimationTimer animationTimer;
    private Frog frog;
    private GraphicsContext gc;
    private Chunk[] chunks;
    private Label scoreLabel;
    private long startTime;
    private long lastTime;

    public Game(GridPane root) {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                gameLoopIncrement(l);
            }
        };
        Pane pane = new Pane();
        Canvas canvas = new Canvas(Froqr.GAME_SIZE_X, Froqr.GAME_SIZE_Y);

        scoreLabel = new Label("0");
        startTime = 0;

        pane.getChildren().add(canvas);
        pane.getChildren().add(scoreLabel);

        root.getChildren().add(pane);

        gc = canvas.getGraphicsContext2D();

        frog = new Frog();
        canvas.addEventHandler(KeyEvent.KEY_PRESSED, this);
        canvas.addEventHandler(MouseEvent.ANY, this);

        generateChunks();
    }

    public void start() {
        animationTimer.start();
    }

    public void gameLoopIncrement(long dt) {
        update(dt);
        draw(dt);
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
            else if(keyEvent.getCode() == KeyCode.W) {
                frog.move(0, -32);
            }
            else if(keyEvent.getCode() == KeyCode.S) {
                frog.move(0,  32);
            }
        }

    }

    public void generateChunks() {
        chunks = new Chunk[2];

        for(int i = 0; i < chunks.length; i++) {
            chunks[i] = new Chunk(new Vec2d(0, i * Tile.TILESIZE));
        }
    }

    private void update(long dt) {
        if(startTime == 0) {
            startTime = dt;
        }

        scoreLabel.setText("Delta time: " + (dt - startTime) / 1_000_000 + "\n" +
        "Dt: " + (double)(dt - lastTime) / 1_000_000 + "ms");

        for(Chunk chunk : chunks) {
            chunk.move(0.1);
        }
        frog.move(0, 0.1);
        lastTime = dt;
    }

    private void draw(long dt) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, Froqr.GAME_SIZE_X, Froqr.GAME_SIZE_Y);

        for(Chunk chunk : chunks) {
            chunk.draw(gc);
        }

        frog.draw(gc);
    }
}
