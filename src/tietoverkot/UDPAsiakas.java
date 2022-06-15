package tietoverkot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
* UDP asiakas harjoitus / Tietoverkot kurssi harjoitus 3b
* @author jenni yrjänä
* @version 26 Jun 2020
*/
public class UDPAsiakas {

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        
        BufferedReader kayttajaIn  = new BufferedReader(new InputStreamReader(System.in));
        @SuppressWarnings("resource")
        DatagramSocket kayttajaSoketti = new DatagramSocket();
        InetAddress ipAdd = InetAddress.getByName("localhost");
        
        boolean b = true;
        
        while(b) {
        
        byte inData[] = new byte[1024];
        byte outData[] = new byte[1024];
        String str = kayttajaIn.readLine();
        if(str.equals("quit")) {     // lopetetaan quit komennolla
            b = false;  
        }
        outData = str.getBytes();
        DatagramPacket paketti = new DatagramPacket(outData, outData.length, ipAdd, 9999);
        kayttajaSoketti.send(paketti);
        DatagramPacket paketti2 = new DatagramPacket(inData, inData.length, ipAdd, 9999);
        kayttajaSoketti.receive(paketti2);
        String vastaanotettu = new String(paketti2.getData());
        System.out.println(vastaanotettu); 
        }
        kayttajaSoketti.close();
    }

}
