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
    private final int CHECK_PKT_TYPE = 1;
    private final int SET_PLANE_PKT = 2;
    private final int BYE_PKT_TYPE = 3;
    private final int TURN_PKT_TYPE = 4;

    private final int PLAY_PKT_TYPE = 5;

    private final int PKT_RESULT = 6;

    private int WIDTH_MAP_SIZE = 20;
    private int HEIGHT_MAP_SIZE = 20;

    private int NUM_TRAP = 15;

    private int arr_trap[][];
    private int clientID;
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
    private boolean is_win;

    private int count_time_client_1;
    private int count_time_client_2;
    Game gameClient_1;
    Game gameClient_2;

    private int client_Check;

    static int[][] mayBay ={{0,0,2,0,0},       //          2
            {0,2,0,0,2},       //         2  2
            {2,2,2,2,2},       //        22222      *Tam may bay o toa do [2;2]*
            {0,2,0,0,2},       //         2  2
            {0,0,2,0,0}};
    public Server(int port) {
        try {

//                Socket socket1 = new Socket("104.194.240.16", 8881);
//                System.out.println("hello");
//                in = new DataInputStream(socket1.getInputStream());
//                out = new DataOutputStream(socket1.getOutputStream());
//                byte[] action_byte = inttobyte(1);
//                byte[] a_byte = inttobyte(11);
//                byte[] ip_byte = Stringtobyte("4.tcp.ngrok.io");
//                byte[] port_byte = inttobyte(12649);
//                byte[] s_byte = inttobyte(0);
//                String name_game = "ban_may_bay";
//                byte[] c_byte = inttobyte(name_game.length());
//                byte[] name_byte = Stringtobyte(name_game);
//                String rule = "ban het may bay thi thang";
//                byte[] d_byte = inttobyte(rule.length());
//                byte[] rule_byte = Stringtobyte(rule);
//                String author = "vanh";
//                byte[] e_byte = inttobyte(author.length());
//                byte[] author_byte = Stringtobyte(author);
//                int size = action_byte.length + a_byte.length + ip_byte.length + port_byte.length + s_byte.length + c_byte.length
//                        + name_byte.length + d_byte.length + rule_byte.length + e_byte.length + author_byte.length;
//                ByteBuffer before_send2 = ByteBuffer.allocate(size);
//                before_send2.put(action_byte);
//                before_send2.put(a_byte);
//                before_send2.put(ip_byte);
//                before_send2.put(port_byte);
//                before_send2.put(s_byte);
//                before_send2.put(c_byte);
//                before_send2.put(name_byte);
//                before_send2.put(d_byte);
//                before_send2.put(rule_byte);
//                before_send2.put(e_byte);
//                before_send2.put(author_byte);
//                out.write(before_send2.array());
//                while (true) {
//                    pkt_from_client = new byte[5000];
//                    in.read(pkt_from_client);
//                    type_byte = getBytebyIndex(port_byte, 0, 4);
//                    int type = byte_int(type_byte);
//                    System.out.println("type:  " + type);
//                    if (type == 0) {
//                        break;
//                    }
//                }

            gameClient_1 = new Game(1, 1);
            gameClient_2 = new Game(1, 1);
            client_Check = 0;
            listSockets = new ArrayList<>();
            server = new ServerSocket(port);
            ins = new ArrayList<>();
            outs = new ArrayList<>();
            arr_trap = new int[NUM_TRAP][2];
            clientID = -1;
            clientID_1 = -1;
            clientID_2 = -1;
            turn_check = true;
            set_plan_ok = false;
            is_win = false;
            count_time_client_1 = 0;
            count_time_client_2 = 0;
            for (int i = 0; i < NUM_TRAP; i++) {
                arr_trap[i][0] = i;
                arr_trap[i][1] = 1;
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

            while (true) {
                for (int index = 0; index < ins.size(); index++) {
                    in = ins.get(index);
                    out = outs.get(index);

                try {
                        pkt_from_client = new byte[5000];
                        in.read(pkt_from_client);
                        type_byte = getBytebyIndex(pkt_from_client, 0, 4);
                        len_byte = getBytebyIndex(pkt_from_client, 4, 8);
                        int len = byte_int(len_byte);
                        data_byte = getBytebyIndex(pkt_from_client, 8, 8 + len);
                        int type = byte_int(type_byte);
//                    System.out.println("str_send: " + str_send);
//                    for (int i = 0; i < arrTest.length; i++) {
//                        System.out.print(arrTest[i] + "  ");
//                    }
                        System.out.println();
                        if (type == HELLO_PKT_TYPE) {
                            String msv = byteToString(data_byte);
                            System.out.println(msv);
                            clientID = hashIDFromMSV(msv);
                            if (clientID_1 == -1) {
                                clientID_1 = clientID;
                                client_Check = 0;
                            } else {
                                clientID_2 = clientID;
                                client_Check = 1;
                            }
                            type_byte = inttobyte(1);
                            ID_byte = inttobyte(clientID);
                            width_byte = inttobyte(WIDTH_MAP_SIZE);
                            height_byte = inttobyte(HEIGHT_MAP_SIZE);
                            if (client_Check == 0) {
                                gameClient_1.setWidth(WIDTH_MAP_SIZE);
                                gameClient_1.setHeight(HEIGHT_MAP_SIZE);
                                //add_trap
                            } else {
                                gameClient_2.setWidth(WIDTH_MAP_SIZE);
                                gameClient_2.setHeight(HEIGHT_MAP_SIZE);
                            }
                            num_trap_byte = inttobyte(NUM_TRAP);
                            sizeOfPacket = 4 * (4 + NUM_TRAP * 2);
                            len_byte = inttobyte(sizeOfPacket);

                            if (index_receive == 0) {
                                System.out.println(sizeOfPacket);
                                ByteBuffer before_send = ByteBuffer.allocate(sizeOfPacket + 16);
                                before_send.put(inttobyte(0));
                                before_send.put(inttobyte(0));
                                before_send.put(type_byte);
                                before_send.put(len_byte);
                                before_send.put(ID_byte);
                                before_send.put(width_byte);
                                before_send.put(height_byte);
                                before_send.put(num_trap_byte);
                                for (int i = 0; i < NUM_TRAP; i++) {
                                    x_location_trap = inttobyte(arr_trap[i][0]);
                                    y_location_trap = inttobyte(arr_trap[i][1]);
                                    before_send.put(x_location_trap);
                                    before_send.put(y_location_trap);
                                    System.out.println(arr_trap[i][0] + " " + arr_trap[i][1]);
                                }
                                out.write(before_send.array());

                            }
                        }
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
                            boolean checkSetPlane = true;
                            // checkplane method

                            if (checkSetPlane) {
                                ByteBuffer before_send = ByteBuffer.allocate(12);
                                type_byte = inttobyte(TURN_PKT_TYPE);
                                len_byte = inttobyte(4);
                                if (turn_check) {
                                    ID_byte = inttobyte(clientID);
                                    clientID = clientID_2;
                                    turn_check = false;
                                } else {
                                    ID_byte = inttobyte(clientID);
                                    clientID = clientID_1;
                                    set_plan_ok = true;
                                    turn_check = true;
                                }
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

                            //send RESULT//
//                            type_byte = inttobyte(PKT_RESULT);
//                            len_byte = inttobyte(8);
//                            //checkResult
//                            result = 1;
//                            result_byte = inttobyte(result);
//                            ByteBuffer before_send_2 = ByteBuffer.allocate(12);
//                            before_send_2.put(type_byte);
//                            before_send_2.put(len_byte);
//                            before_send_2.put(ID_byte);
//                            before_send_2.put(result_byte);
//                            out.write(before_send_2.array());

                            if (!is_win) {
                                ByteBuffer before_send = ByteBuffer.allocate(12);
                                type_byte = inttobyte(TURN_PKT_TYPE);
                                len_byte = inttobyte(4);
                                if (turn_check) {
                                    ID_byte = inttobyte(clientID);
                                    clientID = clientID_2;
                                    turn_check = false;
                                    count_time_client_1 ++;
                                } else {
                                    ID_byte = inttobyte(clientID);
                                    clientID = clientID_1;
                                    turn_check = true;
                                    count_time_client_2 ++;
                                }
                                System.out.println("---send_to" + clientID);
                                before_send.put(type_byte);
                                before_send.put(len_byte);
                                before_send.put(ID_byte);
                                out.write(before_send.array());
                                //check till is_win = true

                            } else {

                            }
                        }

                } catch (IOException i) {
                    System.out.println(i);
                }
            }
            }
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