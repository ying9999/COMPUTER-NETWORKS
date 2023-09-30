import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.io.File;
import java.net.InetAddress;
import java.io.FileInputStream;


public class serverA extends Node {
	static final int DEFAULT_PORT = 54321;
    InetSocketAddress dstAddress;
    SocketAddress AddressA;
	/*
	 *
	 */
	serverA(int port) {
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

            if (content.getType()==PacketContent.ACKPACKET){
                System.out.println(content.toString());
            }
			if (content.getType()==PacketContent.FILEINFO) {
                AddressA = packet.getSocketAddress();
				DatagramPacket response;
				response= new AckPacketContent("OK - ServerA Received this").toDatagramPacket();
				response.setSocketAddress(packet.getSocketAddress());
				socket.send(response);
			}
            if (content.getType()==PacketContent.FILEINFOA) {
                packet.setSocketAddress(AddressA);
                socket.send(packet);
                System.out.println("Packet sent");
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
			(new serverA(DEFAULT_PORT)).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}
