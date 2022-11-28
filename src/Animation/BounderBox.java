package Animation;

import javafx.geometry.Rectangle2D;

public class BounderBox {
    public int x;
    public int y;
    int width;
    int height;
    Rectangle2D boundary;
    public BounderBox(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        width = w;
        height = h;
        boundary = new Rectangle2D(x, y, width, height);
    }

    public void setOffset() {
        this.x -= GloVariables.offSet;
    }

    public Rectangle2D getBoundary() {
        return boundary;
    }
}
