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

    public enum GameState {
        RUNNING,
        STOPPED,
        STARTING,
        GAME_OVER
    }

    private AnimationTimer animationTimer;
    private ChunkGenerator chunkGenerator;
    private Frog frog;
    private GraphicsContext gc;
    private ArrayList<Chunk> chunks;
    private Label debugText;
    private long startTime;
    private long lastTime;
    private boolean drawDebug;
    private GameState gameState;

    public Game(GridPane root) {
        gameState = GameState.STARTING;

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                gameLoopIncrement(l);
            }
        };
        Pane pane = new Pane();
        Canvas canvas = new Canvas(Froqr.GAME_SIZE_X, Froqr.GAME_SIZE_Y);

        debugText = new Label("");
        debugText.setTextFill(Color.RED);
        startTime = 0;
        drawDebug = false;

        pane.getChildren().add(canvas);
        pane.getChildren().add(debugText);

        root.getChildren().add(pane);

        gc = canvas.getGraphicsContext2D();

        frog = new Frog(13, 6);
        canvas.addEventHandler(KeyEvent.KEY_PRESSED, this);
        canvas.addEventHandler(MouseEvent.ANY, this);

        chunkGenerator = new ChunkGenerator(0);
        chunks = chunkGenerator.getStartingChunks();
    }

    public void start() {
        gameState = GameState.RUNNING;
        animationTimer.start();
    }

    public void gameLoopIncrement(long dt) {
        if(gameState == GameState.RUNNING || gameState == GameState.GAME_OVER) {
            update(dt);
            draw(dt);
        }
        else {
            animationTimer.stop();
        }
    }

    @Override
    public void handle(Event event) {
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
            else if(keyEvent.getCode() == KeyCode.Q) {
                debugText.setText("");
                drawDebug = !drawDebug;
            }
        }

    }

    private void update(long dt) {
        final double fps = 60;
        final double t = 1/fps;

        if(dt-lastTime > t * 1_000_000_000) {
            if(startTime == 0) {
                startTime = lastTime = dt;
                return;
            }
            long itc = dt - lastTime;

            if(gameState == GameState.GAME_OVER) {
                //Start gameover state
            }

            int lastIndex = 0;
            boolean generateNew = false;

            for(int i = 0; i < chunks.size(); i++) {
                Chunk chunk = chunks.get(i);

                if(chunk.isOutOfScreen()) {
                    generateNew = true;
                    lastIndex = i;
                }

                chunk.move(SCROLL_SPEED * itc);
            }

            if(generateNew) {
                Chunk lastChunk = chunks.get(lastIndex);
                Chunk newChunk = chunkGenerator.getNextChunk(lastChunk.getOffset());
                chunks.remove(lastIndex);
                chunks.add(newChunk);
            }

            frog.offsetY(SCROLL_SPEED * itc);

            checkCollision();
            lastTime = dt;
        }
    }

    private void checkCollision() {
        boolean collidedWithMovableObject = false;

        for(Chunk chunk : chunks) {
            if(chunk.getOffset() == frog.getChunkOffset()) {
                Tile tile = chunk.getTile(frog.getTilePosition());
                int type = tile.getType();

                if(type == TileType.WATER) {
                    gameState = GameState.GAME_OVER;
                }

                for(MovableObject movableObject : chunk.getMovableObjects()) {
                    //System.out.println(movableObject.getLeftX() + " " + movableObject.getRightX() + " " + frog.getXPosition());
                    if(frog.getXPosition() > movableObject.getLeftX() &&
                            frog.getXPosition() < movableObject.getRightX()) {
                        int movableObjectType = movableObject.getType();
                        if(movableObjectType == MovableObjectType.CAR ||
                                movableObjectType == MovableObjectType.LOG) {
                            //Collision with moving object - endgame
                            System.out.println("Collision");
                        }
                        else if(movableObjectType == MovableObjectType.BUS) {
                            movableObject.setPlayer(frog);
                            System.out.println("Collision");
                        }
                        System.out.println(movableObjectType);

                        collidedWithMovableObject = true;
                    }
                }
            }
        }

//        if(!collidedWithMovableObject && frog.getConnectedObject() != null) {
//            frog.getConnectedObject().setPlayer(null);
//        }
    }

    private void drawDebug(long dt) {
        debugText.setText("");

        addDebugText("Time from start: " + (dt - startTime) / 1_000_000 + "ms");
        addDebugText("Delta time: " + (double)(dt - lastTime) / 1_000_000 + "ms");

        for(int i = 0; i < chunks.size(); i++) {
            Chunk chunk = chunks.get(i);

            addDebugText(i + ": " + chunk.getOffset() / 1_000_000_000);
        }

        addDebugText(frog.getTilePosition() + " " + frog.getChunkPosition() + " " + frog.getOffsetY() / 1_000_000_000 + " " + frog.getChunkOffset() / 1_000_000_000);

        for(Chunk chunk : chunks) {
            if(chunk.getOffset() == frog.getChunkOffset()) {
                Tile tile = chunk.getTile(frog.getTilePosition());
                addDebugText("" + tile.getType());
            }
        }
    }

    private void addDebugText(String text) {
        debugText.setText(debugText.getText() + text + '\n');
    }

    private void draw(long dt) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, Froqr.GAME_SIZE_X, Froqr.GAME_SIZE_Y);

        for(Chunk chunk : chunks) {
            chunk.draw(gc);
        }

        frog.draw(gc);

        if(drawDebug) {
            drawDebug(dt);
        }
    }
}
