package ThreadDiUtilità;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Set;

public class GestioneClient extends Thread{

    private DatagramPacket dp;
    private HashMap<InetAddress, Integer> mappa;
    private InetAddress server;
    private static int clientPort= 8002;

    public GestioneClient(DatagramPacket packet, HashMap<InetAddress, Integer> mappa, InetAddress serverIP) {
            this.dp=packet;
            this.mappa=mappa;
            this.server=serverIP;
    }

    @Override
    public void run() {
        InetAddress clientAddress = dp.getAddress();
        Set<InetAddress> set= mappa.keySet();
        set.remove(clientAddress);
        StringBuilder sb= new StringBuilder();
        for (InetAddress i: set){
            sb.append(i+",");
        }
        String msg= sb.toString();
        byte[] buff= msg.getBytes();
        try {
            DatagramPacket pack= new DatagramPacket(buff, buff.length, clientAddress, clientPort);
            DatagramSocket ds= new DatagramSocket();
            ds.send(pack);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
