import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

public class Game implements EventHandler<Event> {
    private long SCROLL_SPEED = 20;

    public enum GameState {
        RUNNING,
        STARTING,
        GAME_OVER
    }

    private AnimationTimer animationTimer;
    private ChunkGenerator chunkGenerator;
    private Frog frog;
    private GraphicsContext gc;
    private Pane gameOverPane;
    private Text gameOverScore;
    private TextArea highScoresTextArea;
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
        Pane gamePane = new Pane();
        gamePane.setPrefSize(Froqr.GAME_SIZE_X, Froqr.GAME_SIZE_Y);
        Canvas canvas = new Canvas(Froqr.GAME_SIZE_X, Froqr.GAME_SIZE_Y);

        debugText = new Label("");
        debugText.setTextFill(Color.RED);
        drawDebug = false;

        gamePane.getChildren().add(canvas);
        gamePane.getChildren().add(debugText);
        GridPane.setColumnIndex(gamePane, 0);

        root.getChildren().add(gamePane);

        gc = canvas.getGraphicsContext2D();

        canvas.addEventHandler(KeyEvent.KEY_PRESSED, this);
        canvas.addEventHandler(MouseEvent.ANY, this);

        setupHighScorePane(root);
        setupGameOverScreen(root);

        displayHighScores();
    }

    public void start(int seed) {
        gameState = GameState.RUNNING;

        frog = new Frog(13, 6);
        startTime = 0;

        chunkGenerator = new ChunkGenerator(seed);
        chunks = chunkGenerator.getStartingChunks();
        animationTimer.start();
    }

    private void reset() {
        if(gameState != GameState.GAME_OVER) {
            animationTimer.stop();
            Random random = new Random();
            start(random.nextInt());
        }
    }

    private void setupGameOverScreen(GridPane pane) {
        Rectangle cover = new Rectangle(0, 0, Froqr.GAME_SIZE_X, Froqr.GAME_SIZE_Y);
        cover.setFill(Paint.valueOf("#FFFFFF"));

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setSpacing(30);
        int padX = (Froqr.GAME_SIZE_X - 200)/2;
        int padY = (Froqr.GAME_SIZE_Y - 200)/2;
        vbox.setPadding(new Insets(padY, padX, padY, padX));

        Text gameover = new Text("Mäng läbi!");
        gameover.setFont(new Font(26));
        gameOverScore = new Text(calculateScore() + " punkti");
        gameOverScore.setFont(new Font(18));
        gameOverScore.setFill(Color.RED);
        TextField textField = new TextField();
        textField.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        textField.setPromptText("Nimi");

        Button button = new Button("Salvesta skoor");

        textField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                if(!saveScore(textField.getText())) {
                    button.setText("Sisesta nimi!");
                }
            }
        });

        button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(!saveScore(textField.getText())) {
                button.setText("Sisesta nimi!");
            }
        });

        vbox.getChildren().addAll(gameover, gameOverScore, textField, button);

        gameOverPane = new Pane();
        gameOverPane.getChildren().addAll(cover, vbox);

        pane.getChildren().add(gameOverPane);
        gameOverPane.setVisible(false);
    }

    private void setupHighScorePane(GridPane root) {
        highScoresTextArea = new TextArea();
        highScoresTextArea.appendText("test");
        highScoresTextArea.setMinSize(200, Froqr.GAME_SIZE_Y);
        highScoresTextArea.setPrefColumnCount(0);
        highScoresTextArea.setEditable(false);
        highScoresTextArea.setFocusTraversable(false);

        Pane highScorePane = new Pane();
        highScorePane.getChildren().addAll(highScoresTextArea);

        GridPane.setColumnIndex(highScorePane, 1);

        root.getChildren().add(highScorePane);
    }

    public void gameLoopIncrement(long dt) {
        if(gameState == GameState.RUNNING) {
            update(dt);
            draw(dt);
        }
        else if (gameState == GameState.GAME_OVER) {
            animationTimer.stop();
            showGameOverScreen();
        }
        else {
            animationTimer.stop();
        }
    }

    private void showGameOverScreen() {
        gameOverScore.setText(calculateScore() + " punkti");
        gameOverPane.setVisible(true);
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
            else if(keyEvent.getCode() == KeyCode.R) {
                reset();
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

        try {
            for(Chunk chunk : chunks) {
                if(chunk.getOffset() == frog.getChunkOffset()) {
                    Tile tile = chunk.getTile(frog.getTilePosition());
                    int type = tile.getType();

                    boolean onLog = false;

                    for(MovableObject movableObject : chunk.getMovableObjects()) {
                        if(frog.getXPosition() > movableObject.getLeftX() &&
                                frog.getXPosition() < movableObject.getRightX()) {
                            int movableObjectType = movableObject.getType();
                            if(movableObjectType == MovableObjectType.CAR ||
                                    movableObjectType == MovableObjectType.BUS) {
                                throw new CollisionException("Sa jäid auto alla!");
                            }
                            else if(movableObjectType == MovableObjectType.LOG) {
                                onLog = true;
                                collidedWithMovableObject = true;
                                movableObject.setPlayer(frog);
                            }
                        }
                    }

                    if (type == TileType.WATER && !onLog) {
                        throw new CollisionException("Sa uppusid!");
                    }
                }
            }

            if(!collidedWithMovableObject && frog.getConnectedObject() != null) {
                frog.getConnectedObject().setPlayer(null);
            }

            if(frog.getChunkPosition() * Tile.TILE_SIZE_X + frog.getYOffset() / 1_000_000_000L > (long)Froqr.GAME_SIZE_Y) {
                throw new CollisionException("Ekraan sõi su ära");
            }

        }
        catch (CollisionException e) {
            System.out.println(e.toString());
            gameState = GameState.GAME_OVER;
        }
    }

    private void drawDebug(long dt) {
        debugText.setText("");

        addDebugText("Time from start: " + (dt - startTime) / 1_000_000 + "ms");
        addDebugText("Delta time: " + (double)(dt - lastTime) / 1_000_000 + "ms");

        for(int i = 0; i < chunks.size(); i++) {
            Chunk chunk = chunks.get(i);

            addDebugText(i + ": " + chunk.getOffset() / 1_000_000_000);
        }

        addDebugText(frog.getTilePosition() + " " + frog.getChunkPosition() + " " + frog.getYOffset() / 1_000_000_000 + " " + frog.getChunkOffset() / 1_000_000_000);

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

        gc.setFill(Color.BEIGE.darker());
        gc.fillRect(0, 0, Froqr.GAME_SIZE_X, Tile.TILE_SIZE_Y);

        gc.setFill(Color.BLACK);
        gc.fillText("Score: " + calculateScore(), 10, 20);

        if(drawDebug) {
            drawDebug(dt);
        }
    }

    private int calculateScore() {
        return (int) ((lastTime - startTime) / 1_000_000_000);
    }

    private boolean saveScore(String name) {
        try {
            HighScores.addScore(name, calculateScore());
            gameOverPane.setVisible(false);
            displayHighScores();
        }
        catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    private void displayHighScores() {
        HashMap<String, Integer> scores = HighScores.load();

        ArrayList<Pair<String, Integer>> scoresList = new ArrayList<>();

        for(String key : scores.keySet()) {
            scoresList.add(new Pair<>(key, scores.get(key)));
        }

        scoresList.sort(new Comparator<Pair<String, Integer>>() {
            @Override
            public int compare(Pair<String, Integer> stringIntegerPair, Pair<String, Integer> t1) {
                return t1.getValue() - stringIntegerPair.getValue() ;
            }
        });

        highScoresTextArea.setText("");

        for(Pair<String, Integer> score : scoresList) {
            highScoresTextArea.appendText(score.getKey() + ": " + score.getValue() + "\n");
        }
    }
}
