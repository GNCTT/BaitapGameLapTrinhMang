package Start;

import java.util.Scanner;

import static Start.Function.*;
import static Start.RotatePlane.updateHuong;
//import static Start.RotatePlane.*;

public class Game {
    static int [][] map;
    static int[][] mayBay ={{0,0,2,0,0},       //          2
            {0,2,0,0,2},       //         2  2
            {2,2,2,2,2},       //        22222      *Tam may bay o toa do [2;2]*
            {0,2,0,0,2},       //         2  2
            {0,0,2,0,0}};
    static int[][] mayBay1 ={{0,0,2,0,0},       //          2
                            {0,2,0,0,2},       //         2  2
                            {2,2,2,2,2},       //        22222      *Tam may bay o toa do [2;2]*
                            {0,2,0,0,2},       //         2  2
                            {0,0,2,0,0}};
    //          2
    static int[][] mayBay2 ={{0,0,2,0,0},       //          2
                            {0,2,0,2,0},       //         2  2
                            {2,2,2,2,2},       //        22222      *Tam may bay o toa do [2;2]*
                            {0,2,0,0,2},       //         2  2
                            {0,0,2,0,0}};
    static int[][] mayBay3 ={{0,0,2,0,0},       //          2
            {0,2,0,0,2},       //         2  2
            {2,2,2,2,2},       //        22222      *Tam may bay o toa do [2;2]*
            {0,2,0,0,2},       //         2  2
            {0,0,2,0,0}};

    static int[][] mayBay4 ={{0,0,2,0,0},       //          2
            {0,2,0,0,2},       //         2  2
            {2,2,2,2,2},       //        22222      *Tam may bay o toa do [2;2]*
            {0,2,0,0,2},       //         2  2
            {0,0,2,0,0}};

    static int[][] trap;

    static int width;   // Chieu rong map
    static int height;  // Chieu cao map
    static int x;       //Toa do may bay
    static int y;       //Toa do may bay
    static int lastMove=4;
    static int newMove=4;

    public int dir;

