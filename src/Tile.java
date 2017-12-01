import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Tile extends Drawable {
    public static int TILESIZE = 32;

    private int id;
    private int posX;
    private long offset;

    public Tile(int id, int x, long o) {
        this.id = id;
        posX = x;
        offset = o;
    }

    public void draw(GraphicsContext gc) {
        if(id == 0) {
            gc.setFill(Color.GREEN);
        }
        else if(id == 1) {
            gc.setFill(Color.GRAY);
        }
        else if(id == 2) {
            gc.setFill(Color.BLUE);
        }

        gc.fillRect(posX, offset / 1_000_000_000, 32, 32);
    }

    public void move(long a) {
        offset += a;
    }
}
