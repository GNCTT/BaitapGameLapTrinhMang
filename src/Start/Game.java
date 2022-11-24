package Start;

import java.util.Random;
import java.util.Scanner;

//import static Start.Function.*;
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
        setupPlane();

//        setDir(chonHuongMayBay());
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
                setDir(checkMove());
                move(getDir());
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

    public static int getX() {
        return x;
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

    //Function
    public static int checkMove() {
        // 1: Len, 2: Phai, 3: Xuong, 4: Trai
        Scanner in = new Scanner(System.in);
        int move=0;
        boolean check = true;
        int count=0;
        while (check) {
            System.out.println("-------------------------MOVE--------------------------");
            System.out.print("Nhap Huong Di Chuyen( 1: Len, 2: Phai, 3: Xuong, 4: Trai) : ");
            int newMoveOld=getNewMove();
            move = in.nextInt();
            int dir= move;
            updateHuong(dir,getLastMove());
            if (move == 1) {
                //Di Chuyen Len
                if (checkSetPlane(map, mayBay, x, y - 1) == false) {
                    System.out.println("Di Chuyen Khong Hop Le !! Vui Long Nhap Lai !!!");
                    updateHuong(newMoveOld,dir);
                    count++;
                } else {
                    setX(x);
                    setY(y);
                    setupDir(dir);
                    check = false;
                    break;
                }
            } else if (move == 2) {
                //Di Chuyen Phai
                if (checkSetPlane(map, mayBay, x + 1, y) == false) {
                    System.out.println("Di Chuyen Khong Hop Le !! Vui Long Nhap Lai !!!");
                    updateHuong(newMoveOld,dir);
                    count++;
                } else {
                    setX(x);
                    setY(y);
                    setupDir(dir);
                    check = false;
                    break;
                }
            } else if (move == 3) {
                //Di Chuyen Xuong
                if (checkSetPlane(map, mayBay, x, y + 1) == false) {
                    System.out.println("Di Chuyen Khong Hop Le !! Vui Long Nhap Lai !!!");
                    updateHuong(newMoveOld,dir);
                    count++;
                } else {
                    setX(x);
                    setY(y);
                    setupDir(dir);
                    check = false;
                    break;
                }
            } else if (move == 4) {
                //Di Chuyen Trai
                if (checkSetPlane(map, mayBay, x - 1, y) == false) {
                    System.out.println("Di Chuyen Khong Hop Le !! Vui Long Nhap Lai !!!");
                    updateHuong(newMoveOld,dir);
                    count++;
                } else {
                    setX(x);
                    setY(y);
                    setupDir(dir);
                    check = false;
                    break;
                }
            } else {
                //Khong Hop Le
                System.out.println("Lua Chon Khong Hop Le !! Vui Long Chon Lai !!");
                updateHuong(newMoveOld,dir);
                count++;
            }
        }
        return move;
    }

    public static void move(int dir) {
        // 1: Len, 2: Phai, 3: Xuong, 4: Trai
        Scanner in = new Scanner(System.in);
        boolean check = true;
        while (check) {
            if (dir == 1) {
                //Di Chuyen Len
                y--;
                setDir(dir);
                updateMap(map, mayBay, x, y);
                check = false;
                break;
            }else if (dir == 2) {
                //Di Chuyen Phai
                x++;
                setDir(dir);
                updateMap(map, mayBay, x, y);
                check = false;
                break;
            } else if (dir == 3) {
                //Di Chuyen Xuong
                y++;
                setDir(dir);
                updateMap(map, mayBay, x, y);
                check = false;
                break;
            } else if (dir == 4) {
                //Di Chuyen Trai
                x--;
                setDir(dir);
                updateMap(map, mayBay, x, y);
                check = false;
                break;
            }
        }
    }

    public static int firer(int xFirer, int yFirer) {
        if (map[yFirer][xFirer] == 2) {
            mayBay[2 - y + yFirer][2 - x + xFirer] = 3;
            map[yFirer][xFirer] = 4;
            updateMap(map, mayBay, x, y);
            System.out.println("-----------TRUNG------------");
            return 1;
        } else if (map[yFirer][xFirer] == 0) {
            map[yFirer][xFirer] = 4;
            System.out.println("-----------TRUOT------------");
            return 0;
        } else if (map[yFirer][xFirer] == 1) {
            System.out.println("-----------TRUNGBAY------------");
            return 2;
        }
        return 0;
    }

    public static void createTrap() {
        sumTrap = (getHeight() + getWidth()) / 3;
        trap = new int[getSumTrap()][2];
        int count = 0;
        Random generator = new Random();
        int num = 0;
        boolean check = true;
        while (check) {

            for (int i = 0; i < getHeight(); i++) {
                for (int j = 0; j < getWidth(); j++) {
                    if (count >= getSumTrap()) {
                        check = false;
                        break;
                    }
                    num = generator.nextInt(30) + 1;
                    if (num <= 2) {
                        trap[count][0] = j;
                        trap[count][1] = i;
                        count++;
                        if (count >= getSumTrap()) {
                            check = false;
                            break;
                        }
                    }
                }

            }
        }
    }

    public static void addTrap() {
        for (int i = 0; i < getSumTrap(); i++) {
            map[trap[i][0]][trap[i][1]] = 1;
        }
    }

    public static int selection() {
        Scanner in = new Scanner(System.in);
        System.out.println("-------------------------LUA CHON--------------------------");
        System.out.println("Chọn số 1:BẮN hoặc 2:DI CHUYỂN");
        System.out.print("Bạn Chọn: ");
        int action = 0;
        boolean check = true;
        action = in.nextInt();
        while (check) {
            if (action < 1 || action > 2) {
                System.out.println("Lựa chọn không hợp lệ!!! Vui long chọn lại");
                System.out.println("-------------------------LUA CHON--------------------------");
                System.out.println("Chọn số 1:BẮN hoặc 2:DI CHUYỂN");
                System.out.print("Bạn Chọn: ");
                action = in.nextInt();
            } else {
                System.out.println("Bạn đã chọn số: " + action);
                if (action == 1) {
                    return action;
                } else {
                    if (checkSetPlane(map, mayBay, x - 1, y) == false &&
                            checkSetPlane(map, mayBay, x + 1, y) == false &&
                            checkSetPlane(map, mayBay, x, y - 1) == false &&
                            checkSetPlane(map, mayBay, x, y + 1) == false) {
                        System.out.println("Bạn Không Thể Di Chuyển !! Chỉ Có Thể Bắn !!");
                        return 1;
                    } else {
                        return action;
                    }
                }
            }
        }
        return action;
    }

    public static void setupDir(int dir) {
        setNewMove(dir);
        setDir(dir);
        updateHuong(getNewMove(), getLastMove());
        updateMap(map, mayBay, x, y);
    }
    public static void checkSetPlane(){
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
            if(checkSetPlane(map,mayBay,x,y)==false){
                System.out.println("Toa Do Khong Hop Le !! Vui Long Nhap Lai !!!");
                System.out.print("Nhap toa do x: ");
                x=in.nextInt();
                System.out.print("Nhap toa do y: ");
                y=in.nextInt();
                System.out.println("Toa do: ["+ x +":"+ y +"]");
            }else{
                setX(x);
                setY(y);
                updateMap(map,mayBay,x,y);
                checkToaDoFalse=false;
            }
        }
    }

    public static void setupPlane(){
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
        boolean check=true;
        while (checkToaDoFalse){
            if(checkSetPlane(map,mayBay,x,y)==false){
                System.out.println("Toa Do Khong Hop Le !! Vui Long Nhap Lai !!!");
                System.out.print("Nhap toa do x: ");
                x=in.nextInt();
                System.out.print("Nhap toa do y: ");
                y=in.nextInt();
                System.out.println("Toa do: ["+ x +":"+ y +"]");
            }else{
                setX(x);
                setY(y);
                updateMap(map,mayBay,getX(),getY());
                checkToaDoFalse=false;
            }
        }
    }
    public static void chuongNgaiVat(){

    }
    public static boolean checkLose(){
        for (int i=0;i<mayBay.length;i++){
            for (int j=0;j<mayBay.length;j++){
                if(mayBay[i][j]==2){
                    return false;
                }
            }
        }
        return true;
    }
    public static boolean checkSetPlane(int[][] map,int[][] mayBay,int x,int y){
        int h=0,k=0;
        if(x-2<0 || x+2>= map[0].length || y-2<0 || y+2>= map.length){
            return false;
        }
        for(int i=y-2;i<y-2+ mayBay.length;i++){
            for(int j=x-2;j<x-2+mayBay[0].length;j++){
                if(j<0 || j>= map[0].length || i<0 || i>= map.length){
                    return false;
                }
                if(map[i][j]==1 && mayBay[h][k]==2||map[i][j]==1 && mayBay[h][k]==3){
                    return false;
                }if(map[i][j]==4 && mayBay[h][k]==2 ||map[i][j]==4 && mayBay[h][k]==3){
                    return false;
                }
                k++;
            }
            h++; k=0;
        }
        return true;
    }

    public static void updateMap(int[][] map,int[][] mayBay,int x,int y){
        for(int i=0;i< map.length;i++){
            for(int j=0;j<map[0].length;j++){
                if(map[i][j]==2 || map[i][j]==3){
                    map[i][j]=0;
                }
            }
        }
        int h=0,k=0;
        for(int i=y-2;i<y-2+ mayBay.length;i++){
            for(int j=x-2;j<x-2+mayBay[0].length;j++){
                if(map[i][j]!=1 && map[i][j]!=4){
                    map[i][j]=mayBay[h][k];
                    k++;
                }else {
                    k++;
                }
            }
            h++;
            k=0;
        }
    }
    public static void createMap(int width, int height) {
        map = new int [getHeight()][getWidth()];
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                map[i][j]=0;
            }
        }
    }

    public static void renderPlane() {
        System.out.println("-----------------MAY BAY--------------");
        for (int i=0;i<mayBay.length;i++){
            for(int j=0;j<mayBay[0].length;j++){
                if(mayBay[i][j]==0){
                    System.out.print(".  ");
                } else if (mayBay[i][j]==2) {
                    System.out.print("*  ");
                } else if (mayBay[i][j]==3) {
                    System.out.print("#  ");
                }
            }
            System.out.println();
        }
    }

    public static void renderMap(int width, int height) {
        System.out.println("-------------------------MAP------------------------");
        int count=0;
        for(int i=-1;i<=height;i++){
            for(int j=-1;j<=width;j++){
                if(i==-1 ){
                    if(j==-1){
                        System.out.print("y/x ");
                    } else if (j==width) {
                        System.out.print("  ");
                    }else if(j>=0 && j<10){
                        System.out.print(j + "  ");
                    }else if(j>=10 && j<height-1){
                        System.out.print(j + " ");
                    }else if(j==height-1){
                        System.out.print(j + " x/y");
                    }
                } else if (i>=0 && i<10){
                    if(j==-1){
                        System.out.print(i + "   ");
                    } else if (j>=0 && j<width) {
                        if(map[i][j]==0){
                            System.out.print(".  ");
                        } else if(map[i][j]==1) {
                            System.out.print("=  ");
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
                        System.out.print(i + "  ");
                    } else if (j>=0 && j<width) {
                        if(map[i][j]==0){
                            System.out.print(".  ");
                        } else if(map[i][j]==1) {
                            System.out.print("=  ");
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
                        System.out.print("y/x ");
                    } else if (j==width) {
                        System.out.print("  ");
                    }else if(j>=0 && j<10){
                        System.out.print(j + "  ");
                    }else if(j>=10 && j<height-1){
                        System.out.print(j + " ");
                    }else if( j==height-1){
                        System.out.print(j+" x/y");
                    }
                }
            }
            System.out.println();
        }
    }
}

