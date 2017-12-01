import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

public class Chunk extends Drawable {
    public static int CHUNK_TILE_AMT = 13;

    private int id;
    private int seed;
    private Tile[] tiles;

    private long offset;

    public Chunk(long offset, int seed) {
        this.seed = seed;
        this.offset = offset;

        Random random = new Random(seed);
        id = random.nextInt(10);
        generateTiles();
    }

    public void draw(GraphicsContext gc) {
        for(int i = 0; i < tiles.length; i++) {
            tiles[i].draw(gc);
        }
    }

    public void move(long o) {
        offset += o;

        for(Tile tile : tiles) {
            tile.move(o);
        }
    }

    private void generateTiles() {
        tiles = new Tile[CHUNK_TILE_AMT];

        Random random = new Random();

        for(int i = 0; i < tiles.length; i++) {
            tiles[i] = new Tile(random.nextInt(3), i * Tile.TILESIZE, offset);
        }
    }

    public boolean isOutOfScreen() {
        return offset >= (long)Froqr.GAME_SIZE_Y * 1_000_000_000L;
    }

    public long getOffset() {
        return offset;
    }
}
