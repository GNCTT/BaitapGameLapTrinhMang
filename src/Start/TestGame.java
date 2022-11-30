package Start;

import java.util.Scanner;

public class TestGame {

    public static void main(String[] args) {
        Game game = new Game(10, 10);
        Scanner sc = new Scanner(System.in);
        int[][] arr_trap = new int[10][2];
        for (int i = 0; i < 10; i++) {
            arr_trap[i][0] = i;
            arr_trap[i][1] = 1;
        }

            System.out.println("nhap x y:");
            int x = sc.nextInt();
            int y = sc.nextInt();
            System.out.println();
            System.out.println(game.checkLocationPlane(x, y));
            game.addTrap(arr_trap);
            game.setPlane(x, y);
            while (true) {
                game.render();
                int move = sc.nextInt();
                game.move(move);
            }

    }

}
