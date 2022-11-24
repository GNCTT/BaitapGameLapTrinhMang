package Start;

import static Start.Game.*;
import static Start.Game.*;

public class RotatePlane {
    public static void updateHuong(int newMove, int lastMove){
        if (lastMove==1){
            if(newMove==2){
                setLastMove(newMove);
                rotateRight();
            }else  if(newMove==3){
                setLastMove(newMove);
                reverseHeight();
            }else if(newMove==4){
                setLastMove(newMove);
                rotateLeft();
            }
        } else if (lastMove==2) {
            if(newMove==1){
                setLastMove(newMove);
                rotateLeft();
            }else if(newMove==3){
                setLastMove(newMove);
                rotateRight();
            }else if(newMove==4){
                setLastMove(newMove);
                reverseWidth();
            }
        } else if (lastMove==3) {
            if(newMove==1){
                setLastMove(newMove);
                reverseHeight();
            }else if(newMove==2){
                setLastMove(newMove);
                rotateLeft();
            }else if(newMove==4){
                setLastMove(newMove);
                rotateRight();
            }
        } else if (lastMove==4) {
            if(newMove==1){
                setLastMove(newMove);
                rotateRight();
            }else if(newMove==2){
                setLastMove(newMove);
                reverseWidth();
            }else if(newMove==3){
                setLastMove(newMove);
                rotateLeft();}
        }
    }

    public static void swap(int[][] New, int[][] Old){
        for (int i = 0; i < New.length; ++i) {
            for (int j = 0; j < New.length; ++j) {
                Old[i][j]=New[i][j];
            }
        }
    }
    public static void rotateRight(){
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

    public static void rotateLeft(){
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

    public static void reverseWidth(){
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

    public static void reverseHeight(){
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
}
