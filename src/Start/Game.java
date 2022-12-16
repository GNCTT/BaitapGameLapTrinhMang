package Start;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Scanner;

//import static Start.RotatePlane.*;

public class Game {
    private int [][] map;

    private int[][] mayBay;
    private int[][] mayBay1 ={{0,0,2,0,0},       //          2
                             {0,2,0,0,2},       //         2  2
                             {2,2,2,2,2},       //        22222      *Tam may bay o toa do [2;2]*
                             {0,2,0,0,2},       //         2  2
                             {0,0,2,0,0}};
    //          2
    private int[][] mayBay2 ={{0,0,2,0,0},       //          2
                             {0,2,2,2,0},       //         2  2
                             {2,0,2,0,2},       //        22222      *Tam may bay o toa do [2;2]*
                             {0,0,2,0,0},       //         2  2
                             {2,2,2,2,2}};
    private int[][] mayBay3 ={{2,0,2,0,0},       //          2
                             {2,0,0,2,0},       //         2  2
                             {2,2,2,2,2},       //        22222      *Tam may bay o toa do [2;2]*
                             {2,0,0,2,0},       //         2  2
                             {2,0,2,0,0}};

    private int[][] mayBay4 ={{2,2,2,2,2},       //          2
                             {0,0,2,0,0},       //         2  2
                             {2,0,2,0,2},       //        22222      *Tam may bay o toa do [2;2]*
                             {0,2,2,2,0},       //         2  2
                             {0,0,2,0,0}};

    private int [][] map_load;

    private int[][] trap;

    private int width;   // Chieu rong map
    private int height;  // Chieu cao map
    private int x;
    private int y;

    public int action;

    static int clientID;

    private int x_plane;
    private int y_plane;
    public int dir;

    public int WIDTH_DEFAULT = 30;
    public int HEIGHT_DEFAULT = 30;


