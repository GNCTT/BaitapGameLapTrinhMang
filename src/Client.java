
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private static final int CREATE_MAP_PKT = 1;
    private static final int BYE_PKT_TYPE = 3;
    private static final int FLAG_PKT_TYPE = 4;

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

    private static int data_length;
    private static byte[] data_byte;
    private static byte[] data_byte1;
    private static byte[] data_byte2;
    private static byte[] pkt_sent;
    private static byte[] datax = new byte[4];
    private static byte[] dataN = new byte[4];
    private static byte[] dataM = new byte[4];
    private static byte[] dataArrElement;
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        GameClient gameClient = new GameClient();

        gameClient.createMap(20, 20);


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

//                    data_byte = getBytebyIndex(buffer, 16, 16 + size_data);
                    datax = getBytebyIndex(buffer, 12, 16);
                    dataN = getBytebyIndex(buffer, 16, 20);
                    dataM = getBytebyIndex(buffer, 20, 24);
                    int x = bytetoINT(datax);
                    int N = bytetoINT(dataN);
                    int M = bytetoINT(dataM);
                    dataArrElement = new byte[4];
                    int [][] arrdata = new int[N][M];
                    int index = 24;
                    int resultx = -1;
                    int resulty = -1;
                    for (int i = 0; i < N; i++) {
                        for (int j = 0; j < M; j++) {
                            dataArrElement = getBytebyIndex(buffer, index, index + 4);
                            arrdata[i][j] = bytetoINT(dataArrElement);
                            if (arrdata[i][j] == x) {
                                resultx = i;
                                resulty = j;
                            }
                            index += 4;
                        }
                    }
                    System.out.println("Hellox " + x + " " + N + " " + M);
                    for (int i = 0; i < N; i++) {
                        for (int j = 0; j < M; j++) {
                            System.out.print(arrdata[i][j] + " ");
                        }
                        System.out.println();
                    }
                    type_byte = intobyte(2);
                    len_byte = intobyte(8);
                    data_byte1 = intobyte(-1);
                    data_byte2 = intobyte(-1);
                    data_byte = make_pkt_data(data_byte1, data_byte2);
                    pkt_sent = make_pkt_send(type_byte, len_byte, data_byte);
                    out.write(pkt_sent);

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

            if (type == FLAG_PKT_TYPE) {
                byte[] len = getBytebyIndex(buffer, 4, 8);
                int datasize = bytetoINT(len);
                byte[] flag = getBytebyIndex(buffer, 8, 8 + datasize);
                System.out.println(bytetoINT(flag));
                System.out.println("flag is " + byteToString(flag));
                break;
            }
            count++;
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