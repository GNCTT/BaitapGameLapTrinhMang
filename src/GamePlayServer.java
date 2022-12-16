import Start.Game;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.net.URI;
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

    private boolean isOver;
    private int countSendEnd;


    private Server server;




    private int width_map;
    private int height_map;
    private WebsocketClientEndpoint out_server_ws;
    private int match_id;


    Executor threadPool = Executors.newFixedThreadPool(5);
    Executor threadPool1 = Executors.newFixedThreadPool(5);


    @Override
    public void start(Stage primaryStage) throws Exception {

        width_map = 20;
        height_map = 20;
        has_change = true;
        isOver = false;
        countSendEnd = 0;

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
        match_id = server.getmatchId();
        JSONObject jsonObject2 = makeJson_match(2, match_id, 1, 0, 0);
        out_server_ws = new WebsocketClientEndpoint(new URI("ws://104.194.240.16/ws/channels/"));
        out_server_ws.sendMessage(jsonObject2.toString());
        out_server_ws.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
            public void handleMessage(String message) {
                System.out.println(message);
            }
        });
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
                        System.out.println("result_waiting_pkt: " + 1);
                        render();
                    } else {
                        if (server.checkWin() == false) {
                            server.waiting_PKT_Play();
                        } else {
                            isOver = true;
                            break;
                        }

                    }

                }


            }

        });

        threadPool.execute(() -> {
            while (true) {
//                System.out.println("clientId: " + server.clientID);
                if (isOver) {
                    System.out.println("have Rs");
                    server.sendPkt_End();
                    sendResult_End();
                    render();
                    countSendEnd ++;
                    if (countSendEnd == 5) {
                        countSendEnd = 0;
                        break;
                    }
//                    break;

                } else {
                    sendResult();
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

        if (server.getResult_Match() == -1) {
            gc.fillText("Playing", gamePlayer_1.getMap().length * WIDTH_DEFAULT + 20, 200);
        }
        if (server.getResult_Match() == 0) {
            gc.fillText("Draw", gamePlayer_1.getMap().length * WIDTH_DEFAULT + 20, 200);
        }
        if (server.getResult_Match() == 1) {
            gc.fillText("Player 1 Win", gamePlayer_1.getMap().length * WIDTH_DEFAULT + 20, 200);
        }
        if (server.getResult_Match() == 2) {
            gc.fillText("Player 2 Win", gamePlayer_1.getMap().length * WIDTH_DEFAULT + 20, 200);
        }


    }

    public void renderMapPlayer_1(int start_x, int start_y) {
        gamePlayer_1.renderGame(gc, start_x, start_y);
    }

    public void renderMapPlayer_2(int start_x, int start_y) {
        gamePlayer_2.renderGame(gc, start_x, start_y);
    }

    public JSONObject makeJson_start() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", 1);
        jsonObject.put("ip", "0.tcp.ngrok.io");
        jsonObject.put("port", 13410);
        jsonObject.put("path", "/shoot_plane");
        return jsonObject;
    }

    public JSONObject makeJson_match(int result, int match_id, int statuss, int score_1, int score_2) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        jsonObject.put("match", match_id);
        jsonObject.put("status", statuss);
        jsonObject.put("id1", score_1);
        jsonObject.put("id2", score_2);
        return jsonObject;
    }

    public void sendResult() {
        int score_client_1 = 200;
        int score_client_2 = 200;
        JSONObject jsonObject = makeJson_match(2, match_id, 1, 5, 5);
        out_server_ws.sendMessage(jsonObject.toString());
    }

    public void sendResult_End() {
        int score_client_1 = 200 - 400;
        int score_client_2 = 200 - 400;
        JSONObject jsonObject = makeJson_match(2, match_id, 2, score_client_1, score_client_2);
        out_server_ws.sendMessage(jsonObject.toString());
    }

}
