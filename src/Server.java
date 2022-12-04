import Start.Game;

import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

public class Server
{
    private Socket		 socket = null;
    private ArrayList<Socket> listSockets;
    private ServerSocket server = null;
    private ArrayList<DataInputStream> ins;

    private ArrayList<DataOutputStream> outs;
    private DataInputStream in;
    private DataOutputStream out;

    private final int HELLO_PKT_TYPE = 0;
    private final int PKT_CREATE_MAP = 1;
    private final int SET_PLANE_PKT = 2;
    private final int BYE_PKT_TYPE = 3;
    private final int TURN_PKT_TYPE = 4;

    private final int PLAY_PKT_TYPE = 5;

    private final int PKT_RESULT = 6;

    private final int PKT_END = 7;

    private int WIDTH_MAP_SIZE = 20;
    private int HEIGHT_MAP_SIZE = 20;

    private int NUM_TRAP = 15;

    private int arr_trap[][];
    public int clientID;
    private int clientID_1;
    private int clientID_2;
    private byte pkt_from_client[];

    private byte dir_byte[];
    private int dir_plane;
    private byte x_location_byte[];
    private int x_location;
    private byte y_location_byte[];
    private int y_location;
    private byte pkt_send[];
    private byte type_byte[];
    private byte len_byte[];

    private int sizeOfPacket;
    private byte data_byte[];

    private byte ID_byte[];
    private byte width_byte[];
    private byte height_byte[];

    private byte num_trap_byte[];

    private byte x_location_trap[];
    private byte y_location_trap[];

    private byte command_byte[];

    private int command;

    private byte x_fire_byte[];
    private byte y_fire_byte[];

    private int x_fire;
    private int y_fire;

    private byte result_byte[];
    private int result;

    int index_receive;

    private boolean turn_check;
    private boolean set_plan_ok;
    private boolean is_over;

    private int count_time_client_1;
    private int count_time_client_2;
    Game gameClient_1;
    Game gameClient_2;
    public int status;

    private int client_Check;

    private int count_hello_pkt;

    private int count_set_plane;
    public boolean has_change;

    public boolean send_turn;

    private int clientID_Turn;

    private boolean client1_send;
    private boolean client2_send;


    private final static int WIDTH_MAP = 20;
    private final static int HEIGHT_MAP = 20;

    private int client_first;

    private int result_match;

    static int[][] mayBay ={{0,0,2,0,0},       //          2
            {0,2,0,0,2},       //         2  2
            {2,2,2,2,2},       //        22222      *Tam may bay o toa do [2;2]*
            {0,2,0,0,2},       //         2  2
            {0,0,2,0,0}};

