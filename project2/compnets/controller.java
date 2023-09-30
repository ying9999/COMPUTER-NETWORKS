import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.io.File;
import java.net.InetAddress;
import java.io.FileInputStream;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Stack;

public class controller extends Node {
    static final int DEFAULT_PORT = 54321;
    InetSocketAddress dstAddress;
    SocketAddress AddressA;
    static EdgeWeightedDigraph graph = new EdgeWeightedDigraph(1772371);
    DirectedEdge edge;
    String[] string = {"node1","node2","node3","node4","node5","node6","node7","node8","node9"};
    /*
     *
     */
    controller(int port) {
        try {
            socket= new DatagramSocket(port);
            edge = new DirectedEdge(1,2,1);
            graph.addEdge(edge);
            edge = new DirectedEdge(2,1,1);
            graph.addEdge(edge);
            edge = new DirectedEdge(2,3,1);
            graph.addEdge(edge);
            edge = new DirectedEdge(3,2,1);
            graph.addEdge(edge);
            edge = new DirectedEdge(3,4,1);
            graph.addEdge(edge);
            edge = new DirectedEdge(4,3,1);
            graph.addEdge(edge);
            edge = new DirectedEdge(4,5,1);
            graph.addEdge(edge);
            edge = new DirectedEdge(5,4,1);
            graph.addEdge(edge);
            edge = new DirectedEdge(6,2,1);
            graph.addEdge(edge);
            edge = new DirectedEdge(2,6,1);
            graph.addEdge(edge);
            edge = new DirectedEdge(6,3,1);
            graph.addEdge(edge);
            edge = new DirectedEdge(3,6,1);
            graph.addEdge(edge);
            edge = new DirectedEdge(6,7,1);
            graph.addEdge(edge);
            edge = new DirectedEdge(7,6,1);
            graph.addEdge(edge);
            edge = new DirectedEdge(8,2,1);
            graph.addEdge(edge);
            edge = new DirectedEdge(2,8,1);
            graph.addEdge(edge);
            edge = new DirectedEdge(8,3,1);
            graph.addEdge(edge);
            edge = new DirectedEdge(3,8,1);
            graph.addEdge(edge);
            edge = new DirectedEdge(8,9,1);
            graph.addEdge(edge);
            edge = new DirectedEdge(9,8,1);
            graph.addEdge(edge);
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
                
                String name = ((FileInfoContent)content).getFileName();
                int index=0;
                for (int i=0; i < string.length; i++){
                     if(string[i].equals(name)){
                          index = i;
                         }
                }
                index = index+1;
                String hostName = ((InetSocketAddress) packet.getSocketAddress()).getHostName();
                String[] parts = hostName.split("\\.");
                String[] afterDot = parts[0].split("");
                DijkstraSP sp = new DijkstraSP(graph, Integer.parseInt(afterDot[4]));
                List<DirectedEdge> DirectedEdge = new Stack<DirectedEdge>();
                int x = index;
                DirectedEdge = sp.pathTo(x);
                int integer = DirectedEdge.get(DirectedEdge.size()-1).to();
                FileInfoContent content3;
                content3 = new FileInfoContent(string[integer-1],2);
                AddressA = packet.getSocketAddress();
                packet = content3.toDatagramPacket();
                packet.setSocketAddress(AddressA);
                socket.send(packet);
                
                DatagramPacket response;
                response= new AckPacketContent("OK - controller Received this").toDatagramPacket();
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
            (new controller(DEFAULT_PORT)).start();
            System.out.println("Program completed");
        } catch(java.lang.Exception e) {e.printStackTrace();}
    }
}
