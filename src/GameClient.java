import java.util.Scanner;

public class Game{
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
                if(map[i][j]==2){
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

    public static void main(String[] args) {
        int[][] map;
        int N=0,M=0;
        Scanner in= new Scanner(System.in);
        System.out.print("Nhap M: ");
        M=in.nextInt();
        System.out.print("Nhap N: ");
        N=in.nextInt();
        //Create map N>=7 M>=5
        //0: tu do ,1: chuong ngai vat, 2: may bay
        System.out.println("--------------MAP----------------");
        map= new int[M][N];
        for(int i=0;i<N;i++){
            for(int j=0;j<M;j++){
                map[j][i]=0;
            }
        }
        for(int i=0;i<N;i++){
            for(int j=0;j<M;j++){
                System.out.print(map[j][i]+ " ");
            }
            System.out.println();
        }
        //may bay
//          2
//         2  2
//        22222      *Tam may bay o toa do [2;2]*
//         2  2
//          2

        System.out.println("---MAY BAY---");
        int[][] mayBay ={{0,0,2,0,0},
                         {0,2,0,0,2},
                         {2,2,2,2,2},
                         {0,2,0,0,2},
                         {0,0,2,0,0}};
        for (int i=0;i<mayBay.length;i++){
            for(int j=0;j<mayBay[0].length;j++){
                System.out.print(mayBay[j][i] + " ");
            }
            System.out.println();
        }
        System.out.println("------------TOA DO---------------");
        //Dat may bay
        //Chon vi tri dat
        System.out.print("Nhap toa do x: ");
        int x=in.nextInt();
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
                updateDiChuyen(map,mayBay,x,y);
                checkToaDoFalse=false;
            }
        }

        System.out.println("---------------MAP---------------");
        for(int i=0;i<N;i++){
            for(int j=0;j<M;j++){
                System.out.print(map[i][j]+ " ");
            }
            System.out.println();
        }

        System.out.println("--------------START--------------");
        boolean start=true;
        while (start){
            // chon 1: BAN  2:DI CHUYEN
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
                    check=false;
                }
            }

            // Ban
            if(action==1){
                System.out.print("Nhap toa do x can ban: ");
                int i=in.nextInt();
                System.out.print("Nhap toa do y can ban: ");
                int j=in.nextInt();
                System.out.println("Toa do ban: ["+ i +":"+ j +"]");
                check=true;
                while (check){
                    if(i<0 || i>=N || j<0 || j>M){
                        System.out.println("Toa Do Khong Hop Le !! Vui Long Nhap Lai !!");
                        System.out.print("Nhap toa do x can ban: ");
                        i=in.nextInt();
                        System.out.print("Nhap toa do y can ban: ");
                        j=in.nextInt();
                        System.out.println("Toa do ban: ["+ i +":"+ j +"]");
                    }else {
                        break;
                    }
                }

            }

            // Di Chuyen
            if(action==2){
                // 1: Len, 2: Phai, 3: Xuong, 4: Trai
                check=true;
                while (check){
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
            for(int i=0;i<N;i++){
                for(int j=0;j<M;j++){
                    System.out.print(map[i][j]+ " ");
                }
                System.out.println();
            }
        }
    }
}

