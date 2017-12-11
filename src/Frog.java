import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Frog extends Drawable {
    private int tilePosition;
    private int chunkPosition;  //Current tile the frog is on
    private long offset;        //The global offset that does not wrap around

    public Frog(int chunk, int tile) {
        tilePosition = tile;
        chunkPosition = chunk;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.YELLOW);
        gc.fillRect(tilePosition * Tile.TILESIZE, chunkPosition * Tile.TILESIZE + offset / 1_000_000_000, Tile.TILESIZE, Tile.TILESIZE);
    }

    public void moveTile(int x, int y) {
        if(tilePosition + x >= 0 && tilePosition + x < Chunk.CHUNK_TILE_AMT) {
            tilePosition += x;
        }
        chunkPosition += y;
    }

    public void offset(long o) {
        offset += o;
    }

    public int getTilePosition() {
        return tilePosition;
    }

    public int getChunkPosition() {
        return chunkPosition;
    }

    public long getOffset() {
        return offset;
    }

    public long getChunkOffset() {
        return offset + (long)chunkPosition * 32 * 1_000_000_000;
    }
}
