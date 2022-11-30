package Start;

public class GameOther {
    private int[][] map;
    int[][] arr_trap;
    int[][] arr_shoot;

    public GameOther(int width, int height) {
        map = new int[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = 0;
            }
        }
    }

    public void add_arr_shoot(int [][]arr_shoot) {

        }

    public void add_trap(int [][]arr_trap) {
        for (int i = 0; i < arr_trap.length; i++) {
            map[arr_trap[i][0]][arr_trap[i][1]] = 20;
        }
    }

    public void render() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == 0) {
                    System.out.print(". ");
                }
                if (map[i][j] == 2) {
                    System.out.print("* ");
                }
                if (map[i][j] == 20) {
                    System.out.print("# ");
                }
            }
            System.out.println();
        }
    }

    public boolean checkShoot(int x, int y) {
        if (map[x][y] == 2) {
            return true;
        }
        return false;
    }
    public void beShoot(int x, int y) {

    }

    public int [][] getMap() {
        return map;
    }
}
