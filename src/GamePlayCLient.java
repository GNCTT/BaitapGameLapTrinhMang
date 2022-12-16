import Start.Game;
import Start.GameOther;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

//    private Client client;

    private int clientID;

    private int status;

    private int width_map;
    private int height_map;
    Random rand = new Random();

    Executor threadPool = Executors.newFixedThreadPool(5);


    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Hello");
        width_map = 20;
        height_map = 20;
        status = 0;
        x_set_plane = 0;
        y_set_plane = 0;
        canvas = new Canvas(WIDTH_DEFAULT * width_map * 2 + 200, HEIGHT_DEFAULT * height_map);
        gc = canvas.getGraphicsContext2D();
        Group root = new Group();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        game = new Game(width_map, height_map);
        gameOther = new GameOther(width_map, height_map);
//        client = new Client("127.0.0.1", 8080, game, gameOther);
//        arr_trap = client.getArr_trap();
        int NUM_TRAP = 15;
        Random random = new Random();
        arr_trap = new int[NUM_TRAP][2];
        for (int i = 0; i < NUM_TRAP; i++) {
            arr_trap[i][0] = random.nextInt(15);
            arr_trap[i][1] = random.nextInt(15);
        }
        game.addTrap(arr_trap);
        gameOther.add_trap(arr_trap);
//        status = client.getStatus();
        System.out.println("jjda " + width_map + " " + height_map);

//        clientID = client.getClientID();

//        game.setPlane(0, 0);

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                x_set_plane = (int)event.getSceneX() / WIDTH_DEFAULT;
                y_set_plane = (int)event.getSceneY() / HEIGHT_DEFAULT;
                System.out.println("location: " + x_set_plane + " " + y_set_plane);
                if (status == 0 && game.checkLocationPlane(x_set_plane, y_set_plane)) {
                    status = 1;
                    int dir = 1;
//                    client.sendSetPlanePkt(dir, x_set_plane, y_set_plane);
//                    client.readDataFromServer();
//                    System.out.println("ID: " + clientID + " " + client.getClientID());
                    game.setPlane(x_set_plane, y_set_plane);
//                    if (clientID == client.getID_receive()) {
//
//                    }
                }

                if (status == 1) {
                    game.beShoot(x_set_plane, y_set_plane);
                }
                game.renderGame(gc, 0, 0);

            }
        });
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if (key.getCode() == KeyCode.RIGHT) {
//                client.sendPktPlay(1, 0, 0);
//                client.readDataFromServer();
                System.out.println("hello");
                game.move(2);
//                if (client.isCanMove()) {
//                    game.move(2);
//                    client.setCanMove(false);
//                    client.setStatus(2);
//                }

            }
            if (key.getCode() == KeyCode.LEFT) {
//                    client.readDataFromServer();
//                client.sendPktPlay(1, 0, 0, 0);
//                client.readDataFromServer();
//                System.out.println("check: " + client.isCanMove());
                game.move(0);
//                if (client.isCanMove()) {
//                    game.move(0);
//                    client.setCanMove(false);
//                    client.setStatus(2);
//                }
            }
            if (key.getCode() == KeyCode.UP) {
//                client.sendPktPlay(1, 1, 0, 0);
//                client.readDataFromServer();
//                System.out.println("check: " + client.isCanMove());
                game.move(1);
//                if (client.isCanMove()) {
//                    game.move(1);
//                    client.setCanMove(false);
//                    client.setStatus(2);
//                }
            }
            if (key.getCode() == KeyCode.DOWN) {
//                client.sendPktPlay(1, 3, 0, 0);
//                client.readDataFromServer();
//                System.out.println("check: " + client.isCanMove());
                game.move(3);
//                if (client.isCanMove()) {
//                    game.move(3);
//                    client.setCanMove(false);
//                    client.setStatus(2);
//                }
            }
            game.renderGame(gc, 0, 0);

        });

        map = game.getMap();
        mapOther = gameOther.getMap();
        primaryStage.setScene(scene);
        primaryStage.show();

//        AnimationTimer timer = new AnimationTimer() {
//            @Override
//            public void handle(long l) {
////                client.readDataFromServer();
//                render();
//                update();
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        };
//
//        timer.start();


    }



    public static void main(String args[]){
        launch(args);
    }

    public void render() {
        System.out.println("???");
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (status == 0) {
            gc.fillText("Set Your Plane", map.length * WIDTH_DEFAULT + 10, 50);
        }
        if (status == 1) {
            gc.fillText("Move Your Plane", map.length * WIDTH_DEFAULT + 10, 50);
        }
        gc.fillText("Your ID: " + String.valueOf(clientID), map.length * WIDTH_DEFAULT + 10, 100);
//        gc.fillText("Your ID: " + String.valueOf(client.getID_receive()), map.length * WIDTH_DEFAULT + 10, 200);
        gc.setFill(Color.BLUE);
        renderMapPlayer();
        renderMapOther();
    }

    public void renderMapPlayer() {
        game.renderGame(gc, 0, 0);
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
//        status = client.getStatus();
//        System.out.println("status: " + status);
//        Scanner sc = new Scanner(System.in);
//        int t = sc.nextInt();
//        System.out.println(t);
    }
}
