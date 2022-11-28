package Animation;

public abstract class Entity {
    public int positionX;
    public int positionY;
    public int layer;
    public int width;
    public int height;
    public double scale;
    public Sprite sprite;
    public BounderBox bounderBox;

    public Entity(int x, int y) {}

    public Entity() {}

    abstract public void render();

    abstract public boolean remove();

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public int getLayer() {
        return layer;
    }

    public double getScale() {
        return scale;
    }
}
