package Start;

import java.util.Scanner;


public class Game {
    static int [][] map;
    static int[][] mayBay ={{0,0,2,0,0},       //          2
                            {0,2,0,0,2},       //         2  2
                            {2,2,2,2,2},       //        22222      *Tam may bay o toa do [2;2]*
                            {0,2,0,0,2},       //         2  2
                            {0,0,2,0,0}};      //          2
    static int width;   // Chieu rong map
    static int height;  // Chieu cao map

    static int x;       //Toa do may bay
    static int y;       //Toa do may bay

    static int lastMove=4;
    static int newMove=4;



    public static void main(String[] args) {
//        Scanner in= new Scanner(System.in);
//        int N,M;
//        M=in.nextInt();
//        N=in.nextInt();
        Game game= new Game(10,10);
        game.start(game);

    }

    public void start(Game game){
        game.createMap(game.getWidth(), game.getHeight());
        game.renderMap(game.getWidth(), game.getHeight());
        game.renderPlane();
        game.nhapToaDo();
        game.renderMap(game.getWidth(), game.getHeight());

        System.out.println("--------------START--------------");
        Scanner in= new Scanner(System.in);
        boolean start=true;
        while (start){
            // chon 1: BAN  2:DI CHUYEN
            System.out.println("Chọn số 1:BẮN hoặc 2:DI CHUYỂN");
            System.out.print("Bạn Chọn: ");
            int action= game.selection();
            boolean check= true;
            // Ban
            if(action==1){
                game.firer();
            }

            // Di Chuyen
            if(action==2){
                game.move();
            }
            game.renderMap(game.getWidth(), game.getHeight());
        }

    }

    public void updateHuong(int newMove, int lastMove){
        if (lastMove==1){
            if(newMove==2){
                setLastMove(getNewMove());
                rotateRight();
            }else  if(newMove==3){
                setLastMove(getNewMove());
                reverseHeight();
            }else if(newMove==4){
                setLastMove(getNewMove());
                rotateLeft();
            }
        } else if (lastMove==2) {
            if(newMove==1){
                setLastMove(getNewMove());
                rotateLeft();
            }else if(newMove==3){
                setLastMove(getNewMove());
                rotateRight();
            }else if(newMove==4){
                setLastMove(getNewMove());
                reverseWidth();
            }
        } else if (lastMove==3) {
            if(newMove==1){
                setLastMove(getNewMove());
                reverseHeight();
            }else if(newMove==2){
                setLastMove(getNewMove());
                rotateLeft();
            }else if(newMove==4){
                setLastMove(getNewMove());
                rotateRight();
            }
        } else if (lastMove==4) {
            if(newMove==1){
                setLastMove(getNewMove());
                rotateRight();
            }else if(newMove==2){
                setLastMove(getNewMove());
                reverseWidth();
            }else if(newMove==3){
                setLastMove(getNewMove());
                rotateLeft();}
        }
    }

    public void swap(int[][] New, int[][] Old){
        for (int i = 0; i < New.length; ++i) {
            for (int j = 0; j < New.length; ++j) {
                Old[i][j]=New[i][j];
            }
        }
    }
    public void rotateRight(){
        int[][] ret1=new int[mayBay.length][mayBay.length];
        int[][] ret2=new int[mayBay.length][mayBay.length];
        swap(mayBay,ret2);
        for (int i = 0; i < mayBay.length; ++i) {
            for (int j = 0; j < mayBay.length; ++j) {
                ret1[i][j]=ret2[mayBay.length-j-1][i];
            }
        }
        swap(ret1,mayBay);
    }

    public void rotateLeft(){
        int[][] ret1=new int[mayBay.length][mayBay.length];
        int[][] ret2=new int[mayBay.length][mayBay.length];
        swap(mayBay,ret2);
        for (int i = 0; i < mayBay.length; ++i) {
            for (int j = 0; j < mayBay.length; ++j) {
                ret1[i][j]=ret2[j][mayBay.length-i-1];
            }
        }
        swap(ret1,mayBay);
    }

    public void reverseWidth(){
        int[][] ret1=new int[mayBay.length][mayBay.length];
        int[][] ret2=new int[mayBay.length][mayBay.length];
        swap(mayBay,ret2);
        for (int i = 0; i < mayBay.length; ++i) {
            for (int j = 0; j < mayBay.length; ++j) {
                ret1[i][j]=ret2[i][mayBay.length-j-1];
            }
        }
        swap(ret1,mayBay);
    }

