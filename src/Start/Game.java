package Start;

import java.util.Scanner;

import static Start.Function.*;
import static Start.RotatePlane.*;

public class Game {
    static int [][] map;
    static int[][] mayBay ={{0,0,2,0,0},       //          2
                            {0,2,0,0,2},       //         2  2
                            {2,2,2,2,2},       //        22222      *Tam may bay o toa do [2;2]*
                            {0,2,0,0,2},       //         2  2
                            {0,0,2,0,0}};//          2

    static int width;   // Chieu rong map
    static int height;  // Chieu cao map
    static int x;       //Toa do may bay
    static int y;       //Toa do may bay
    static int lastMove=4;
    static int newMove=4;

    static int clientID;



    public static void main(String[] args) {
//        Scanner in= new Scanner(System.in);
//        int N,M;
//        M=in.nextInt();
//        N=in.nextInt();

        Game game= new Game(width,height);
        game.setWidth(15);
        game.setHeight(15);
        game.start(game);

    }


    public void start(Game game){
        createMap(game.getWidth(), game.getHeight());
        renderMap(game.getWidth(), game.getHeight());
        renderPlane();
        nhapToaDo();
        chonHuongMayBay();
        renderMap(game.getWidth(), game.getHeight());

        System.out.println("------------------------------START---------------------------");
        Scanner in= new Scanner(System.in);
        boolean start=true;
        while (start){
            if(checkLose()==true){
                System.out.println("------------------ĐÃ THUA-------------------");
                break;
            }
            // chon 1: BAN  2:DI CHUYEN
            int action= selection();
            boolean check= true;
            // Ban
            if(action==1){
                firer();
            }

            // Di Chuyen
            if(action==2){
                move();
            }
            renderMap(game.getWidth(), game.getHeight());
        }

    }

    public static void setLastMove(int lastMove) {
        Game.lastMove = lastMove;
    }

    public static int getLastMove() {
        return lastMove;
    }

    public static void setNewMove(int newMove) {
        Game.newMove = newMove;
    }

    public static int getNewMove() {
        return newMove;
    }

    public static int[][] getMayBay() {
        return mayBay;
    }

    public static void setX(int x) {
        Game.x = x;
    }

    public int getX() {
        return this.x;
    }

    public static int getY() {
        return y;
    }

    public static void setY(int y) {
        Game.y = y;
    }

    public Game(int width, int height){
        this.setWidth(width);
        this.setHeight(height);
    }

    public Game(){
    }
    public static int[][] getMap() {
        return map;
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
}

