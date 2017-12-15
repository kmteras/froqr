import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MovableObject extends Drawable {
    private long x;
    private long speed;
    private long offset;
    private int type;
    private boolean startOffScreen;

    private Frog player;

    public MovableObject(long offset, long speed, int type, int x) {
        this.x = x * 1_000_000_000L;
        if(x < 0) {
            startOffScreen = true;
        }
        this.speed = speed;
        this.offset = offset;
        this.type = type;
        player = null;
    }

    public void draw(GraphicsContext gc) {
        if(type == MovableObjectType.CAR) {
            gc.setFill(Color.BLACK);
            gc.fillRect(x / 1_000_000_000L, offset / 1_000_000_000, Tile.TILE_SIZE_X * 2, Tile.TILE_SIZE_X);
        }
        else if(type == MovableObjectType.BUS) {
            gc.setFill(Color.RED);
            gc.fillRect(x / 1_000_000_000L, offset / 1_000_000_000, Tile.TILE_SIZE_X * 3, Tile.TILE_SIZE_X);
        }
        else if(type == MovableObjectType.LOG) {
            gc.setFill(Color.BROWN);
            gc.fillRect(x / 1_000_000_000L, offset / 1_000_000_000, Tile.TILE_SIZE_X * 3, Tile.TILE_SIZE_X);
        }
    }

    public void move(long o) {
        offset += o;
        this.x += o * speed;
        if(player != null) {
            player.offsetX(o * speed);
        }

        if(type == MovableObjectType.LOG) {
            if(x / 1_000_000_000L + Tile.TILE_SIZE_X * 3 > Froqr.GAME_SIZE_X ||                //Goes out of screen on right
                    x < 0) {    //Goes out of screen on left
                if(!startOffScreen)
                    speed *= -1;    //Start going other way
            }
            else {
                startOffScreen = false;
            }
        }
    }

    public long getLeftX() {
        return x;
    }

    public long getRightX() {
        if(type == MovableObjectType.CAR) {
            return x + 2L * (long)Tile.TILE_SIZE_X * 1_000_000_000L;
        }
        else if(type == MovableObjectType.BUS || type == MovableObjectType.LOG) {
            return x + 3L * (long)Tile.TILE_SIZE_X * 1_000_000_000L;
        }
        return x;
    }

    public void setPlayer(Frog player) {
        if(player == null) {
            this.player.setConnectedObject(null);
        }
        else {
            player.setConnectedObject(this);
        }

        this.player = player;
    }

    public void resetPlayer() {
        player = null;
    }

    public int getType() {
        return type;
    }
}