    public void reverseHeight(){
        int[][] ret1=new int[mayBay.length][mayBay.length];
        int[][] ret2=new int[mayBay.length][mayBay.length];
        swap(mayBay,ret2);
        for (int i = 0; i < mayBay.length; ++i) {
            for (int j = 0; j < mayBay.length; ++j) {
                ret1[i][j] = ret2[mayBay.length-i-1][j];
            }
        }
        swap(ret1,mayBay);
    }
    public int selection(){
        Scanner in= new Scanner(System.in);
        System.out.println("Chọn số 1:BẮN hoặc 2:DI CHUYỂN");
        System.out.print("Bạn Chọn: ");
        int action= 0;
        boolean check= true;
        action= in.nextInt();
        while (check){
            if(action<1 || action>2){
                System.out.println("Lựa chọn không hợp lệ!!! Vui long chọn lại");
                System.out.println("Chọn số 1:BẮN hoặc 2:DI CHUYỂN");
                System.out.print("Bạn Chọn: ");
                action= in.nextInt();
            }else {
                System.out.println("Bạn đã chọn số: "+ action);
                return action;
            }
        }
        return action;
    }


    public void move(){
        // 1: Len, 2: Phai, 3: Xuong, 4: Trai
        Scanner in= new Scanner(System.in);
        boolean check=true;
        while (check){
            System.out.print("Nhap Huong Di Chuyen( 1: Len, 2: Phai, 3: Xuong, 4: Trai) : ");
            int move= in.nextInt();
            if(move==1){
                //Di Chuyen Len
                if(checkDiChuyen(map,mayBay,x,y-1)==false){
                    System.out.println("Di Chuyen Khong Hop Le !! Vui Long Nhap Lai !!!");
                }else{
                    y--;
                    setNewMove(1);
                    updateHuong(getNewMove(),getLastMove());
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
                    setNewMove(2);
                    updateHuong(getNewMove(),getLastMove());
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
                    setNewMove(3);
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
                    setNewMove(4);
                    updateHuong(getNewMove(),getLastMove());
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

    public void firer(){
        Scanner in= new Scanner(System.in);
        System.out.print("Nhap toa do x can ban: ");
        int i=in.nextInt();
        System.out.print("Nhap toa do y can ban: ");
        int j=in.nextInt();
        System.out.println("Toa do ban: ["+ i +":"+ j +"]");
        boolean check=true;
        while (check){
            if(i<0 || i>=getHeight() || j<0 || j>=getWidth()){
                System.out.println("Toa Do Khong Hop Le !! Vui Long Nhap Lai !!");
                System.out.print("Nhap toa do x can ban: ");
                i=in.nextInt();
                System.out.print("Nhap toa do y can ban: ");
                j=in.nextInt();
                System.out.println("Toa do ban: ["+ i +":"+ j +"]");
            }else {
                System.out.println("Toa Do Ban la : [" + i + ":" + j +"]");
                if(map[i][j]==2){
                    mayBay[x-i][y-j]=3;
                    updateDiChuyen(map,mayBay,x,y);
                }
                check=false;
            }
        }

    }

    public void nhapToaDo(){
        Scanner in= new Scanner(System.in);
        System.out.println("------------TOA DO---------------");
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

    public Game(){
    }

    public static int[][] getMap() {
        return map;
    }



    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void createMap(int width, int height) {
        this.map = new int [width][height];
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                map[j][i]=0;
            }
        }
    }

    public void renderPlane() {
        System.out.println("---MAY BAY---");
        for (int i=0;i<mayBay.length;i++){
            for(int j=0;j<mayBay[0].length;j++){
                System.out.print(mayBay[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void renderMap(int width, int height) {
        System.out.println("---------------MAP---------------");
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                System.out.print(map[i][j]+ " ");
            }
            System.out.println();
        }
    }


    public static boolean checkDiChuyen(int[][] map,int[][] mayBay,int x,int y){
        int h=0,k=0;
        if(x-2<0 || x+2>= map[0].length || y-2<0 || y+2>= map.length){
            return false;
        }
        for(int i=y-2;i<y-2+ mayBay.length;i++){
            for(int j=x-2;j<x-2+mayBay[0].length;j++){
                if(j<0 || j>= map[0].length || i<0 || i>= map.length){
                    return false;
                }
                if(map[j][i]==1 && mayBay[k][h]==2){
                    return false;
                }
                k++;
            }
            h++; k=0;
        }
        return true;
    }

    public static void updateDiChuyen(int[][] map,int[][] mayBay,int x,int y){
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
                map[i][j]=mayBay[h][k];
                k++;
            }
            h++;
            k=0;
        }
    }
}

