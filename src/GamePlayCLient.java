import Start.Game;
import Start.GameOther;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Scanner;

public class GamePlayCLient extends Application {
    private GraphicsContext gc;
    private Canvas canvas;
    private int x;
    private int [][] arr = {{0,0,2,0,0},       //          2
            {0,2,0,0,2},       //         2  2
            {2,2,2,2,2},       //        22222      *Tam may bay o toa do [2;2]*
            {0,2,0,0,2},       //         2  2
            {0,0,2,0,0}};;

    public static final int WIDTH_DEFAULT = 30;
    public static final int HEIGHT_DEFAULT = 30;
    private int [][] map;
    private int [][] mapOther;
    private int [][] arr_trap;

    private Game game;
    private GameOther gameOther;

    private int x_set_plane;
    private int y_set_plane;

    private Client client;

    private int clientID;

    private int status;

    private int width_map;
    private int height_map;


    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Hello");

        status = 0;
        x_set_plane = 0;
        y_set_plane = 0;

        client = new Client("127.0.0.1", 8080);
        arr_trap = client.getArr_trap();
        width_map = client.getWidth_map();
        height_map = client.getHeight_map();
        System.out.println("jjda " + width_map + " " + height_map);
        game = new Game(width_map, height_map);
        gameOther = new GameOther(width_map, height_map);
        canvas = new Canvas(WIDTH_DEFAULT * width_map * 2 + 200, HEIGHT_DEFAULT * height_map);
        gc = canvas.getGraphicsContext2D();
        Group root = new Group();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        clientID = client.getClientID();
        game.addTrap(arr_trap);
        gameOther.add_trap(arr_trap);
        game.setPlane(0, 0);

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                x_set_plane = (int)event.getSceneX() / WIDTH_DEFAULT;
                y_set_plane = (int)event.getSceneY() / HEIGHT_DEFAULT;
                System.out.println("location: " + x_set_plane + " " + y_set_plane);
                if (status == 0 && game.checkLocationPlane(x_set_plane, y_set_plane)) {
                    status = 1;
                    int dir = 1;
                    client.sendSetPlanePkt(dir, x_set_plane, y_set_plane);
                    client.readDataFromServer();
                    System.out.println("ID: " + clientID + " " + client.getClientID());
                    if (clientID == client.getID_receive()) {
                        game.setPlane(x_set_plane, y_set_plane);
                    }
                }

            }
        });
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if (status == 1) {

                if (key.getCode() == KeyCode.RIGHT && clientID == client.getID_receive()) {
                    client.sendPktPlay(1, 2, 0, 0);
                    client.readDataFromServer();
                    System.out.println("hello");
                    if (client.isCanMove()) {
                        game.move(2);
                        client.setCanMove(false);
                    }

                }
                if (key.getCode() == KeyCode.LEFT) {
//                    client.readDataFromServer();
                    client.sendPktPlay(1, 0, 0, 0);
                    client.readDataFromServer();
                    System.out.println("check: " + client.isCanMove());
                    if (client.isCanMove()) {
                        game.move(0);
                        client.setCanMove(false);
                    }
                }
                if (key.getCode() == KeyCode.UP) {
                    client.sendPktPlay(1, 1, 0, 0);
                    client.readDataFromServer();
                    System.out.println("check: " + client.isCanMove());
                    if (client.isCanMove()) {
                        game.move(1);
                        client.setCanMove(false);
                    }
                }
                if (key.getCode() == KeyCode.DOWN) {
                    client.sendPktPlay(1, 3, 0, 0);
                    client.readDataFromServer();
                    System.out.println("check: " + client.isCanMove());
                    if (client.isCanMove()) {
                        game.move(3);
                        client.setCanMove(false);
                    }
                }
            }
        });

        map = game.getMap();
        mapOther = gameOther.getMap();
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
//                client.readDataFromServer();
                render();
                update();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        timer.start();

    }



    public static void main(String args[]){
        launch(args);
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (status == 0) {
            gc.fillText("Set Your Plane", map.length * WIDTH_DEFAULT + 10, 50);
        }
        if (status == 1) {
            gc.fillText("Move Your Plane", map.length * WIDTH_DEFAULT + 10, 50);
        }
        gc.fillText("Your ID: " + String.valueOf(clientID), map.length * WIDTH_DEFAULT + 10, 100);
        gc.setFill(Color.BLUE);
        renderMapPlayer();
        renderMapOther();


    }

    public void renderMapPlayer() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == 0) {
//                    gc.setFill(Color.WHITE);
//                    gc.fillRect(j * WIDTH_DEFAULT, i * HEIGHT_DEFAULT, WIDTH_DEFAULT, HEIGHT_DEFAULT);
                    gc.setFill(Color.GREEN);
                    gc.fillRect(j * WIDTH_DEFAULT + 1, i * HEIGHT_DEFAULT + 1, WIDTH_DEFAULT - 1, HEIGHT_DEFAULT - 1);
                } else {
                    if (map[i][j] == 20) {
                        gc.setFill(Color.RED);
                        gc.fillRect(j * WIDTH_DEFAULT + 1, i * HEIGHT_DEFAULT + 1, WIDTH_DEFAULT - 1, HEIGHT_DEFAULT - 1);
                    } else {
//                    gc.setFill(Color.WHITE);
//                    gc.fillRect(j * WIDTH_DEFAULT, i * HEIGHT_DEFAULT, WIDTH_DEFAULT, HEIGHT_DEFAULT);
                        gc.setFill(Color.BLACK);
                        gc.fillRect(j * WIDTH_DEFAULT + 1, i * HEIGHT_DEFAULT + 1, WIDTH_DEFAULT - 1, HEIGHT_DEFAULT - 1);
                    }
                }
            }
        }
    }

    public void renderMapOther() {
        int startX = map.length * WIDTH_DEFAULT + 200;
        for (int i = 0; i < mapOther.length; i++) {
            for (int j = 0; j < mapOther.length; j++) {
                if (mapOther[i][j] == 0) {
                    gc.setFill(Color.GREEN);
                    gc.fillRect(startX + j * WIDTH_DEFAULT + 1, i * HEIGHT_DEFAULT + 1, WIDTH_DEFAULT - 1, HEIGHT_DEFAULT - 1);
                } else {
                    gc.setFill(Color.RED);
                    gc.fillRect(startX + j * WIDTH_DEFAULT + 1, i * HEIGHT_DEFAULT + 1, WIDTH_DEFAULT - 1, HEIGHT_DEFAULT - 1);
                }
            }
        }
    }

    public void update() {

    }
}
