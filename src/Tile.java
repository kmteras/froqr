import com.sun.javafx.geom.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Tile extends Drawable {
    public static int TILESIZE = 32;

    private int id;
    private Vec2d position;

    public Tile(int id, Vec2d pos) {
        this.id = id;
        position = pos;
    }

    public void draw(GraphicsContext gc) {
        if(id == 0) {
            gc.setFill(Color.GREEN);
        }
        else if(id == 1) {
            gc.setFill(Color.GRAY);
        }

        gc.fillRect((int)position.x, (int)position.y, 32, 32);
    }

    public void move(double a) {
        position.y += a;
    }
}
