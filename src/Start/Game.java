package Start;

import java.util.Scanner;

//import static Start.RotatePlane.*;

public class Game {
    static int [][] map;

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
                             {0,2,0,2,0},       //         2  2
                             {0,0,2,0,0}};

    private int [][] map_load;

    private int[][] trap;

    static int width;   // Chieu rong map
    static int height;  // Chieu cao map
    private int x;
    private int y;

    public int action;

    static int clientID;

    private int x_plane;
    private int y_plane;
    public int dir;


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
        this.setWidth(width);
        this.setHeight(height);
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
        if (x_plane - mayBay.length / 2 < 0 || x_plane >= map.length - mayBay.length / 2 || y_plane < mayBay.length / 2 || y_plane >= map.length - mayBay.length / 2) {
            return false;
        } else {
            int start_x = x_plane - mayBay.length / 2;
            int start_y = y_plane - mayBay.length / 2;
            for (int i = start_y; i < mayBay.length + start_y; i++) {
                for (int j = start_x; j < mayBay.length + start_x; j++) {
                    if (mayBay[i - start_y][j - start_x] != 0) {
                        int check = map[i][j] + mayBay[i - start_y][j - start_x];
                        System.out.println(check);
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

    public void reloadPlane(int [][] newPlane) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                mayBay[i][j] = newPlane[i][j];
            }
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
    }

    public boolean checkShoot(int x, int y) {
        if (map[x][y] == 2) {
            return true;
        }
        return false;
    }
    public void beShoot(int x, int y) {
        int startX = x_plane - mayBay.length / 2;
        int startY = y_plane - mayBay.length / 2;
        mayBay[x - startX][y - startY] = 0;
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


    public Game(){
        action = -1;
    }
    public static int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public static int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void renderMap() {
        System.out.println("---------------MAP---------------");
        int count=0;
        for(int i=-1;i<=height;i++){
            for(int j=-1;j<=width;j++){
                if(i==-1 ){
                    if(j==-1){
                        System.out.print("   ");
                    } else if (j==width) {
                        System.out.print("  ");
                    }else if(j>=0 && j<10){
                        System.out.print(j + "  ");
                    }else if(j>=10 && j<height){
                        System.out.print(j + " ");
                    }
                } else if (i>=0 && i<10){
                    if(j==-1){
                        System.out.print(i + "  ");
                    } else if (j>=0 && j<width) {
                        if(map[i][j]==0){
                            System.out.print(".  ");
                        } else if(map[i][j]==1) {
                            System.out.print("0  ");
                        } else if (map[i][j]==2) {
                            System.out.print("*  ");
                        } else if (map[i][j]==3) {
                            System.out.print("#  ");
                        }else if (map[i][j]==4) {
                            System.out.print("&  ");
                        }
                    }else if (j==width) {
                        System.out.print(i + "  ");
                    }
                }else if (i>=10 && i<width){
                    if(j==-1){
                        System.out.print(i + " ");
                    } else if (j>=0 && j<width) {
                        if(map[i][j]==0){
                            System.out.print(".  ");
                        } else if(map[i][j]==1) {
                            System.out.print("0  ");
                        } else if (map[i][j]==2) {
                            System.out.print("*  ");
                        } else if (map[i][j]==3) {
                            System.out.print("#  ");
                        }else if (map[i][j]==4) {
                            System.out.print("&  ");
                        }
                    } else if (j==width) {
                        System.out.print(i + "  ");
                    }
                } else if (i==height) {
                    if(j==-1){
                        System.out.print("   ");
                    } else if (j==width) {
                        System.out.print("  ");
                    }else if(j>=0 && j<10){
                        System.out.print(j + "  ");
                    }else if(j>=10 && j<height){
                        System.out.print(j + " ");
                    }
                }
            }
            System.out.println();
        }
    }

    public void createMap() {
        map = new int [width][height];
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                map[i][j]=0;
            }
        }
    }


    public void chonHuongMayBay(){
        Scanner in= new Scanner(System.in);
        boolean check=true;
        while (check){
            System.out.println("-------------------------Chọn Hướng Máy Bay--------------------------");
            System.out.print("Nhap Huong ( 1: Len, 2: Phai, 3: Xuong, 4: Trai) : ");
            int move= in.nextInt();
            if(move>=1 && move<=4){
                check=false;
                dir = move;
            }else{
                //Khong Hop Le
                System.out.println("Lua Chon Khong Hop Le !! Vui Long Chon Lai !!");
            }
        }
    }



}

