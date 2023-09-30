import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.io.File;
import java.net.InetAddress;
import java.io.FileInputStream;


public class Server extends Node {
	static final int DEFAULT_PORT = 54321;
    InetSocketAddress dstAddress;
    SocketAddress AddressA;
	/*
	 *
	 */
	Server(int port) {
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
				response= new AckPacketContent("OK - Server Received this").toDatagramPacket();
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


 /*   public void onReceipt(DatagramPacket packet) {
            try {
                System.out.println("Received packet");

                PacketContent content= PacketContent.fromDatagramPacket(packet);

                if (content.getType()==PacketContent.ACKPACKET){
                    System.out.println(content.toString());
                }
               
                if (content.getType()==PacketContent.FILEINFO) {
                                    System.out.println("File name: " + ((FileInfoContent)content).getFileName());
                                    System.out.println("File size: " + ((FileInfoContent)content).getFileSize());
     
                    AddressA = packet.getSocketAddress();
                    String fileName = ((FileInfoContent)content).getFileName();
                    String extension = "";
                            int i = fileName.lastIndexOf('.');
                            if (i >= 0) { extension = fileName.substring(i+1); }
                    
                    DatagramPacket response;
                    response= new AckPacketContent("OK - Received this").toDatagramPacket();
                    response.setSocketAddress(packet.getSocketAddress());
                    socket.send(response);
                    if(extension.equals("txt")){
                        dstAddress = new InetSocketAddress(DEFAULT_DST_NODE, 52022);
                        packet.setSocketAddress(dstAddress);
                        socket.send(packet);
                        System.out.println("Packet sent");
                    }
                    else {
                        dstAddress = new InetSocketAddress("workB", 52052);
                        packet.setSocketAddress(dstAddress);
                        socket.send(packet);
                        System.out.println("Packet sent");
                    }
            }
                if (content.getType()==PacketContent.FILEINFOA) {
                    packet.setSocketAddress(AddressA);
                    socket.send(packet);
                    System.out.println("Packet sent");
                }
              
            }
            catch(Exception e) {e.printStackTrace();}
        }
    
  */
    
    
	public synchronized void start() throws Exception {
		System.out.println("Waiting for contact");
		this.wait();
	}

	/*
	 *
	 */
	public static void main(String[] args) {
		try {
			(new Server(DEFAULT_PORT)).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}