    public int getDir() {
        return dir;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return this.x;
    }


    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Game(int width, int height){
        this.width = width;
        this.height = height;
        map = new int[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = 0;
            }
        }
        map_load = new int[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map_load[i][j] = 0;
            }
        }
        mayBay = new int[5][5];
        reloadPlane(mayBay2);
        x_plane = 3;
        y_plane = 3;
    }

    public boolean checkLocationPlane(int x_plane, int y_plane) {
        System.out.println("left: " + getLeftPlane() + " right: " + getRightPlane());

        if (x_plane - getLeftPlane() < 0 || x_plane + getRightPlane() >= map.length|| y_plane - getTopPlane() < 0 || y_plane + getBotPlane() >= map.length) {
            return false;
        } else {
            int start_x = x_plane - mayBay.length / 2;
            int start_y = y_plane - mayBay.length / 2;
            for (int i = start_y; i < mayBay.length + start_y; i++) {
                for (int j = start_x; j < mayBay.length + start_x; j++) {
                    if (mayBay[i - start_y][j - start_x] != 0) {
                        int check = map[i][j] + mayBay[i - start_y][j - start_x];
                        if (check > 20) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean checkLocationPlane(int dir, int x_plane, int y_plane) {
        if (dir == 0) {
            reloadPlane(mayBay1);
        }
        if (dir == 1) {
            reloadPlane(mayBay2);
        }
        if (dir == 2) {
            reloadPlane(mayBay3);
        }
        if (dir == 3) {
            reloadPlane(mayBay4);
        }
        System.out.println("left: " + getLeftPlane() + " right: " + getRightPlane());

        if (x_plane - getLeftPlane() < 0 || x_plane + getRightPlane() >= map.length|| y_plane - getTopPlane() < 0 || y_plane + getBotPlane() >= map.length) {
            return false;
        } else {
            int start_x = x_plane - mayBay.length / 2;
            int start_y = y_plane - mayBay.length / 2;
            for (int i = start_y; i < mayBay.length + start_y; i++) {
                for (int j = start_x; j < mayBay.length + start_x; j++) {
                    if (mayBay[i - start_y][j - start_x] != 0) {
                        int check = map[i][j] + mayBay[i - start_y][j - start_x];
                        if (check > 20) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    public void reloadMap() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = map_load[i][j];
            }
        }
    }

    public int[][] getMap() {
        return this.map;
    }
    public void setPlane(int x_plane, int y_plane) {
        reloadMap();
        if (checkLocationPlane(x_plane, y_plane)) {
            int start_x = x_plane - mayBay.length / 2;
            int start_y = y_plane - mayBay.length / 2;
            for (int i = start_y; i < mayBay.length + start_y; i++) {
                for (int j = start_x; j < mayBay.length + start_x; j++) {
                    if (mayBay[i - start_y][j - start_x] != 0) {
                        map[i][j] = mayBay[i - start_y][j - start_x];
                    }
                }
            }
            this.x_plane = x_plane;
            this.y_plane = y_plane;
        }
    }

    public void setPlane(int dir, int x_plane, int y_plane) {
        reloadMap();
        switch (dir) {
            case 0:
                reloadPlane(mayBay1);
                break;
            case 1:
                reloadPlane(mayBay2);
                break;
            case 2:
                reloadPlane(mayBay3);
                break;
            case 3:
                reloadPlane(mayBay4);
                break;
        }
        if (checkLocationPlane(x_plane, y_plane)) {
            int start_x = x_plane - mayBay.length / 2;
            int start_y = y_plane - mayBay.length / 2;
            for (int i = start_y; i < mayBay.length + start_y; i++) {
                for (int j = start_x; j < mayBay.length + start_x; j++) {
                    if (mayBay[i - start_y][j - start_x] != 0) {
                        map[i][j] = mayBay[i - start_y][j - start_x];
                    }
                }
            }
            this.x_plane = x_plane;
            this.y_plane = y_plane;
        }
    }

    public void reloadPlane(int [][] newPlane) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                mayBay[i][j] = newPlane[i][j];
            }
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
        map_load = new int[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map_load[i][j] = 0;
            }
        }
        mayBay = new int[5][5];
        reloadPlane(mayBay2);
    }

    public int getLeftPlane() {
        int min = 9;
        for (int i = 0; i < mayBay.length; i++) {
            for (int j = 0; j < mayBay.length; j++) {
                if (mayBay[i][j] == 2) {
                    if (j < min) {
                        min = j;
                    }
                }
            }
        }
        return mayBay.length / 2 - min;
    }

    public int getRightPlane() {
        int max = -9;
        for (int i = 0; i < mayBay.length; i++) {
            for (int j = 0; j < mayBay.length; j++) {
                if (mayBay[i][j] == 2) {
                    if (j > max) {
                        max = j;
                    }
                }
            }
        }
        return max - mayBay.length / 2;
    }

    public int getBotPlane() {
        int max = -9;
        for (int i = 0; i < mayBay.length; i++) {
            for (int j = 0; j < mayBay.length; j++) {
                if (mayBay[i][j] == 2) {
                    if (i > max) {
                        max = i;
                    }
                }
            }
        }
        return max - mayBay.length / 2;
    }

    public int getTopPlane() {
        int min = 9;
        for (int i = 0; i < mayBay.length; i++) {
            for (int j = 0; j < mayBay.length; j++) {
                if (mayBay[i][j] == 2) {
                    if (i < min) {
                        min = i;
                    }
                }
            }
        }
        return mayBay.length / 2 - min;
    }






    public boolean checkMove(int dir) {
        switch (dir) {
            case 0:
                if (checkLocationPlane(x_plane - 1, y_plane)) {
                    return true;
                } else return false;
            case 1:
                if (checkLocationPlane(x_plane, y_plane - 1)) {
                    return true;
                } else return false;
            case 2:
                if (checkLocationPlane(x_plane + 1, y_plane)) {
                    return true;
                } else return false;
            default:
                if (checkLocationPlane(x_plane, y_plane + 1)) {
                    return true;
                } else return false;
        }
    }
    public void move(int dir) {
        switch (dir) {
            case 0:
                if (checkLocationPlane(x_plane -1, y_plane)) {
                    x_plane -= 1;
                }
                break;
            case 1:
                if (checkLocationPlane(x_plane, y_plane - 1)) {
                    y_plane -= 1;
                }
                break;
            case 2:
                if (checkLocationPlane(x_plane + 1, y_plane)) {
                    x_plane += 1;
                }
                break;
            default:
                if (checkLocationPlane(x_plane, y_plane + 1)) {
                    y_plane += 1;
                }
        }
        setPlane(x_plane, y_plane);
    }



    public void addTrap(int [][] arr_trap) {
        for (int i = 0; i < arr_trap.length; i++) {
            map_load[arr_trap[i][0]][arr_trap[i][1]] = 20;
        }
        reloadMap();
    }

    public boolean checkShoot(int x, int y) {
        System.out.println(map[y][x]);

        if (map[y][x] == 2) {
            System.out.println("shoottttt");
            return true;
        }
        return false;
    }



    public void beShoot(int x, int y) {
        System.out.println("map: " + map[y][x]);
        if (map[y][x] == 2) {
            int startX = x_plane - mayBay.length / 2;
            int startY = y_plane - mayBay.length / 2;
            mayBay[y - startY][x - startX] = 0;
        }
        setPlane(x_plane, y_plane);
        render();
    }

    public boolean checkLose() {
        for (int i = 0; i < mayBay.length; i++) {
            for (int j = 0; j < mayBay.length; j++) {
                if (mayBay[i][j] == 2) {
                    return false;
                }
            }
        }
        return true;
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


    public void renderGame(GraphicsContext gc, int Start_x, int Start_Y) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == 0) {
//                    gc.setFill(Color.WHITE);
//                    gc.fillRect(j * WIDTH_DEFAULT, i * HEIGHT_DEFAULT, WIDTH_DEFAULT, HEIGHT_DEFAULT);
                    gc.setFill(Color.GREEN);
                    gc.fillRect(Start_x + j * WIDTH_DEFAULT + 1, Start_Y + i * HEIGHT_DEFAULT + 1, WIDTH_DEFAULT - 1, HEIGHT_DEFAULT - 1);
                } else {
                    if (map[i][j] == 20) {
                        gc.setFill(Color.RED);
                        gc.fillRect(Start_x + j * WIDTH_DEFAULT + 1, Start_Y + i * HEIGHT_DEFAULT + 1, WIDTH_DEFAULT - 1, HEIGHT_DEFAULT - 1);
                    } else {
//                    gc.setFill(Color.WHITE);
//                    gc.fillRect(j * WIDTH_DEFAULT, i * HEIGHT_DEFAULT, WIDTH_DEFAULT, HEIGHT_DEFAULT);
                        gc.setFill(Color.BLACK);
                        gc.fillRect(Start_x + j * WIDTH_DEFAULT + 1, Start_Y + i * HEIGHT_DEFAULT + 1, WIDTH_DEFAULT - 1, HEIGHT_DEFAULT - 1);
                    }
                }
            }
        }
    }

    public void renderResult(GraphicsContext gc, int result, int startX, int startY) {
        gc.setFont(new Font(40));
        if (result == 1) {
            gc.fillText(" DRAW ", startX + 400, startY + 400);
        }
        if (result == 0) {
            gc.fillText(" LOSE ", startX + 400, 400);
        }
        if (result == 2) {
            gc.fillText(" WIN ", startX + 400, 400);
        }
    }





}

