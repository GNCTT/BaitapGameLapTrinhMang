package Animation;

public class Wall extends Tile{
    public Wall(int x, int y) {
        super(x, y);
        scale = 3.2;
        sprite = new Sprite(this, 16, 0, 0, 225, 1, width, height, getScale());
        bounderBox = new BounderBox(positionX, positionY, (int) (width * (getScale() + 0.9)), (int) (height * (getScale() + 0.9)));
    }

    @Override
    public boolean remove() {
        return false;
    }
}
