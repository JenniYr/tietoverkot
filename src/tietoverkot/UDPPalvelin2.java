package tietoverkot;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
* UDP palvelin harjoitustyö versio 2
* Toivottavasti toteutus ei ole aivan järkyttävä.
* @author jenni yrjänä
* @version 11 Jun 2020
*/
public class UDPPalvelin2 {

    /**
     * @param portti on portti joka halutaan ehkä lisätä portteihin
     * @param portit on taulukko porttinumeroista
     * @param numerointi on seuraavan vapaa alkio
     * @return palauttaa taulukon porteista 
     */
    public static  int[] paivitaPortit(Integer portti, int[] portit, int numerointi) {
        
        int[] uudetPortit = portit;
        
        for(int i = 0; i< portit.length; i++) {
            int porttinen = portit[i];
            if(portti == porttinen) return uudetPortit;
        }
        
        uudetPortit[numerointi] = portti;
        
        return uudetPortit;
    }
    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        
        int portti = 9999;
        @SuppressWarnings("resource")
        DatagramSocket socket = new DatagramSocket(portti);
       
        int[] portit = new int[100];
        int numerointi = 0;
       
        while(true) {
            
            byte[] in = new byte[1024];    // Nämä laitoin silmukan sisälle, koska ulkopuolella seuraavan viestiin jäi jälkiä edellisen viestistä jos edellinen oli pidempi kuin seuraavan viesti
            byte[] out = new byte[1024];
            
            DatagramPacket dp = new DatagramPacket(in, in.length);
            socket.receive(dp); 
            String str = new String(dp.getData());
            System.out.println(str);
            
            InetAddress IPAddressClient = dp.getAddress();
            Integer port = dp.getPort();
            portit = paivitaPortit(port, portit, numerointi);
            numerointi++;
            str = port.toString() + ";" + str; // koska lähettäjillä tässä ei varsinaisesti ole nimiä, nimeksi tulee portti josta viesti tulee
            out  =str.getBytes();

            
            for(int i = 0; i<numerointi; i++) {
            DatagramPacket dp1 =  new DatagramPacket(out, out.length, IPAddressClient, portit[i]);
            socket.send(dp1);
            }           
        }
    }
}
