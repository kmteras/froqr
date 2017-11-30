import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class Frog {
    private Rectangle image;
    private double xPos, yPos;

    public Frog() {
        xPos = 0;
        yPos = 0;
    }

    public void draw(GraphicsContext gc) {
        gc.fillRect(xPos, yPos, 32, 32);
    }

    public void move(double x, double y) {
        xPos += x;
        yPos += y;
    }
}
