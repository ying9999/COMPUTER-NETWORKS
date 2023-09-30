import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.io.File;
import java.net.InetAddress;
import java.io.FileInputStream;
import java.util.HashMap;

public class forwardE extends Node {
    static final int DEFAULT_PORT = 54321;
    InetSocketAddress dstAddress;
    InetSocketAddress dstAddress2;
    InetSocketAddress dstAddress3;
    SocketAddress AddressA;
    DatagramPacket dataA;
    String str2;
    HashMap<String, String> table = new HashMap<String, String>();
    /*
     *
     */
    forwardE(int port) {
        try {
            socket= new DatagramSocket(port);
            table.put("node2","node2");
            table.put("node3","node3");
            table.put("node6","node6");
            table.put("node9","node9");
            listener.go();
        }
        catch(java.lang.Exception e) {e.printStackTrace();}
    }

    /**
     * Assume that incoming packets contain a String and print the string.
     */
    public void onReceipt(DatagramPacket packet) {
        try {
            System.out.println("Received packet");

            PacketContent content= PacketContent.fromDatagramPacket(packet);

            if (content.getType()==PacketContent.ACKPACKET){
                System.out.println(content.toString());
            }
            if (content.getType()==PacketContent.FILEINFO) {
                AddressA = packet.getSocketAddress();
                DatagramPacket response;
                response= new AckPacketContent("OK - ForwardE Received this").toDatagramPacket();
                response.setSocketAddress(packet.getSocketAddress());
                socket.send(response);
                if(((FileInfoContent)content).getFileSize()==2){
                    String fileName2 = ((FileInfoContent)content).getFileName();
                    System.out.println(fileName2);
                    dstAddress3 = new InetSocketAddress(fileName2, 54321);
                    dataA.setSocketAddress(dstAddress3);
                    socket.send(dataA);
                    table.put(str2,fileName2);
                    }
                if(((FileInfoContent)content).getFileSize()==0){
                    String str = ((FileInfoContent)content).getFileName();
                    String[] splited = str.split(" ");
                        String value = splited[0];
                        str2 = value;
                        String val;
                            if (table.keySet().contains(value)) {
                                val = table.get(value);
                                dstAddress2 = new InetSocketAddress(val, 54321);
                                packet.setSocketAddress(dstAddress2);
                                socket.send(packet);
                                System.out.println("Packet sent");
                            }
                            else {
                                FileInfoContent  content2;
                                content2 = new FileInfoContent(value,0);
                                dataA = packet;
                                dstAddress2 = new InetSocketAddress("controll", 54321);
                                packet = content2.toDatagramPacket();
                                packet.setSocketAddress(dstAddress2);
                                socket.send(packet);
                                System.out.println("Packet sent");
                           }
                    }
            }
            
        }
        catch(Exception e) {e.printStackTrace();}
    }


    public synchronized void start() throws Exception {
        System.out.println("Waiting for contact");
        this.wait();
    }

    /*
     *
     */
    public static void main(String[] args) {
        try {
            (new forwardE(DEFAULT_PORT)).start();
            System.out.println("Program completed");
        } catch(java.lang.Exception e) {e.printStackTrace();}
    }
}
