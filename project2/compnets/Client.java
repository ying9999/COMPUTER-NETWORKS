/**
 *
 */
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.io.File;
import java.net.InetAddress;
import java.io.FileInputStream;

import java.util.Random;

/**
 *
 * Client class
 *
 * An instance accepts user input
 *
 */
public class Client extends Node {
	static int DEFAULT_SRC_PORT = 54321;
	static final int DEFAULT_DST_PORT = 54321;
	InetSocketAddress dstAddress;
    InetAddress IPv4;
    static int number=1;

	/**
	 * Constructor
	 *
	 * Attempts to create socket at given port and create an InetSocketAddress for the destinations
	 */
	Client(int dstPort, int srcPort) {
		try {
            IPv4 = InetAddress.getByName("172.20.1.3");
			dstAddress= new InetSocketAddress(IPv4, 54321);
			socket= new DatagramSocket(srcPort);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}


	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		PacketContent content= PacketContent.fromDatagramPacket(packet);

		System.out.println(content.toString());
        
		this.notify();
	}


	/**
	 * Sender Method
	 *
	 */
	public synchronized void start() throws Exception {
		String fname;
		FileInfoContent fcontent;

		int size;
		DatagramPacket packet= null;

        System.out.println("Which node do you want to transmit: ");
        fname= System.console().readLine();
        System.out.println("Enter message: ");
		fname= fname + " " + System.console().readLine();
		size= 0;
		fcontent= new FileInfoContent(fname, size);
		packet= fcontent.toDatagramPacket();
		packet.setSocketAddress(dstAddress);
		socket.send(packet);
		System.out.println("Packet sent");
		this.wait();
	}


	/**
	 * Test method
	 *
	 * Sends a packet to a given address
	 */
	public static void main(String[] args) {
		try {
                (new Client(DEFAULT_DST_PORT, DEFAULT_SRC_PORT)).start();
                System.out.println("Program completed");
            } catch(java.lang.Exception e) {e.printStackTrace();}
	}
}