    public int action;

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
        createMap();
        renderMap();
        renderPlane();
        nhapToaDo();
        chonHuongMayBay();
        renderMap();

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
            renderMap();
        }

    }

    public static void setLastMove(int lastMove) {
        Game.lastMove = lastMove;
    }

    public int getDir() {
        return dir;
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
        action = -1;
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

    public boolean checkSetPlane(int x, int y, int dir) {

        return true;
    }
    public void setPlane(int x, int y, int dir) {

    }

    public void chonHuongMayBay(){
        Scanner in= new Scanner(System.in);
        boolean check=true;
        while (check){
            System.out.println("-------------------------Chọn Hướng Máy Bay--------------------------");
            System.out.print("Nhap Huong ( 1: Len, 2: Phai, 3: Xuong, 4: Trai) : ");
            int move= in.nextInt();
            if(move>=1 && move<=4){
                setNewMove(move);
                updateHuong(getNewMove(),getLastMove());
                updateDiChuyen(map,mayBay,x,y);
                check=false;
                dir = move;
            }else{
                //Khong Hop Le
                System.out.println("Lua Chon Khong Hop Le !! Vui Long Chon Lai !!");
            }
        }
    }

    public void nhapToaDo(){
        Scanner in= new Scanner(System.in);
        System.out.println("---------------------TOA DO-----------------------");
        //Dat may bay
        //Chon vi tri dat
        System.out.println("Nhap toa do dat may bay !!");
        System.out.print("Nhap toa do x: ");
        int x= in.nextInt();
        System.out.print("Nhap toa do y: ");
        int y=in.nextInt();
        System.out.println("Toa do: ["+ x +":"+ y +"]");

        boolean checkToaDoFalse=true;
        while (checkToaDoFalse){
            if(checkDiChuyen(map,mayBay,x,y)==false){
                System.out.println("Toa Do Khong Hop Le !! Vui Long Nhap Lai !!!");
                System.out.print("Nhap toa do x: ");
                x=in.nextInt();
                System.out.print("Nhap toa do y: ");
                y=in.nextInt();
                System.out.println("Toa do: ["+ x +":"+ y +"]");
            }else{
                setX(x);
                setY(y);
                updateDiChuyen(map,mayBay,x,y);
                checkToaDoFalse=false;
            }
        }
    }

    public int selection(){
        Scanner in= new Scanner(System.in);
        System.out.println("-------------------------LUA CHON--------------------------");
        System.out.println("Chọn số 1:BẮN hoặc 2:DI CHUYỂN");
        System.out.print("Bạn Chọn: ");
        int action= 0;
        boolean check= true;
        action= in.nextInt();
        while (check){
            if(action<1 || action>2){
                System.out.println("Lựa chọn không hợp lệ!!! Vui long chọn lại");
                System.out.println("-------------------------LUA CHON--------------------------");
                System.out.println("Chọn số 1:BẮN hoặc 2:DI CHUYỂN");
                System.out.print("Bạn Chọn: ");
                action= in.nextInt();
            }else {
                System.out.println("Bạn đã chọn số: "+ action);
                if(action==1){
                    return action;
                }else {
                    if(checkDiChuyen(map,mayBay,x-1,y)==false &&
                            checkDiChuyen(map,mayBay,x+1,y)==false &&
                            checkDiChuyen(map,mayBay,x,y-1)==false &&
                            checkDiChuyen(map,mayBay,x,y+1)==false ){
                        System.out.println("Bạn Không Thể Di Chuyển !! Chỉ Có Thể Bắn !!");
                        return 1;
                    }else {
                        return action;
                    }
                }
            }
        }
        return action;
    }

    public void firer(){
        Scanner in= new Scanner(System.in);
        System.out.println("-------------------------BAN--------------------------");
        System.out.print("Nhap toa do x can ban: ");
        int j=in.nextInt();
        System.out.print("Nhap toa do y can ban: ");
        int i=in.nextInt();
        System.out.println("Toa do ban: ["+ j +":"+ i +"]");
        boolean check=true;
        while (check){
            if(i<0 || i>=getHeight() || j<0 || j>=getWidth()){
                System.out.println("Toa Do Khong Hop Le !! Vui Long Nhap Lai !!");
                System.out.println("-------------------------BAN--------------------------");
                System.out.print("Nhap toa do x can ban: ");
                j=in.nextInt();
                System.out.print("Nhap toa do y can ban: ");
                i=in.nextInt();
                System.out.println("Toa do ban: ["+ j +":"+ i +"]");
            }else {
                System.out.println("Toa Do Ban la : [" + j + ":" + i +"]");
                if(map[i][j]==2){
                    mayBay[2-y+i][2-x+j]=3;
                    updateDiChuyen(map,mayBay,x,y);
                } else if (map[i][j]==0) {
                    map[i][j]=4;
                }
                check=false;
            }
        }
    }

    public void move(){
        // 1: Len, 2: Phai, 3: Xuong, 4: Trai
        Scanner in= new Scanner(System.in);
        boolean check=true;
        while (check){
            System.out.println("-------------------------MOVE--------------------------");
            System.out.print("Nhap Huong Di Chuyen( 1: Len, 2: Phai, 3: Xuong, 4: Trai) : ");
            int move= in.nextInt();
            if(move==1){
                //Di Chuyen Len
                if(checkDiChuyen(map,mayBay,x,y-1)==false){
                    System.out.println("Di Chuyen Khong Hop Le !! Vui Long Nhap Lai !!!");
                }else{
                    y--;
                    updateDiChuyen(map,mayBay,x,y);
                    check=false;
                    break;
                }
            }else if(move==2){
                //Di Chuyen Phai
                if(checkDiChuyen(map,mayBay,x+1,y)==false){
                    System.out.println("Di Chuyen Khong Hop Le !! Vui Long Nhap Lai !!!");
                }else{
                    x++;
                    updateDiChuyen(map,mayBay,x,y);
                    check=false;
                    break;
                }
            }else if(move==3){
                //Di Chuyen Xuong
                if(checkDiChuyen(map,mayBay,x,y+1)==false){
                    System.out.println("Di Chuyen Khong Hop Le !! Vui Long Nhap Lai !!!");
                }else{
                    y++;
                    updateHuong(getNewMove(),getLastMove());
                    updateDiChuyen(map,mayBay,x,y);
                    check=false;
                    break;
                }
            } else if(move==4) {
                //Di Chuyen Trai
                if(checkDiChuyen(map,mayBay,x-1,y)==false){
                    System.out.println("Di Chuyen Khong Hop Le !! Vui Long Nhap Lai !!!");
                }else{
                    x--;
                    updateDiChuyen(map,mayBay,x,y);
                    check=false;
                    break;
                }
            }else{
                //Khong Hop Le
                System.out.println("Lua Chon Khong Hop Le !! Vui Long Chon Lai !!");
            }
        }
    }

}

