import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ServerTest {

    public ServerTest(String host, int port) {
        int match_id = 496;
        JSONObject jsonObject1 = makeJson_match(match_id, 2, 100, 0);
//            out_server_game.write(jsonObject1.toString().getBytes());


        try {
//            out_server_ws = new WebsocketClientEndpoint(new URI("ws://104.194.240.16/ws/channels/"));

            // add listener


            System.out.println("befor_send");

            String msg = match_update(match_id, 100, 0, 2);
            WebsocketClientEndpoint out_server_ws = new WebsocketClientEndpoint(new URI("ws://104.194.240.16/ws/channels/"));
            out_server_ws.sendMessage(msg + " ");
            System.out.println("Hello " + msg);
            out_server_ws.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message + " ");
                }
            });

            // wait 5 seconds for messages from websocket
            System.out.println("hh");
            Thread.sleep(5000);


        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }


    }



    public JSONObject makeJson_match(int match_id, int status, int score_1, int score_2) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", 2);
        jsonObject.put("match", match_id);
        jsonObject.put("status", 2);
        jsonObject.put("id1", score_1);
        jsonObject.put("id2", score_2);
        return jsonObject;
    }

    public JSONObject makeJson_match(int result, int match_id, int statuss, int score_1, int score_2) {
        JSONObject jsonObjectx = new JSONObject();
        jsonObjectx.put("result", result);
        jsonObjectx.put("match", match_id);
        jsonObjectx.put("status", statuss);
        jsonObjectx.put("id1", score_1);
        jsonObjectx.put("id2", score_2);
        return jsonObjectx;
    }

    public static String match_update(int matchID, int score1, int score2, int status) {
        Map res = new HashMap();
        res.put("result", 2);
        res.put("match", matchID);
        res.put("status", status);
        res.put("id1", score1);
        res.put("id2", score2);
        res.put("status", status);
        String jsonText = JSONObject.valueToString(res);
        return jsonText;
    }

    static String byteToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }



    public static String[] removeAStrByIndex(int index, String[] arrTest) {
        String [] arrTest2 = new String[arrTest.length - 1];
        for (int i = 0; i < arrTest.length; i++) {
            if (i < index) {
                arrTest2[i] = arrTest[i];
            }
            if (i > index) {
                arrTest2[i - 1] = arrTest[i];
            }
        }
        return arrTest2;
    }



    public static void main(String[] args) {
        ServerTest sv = new ServerTest("104.194.240.16", 8881);
    }
}
