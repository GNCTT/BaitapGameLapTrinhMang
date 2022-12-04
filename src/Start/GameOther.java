package Start;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameOther {
    private int[][] map;
    int[][] arr_trap;
    int[][] arr_shoot;
    private int width;
    private int height;

    public int WIDTH_DEFAULT = 30;
    public int HEIGHT_DEFAULT = 30;

    public GameOther(int width, int height) {
        this.width = width;
        this.height = height;
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

    public void reload(int width, int height) {
        this.width = width;
        this.height = height;
        map = new int[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = 0;
            }
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

    public void renderGame(GraphicsContext gc, int startX, int startY) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == 0) {
//                    gc.setFill(Color.WHITE);
//                    gc.fillRect(j * WIDTH_DEFAULT, i * HEIGHT_DEFAULT, WIDTH_DEFAULT, HEIGHT_DEFAULT);
                    gc.setFill(Color.GREEN);
                    gc.fillRect(startX + j * WIDTH_DEFAULT + 1, startY + i * HEIGHT_DEFAULT + 1, WIDTH_DEFAULT - 1, HEIGHT_DEFAULT - 1);
                } else {
                    if (map[i][j] == 20) {
                        gc.setFill(Color.RED);
                        gc.fillRect(startX + j * WIDTH_DEFAULT + 1, startY + i * HEIGHT_DEFAULT + 1, WIDTH_DEFAULT - 1, HEIGHT_DEFAULT - 1);
                    } else {
//                    gc.setFill(Color.WHITE);
//                    gc.fillRect(j * WIDTH_DEFAULT, i * HEIGHT_DEFAULT, WIDTH_DEFAULT, HEIGHT_DEFAULT);
                        gc.setFill(Color.BLACK);
                        gc.fillRect(startX + j * WIDTH_DEFAULT + 1, startY+ i * HEIGHT_DEFAULT + 1, WIDTH_DEFAULT - 1, HEIGHT_DEFAULT - 1);
                    }
                }
            }
        }
    }

    public int [][] getMap() {
        return map;
    }
}
