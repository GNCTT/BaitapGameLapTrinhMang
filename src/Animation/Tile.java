package Animation;

public class Tile extends Entity {
    boolean remove = false;
    BounderBox bounderBox;
    public Tile(int x, int y) {
        positionX = x;
        positionY = y;
        width = 16;
        height = 16;
        scale = 3.1;
        layer = 1;
        bounderBox = new BounderBox(positionX, positionY, (int) (width * (getScale() + 0.9)), (int) (height * (getScale() + 0.9)));
    }

    public boolean remove() {
        return remove;
    }

    @Override
    public void render() {
        Render.playAnimation(sprite);
    }
}
