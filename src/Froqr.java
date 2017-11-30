import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Froqr extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        GridPane root = new GridPane();
        Canvas canvas = new Canvas(400, 400);

        Game game = new Game(canvas);

        stage.setTitle("Froqr");
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, 400, 400);
        scene.addEventFilter(KeyEvent.ANY, game);
        stage.setScene(scene);
        stage.show();

        game.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
