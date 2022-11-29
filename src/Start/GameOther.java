package Start;

public class GameOther {
    static int width;
    static int heigth;
    public static int[][] map;
    public static int[][] trap;
    static int sumTrap;

    public  GameOther() {
    }
    public  GameOther(int width, int heigth) {
        this.width=width;
        this.heigth=heigth;
    }

    public static int getWidth() {
        return width;
    }

    public static int getSumTrap() {
        return sumTrap;
    }


    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    public static void createMap(int width, int heigth) {
        map = new int[heigth][width];
        for (int i = 0; i < heigth; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = 0;
            }
        }
    }
    public static void addTrap(){
        for (int i = 0; i < getSumTrap(); i++) {
            map[trap[i][0]][trap[i][1]] = 1;
        }
    }
}