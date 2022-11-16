package Start;

import java.util.Scanner;

import static Start.Game.*;
import static Start.RotatePlane.*;


public class Function {
    public static void move(){
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

    public static void firer(){
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

    public static int selection(){
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

    public static void chonHuongMayBay(){
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
            }else{
                //Khong Hop Le
                System.out.println("Lua Chon Khong Hop Le !! Vui Long Chon Lai !!");
            }
        }
    }

    public static void nhapToaDo(){
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
        map = new int [width][height];
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
                    System.out.print(". ");
                } else if (mayBay[i][j]==2) {
                    System.out.print("* ");
                } else if (mayBay[i][j]==3) {
                    System.out.print("# ");
                }
            }
            System.out.println();
        }
    }

    public static void renderMap(int width, int height) {
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
//                if(map[i][j]==0){
//                    System.out.print(". ");
//                } else if(map[i][j]==1) {
//                    System.out.print("0 ");
//                } else if (map[i][j]==2) {
//                    System.out.print("* ");
//                } else if (map[i][j]==3) {
//                    System.out.print("# ");
//                }
            }
            System.out.println();
        }
    }
}
