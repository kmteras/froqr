import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MovableObject extends Drawable {
    private long x;
    private long speed;
    private long offset;
    private int type;

    public MovableObject(long offset, long speed, int type) {
        x = 0;
        this.speed = speed;
        this.offset = offset;
        this.type = type;
    }

    public void draw(GraphicsContext gc) {
        if(type == MovableObjectType.CAR) {
            gc.setFill(Color.BLACK);
            gc.fillRect(x / 1_000_000_000L, offset / 1_000_000_000, Tile.TILE_SIZE * 2, Tile.TILE_SIZE);
        }
        else if(type == MovableObjectType.BUS) {
            gc.setFill(Color.RED);
            gc.fillRect(x / 1_000_000_000L, offset / 1_000_000_000, Tile.TILE_SIZE * 3, Tile.TILE_SIZE);
        }
        else if(type == MovableObjectType.LOG) {
            gc.setFill(Color.BROWN);
            gc.fillRect(x / 1_000_000_000L, offset / 1_000_000_000, Tile.TILE_SIZE * 3, Tile.TILE_SIZE);
        }
    }

    public void move(long o) {
        offset += o;
        this.x += o * speed;
    }
}
