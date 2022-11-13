
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class Client2 {
    private static final int CHECK_PKT_TYPE = 1;
    private static final int BYE_PKT_TYPE = 3;
    private static final int FLAG_PKT_TYPE = 4;
    public static void main(String[] args) throws Exception {

        Socket clientsocket = new Socket("127.0.0.1", 8080);
        DataInputStream in = new DataInputStream(clientsocket.getInputStream());
        DataOutputStream out = new DataOutputStream(clientsocket.getOutputStream());
        byte[] pkt = make_pkt_hello(0, "19020212");
        out.write(pkt);
        System.out.println("have sent pkt 0, " + pkt.length);
        byte[] buffer;
        byte[] type_byte;
        byte[] len_byte;
        byte[] data_byte = new byte[4];
        byte[] data_byte1;
        byte[] data_byte2;
        byte[] pkt_sent;
        byte[] datax = new byte[4];
        byte[] dataN = new byte[4];
        byte[] dataM = new byte[4];
        byte[] dataArrElement;
        int count = 0;
        while (true) {
            buffer = new byte[50000];
            type_byte = new byte[4];
            in.read(buffer);
            if (count == 0) {
                type_byte = getBytebyIndex(buffer, 4, 8);

                System.out.println("Helloooo: " + bytetoINT(type_byte));
            }
            if (count != 0) {
                type_byte = getBytebyIndex(buffer, 0, 4);
            }
            int type = bytetoINT(type_byte);
            if (type == BYE_PKT_TYPE) {
                System.out.println("???");
                break;
            }
            if (type == CHECK_PKT_TYPE || type == 5) {
                if (count == 0) {
                    byte[] len = getBytebyIndex(buffer, 8, 12);
                    int size_data = bytetoINT(len);
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