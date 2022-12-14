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

    public static void firer(){
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

    public static int selection(){
        Scanner in= new Scanner(System.in);
        System.out.println("Ch???n s??? 1:B???N ho???c 2:DI CHUY???N");
        System.out.print("B???n Ch???n: ");
        int action= 0;
        boolean check= true;
        action= in.nextInt();
        while (check){
            if(action<1 || action>2){
                System.out.println("L???a ch???n kh??ng h???p l???!!! Vui long ch???n l???i");
                System.out.println("Ch???n s??? 1:B???N ho???c 2:DI CHUY???N");
                System.out.print("B???n Ch???n: ");
                action= in.nextInt();
            }else {
                System.out.println("B???n ???? ch???n s???: "+ action);
                return action;
            }
        }
        return action;
    }


    public static void nhapToaDo(){
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
    public static void createMap(int width, int height) {
        map = new int [width][height];
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                map[j][i]=0;
            }
        }
    }

    public static void renderPlane() {
        System.out.println("---MAY BAY---");
        for (int i=0;i<mayBay.length;i++){
            for(int j=0;j<mayBay[0].length;j++){
                System.out.print(mayBay[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void renderMap(int width, int height) {
        System.out.println("---------------MAP---------------");
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                if (map[i][j] == 0) {
                    System.out.print("." + " ");
                } else {
                    System.out.print("x" +  " ");
                }
//                System.out.print(map[i][j]+ " ");
            }
            System.out.println();
        }
    }
}