    public Server(int port, int clientID_1, int clientID_2, Game gameClient_1, Game gameClient_2) {
        try {
            server = new ServerSocket(port);
            client_first = clientID_2;
            this.clientID_1 = clientID_1;
            this.clientID_2 = clientID_2;
            this.gameClient_1 = gameClient_1;
            this.gameClient_2 = gameClient_2;
            count_time_client_1 = 1;
            count_time_client_2 = 1;
            Random random = new Random();
            result_match = -1;
            has_change = true;
            send_turn = false;
            client1_send = false;
            client2_send = false;
            arr_trap = new int[NUM_TRAP][2];
            for (int i = 0; i < NUM_TRAP; i++) {
                arr_trap[i][0] = random.nextInt(15);
                arr_trap[i][1] = random.nextInt(15);
            }
            int countClient = 0;
            count_hello_pkt = 0;
            count_set_plane = 0;
            status = 0;
            ins = new ArrayList<>();
            outs = new ArrayList<>();
            clientID_Turn = clientID_1;
            while (countClient != 2) {
                socket = server.accept();
                in = new DataInputStream(
                        socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                ins.add(in);
                outs.add(out);
                countClient += 1;
                System.out.println("has" + countClient + "connect");
            }
            System.out.println("Client accepted");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public int getStatus() {
        return status;
    }

    public void waiting_Hello_packet() {
//        while ()
        while (count_hello_pkt != 2) {
            for (int i = 0; i < ins.size(); i++) {
                in = ins.get(i);
                out = outs.get(i);
                if (i == 0) {
                    clientID = clientID_1;
                } else {
                    clientID = clientID_2;
                }
                pkt_from_client = new byte[5000];
                try {
                    in.read(pkt_from_client);
                    if (pkt_from_client == null) {
                        System.out.println("nothing to receive");
                    } else {
                        count_hello_pkt ++;
                        type_byte = getBytebyIndex(pkt_from_client, 0, 4);
                        len_byte = getBytebyIndex(pkt_from_client, 4, 8);
                        int len = byte_int(len_byte);
                        data_byte = getBytebyIndex(pkt_from_client, 8, 8 + len);
                        String msv = byteToString(data_byte);
                        System.out.println(msv);
                        int size_pkt = 4 * (4 + NUM_TRAP * 2);
                        ByteBuffer before_send = ByteBuffer.allocate(16 + size_pkt);
                        before_send.put(inttobyte(0));
                        before_send.put(inttobyte(0));
                        type_byte = inttobyte(PKT_CREATE_MAP);
                        len_byte = inttobyte(size_pkt);
                        ID_byte = inttobyte(clientID);
                        width_byte = inttobyte(WIDTH_MAP);
                        height_byte = inttobyte(HEIGHT_MAP);
                        num_trap_byte = inttobyte(NUM_TRAP);
                        before_send.put(type_byte);
                        before_send.put(len_byte);
                        before_send.put(ID_byte);
                        before_send.put(width_byte);
                        before_send.put(height_byte);
                        before_send.put(num_trap_byte);
                        for (int index = 0; index < NUM_TRAP; index++) {
                            x_location_trap = inttobyte(arr_trap[index][0]);
                            y_location_trap = inttobyte(arr_trap[index][1]);
                            before_send.put(x_location_trap);
                            before_send.put(y_location_trap);
                        }
                        out.write(before_send.array());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public void waiting_Set_Plane() {
                in = ins.get(count_set_plane);
                out = outs.get(count_set_plane);
                pkt_from_client = new byte[5000];
                if (count_set_plane == 0) {
                    clientID = clientID_1;
                } else {
                    clientID = clientID_2;
                }
                try {
                    in.read(pkt_from_client);
                    if (pkt_from_client != null) {
                        type_byte = getBytebyIndex(pkt_from_client, 0, 4);
                        len_byte = getBytebyIndex(pkt_from_client, 0, 8);
                        int type = byte_int(type_byte);
                        System.out.println("type " + type);
                        if (type == 2) {
                            ID_byte = getBytebyIndex(pkt_from_client, 8, 12);
                            int id_client = byte_int(ID_byte);
                            if (id_client == clientID) {
                                dir_byte = getBytebyIndex(pkt_from_client, 12, 16);
                                x_location_byte = getBytebyIndex(pkt_from_client, 16, 20);
                                y_location_byte = getBytebyIndex(pkt_from_client, 20, 24);
                                dir_plane = byte_int(dir_byte);
                                System.out.println("dir: " + dir_plane);
                                x_location = byte_int(x_location_byte);
                                y_location = byte_int(y_location_byte);
                                boolean checkSetPlane = false;
                                if (clientID == clientID_1 && gameClient_1.checkLocationPlane(x_location, y_location)) {
                                    count_set_plane++;
                                    gameClient_1.setPlane(dir_plane, x_location, y_location);
                                    has_change = true;
                                    checkSetPlane = true;
                                }
                                if (clientID == clientID_2 && gameClient_2.checkLocationPlane(x_location, y_location)) {
                                    count_set_plane++;
                                    gameClient_2.setPlane(dir_plane, x_location, y_location);
                                    has_change = true;
                                    checkSetPlane = true;
                                }
                                if (checkSetPlane) {
                                    ByteBuffer before_send = ByteBuffer.allocate(12);
                                    type_byte = inttobyte(TURN_PKT_TYPE);
                                    len_byte = inttobyte(4);
                                    ID_byte = inttobyte(clientID_Turn);
                                    System.out.println("---send_to" + clientID);
                                    before_send.put(type_byte);
                                    before_send.put(len_byte);
                                    before_send.put(ID_byte);
                                    out.write(before_send.array());

                                } else {
                                    ByteBuffer before_send = ByteBuffer.allocate(12);
                                    type_byte = inttobyte(BYE_PKT_TYPE);
                                    len_byte = inttobyte(4);
                                    data_byte = inttobyte(4);
                                    before_send.put(type_byte);
                                    before_send.put(len_byte);
                                    before_send.put(data_byte);
                                    out.write(before_send.array());
                                }

                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("receive_nothing");
                    throw new RuntimeException(e);
                }

    }

    public void waiting_set_plane() {

    }

    public void waiting_PKT_Play() {
        if (client_first == clientID_2) {
            in = ins.get(0);
            out = outs.get(0);
            client_first = clientID_1;
        } else {
            in = ins.get(1);
            out = outs.get(1);
            client_first = clientID_2;
        }
        pkt_from_client = new byte[5000];
        try {
            in.read(pkt_from_client);
            if (pkt_from_client != null) {
                System.out.println("have pkt");
                type_byte = getBytebyIndex(pkt_from_client, 0, 4);
                len_byte = getBytebyIndex(pkt_from_client, 4, 8);
                ID_byte = getBytebyIndex(pkt_from_client, 8, 12);
                int id_receive = byte_int(ID_byte);
                int type = byte_int(type_byte);
                System.out.println("type server: " + type);
                int res = 0;
                if (type == PLAY_PKT_TYPE) {
                        command_byte = getBytebyIndex(pkt_from_client, 12, 16);
                        command = byte_int(command_byte);
                        send_turn = true;
                        has_change = true;
                        if (command == 1) {
                            dir_byte = getBytebyIndex(pkt_from_client, 16, 20);
                            dir_plane = byte_int(dir_byte);
                            System.out.println("dir: " + dir_plane);
                            if (id_receive == clientID_1) {
                                count_time_client_1 --;
                                if (gameClient_1.checkMove(dir_plane)) {
                                    res = 3;
                                    gameClient_1.move(dir_plane);
                                }
                            } else {
                                count_time_client_2 --;
                                if (gameClient_2.checkMove(dir_plane)) {
                                    res = 3;
                                    gameClient_2.move(dir_plane);
                                }
                            }
                            ByteBuffer before_send = ByteBuffer.allocate(16);
                            type_byte = inttobyte(PKT_RESULT);
                            len_byte = inttobyte(4);
                            ID_byte = inttobyte(client_first);
                            result_byte = inttobyte(res);
                            System.out.println("---send_to" + client_first);
                            before_send.put(type_byte);
                            before_send.put(len_byte);
                            before_send.put(ID_byte);
                            before_send.put(result_byte);
                            out.write(before_send.array());
                            if (id_receive == clientID_1) {
                                clientID = clientID_2;
                                out = outs.get(1);
                            } else {
                                clientID = clientID_1;
                                out = outs.get(0);
                            }
                            ByteBuffer before_send2 = ByteBuffer.allocate(12);
                            type_byte = inttobyte(TURN_PKT_TYPE);
                            len_byte = inttobyte(4);
                            ID_byte = inttobyte(clientID);
                            result_byte = inttobyte(res);
                            System.out.println("---send_to" + clientID);
                            before_send2.put(type_byte);
                            before_send2.put(len_byte);
                            before_send2.put(ID_byte);
                            out.write(before_send2.array());

                        }

                        if (command == 2) {
                            x_location_byte = getBytebyIndex(pkt_from_client, 16, 20);
                            y_location_byte = getBytebyIndex(pkt_from_client, 20, 24);
                            x_location = byte_int(x_location_byte);
                            y_location = byte_int(y_location_byte);
                            System.out.println("receive location: " + x_location + " " + y_location);
                            res = 0;
                            if (id_receive == clientID_1) {
                                if (gameClient_2.checkShoot(x_location, y_location)) {
                                    res = 1;
                                    gameClient_2.beShoot(x_location, y_location);
                                    has_change = true;
                                }
                                gameClient_2.beShoot(x_location, y_location);
                            }
                            if (id_receive == clientID_2) {
                                if (gameClient_1.checkShoot(x_location, y_location)) {
                                    res = 1;
                                }
                                gameClient_1.beShoot(x_location, y_location);
                            }

                            ByteBuffer before_send = ByteBuffer.allocate(16);
                            type_byte = inttobyte(PKT_RESULT);
                            len_byte = inttobyte(4);
                            ID_byte = inttobyte(id_receive);
                            result_byte = inttobyte(res);
                            System.out.println("---send_to" + client_first);
                            before_send.put(type_byte);
                            before_send.put(len_byte);
                            before_send.put(ID_byte);
                            before_send.put(result_byte);
                            out.write(before_send.array());
                            if (id_receive == clientID_1) {
                                clientID = clientID_2;
                                out = outs.get(1);
                            } else {
                                clientID = clientID_1;
                                out = outs.get(0);
                            }
                            if (res == 1) {
                                ByteBuffer before_send2 = ByteBuffer.allocate(20);
                                type_byte = inttobyte(8);
                                len_byte = inttobyte(12);
                                ID_byte = inttobyte(clientID);
                                x_location_byte = inttobyte(x_location);
                                y_location_byte = inttobyte(y_location);
                                System.out.println("---send_to" + clientID);
                                before_send2.put(type_byte);
                                before_send2.put(len_byte);
                                before_send2.put(ID_byte);
                                before_send2.put(x_location_byte);
                                before_send2.put(y_location_byte);
                                out.write(before_send2.array());
                            } else {
                                ByteBuffer before_send2 = ByteBuffer.allocate(12);
                                type_byte = inttobyte(TURN_PKT_TYPE);
                                len_byte = inttobyte(4);
                                ID_byte = inttobyte(clientID);
                                System.out.println("---send_to" + clientID);
                                before_send2.put(type_byte);
                                before_send2.put(len_byte);
                                before_send2.put(ID_byte);
                                out.write(before_send2.array());
                            }


                        }

                }
            }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


    }

    public void checkWinMatch() {
        System.out.println(count_time_client_1 + " " + count_time_client_2 + " count_time");
        if (count_time_client_1 == 0 && count_time_client_2 == 0) {
            if (gameClient_1.checkLose() && gameClient_2.checkLose()) {
                result_match = 0;
            } else {
                if (gameClient_1.checkLose()) {
                    result_match = 2;
                } else {
                    if (gameClient_2.checkLose()) {
                        result_match = 1;
                    } else result_match = 0;
                }
            }
        } else {
            if (gameClient_1.checkLose()) {
                result_match = 2;
            }
            if (gameClient_2.checkLose()) {
                result_match = 1;
            }
        }
    }
    public boolean checkWin() {
        checkWinMatch();
        if (result_match == -1) {
            return false;
        }
        return true;
    }

    public int getResult_match() {
        return result_match;
    }

    public void sendPkt_End() {
        int result_1 = -1;
        int result_2 = -1;
        if (result_match == 0) {
            result_1 = 1;
            result_2 = 1;
        }
        if (result_match == 1) {
            result_1 = 2;
            result_2 = 0;
        }
        if (result_match == 2) {
            result_1 = 0;
            result_2 = 2;
        }
        out = outs.get(0);

        try {
            ByteBuffer before_send2 = ByteBuffer.allocate(16);
            type_byte = inttobyte(PKT_END);
            len_byte = inttobyte(8);
            ID_byte = inttobyte(clientID_1);
            result_byte = inttobyte(result_1);
            System.out.println("---send_to" + clientID_1);
            before_send2.put(type_byte);
            before_send2.put(len_byte);
            before_send2.put(ID_byte);
            before_send2.put(result_byte);
            out.write(before_send2.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        out = outs.get(1);
        try {
            ByteBuffer before_send2 = ByteBuffer.allocate(16);
            type_byte = inttobyte(PKT_END);
            len_byte = inttobyte(8);
            ID_byte = inttobyte(clientID_2);
            result_byte = inttobyte(result_2);
            System.out.println("---send_to" + clientID_2);
            before_send2.put(type_byte);
            before_send2.put(len_byte);
            before_send2.put(ID_byte);
            before_send2.put(result_byte);
            out.write(before_send2.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void senPktTurn() {

    }


    public boolean checkSet2Plane() {
        if (count_set_plane == 2) {
            return true;
        }
        return false;
    }

    public boolean checkReady() {
        if (count_hello_pkt == 2) {
            return true;
        }
        return false;
    }

    public void waiting_Other_Pkt() {

    }

    public Server(int port) {
        try {
            gameClient_1 = new Game(WIDTH_MAP, HEIGHT_MAP);
            gameClient_2 = new Game(WIDTH_MAP, HEIGHT_MAP);
            client_Check = 0;
            listSockets = new ArrayList<>();
            server = new ServerSocket(port);
            ins = new ArrayList<>();
            outs = new ArrayList<>();
            arr_trap = new int[NUM_TRAP][2];
            clientID = 123;
            clientID_1 = 123;
            clientID_2 = 321;
            turn_check = true;
            set_plan_ok = false;
            is_over = false;

            Random random = new Random();
            for (int i = 0; i < NUM_TRAP; i++) {
                arr_trap[i][0] = random.nextInt(15);
                arr_trap[i][1] = random.nextInt(15);
            }
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");
            index_receive = 0;
            int countClient = 0;
            while (countClient != 2) {
                socket = server.accept();
                in = new DataInputStream(
                        socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                ins.add(in);
                outs.add(out);
                countClient += 1;

            }
            System.out.println("Client accepted");

            //receive_PKT_HELLO_AND_Send_MAP
            int count_Send_Hello = 0;
            for (int i = 0; i < ins.size(); i++) {
                in = ins.get(i);
                out = outs.get(i);
                if (i == 0) {
                    clientID = clientID_1;
                } else {
                    clientID = clientID_2;
                }
                pkt_from_client = new byte[5000];
                in.read(pkt_from_client);
                type_byte = getBytebyIndex(pkt_from_client, 0, 4);
                len_byte = getBytebyIndex(pkt_from_client, 4, 8);
                int len = byte_int(len_byte);
                data_byte = getBytebyIndex(pkt_from_client, 8, 8 + len);
                String msv = byteToString(data_byte);
                System.out.println(msv);
                if (i == 0) {
                    gameClient_1.addTrap(arr_trap);
                    gameClient_1.render();
                } else {
                    gameClient_2.addTrap(arr_trap);
                    gameClient_2.render();
                }
                int size_pkt = 4 * (4 + NUM_TRAP * 2);
                ByteBuffer before_send = ByteBuffer.allocate(16 + size_pkt);
                before_send.put(inttobyte(0));
                before_send.put(inttobyte(0));
                type_byte = inttobyte(PKT_CREATE_MAP);
                len_byte = inttobyte(size_pkt);
                ID_byte = inttobyte(clientID);
                width_byte = inttobyte(WIDTH_MAP);
                height_byte = inttobyte(HEIGHT_MAP);
                num_trap_byte = inttobyte(NUM_TRAP);
                before_send.put(type_byte);
                before_send.put(len_byte);
                before_send.put(ID_byte);
                before_send.put(width_byte);
                before_send.put(height_byte);
                before_send.put(num_trap_byte);
                for (int index = 0; index < NUM_TRAP; index++) {
                    x_location_trap = inttobyte(arr_trap[index][0]);
                    y_location_trap = inttobyte(arr_trap[index][1]);
                    before_send.put(x_location_trap);
                    before_send.put(y_location_trap);
                }
                out.write(before_send.array());
            }
            clientID = clientID_1;
            out = outs.get(0);
            in = ins.get(0);
            int count_set_plane = 0;

            while (true) {
//                for (int index = 0; index < ins.size(); index++) {
//                    in = ins.get(index);
//                    out = outs.get(index);
                try {
                        pkt_from_client = new byte[5000];
                        in.read(pkt_from_client);
                        type_byte = getBytebyIndex(pkt_from_client, 0, 4);
                        len_byte = getBytebyIndex(pkt_from_client, 4, 8);
                        int len = byte_int(len_byte);
                        data_byte = getBytebyIndex(pkt_from_client, 8, 8 + len);
                        int type = byte_int(type_byte);
                        System.out.println();
                        if (type == SET_PLANE_PKT) {
                            //get result
                            ID_byte = getBytebyIndex(data_byte, 0, 4);
                            dir_byte = getBytebyIndex(data_byte, 4, 8);
                            x_location_byte = getBytebyIndex(data_byte, 8, 12);
                            y_location_byte = getBytebyIndex(data_byte, 12, 16);
                            clientID = byte_int(ID_byte);
                            //xac dinh may bay nao
                            dir_plane = byte_int(dir_byte);
                            x_location = byte_int(x_location_byte);
                            y_location = byte_int(y_location_byte);
                            //check vi tri dat may bay va check thong tin nguoi gui.
                            System.out.println("type: " + type + " result: " + byte_int(ID_byte) + " index_receive: " + index_receive);
                            System.out.println(x_location + "   ----   " + y_location);
                            boolean checkSetPlane = false;
                            if (clientID == clientID_1 && gameClient_1.checkLocationPlane(x_location, y_location)) {
                                count_set_plane ++;
                                checkSetPlane = true;
                            }
                            if (clientID == clientID_2 && gameClient_2.checkLocationPlane(x_location, y_location)) {
                                count_set_plane ++;
                                checkSetPlane = true;
                            }
                            // checkplane method

                            if (checkSetPlane) {
                                ByteBuffer before_send = ByteBuffer.allocate(12);
                                type_byte = inttobyte(TURN_PKT_TYPE);
                                len_byte = inttobyte(4);
                                ID_byte = inttobyte(clientID);
                                System.out.println("---send_to" + clientID);
                                before_send.put(type_byte);
                                before_send.put(len_byte);
                                before_send.put(ID_byte);
                                out.write(before_send.array());
                            }
                            if (count_set_plane == 2) {
                                checkSetPlane = true;
                            }
                            else {
                                ByteBuffer before_send = ByteBuffer.allocate(12);
                                type_byte = inttobyte(BYE_PKT_TYPE);
                                len_byte = inttobyte(4);
                                data_byte = inttobyte(4);
                                before_send.put(type_byte);
                                before_send.put(len_byte);
                                before_send.put(data_byte);
                                out.write(before_send.array());
                                break;
                            }

                        }

                        if (type == PLAY_PKT_TYPE) {
                            ID_byte = getBytebyIndex(pkt_from_client, 8, 12);
                            clientID = byte_int(ID_byte);
                            System.out.println("pkt_play " + clientID);
                            command_byte = getBytebyIndex(pkt_from_client, 12, 16);
                            command = byte_int(command_byte);
                            if (command == 1) {
                                dir_byte = getBytebyIndex(pkt_from_client, 16, 20);
                                dir_plane = byte_int(dir_byte);

                                //chang_by_ID
                            } else {
                                x_fire_byte = getBytebyIndex(pkt_from_client, 16, 20);
                                y_fire_byte = getBytebyIndex(pkt_from_client, 20, 24);
                                //chang_by_ID

                            }

                            if (!is_over) {
                                ByteBuffer before_send = ByteBuffer.allocate(12);
                                type_byte = inttobyte(TURN_PKT_TYPE);
                                len_byte = inttobyte(4);

                                System.out.println("---send_to" + clientID);
                                before_send.put(type_byte);
                                before_send.put(len_byte);
                                before_send.put(ID_byte);
                                out.write(before_send.array());
                                //check till is_win = true

                            }
                        }

                } catch (IOException i) {
                    System.out.println(i);
                }
                if (clientID == clientID_1) {
                    clientID = clientID_2;
                    out = outs.get(1);
                    in = ins.get(1);
                } else {
                    clientID = clientID_1;
                    out = outs.get(0);
                    in = ins.get(0);
                }
            }
//            }
//            System.out.println("Closing connection");
//            // close connection
//            socket.close();
//            in.close();
//            out.close();
        }

        catch(IOException i)
        {
            System.out.println(i);
        }
    }


    public static byte[] getBytebyIndex(byte[] bytes, int index1, int index2) {
        byte[] outarr = new byte[index2 - index1];
        for (int i = 0; i < index2 - index1; i++) {
            outarr[i] = bytes[i + index1];
        }
        return outarr;
    }
    static byte[] inttobyte(int i) {
        ByteBuffer b = ByteBuffer.allocate(4);
        b.order(ByteOrder.LITTLE_ENDIAN);
        b.putInt(i);
        return b.array();
    }

    static int byte_int(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    static byte[] Stringtobyte(String s) {
        byte b[] = s.getBytes();
        return b;
    }

    static String byteToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    static byte[] make_pkt_send(byte[] type, byte[] len, byte[]data) {
        int size = byte_int(len) + 8;
        ByteBuffer pkt_sent = ByteBuffer.allocate(size);
        pkt_sent.put(type);
        pkt_sent.put(len);
        pkt_sent.put(data);
        return pkt_sent.array();
    }

    public static int getRandIndex(int length) {
        Random rand = new Random();
        return rand.nextInt(length);
    }

    public int[][] getArr_trap() {
        return arr_trap;
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

    public int hashIDFromMSV(String _msv)  {
        String s_out = "";
        for (int i = 0; i < _msv.length(); i++) {
            s_out += (char)((int)_msv.charAt(i) * 5 % 10 + 49);
        }
        return Integer.valueOf(s_out);
    }

    public static void main(String args[])
    {
        Server server = new Server(8080);
    }
}