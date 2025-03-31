import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Client {

    private static int clientPort= 8002;
    private static int groupPort= 8001;
    private static String groupAddress= "230.0.0.0";
    private HashMap<Integer,InetAddress> mappaSfidanti= new HashMap<>();


    public static void main(String[] args) throws IOException {
        Client c= new Client();
        try {
            MulticastSocket ms= new MulticastSocket(groupPort);
            InetAddress group= InetAddress.getByName(groupAddress);
            ms.joinGroup(group);

            String message = "Ciao, sono il client!";
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, groupPort);
            ms.send(packet);
            System.out.println("Messaggio inviato al server!");

            DatagramSocket ds= new DatagramSocket(clientPort);
            byte[] buf= new byte[256];
            DatagramPacket dp= new DatagramPacket(buf, buf.length);
            ds.receive(dp);
            ds.close();


            String lista= new String(dp.getData(),0,dp.getLength());
            InetAddress sfidante= c.scegliSfidante(lista);
            String msgSfidante= sfidante.toString();
            byte[] buffSfidante= msgSfidante.getBytes();
            DatagramSocket dsSfidante= new DatagramSocket();
            DatagramPacket dpSfidante= new DatagramPacket(buffSfidante, buffSfidante.length,InetAddress.getByName(groupAddress),groupPort);
            dsSfidante.send(dpSfidante);


        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private InetAddress scegliSfidante(String lista) throws UnknownHostException {
        StringTokenizer st= new StringTokenizer(lista,",");
        int cnt=0;
        while (st.hasMoreTokens()){
            mappaSfidanti.put(cnt,InetAddress.getByName(st.nextToken()));
            ++cnt;
        }
        toStringMappa();
        System.out.println("Scdgli tra gli sfidanti disponibili> ");
        Scanner sc= new Scanner(System.in);
        String risp= sc.nextLine().trim();
        InetAddress sfidante=null;
        do {
            sfidante= mappaSfidanti.get(risp);
        }while (!mappaSfidanti.containsKey(risp));
        return sfidante;
    }

    private void toStringMappa() {
        for (Integer i: mappaSfidanti.keySet()){
            System.out.println(i+" "+mappaSfidanti.get(i));
        }
    }


}
