import Start.Game;
import Start.GameOther;
import com.sun.deploy.util.SessionState;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GameClient1_2 extends Application {

    private Game game;
    private GameOther gameOther;
    private Client client;
    private int clientID;
    private int arr_trap[][];
    private int width_map;
    private int height_map;

    private GraphicsContext gc;
    private Canvas canvas;

    private final int WIDTH_DEFAULT = 30;
    private final int HEIGHT_DEFAULT = 30;

    private int status;

    Executor threadPool = Executors.newFixedThreadPool(5);
    Executor threadPool2 = Executors.newFixedThreadPool(5);
    @Override
    public void start(Stage primaryStage) throws Exception {
        //init
        game = new Game(0, 0);
        gameOther = new GameOther(0, 0);
        client = new Client("2.tcp.ngrok.io", 15380, game, gameOther);
        client.getArr_Trap();
        clientID = client.getClientID();
        width_map = client.getWidth_map();
        height_map = client.getHeight_map();

        canvas = new Canvas(WIDTH_DEFAULT * width_map * 2 + 200, HEIGHT_DEFAULT * height_map);
        gc = canvas.getGraphicsContext2D();
        Group root = new Group();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        Scanner scanner = new Scanner(System.in);

        status = 0;
        threadPool2.execute(()-> {

            while (true) {
                if (client.has_change) {
                    render();
                    client.has_change = false;
                    if (client.is_over) {
                        renderOver();
                    }

                }
                client.readDataFromServer();
                if (client.is_over) {
                    renderOver();
                }
                System.out.println("client rcv: " + client.getID_receive());

            }
        });

        threadPool.execute(()-> {
            while (true) {

                update();
//                if (client.has_change) {
//                    render();
//                    client.has_change = false;
//                }
                if (status == 0) {
                    boolean loop = true;
                    int dir = 0;
                    int x_ = 0;
                    int y_ = 0;
                    while (loop) {
                        //need validate
                        System.out.print("đặt máy bay(hướng, toạ độ x, tọa độ y): ");
                        dir = scanner.nextInt();
                        x_ = scanner.nextInt();
                        y_ = scanner.nextInt();
                        if (game.checkLocationPlane(dir, x_, y_)) {
                            loop = false;
                        }

                    }
                    client.sendSetPlanePkt(dir, x_, y_);
                    client.setID_receive(0);
//                    render();
//                    client.readDataFromServer();


                    status = 1;

                }
                if (status == 1 && !client.is_over) {
//                    client.readDataFromServer();
//                    if (client.has_change) {
//                        render();
//                        client.has_change = false;
//                    }
                    System.out.println("check:   " + clientID + " " + client.getID_receive());
                    if (client.getID_receive() == clientID) {
                        System.out.print("nhap 1 de di chuyen 2 de ban: ");
                        int command = scanner.nextInt();
                        System.out.println();
                        if (command == 1) {
                            System.out.print("chon huong(0 sang trai, 1 len tren, 2 sang phai, 3 xuong duoi): ");
                            int dir = scanner.nextInt();
                            client.sendPktPlay(command, dir);
                            client.setID_receive(0);
                            render();
//                        client.readDataFromServer();

                        }
                        if (command == 2) {
                            System.out.print(" nhập toạ độ bắn: ");
                            int x_ = scanner.nextInt();
                            int y_ = scanner.nextInt();
                            client.sendPktPlay(command, x_, y_);
                            client.setID_receive(0);
                            render();
//                        client.readDataFromServer();
                        }


                    }
                }

            }
        });




    }

    public void update() {

    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (status == 0) {
            gc.fillText("Set Your Plane", game.getMap().length * WIDTH_DEFAULT + 10, 50);
        }
        if (status == 1) {
            gc.fillText("Move Your Plane", game.getMap().length * WIDTH_DEFAULT + 10, 50);
        }
        gc.fillText("Your ID: " + String.valueOf(client.getClientID()), game.getMap().length * WIDTH_DEFAULT + 10, 100);
        gc.fillText("Your ID: " + String.valueOf(client.getID_receive()), game.getMap().length * WIDTH_DEFAULT + 10, 200);
        gc.setFill(Color.BLUE);
        renderMapPlayer();
        renderMapOther();
    }

    public void renderMapPlayer() {
        game.renderGame(gc, 0, 0);
    }

    public void renderMapOther() {
        gameOther.renderGame(gc, game.getMap().length * WIDTH_DEFAULT + 200, 0);
    }

    public void renderOver() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFont(new Font(40));
        if (client.getResultMatch() == 1) {
            gc.fillText(" DRAW ", 400, 400);
        }
        if (client.getResultMatch() == 0) {
            gc.fillText(" LOSE ", 400, 400);
        }
        if (client.getResultMatch() == 2) {
            gc.fillText(" WIN ", 400, 400);
        }
    }
}
