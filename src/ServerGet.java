import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerGet {
    ServerSocket server;
    public ServerGet() {
        try {
            server = new ServerSocket(8881);
            Socket socket = server.accept();
            DataInputStream in_server_game = new DataInputStream(socket.getInputStream());
            DataOutputStream out_server_game = new DataOutputStream(socket.getOutputStream());
            byte[] pkt_from_server = new byte[5000];
            in_server_game.read(pkt_from_server);
            byte [] type_byte;
            type_byte = Server.getBytebyIndex(pkt_from_server, 0, 63);
            System.out.println("???");
            String res_from_server_game = Server.byteToString(type_byte);
            int match_id = getMatchId(res_from_server_game);
            System.out.println("matchId: " + match_id);
            System.out.println("data: " + Server.byteToString(type_byte));
            JSONObject jsonObject = makeJson_start();
            out_server_game.write(jsonObject.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public int getMatchId (String data) {
        String[] listData = data.split(" ");
//        System.out.println(listData[3]);
        String match = (String) listData[3].subSequence(0, listData[3].length() - 1);
        return Integer.valueOf(match);
    }

    public JSONObject makeJson_start() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", 1);
        jsonObject.put("ip", "0.tcp.ngrok.io");
        jsonObject.put("port", 13410);
        jsonObject.put("path", "/shoot_plane");
        return jsonObject;
    }
    public static void main(String[] args) {

                    ServerGet serverGet = new ServerGet();
    }
}
