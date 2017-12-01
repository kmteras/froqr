import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Frog extends Drawable {
    private int tilePosition;
    private int chunkPosition;
    private long chunkOffset;

    public Frog(int chunk, int tile) {
        tilePosition = tile;
        chunkPosition = chunk;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.YELLOW);
        gc.fillRect(tilePosition * Tile.TILESIZE, chunkPosition * Tile.TILESIZE + chunkOffset / 1_000_000_000, Tile.TILESIZE, Tile.TILESIZE);
    }

    public void moveTile(int x, int y) {
        if(tilePosition + x >= 0 && tilePosition + x < Chunk.CHUNK_TILE_AMT) {
            tilePosition += x;
        }
        chunkPosition += y;
    }

    public void offset(long o) {
        chunkOffset += o;
    }
}
