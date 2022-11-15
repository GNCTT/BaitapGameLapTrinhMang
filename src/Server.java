import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Server
{
    private Socket		 socket = null;
    private ServerSocket server = null;
    private DataInputStream in	 = null;

    private DataOutputStream out	 = null;

    private final int HELLO_PKT_TYPE = 0;
    private final int CHECK_PKT_TYPE = 1;
    private final int SET_PLANE_PKT = 2;
    private final int BYE_PKT_TYPE = 3;
    private final int TURN_PKT = 4;

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
    String arrTest[] = new String[5];
    int arrOut[] = new int[5];
    int index_receive;

    public Server(int port)
    {
        try
        {
            server = new ServerSocket(port);
            arr_trap = new int[NUM_TRAP][2];
            clientID = -1;
            clientID_1 = -1;
            clientID_2 = -1;
            for (int i = 0; i < NUM_TRAP; i++) {
                arr_trap[i][0] = i;
                arr_trap[i][1] = 1;
            }
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client accepted");

            //create arr Strings
            arrTest[0] = "abcdefdaddadsdsdsdr";
            arrTest[1] = "bcddafkdahfa";
            arrTest[2] = "aaaaaaaaaaaaaaaaaaaaaaaaa";
            arrTest[3] = "bbbbcbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
            arrTest[4] = "lkjjkljkl";

//            for (int i = 0; i < 5; i++) {
//                arrOut[i] = checkPara(arrTest[i]);
//                System.out.print(arrOut[i] + " ");
//            }

            System.out.println();
            index_receive = 0;

            in = new DataInputStream(
                    socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            int confirm_result = 0;
            while (true)
            {
                try
                {
                    pkt_from_client = new byte[5000];
                    in.read(pkt_from_client);
                    type_byte = getBytebyIndex(pkt_from_client, 0, 4);
                    len_byte = getBytebyIndex(pkt_from_client, 4, 8);
                    int len = byte_int(len_byte);
                    data_byte = getBytebyIndex(pkt_from_client, 8, 8 + len);
                    int type = byte_int(type_byte);
                    int randIndex = getRandIndex(arrTest.length);
                    String str_send = arrTest[randIndex];
                    arrTest = removeAStrByIndex(randIndex, arrTest);
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
                        } else {
                            clientID_2 = clientID;
                        }
                        type_byte = inttobyte(1);
                        ID_byte = inttobyte(clientID);
                        width_byte = inttobyte(WIDTH_MAP_SIZE);
                        height_byte = inttobyte(HEIGHT_MAP_SIZE);
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
                            confirm_result = checkPara(str_send);
                        }
                    }
                    if (type == SET_PLANE_PKT) {
                        //get result
                        ID_byte = getBytebyIndex(data_byte, 0, 4);
                        dir_byte = getBytebyIndex(data_byte, 4, 8);
                        x_location_byte = getBytebyIndex(data_byte, 8,  12);
                        y_location_byte = getBytebyIndex(data_byte, 12, 16);
                        clientID = byte_int(ID_byte);
                        //xac dinh may bay nao
                        dir_plane = byte_int(dir_byte);
                        x_location = byte_int(x_location_byte);
                        y_location = byte_int(y_location_byte);
                        //check vi tri dat may bay va check thong tin nguoi gui.
                        System.out.println("type: " + type + " result: " + byte_int(ID_byte) + " index_receive: " + index_receive);
                        boolean checkSetPlane = true;
                        int result = 1;
                        if (checkSetPlane) {

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
                }
                catch(IOException i)
                {
                    System.out.println(i);
                }
            }
            System.out.println("Closing connection");

            // close connection
            socket.close();
            in.close();
            out.close();
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

    public static int checkPara(String s) {
        for (int i = 0; i < s.length() / 2; i++) {
            if (s.charAt(i) != s.charAt(s.length() - i - 1)) {
                return 0;
            }
        }
        return 1;
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