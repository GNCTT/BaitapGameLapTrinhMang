import Start.Game;
import Start.GameOther;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Random;
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
    //    private int dir ;
//    private int x_ ;
//    private int y_ ;
    private int xFire = 20;
    private int yFire = 19;
    private int status;
    Random rand = new Random();

    Executor threadPool = Executors.newFixedThreadPool(5);
    Executor threadPool2 = Executors.newFixedThreadPool(5);
    @Override
    public void start(Stage primaryStage) throws Exception {
        //init
        game = new Game(0, 0);
        gameOther = new GameOther(0, 0);
        client = new Client("4.tcp.ngrok.io", 18683, game, gameOther);
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
                client.readDataFromServer();
                if (client.is_over) {
                    renderOver();
                    break;
                }
                if (client.has_change) {
                    render();
                    client.has_change = false;
                    if (client.is_over) {
                        renderOver();
                        break;
                    }
                }

                System.out.println("client rcv: " + client.getID_receive());

            }
        });

        threadPool.execute(()-> {
            while (true) {
                if (client.is_over) {
                    renderOver();
                    break;
                }

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
//                        dir = scanner.nextInt();
//                        x_ = scanner.nextInt();
//                        y_ = scanner.nextInt();
//                        dir = rand.nextInt(4);
//                        x_ = rand.nextInt(10)+10;
//                        y_ = rand.nextInt(10)+10;
                        dir = 1;
                        x_ = 17;
                        y_ = 17;
                        if (game.checkLocationPlane(dir, x_, y_)) {
                            loop = false;
                        }

                    }
                    client.sendSetPlanePkt(dir, x_, y_);
                    client.setID_receive(0);
                    status = 1;
                }
                if (status == 1 && !client.is_over) {
//                    System.out.println("hm");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (client.is_over) {
                        renderOver();
                        break;
                    }
                    if (client.getID_receive() == clientID) {
                        int command = 2 ;
//                        int command = rand.nextInt(2)+1;
//                        int command = scanner.nextInt();
                        System.out.println();
//                        if (client.is_over) {
//                            render();
//                            break;
//                        }
                        if (command == 1) {
                            System.out.print("chon huong(0 sang trai, 1 len tren, 2 sang phai, 3 xuong duoi): ");
                            int dir = rand.nextInt(4);
//                            int dir = scanner.nextInt();
                            client.sendPktPlay(command, dir);
                            client.setID_receive(0);
//                            render();
//                        client.readDataFromServer();

                        }
                        if (command == 2) {
                            System.out.print(" nhập toạ độ bắn: ");
//                            xFire = rand.nextInt(10)+10;
//                            yFire = rand.nextInt(10)+10;
//                            int xFire = -1;
//                            int yFire = 0;
                            xFire--;
                            if(xFire<15){
                                xFire=19;
                                yFire--;
                                if (yFire<15){
                                    yFire=19;
                                    xFire=19;
                                }
                            }
                            System.out.println("--------"+xFire +" --- "+yFire);
                            client.sendPktPlay(command, xFire, yFire);
                            client.setID_receive(0);
//                            render();
//                        client.readDataFromServer();
                        }


                    }
                }

            }
        });
    }

    public String getRes() {
        String s = "";
        switch (client.result_turn) {
            case -1:
                s += "none";
                break;
            case 0:
                s += "miss";
                break;
            case 1:
                s += "good";
                break;
            default:
                s += "continue";
                break;
        }
        return s;
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