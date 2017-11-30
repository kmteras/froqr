import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Froqr extends Application {
    public static int GAME_SIZE_X = 384;
    public static int GAME_SIZE_Y = 512;

    @Override
    public void start(Stage stage) throws Exception {
        GridPane root = new GridPane();

        Game game = new Game(root);

        stage.setTitle("Froqr");
        Scene scene = new Scene(root, GAME_SIZE_X, GAME_SIZE_Y);
        scene.addEventFilter(KeyEvent.ANY, game);
        stage.setScene(scene);
        stage.show();

        game.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
