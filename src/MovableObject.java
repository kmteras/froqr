import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MovableObject extends Drawable {
    private long x;
    private long speed;
    private long offset;

    public MovableObject(long offset, long speed) {
        x = 0;
        this.speed = speed;
        this.offset = offset;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillRect(x / 1_000_000_000L, offset / 1_000_000_000, Tile.TILESIZE, Tile.TILESIZE);
    }

    public void move(long o) {
        offset += o;
        this.x += o * speed;
    }
}
