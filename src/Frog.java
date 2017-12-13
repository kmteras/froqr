import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Frog extends Drawable {
    private long xPos;
    private int chunkPosition;  //Current tile the frog is on
    private long offsetY;        //The global offsetY that does not wrap around
    private MovableObject connectedObject;

    public Frog(int chunk, int tile) {
        xPos = tile * (long)Tile.TILE_SIZE_X * 1_000_000_000L;
        chunkPosition = chunk;
        connectedObject = null;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.YELLOW);
        gc.fillRect(xPos / 1_000_000_000, chunkPosition * Tile.TILE_SIZE_X + offsetY / 1_000_000_000, Tile.TILE_SIZE_X, Tile.TILE_SIZE_X);
    }

    public void moveTile(long x, long y) {
        if(xPos + x * (long)Tile.TILE_SIZE_X * 1_000_000_000L >= 0 &&
                xPos + x * (long)Tile.TILE_SIZE_X * 1_000_000_000L < (long)Chunk.CHUNK_TILE_AMT * (long)Tile.TILE_SIZE_X * 1_000_000_000L) {
            xPos += x * (long)Tile.TILE_SIZE_X * 1_000_000_000L;
        }
        chunkPosition += y;
    }

    public void offsetY(long o) {
        offsetY += o;
    }

    public void offsetX(long o) {
        xPos += o;
    }

    public int getTilePosition() {
        return (int)(xPos / Tile.TILE_SIZE_X / 1_000_000_000);
    }

    public int getChunkPosition() {
        return chunkPosition;
    }

    public long getYOffset() {
        return offsetY;
    }

    public long getChunkOffset() {
        return offsetY + (long)chunkPosition * 32 * 1_000_000_000;
    }

    public long getXPosition() {
        return xPos + (long)Tile.TILE_SIZE_X/2 * 1_000_000L;
    }

    public MovableObject getConnectedObject() {
        return connectedObject;
    }

    public void setConnectedObject(MovableObject connectedObject) {
        if(this.connectedObject != null)         //If the player is already connected to a object (jumping from one to another)
            this.connectedObject.resetPlayer();
        this.connectedObject = connectedObject;
    }
}
