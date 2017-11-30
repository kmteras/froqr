import com.sun.javafx.geom.Vec2d;
import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

public class Chunk extends Drawable {
    public static int CHUNK_TILE_AMT = 13;

    private Tile[] tiles;

    private Vec2d position;

    public Chunk(Vec2d position) {
        this.position = position;
        tiles = new Tile[CHUNK_TILE_AMT];

        Random random = new Random();

        for(int i = 0; i < tiles.length; i++) {
            tiles[i] = new Tile(random.nextInt(2), new Vec2d(position.x + i * Tile.TILESIZE, position.y));
        }
    }

    public void draw(GraphicsContext gc) {
        for(int i = 0; i < tiles.length; i++) {
            tiles[i].draw(gc);
        }
    }

    public void move(double a) {
        position.y += a;

        for(Tile tile : tiles) {
            tile.move(a);
        }
    }
}
