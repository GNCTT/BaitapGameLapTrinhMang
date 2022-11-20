package Start;

import java.util.Random;
import java.util.Scanner;

import static Start.Function.*;
import static Start.Game.getHeight;
import static Start.Game.getWidth;
import static Start.RotatePlane.*;

public class Game {
    static int [][] map;
    static int[][] mayBay ={{0,0,2,0,0},       //          2
                            {0,2,0,0,2},       //         2  2
                            {2,2,2,2,0},       //        22222      *Tam may bay o toa do [2;2]*
                            {0,2,0,0,2},       //         2  2
                            {0,0,2,0,0}};      //          2


    static int width;   // Chieu rong map
    static int height;  // Chieu cao map
    static int x;       //Toa do may bay
    static int y;       //Toa do may bay
    static int xFirer;
    static int yFirer;
    static int lastMove=4;
    static int newMove=4;

    static int clientID;

    Random generator = new Random();
    static int sumTrap;
    static int[][] trap;
    static int dir;
    static int action;

    public static void main(String[] args) {
//        Scanner in= new Scanner(System.in);
//        int N,M;
//        M=in.nextInt();
//        N=in.nextInt();

        Game game= new Game(width,height);
        game.setWidth(20);
        game.setHeight(20);
        game.start(game);

    }

    public static int getSumTrap() {
        return sumTrap;
    }
    public static void setSumTrap(int sumTrap) {
        Game.sumTrap = sumTrap;
    }
    public static int getDir() {
        return dir;
    }
    public static void setDir(int dir) {
        Game.dir = dir;
    }
    public static int getAction() {
        return action;
    }
    public static void setAction(int action) {
        Game.action = action;
    }

    public void start(Game game){
        createMap(game.getWidth(), game.getHeight());
        createTrap();
        addTrap();
        renderMap(game.getWidth(), game.getHeight());
        renderPlane();
        nhapToaDo();
        setDir(chonHuongMayBay());
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
            setAction(selection());
            boolean check= true;
            // Ban
            if(getAction()==1){
                int trangThai; // 0:Truot 1:Trung 2:TrungBay
                getxyFirer();
                trangThai=firer(getxFirer(),getyFirer());
            }
            // Di Chuyen
            if(getAction()==2){
                setDir(move());
            }
            renderMap(game.getWidth(), game.getHeight());

        }

    }
    public static void getxyFirer(){
        Scanner in= new Scanner(System.in);
        System.out.println("-------------------------BAN--------------------------");
        System.out.print("Nhap toa do x can ban: ");
        int j=in.nextInt();
        System.out.print("Nhap toa do y can ban: ");
        int i=in.nextInt();
        System.out.println("Toa do ban: ["+ j +":"+ i +"]");
        boolean check=true;
        while (check) {
            if (i < 0 || i >= getHeight() || j < 0 || j >= getWidth()) {
                System.out.println("Toa Do Khong Hop Le !! Vui Long Nhap Lai !!");
                System.out.println("-------------------------BAN--------------------------");
                System.out.print("Nhap toa do x can ban: ");
                j = in.nextInt();
                System.out.print("Nhap toa do y can ban: ");
                i = in.nextInt();
                System.out.println("Toa do ban: [" + j + ":" + i + "]");
            } else {
                System.out.println("Toa Do Ban la : [" + j + ":" + i + "]");
                setxFirer(j);
                setyFirer(i);
//                if (map[i][j] == 2) {
//                    mayBay[2 - y + i][2 - x + j] = 3;
//                    map[i][j] = 4;
//                    updateDiChuyen(map, mayBay, x, y);
//                } else if (map[i][j] == 0) {
//                    map[i][j] = 4;
//                }
                check = false;
            }
        }
    }
    public static void setxFirer(int xFirer) {
        Game.xFirer = xFirer;
    }

    public static void setyFirer(int yFirer) {
        Game.yFirer = yFirer;
    }

    public static int getxFirer() {
        return xFirer;
    }

    public static int getyFirer() {
        return yFirer;
    }

    public static int getClientID() { return clientID; }

    public static void setClientID(int clientID) { Game.clientID = clientID; }

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

    public Game(){}
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

