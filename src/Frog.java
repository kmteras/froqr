import com.sun.javafx.geom.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Frog extends Drawable {
    private int tilePosition;
    private int chunkPosition;
    private double chunkOffset;

    public Frog(int chunk, int tile) {
        tilePosition = tile;
        chunkPosition = chunk;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.fillRect(tilePosition * Tile.TILESIZE, chunkPosition * Tile.TILESIZE + chunkOffset, Tile.TILESIZE, Tile.TILESIZE);
    }

    public void moveTile(int x, int y) {
        if(tilePosition + x >= 0 && tilePosition + x < Chunk.CHUNK_TILE_AMT) {
            tilePosition += x;
        }
        chunkPosition += y;
    }

    public void offset(double o) {
        chunkOffset += o;
    }
}
