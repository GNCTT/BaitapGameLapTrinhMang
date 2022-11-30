
import Start.Game;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class Client2 {
    private static final int CREATE_MAP_PKT = 1;

    private static final int SET_PLANE_PKT = 2;
    private static final int BYE_PKT_TYPE = 3;
    private static final int TURN_PKT_TYPE = 4;

    private static final int PLAY_PKT_TYPE = 5;

    private static final int PKT_RESULT = 6;

    private static final int PKT_END = 7;

    private static String msv;
    private static int clientID;

    private static int WIDTH_MAP_SIZE;
    private static int HEIGHT_MAP_SIZE;
    private static int num_trap;
    private static int arr_trap[][];
    private static byte[] buffer;
    private static byte[] type_byte;
    private static byte[] len_byte;

    private static byte ID_byte[];

    private static byte width_map_byte[];
    private static byte height_map_byte[];

    private static byte num_trap_byte[];

    private static byte x_trap_byte[];
    private static byte y_trap_byte[];

    private static byte dir_byte[];

    private static int dir_plane;

    private static byte x_location_byte[];
    private static byte y_location_byte[];
    private static int x_location;
    private static int y_location;

    private static int data_length;
    private static byte[] data_byte;
    private static byte[] data_byte1;
    private static byte[] data_byte2;
    private static byte[] pkt_sent;
    private static byte[] datax = new byte[4];
    private static byte[] dataN = new byte[4];
    private static byte[] dataM = new byte[4];
    private static byte[] dataArrElement;

    private static byte[] command_byte;
    private static byte[] x_fire_byte;
    private static byte[] y_fire_byte;

    private static Game gameClient;
    public static void main(String[] args) throws Exception {
        gameClient = new Game(1, 1);
        Scanner sc = new Scanner(System.in);
//        GameClient gameClient = new GameClient();
//
//        gameClient.createMap(20, 20);


        Socket clientsocket = new Socket("127.0.0.1", 8080);
        DataInputStream in = new DataInputStream(clientsocket.getInputStream());
        DataOutputStream out = new DataOutputStream(clientsocket.getOutputStream());
        System.out.print("Nhap ma sv:");
        msv = sc.nextLine();
        byte[] pkt = make_pkt_hello(0, msv);
        type_byte = intobyte(0);
        data_byte = Stringtobyte(msv);
        data_length = data_byte.length;
        len_byte = intobyte(data_length);
        pkt_sent = make_pkt_send(type_byte, len_byte, data_byte);
        out.write(pkt_sent);

        int count = 0;
        while (true) {
            buffer = new byte[50000];
            type_byte = new byte[4];
            in.read(buffer);
            if (count == 0) {
                type_byte = getBytebyIndex(buffer, 8, 12);

                System.out.println("Helloooo: " + bytetoINT(type_byte) );
            }
            if (count != 0) {
                type_byte = getBytebyIndex(buffer, 0, 4);
            }
            int type = bytetoINT(type_byte);
            System.out.println("type: " + type);
            if (type == BYE_PKT_TYPE) {
                System.out.println("???");
                break;
            }
            if (type == CREATE_MAP_PKT || type == 5) {
                if (count == 0) {
                    byte[] len = getBytebyIndex(buffer, 12, 16);
                    int size_data = bytetoINT(len);
                    System.out.println(size_data);
                    ID_byte = getBytebyIndex(buffer, 16, 20);
                    width_map_byte = getBytebyIndex(buffer, 20, 24);
                    height_map_byte = getBytebyIndex(buffer, 24, 28);
                    System.out.println(bytetoINT(width_map_byte) + " " + bytetoINT(height_map_byte));
                    num_trap_byte = getBytebyIndex(buffer, 28, 32);
                    clientID = bytetoINT(ID_byte);
                    WIDTH_MAP_SIZE = bytetoINT(width_map_byte);
                    HEIGHT_MAP_SIZE = bytetoINT(height_map_byte);
                    num_trap = bytetoINT(num_trap_byte);
                    gameClient.setWidth(WIDTH_MAP_SIZE);
                    gameClient.setHeight(HEIGHT_MAP_SIZE);
                    gameClient.createMap();
                    System.out.println("num_trap: " + num_trap);
                    arr_trap = new int[num_trap][2];
                    for (int i = 0; i < num_trap; i++) {
                        x_trap_byte = getBytebyIndex(buffer, 32 + i * 8, 36 + i * 8);
                        y_trap_byte = getBytebyIndex(buffer, 36 + i * 8, 40 + i * 8);
                        arr_trap[i][0] = bytetoINT(x_trap_byte);
                        arr_trap[i][1] = bytetoINT(y_trap_byte);
                        System.out.println(arr_trap[i][0] + " " + arr_trap[i][1]);
                    }
//                    gameClient.addTrap(arr_trap);
                    gameClient.renderMap();
                    //them vi tri bay bang truyen 1 mang vao
                    System.out.println("end");
                    //nhan xong du lieu ben server
                    //render map plane, bay, chon vi tri may bay, huong
//                    game.nhapSetPlane()
                    dir_plane = 1;
                    x_location = 3;
                    y_location = 3;
                    x_location = gameClient.getX();
                    y_location = gameClient.getY();
                    gameClient.chonHuongMayBay();
                    dir_plane = gameClient.getDir();
                    System.out.println(x_location + " " + y_location + " " + dir_plane + " ----");
//                    game.setPlane()

                    ByteBuffer before_send = ByteBuffer.allocate(24);
                    type_byte = intobyte(SET_PLANE_PKT);
                    len_byte = intobyte(16);
                    System.out.println("---------id " + clientID);
                    ID_byte = intobyte(clientID);
                    dir_byte = intobyte(dir_plane);
                    x_location_byte = intobyte(x_location);
                    y_location_byte = intobyte(y_location);
                    before_send.put(type_byte);
                    before_send.put(len_byte);
                    before_send.put(ID_byte);
                    before_send.put(dir_byte);
                    before_send.put(x_location_byte);
                    before_send.put(y_location_byte);
                    out.write(before_send.array());
                }
                if (count != 0) {
                    byte[] len = getBytebyIndex(buffer, 4, 8);
                    int size_data = bytetoINT(len);
                    datax = getBytebyIndex(buffer, 8, 12);
                    dataN = getBytebyIndex(buffer, 12, 16);
                    dataM = getBytebyIndex(buffer, 16, 20);
                    int x = bytetoINT(datax);
                    int N = bytetoINT(dataN);
                    int M = bytetoINT(dataM);
                    dataArrElement = new byte[4];
                    int [][] arrdata = new int[M][N];
                    int index = 20;
                    int resultx = -1;
                    int resulty = -1;
                    for (int i = 0; i < M; i++) {
                        for (int j = 0; j < N; j++) {
                            dataArrElement = getBytebyIndex(buffer, index, index + 4);
                            arrdata[i][j] = bytetoINT(dataArrElement);
                            if (arrdata[i][j] == x) {
                                resultx = i;
                                resulty = j;
                            }
                            index += 4;
                        }
                    }
                    System.out.println("Hello " + x + " " + N + " " + M);
                    for (int i = 0; i < M; i++) {
                        for (int j = 0; j < N; j++) {
                            System.out.print(arrdata[i][j] + " ");
                        }
                        System.out.println();
                    }

//                    System.out.println("result is " + result);
                    int result = 0;
                    type_byte = intobyte(2);
                    len_byte = intobyte(8);
//                    if (resultx != -1) {
//                        resultx += 1;
//                        resulty += 1;
//                    }
                    data_byte1 = intobyte(resultx);
                    data_byte2 = intobyte(resulty);
                    System.out.println("resultx: " + resultx + " resulty: " + resulty);
                    data_byte = make_pkt_data(data_byte1, data_byte2);
                    pkt_sent = make_pkt_send(type_byte, len_byte, data_byte);
                    out.write(pkt_sent);
                }

            }

            if (type == TURN_PKT_TYPE) {
                byte[] len = getBytebyIndex(buffer, 4, 8);
                int datasize = bytetoINT(len);
                ID_byte = getBytebyIndex(buffer, 8, 8 + datasize);
                int ID_RECEIVE = bytetoINT(ID_byte);
                System.out.println("hello_from_213_client" + ID_RECEIVE + " " + clientID);
                if (ID_RECEIVE == clientID) {
                    System.out.println("hello" + ID_RECEIVE);
                    int action = 1;
                    System.out.println("action: " + action);
                    int x_fire = 0, y_fire = 0;
                    if(action==2){
                        //getX_fire, Y_fire
                        x_fire = 1;
                        y_fire = 2;
                    }

                    // Di Chuyen
                    if(action==1){
                        dir_plane = gameClient.getDir();
                    }

                    int K = action;

                    //send packet play
                    type_byte = intobyte(PLAY_PKT_TYPE);
                    len_byte = intobyte(4 * (2 + K));
                    ID_byte = intobyte(clientID);
                    command_byte = intobyte(K);

                    ByteBuffer before_send = ByteBuffer.allocate(8 + 4 *(2 + K));
                    before_send.put(type_byte);
                    before_send.put(len_byte);
                    before_send.put(ID_byte);
                    before_send.put(command_byte);

                    if (K == 1) {
                        dir_byte = intobyte(dir_plane);
                        before_send.put(dir_byte);
                    } else {
                        x_fire_byte = intobyte(x_fire);
                        y_fire_byte = intobyte(y_fire);
                        before_send.put(x_fire_byte);
                        before_send.put(y_fire_byte);
                    }

                    out.write(before_send.array());
                }




            }

            if (type == PKT_RESULT) {
                ID_byte = getBytebyIndex(buffer, 8, 12);
                int ID_receive = bytetoINT(ID_byte);
                if (ID_receive == clientID) {
                    //show RESULT
                    byte result_byte[];
                    result_byte = getBytebyIndex(buffer, 12, 16);
                    int result = bytetoINT(result_byte);
                    System.out.println("result: " + result);
                }
            }
            if (type == PKT_END) {
                ID_byte = getBytebyIndex(buffer, 8, 12);
                int ID_receive = bytetoINT(ID_byte);
                if (ID_receive == clientID) {
                    byte result_match_byte[];
                    result_match_byte = getBytebyIndex(buffer, 12, 16);
                    int result_match = bytetoINT(result_match_byte);
                    switch (result_match) {
                        case 0:
                            System.out.println("you lose");
                            break;
                        case 1:
                            System.out.println("draw");
                            break;
                        case 2:
                            System.out.println("you win");
                            break;
                    }
                }
            }
            if (type == 10) {
                break;
            }
            count++;
            gameClient.renderMap();
        }
        out.close();
        in.close();
        clientsocket.close();
    }


    static int bytetoINT(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    static String byteToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    static byte[] Stringtobyte(String s) {
        byte b[] = s.getBytes();
        return b;
    }

    public static byte[] getBytebyIndex(byte[] bytes, int index1, int index2) {
        byte[] outarr = new byte[index2 - index1];
        for (int i = 0; i < index2 - index1; i++) {
            outarr[i] = bytes[i + index1];
        }
        return outarr;
    }

    public static int checkPara(String s) {
        for (int i = 0; i < s.length() / 2; i++) {
            if (s.charAt(i) != s.charAt(s.length() - i - 1)) {
                return 0;
            }
        }
        return 1;
    }


    static byte[] intobyte(int i) {
        ByteBuffer b = ByteBuffer.allocate(4);
        b.order(ByteOrder.LITTLE_ENDIAN);
        b.putInt(i);
        return b.array();
    }

    static byte[] make_pkt_hello(int i, String d) {
        byte type[] = intobyte(i);
        byte data[] = Stringtobyte(d);
        int datalen = data.length;
        byte len[] = intobyte(datalen);
        ByteBuffer final_array = ByteBuffer.allocate(datalen + 8);
        final_array.put(type);
        final_array.put(len);
        final_array.put(data);
        return final_array.array();
    }

    static  byte[] make_pkt_data(byte[] data1, byte[] data2) {
        ByteBuffer pkt_sent = ByteBuffer.allocate(8);
        pkt_sent.put(data1);
        pkt_sent.put(data2);
        return pkt_sent.array();
    }

    static byte[] make_pkt_send(byte[] type, byte[] len, byte[]data) {
        int size = bytetoINT(len) + 8;
        ByteBuffer pkt_sent = ByteBuffer.allocate(size);
        pkt_sent.put(type);
        pkt_sent.put(len);
        pkt_sent.put(data);
        return pkt_sent.array();
    }
}