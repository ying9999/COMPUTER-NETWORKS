import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.io.File;
import java.io.FileInputStream;

public class workerB extends Node {
    static final int DEFAULT_PORT = 52052;
    /*
     *
     */
    workerB(int port) {
        try {
            socket= new DatagramSocket(port);
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
            FileContent fcontent;
            DatagramPacket packetA= null;
            String fname = ((FileInfoContent)content).getFileName();
            File file= null;
            FileInputStream fin= null;
            int size;
            byte[] buffer= null;
            file= new File(fname);                // Reserve buffer for length of file and read file
            buffer= new byte[(int) file.length()];
            fin= new FileInputStream(file);
            size= fin.read(buffer);
            if (size==-1) {
                fin.close();
                throw new Exception("Problem with File Access:"+fname);
            }
            
            if (content.getType()==PacketContent.FILEINFO) {
                DatagramPacket response;
                response= new AckPacketContent("OK - WorkerB Received this").toDatagramPacket();
                response.setSocketAddress(packet.getSocketAddress());
                socket.send(response);
            }
            
            String value = new String(buffer, "UTF-8");
            fcontent= new FileContent(fname, value, size);
            packetA= fcontent.toDatagramPacket();
            packetA.setSocketAddress(packet.getSocketAddress());
            socket.send(packetA);
            System.out.println("Packet sent");
            fin.close();
            
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
            (new workerB(DEFAULT_PORT)).start();
            System.out.println("Program completed");
        } catch(java.lang.Exception e) {e.printStackTrace();}
    }
}
