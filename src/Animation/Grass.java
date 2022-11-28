package Animation;

public class Grass extends Tile {
    public Grass(int x, int y) {
        super(x,y);
        scale = 3.2;
        layer = -3;
        sprite = new Sprite(this, 16, 0, 0, 245, 1, width, height, getScale());
        bounderBox = new BounderBox(positionX, positionY, (int) (width * (getScale() + 0.4)), (int) (height * (getScale() + 0.4)));
    }
    @Override
    public boolean remove() {
        return false;
    }
}
