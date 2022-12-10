import Start.Game;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GamePlayServer extends Application {
    private GraphicsContext gc;
    private Canvas canvas;
    public static final int WIDTH_DEFAULT = 30;
    public static final int HEIGHT_DEFAULT = 30;
    private int [][] map_client_1;
    private int [][] map_client_2;
    private int [][] arr_trap;

    private Game gamePlayer_1;
    private Game gamePlayer_2;
    private boolean has_change;

//    private int x_set_plane_1;
//    private int y_set_plane_1;
//
//    private int x_set_plane_2;
//    private int y_set_plane_2;

//    private Client client;

    private Server server;

//    private int clientID;
//
//    private int status;

    private int width_map;
    private int height_map;

//    private int time_check;

//
//    private int MAX_TIME_CHECK = 100;

    Executor threadPool = Executors.newFixedThreadPool(5);
    Executor threadPool1 = Executors.newFixedThreadPool(5);


    @Override
    public void start(Stage primaryStage) throws Exception {

        width_map = 20;
        height_map = 20;
        has_change = true;

        canvas = new Canvas(WIDTH_DEFAULT * width_map * 2 + 200, HEIGHT_DEFAULT * height_map);
        gc = canvas.getGraphicsContext2D();
        Group root = new Group();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        gamePlayer_1 = new Game(width_map, height_map);
        gamePlayer_2 = new Game(width_map, height_map);
        server = new Server(8881, 123, 321, gamePlayer_1, gamePlayer_2);
        arr_trap = server.getArr_trap();
        gamePlayer_1.addTrap(arr_trap);
        gamePlayer_2.addTrap(arr_trap);
        map_client_2 = gamePlayer_2.getMap();
        map_client_1 = gamePlayer_1.getMap();

        threadPool.execute(() -> {
            System.out.println("wth");
            while (true) {
                if (server.has_change) {
                    render();
                    server.has_change = false;
                    System.out.println("result: " + server.getResult_match());
                }
                if (!server.checkReady()) {
                    server.waiting_Hello_packet();
                } else {
                    if (!server.checkSet2Plane()) {
                        server.waiting_Set_Plane();
                    } else {
                        server.waiting_PKT_Play();
                    }

                }
                if (server.checkWin()) {
                    System.out.println("have Rs");
                    server.sendPkt_End();
                    server.sendResult_End();
                } else {
                    server.sendResult();
                }

            }

        });



    }


    public static void main(String args[]){
        launch(args);
    }


    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        //thong bao can thiet
        gc.setFill(Color.BLUE);
        renderMapPlayer_1(0, 0);
        renderMapPlayer_2(gamePlayer_2.getMap().length * WIDTH_DEFAULT + 200, 0);
        switch (server.getStatus()) {
            case 0:
                gc.fillText("waiting set plane", gamePlayer_1.getMap().length * WIDTH_DEFAULT + 20, 100);
                break;
            case 1:
                gc.fillText("waiting client ", gamePlayer_1.getMap().length * WIDTH_DEFAULT + 50, 100);
                break;
        }


    }

    public void renderMapPlayer_1(int start_x, int start_y) {
        gamePlayer_1.renderGame(gc, start_x, start_y);
    }

    public void renderMapPlayer_2(int start_x, int start_y) {
        gamePlayer_2.renderGame(gc, start_x, start_y);
    }

    public void update() {
    }
}
