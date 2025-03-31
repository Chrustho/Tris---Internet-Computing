import ThreadDiUtilità.GestioneClient;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;

public class Server {

    private HashMap<InetAddress, Integer> mappaVitoria= new HashMap<>();
    private static int groupPort= 8001;


    public HashMap<InetAddress, Integer> getMappaVitoria() {
        return mappaVitoria;
    }

    public static void main(String[] args) {
        Server server= new Server();
        String groupAddress= "230.0.0.0";
        try {
            InetAddress group = InetAddress.getByName(groupAddress);
            MulticastSocket socket = new MulticastSocket(groupPort);
            socket.joinGroup(group);
            byte[] buffer= new byte[256];
            while (true){
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                System.out.println("Connessione stabilita!");
                InetAddress client=packet.getAddress();
                if (!server.getMappaVitoria().containsKey(client)){
                    server.getMappaVitoria().put(client,0);
                }
                GestioneClient gc= new GestioneClient(packet, server.getMappaVitoria(), InetAddress.getLocalHost());
                gc.start();
                byte[] bufferRP= new byte[256];
                DatagramPacket dpRichiestaPartita= new DatagramPacket(bufferRP, bufferRP.length);
                socket.receive(dpRichiestaPartita);
                InetAddress client1= dpRichiestaPartita.getAddress();
                InetAddress client2= InetAddress.getByName(dpRichiestaPartita.getData().toString());


            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
