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

import java.util.ArrayList;

public class Game implements EventHandler<Event> {
    private static long SCROLL_SPEED = 20;

    private AnimationTimer animationTimer;
    private ChunkGenerator chunkGenerator;
    private Frog frog;
    private GraphicsContext gc;
    private ArrayList<Chunk> chunks;
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
        scoreLabel.setTextFill(Color.RED);
        startTime = 0;

        pane.getChildren().add(canvas);
        pane.getChildren().add(scoreLabel);

        root.getChildren().add(pane);

        gc = canvas.getGraphicsContext2D();

        frog = new Frog(0, 7);
        canvas.addEventHandler(KeyEvent.KEY_PRESSED, this);
        canvas.addEventHandler(MouseEvent.ANY, this);

        chunkGenerator = new ChunkGenerator(0);
        chunks = chunkGenerator.getStartingChunks();
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
        //System.out.println(event.getEventType());
        if(event.getEventType() == KeyEvent.KEY_PRESSED) {
            KeyEvent keyEvent = (KeyEvent)event;
            if(keyEvent.getCode() == KeyCode.D) {
                frog.moveTile(1, 0);
            }
            else if(keyEvent.getCode() == KeyCode.A) {
                frog.moveTile(-1,  0);
            }
            else if(keyEvent.getCode() == KeyCode.W) {
                frog.moveTile(0, -1);
            }
            else if(keyEvent.getCode() == KeyCode.S) {
                frog.moveTile(0,  1);
            }
        }

    }

    private void update(long dt) {
        final double fps = 60;
        final double t = 1/fps;

        if(dt-lastTime > t * 1_000_000_000) {
            if(startTime == 0) {
                startTime = dt;
                lastTime = dt;
                return;
            }

            double tc = (double)(dt - lastTime) / 1_000_000;
            long itc = dt - lastTime;

            scoreLabel.setText("Time from start: " + (dt - startTime) / 1_000_000 + "ms\n" +
                    "Delta time: " + (double)(dt - lastTime) / 1_000_000 + "ms\n");

            int lastIndex = 0;
            boolean generateNew = false;

            for(int i = 0; i < chunks.size(); i++) {
                Chunk chunk = chunks.get(i);

                if(chunk.isOutOfScreen()) {
                    generateNew = true;
                    lastIndex = i;
                }

                chunk.move(SCROLL_SPEED * itc);

                scoreLabel.setText(scoreLabel.getText() + chunk.getOffset() + '\n');
            }

            if(generateNew) {
                Chunk lastChunk = chunks.get(lastIndex);
                Chunk newChunk = chunkGenerator.getNextChunk(lastChunk.getOffset());
                chunks.remove(lastIndex);
                chunks.add(newChunk);
            }

            frog.offset(SCROLL_SPEED * itc);
            lastTime = dt;
        }
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
