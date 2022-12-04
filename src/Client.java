
import Start.Game;
import Start.GameOther;
import com.sun.xml.internal.bind.v2.model.core.ID;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class Client {
    private static final int CREATE_MAP_PKT = 1;

    private static final int SET_PLANE_PKT = 2;
    private static final int BYE_PKT_TYPE = 3;
    private static final int TURN_PKT_TYPE = 4;

    private static final int PLAY_PKT_TYPE = 5;

    private static final int PKT_RESULT = 6;

    private static final int PKT_END = 7;

    private static String msv;
    private int clientID;
    private int ID_receive;

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
    private int width_map;
    private int height_map;

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

    private boolean checkSetPlane;
    private boolean checkQuitServer;
    private boolean canMove;

    private int status;

    public boolean has_change;
    private Game gameClient;
    private GameOther gameOther;

    private DataOutputStream outs;
    private DataInputStream ins;
    private Socket clientSocket;

    public boolean checkMessage;
    public boolean be_send;

    public boolean is_over;

    private int result_match;
    private byte result_byte[];

    public Client(String host, int port, Game gameClient, GameOther gameOther) {
        this.gameClient = gameClient;
        this.gameOther = gameOther;
        checkSetPlane = false;
        is_over = false;
        checkQuitServer = false;
        be_send = false;
        ID_receive = 0;
        canMove = false;
        checkMessage = true;
        status = 0;
        has_change = true;
        try {
            clientSocket = new Socket(host, port);
            ins = new DataInputStream(clientSocket.getInputStream());
            outs = new DataOutputStream(clientSocket.getOutputStream());
            status = 0;
            msv = "234";
//            System.out.print("Nhap ma sv:");
//            Scanner sc = new Scanner(System.in);
//            msv = sc.nextLine();
            type_byte = intobyte(0);
            data_byte = Stringtobyte(msv);
            data_length = data_byte.length;
            len_byte = intobyte(data_length);
            pkt_sent = make_pkt_send(type_byte, len_byte, data_byte);
            outs.write(pkt_sent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getStatus() {
        return status;
    }

    public void readDataFromServer() {
        buffer = new byte[5000];
        try {
            ins.read(buffer);
            if (buffer != null) {
                has_change = true;
                checkMessage = true;
                type_byte = getBytebyIndex(buffer, 0, 4);
                ID_byte = getBytebyIndex(buffer, 8, 12);
                int id_receive = bytetoINT(ID_byte);
                ID_receive = id_receive;
                System.out.println("fasd: " + id_receive + " " + be_send);
                    int type = bytetoINT(type_byte);
                    System.out.println("type: " + type);
                    be_send = true;
                    if (type == BYE_PKT_TYPE) {
                        checkQuitServer = true;
                        status = 3;
                    }
                    if (type == TURN_PKT_TYPE) {
                        ID_byte = getBytebyIndex(buffer, 8, 12);
                        ID_receive = bytetoINT(ID_byte);
                        System.out.println("ID: " + ID_receive);
                        canMove = true;
                        status = 1;
                        has_change = true;
                    }
                    if (type == PKT_RESULT) {
                        ID_byte = getBytebyIndex(buffer, 8, 12);
                        int ID_receive_ = bytetoINT(ID_byte);
                        ID_receive = ID_receive_ + 1;

                        if (ID_receive_ == clientID) {
                            //show RESULT
                            byte result_byte[];
                            status = 4;
                            result_byte = getBytebyIndex(buffer, 12, 16);
                            int result = bytetoINT(result_byte);
                            System.out.println("result: " + result);
                            if (result == 0) {
                                System.out.println("miss");
                            }
                            if (result == 1) {
                                System.out.println("good");
                            }
                            if (result == 4) {
                                System.out.println("continue");
                            }
                        }
                    }
                    if (type == 8) {
                        x_location_byte = getBytebyIndex(buffer, 12, 16);
                        y_location_byte = getBytebyIndex(buffer, 16, 20);
                        int x_ = bytetoINT(x_location_byte);
                        int y_ = bytetoINT(y_location_byte);
                        System.out.println("shoot: " + x_ + y_);
                        gameClient.checkShoot(x_, y_);
                        gameClient.beShoot(x_, y_);
                        gameClient.render();
                        has_change = true;
                    }
                    if (type == PKT_END) {
                        System.out.println("receive Res");
                        result_byte = getBytebyIndex(buffer, 12, 16);
                        is_over = true;
                        result_match = bytetoINT(result_byte);
                        System.out.println(result_match);
                    }

            } else {
                checkMessage = false;
                be_send = false;
            }

            } catch(IOException e){
                throw new RuntimeException(e);
            }

    }

    public int getResultMatch() {
        return result_match;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public void setID_receive(int id_receive) {
        this.ID_receive = id_receive;
    }

    public boolean isCanMove() {
        if (ID_receive == clientID) {
            return true;
        } else {
            return false;
        }
    }

    public void sendPKTPlay() {

    }

    public boolean isCheckMessage() {
        return checkMessage;
    }

    public void setStatus(int _status) {
        this.status = _status;
    }

    public int[][] getArr_trap() {
        buffer = new byte[5000];
        try {
            has_change = true;
            ins.read(buffer);
            byte[] len = getBytebyIndex(buffer, 12, 16);
            int size_data = bytetoINT(len);
            System.out.println(size_data);
            ID_byte = getBytebyIndex(buffer, 16, 20);
            width_map_byte = getBytebyIndex(buffer, 20, 24);
            height_map_byte = getBytebyIndex(buffer, 24, 28);
            width_map = bytetoINT(width_map_byte);
            height_map = bytetoINT(height_map_byte);
            System.out.println(width_map + " " + height_map);
            num_trap_byte = getBytebyIndex(buffer, 28, 32);
            clientID = bytetoINT(ID_byte);
            WIDTH_MAP_SIZE = bytetoINT(width_map_byte);
            HEIGHT_MAP_SIZE = bytetoINT(height_map_byte);
            num_trap = bytetoINT(num_trap_byte);
            System.out.println("num_trap: " + num_trap);
            arr_trap = new int[num_trap][2];
            for (int i = 0; i < num_trap; i++) {
                x_trap_byte = getBytebyIndex(buffer, 32 + i * 8, 36 + i * 8);
                y_trap_byte = getBytebyIndex(buffer, 36 + i * 8, 40 + i * 8);
                arr_trap[i][0] = bytetoINT(x_trap_byte);
                arr_trap[i][1] = bytetoINT(y_trap_byte);
                System.out.println(arr_trap[i][0] + " " + arr_trap[i][1]);
            }

            System.out.println("end");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return arr_trap;
    }

    public void getArr_Trap() {
        buffer = new byte[5000];
        try {
            has_change = true;
            ins.read(buffer);
            byte[] len = getBytebyIndex(buffer, 12, 16);
            int size_data = bytetoINT(len);
            System.out.println(size_data);
            ID_byte = getBytebyIndex(buffer, 16, 20);
            width_map_byte = getBytebyIndex(buffer, 20, 24);
            height_map_byte = getBytebyIndex(buffer, 24, 28);
            width_map = bytetoINT(width_map_byte);
            height_map = bytetoINT(height_map_byte);
            System.out.println(width_map + " " + height_map);
            num_trap_byte = getBytebyIndex(buffer, 28, 32);
            clientID = bytetoINT(ID_byte);
            WIDTH_MAP_SIZE = bytetoINT(width_map_byte);
            HEIGHT_MAP_SIZE = bytetoINT(height_map_byte);
            gameClient.reload(WIDTH_MAP_SIZE, HEIGHT_MAP_SIZE);
            gameOther.reload(WIDTH_MAP_SIZE, HEIGHT_MAP_SIZE);
            num_trap = bytetoINT(num_trap_byte);
            System.out.println("num_trap: " + num_trap);
            arr_trap = new int[num_trap][2];
            for (int i = 0; i < num_trap; i++) {
                x_trap_byte = getBytebyIndex(buffer, 32 + i * 8, 36 + i * 8);
                y_trap_byte = getBytebyIndex(buffer, 36 + i * 8, 40 + i * 8);
                arr_trap[i][0] = bytetoINT(x_trap_byte);
                arr_trap[i][1] = bytetoINT(y_trap_byte);
                System.out.println(arr_trap[i][0] + " " + arr_trap[i][1]);
            }
            gameClient.addTrap(arr_trap);
            gameOther.add_trap(arr_trap);

            System.out.println("end");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getHeight_map() {
        return height_map;
    }

    public int getWidth_map() {
        return width_map;
    }

    public int getID_receive() {
        return ID_receive;
    }

    public int getClientID() {
        return this.clientID;
    }

    public void sendSetPlanePkt(int dir, int x_location, int y_location) {
        has_change = true;
        status = 2;
        ByteBuffer before_send = ByteBuffer.allocate(24);
        type_byte = intobyte(SET_PLANE_PKT);
        len_byte = intobyte(16);
        System.out.println("---------id " + clientID);
        ID_byte = intobyte(clientID);
        dir_byte = intobyte(dir);
        x_location_byte = intobyte(x_location);
        y_location_byte = intobyte(y_location);
        if (gameClient.checkLocationPlane(x_location, y_location)) {
            gameClient.setPlane(dir, x_location, y_location);
        }
        before_send.put(type_byte);
        before_send.put(len_byte);
        before_send.put(ID_byte);
        before_send.put(dir_byte);
        before_send.put(x_location_byte);
        before_send.put(y_location_byte);
        try {
            outs.write(before_send.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendPktPlay(int dir,int t, int x_location, int y_location) {

    }

    public void sendPktPlay(int K, int x_fire, int y_fire) {
        try {
            has_change = true;
        type_byte = intobyte(PLAY_PKT_TYPE);
        len_byte = intobyte(4 * (2 + K));
        ID_byte = intobyte(clientID);
        command_byte = intobyte(K);
        ByteBuffer before_send = ByteBuffer.allocate(8 + 4 *(2 + K));
        before_send.put(type_byte);
        before_send.put(len_byte);
        before_send.put(ID_byte);
        before_send.put(command_byte);
        System.out.println("sendPktPlay");
        if (K == 1) {
            dir_byte = intobyte(dir_plane);
            before_send.put(dir_byte);
        } else {
            x_fire_byte = intobyte(x_fire);
            y_fire_byte = intobyte(y_fire);
            before_send.put(x_fire_byte);
            before_send.put(y_fire_byte);
        }
            outs.write(before_send.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPktPlay(int K, int dir) {
        try {
            has_change = true;
            type_byte = intobyte(PLAY_PKT_TYPE);
            len_byte = intobyte(4 * (2 + K));
            ID_byte = intobyte(clientID);
            dir_byte = intobyte(dir);
            command_byte = intobyte(K);
            ByteBuffer before_send = ByteBuffer.allocate(8 + 4 *(2 + K));
            before_send.put(type_byte);
            before_send.put(len_byte);
            before_send.put(ID_byte);
            before_send.put(command_byte);
            before_send.put(dir_byte);
            gameClient.move(dir);
            System.out.println("sendPktPlay");
            outs.write(before_send.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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