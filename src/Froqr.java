import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sun.tools.jar.Main;

public class Froqr extends Application {
    public static final int GAME_SIZE_X = 416;
    public static final int GAME_SIZE_Y = 512;

    @Override
    public void start(Stage stage) throws Exception {
        GridPane root = new GridPane();

        Game game = new Game(root);

        stage.setTitle("Froqr");
        Scene scene = new Scene(root, GAME_SIZE_X + 200, GAME_SIZE_Y);
        scene.addEventFilter(KeyEvent.ANY, game);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(30);

        Text title = new Text("Froqr");
        title.setFont(new Font(45));
        String introText = "Oled konn! Pead jääma ellu karmis maailmas vältides autoteedel autosid ja busse ning vältides sügavat vett hüpates ujuvatel palkidel ja veeroosilehtedel. Liikuda saab W,A,S,D klahvidega.";
        Text intro = new Text(introText);
        intro.setFont(new Font(16));
        intro.setWrappingWidth(300);

        Button button = new Button("Alusta mängu!");
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                vbox.setVisible(false);
                game.start(0);
            }
        });

        vbox.getChildren().addAll(title, intro, button);
        root.getChildren().add(vbox);
        stage.setScene(scene);

        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            Double change = newVal.doubleValue()/oldVal.doubleValue();
            if(!change.isNaN())
                game.changeScale(change, 1);
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            System.out.print(stage.getHeight());
            Double change = newVal.doubleValue()/oldVal.doubleValue();
            if(!change.isNaN())
                game.changeScale(1, change);
        });
        stage.show();

        stage.setMinWidth(GAME_SIZE_X + 200);

        stage.setMinHeight(stage.getHeight());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
