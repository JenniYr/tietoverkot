package tietoverkot;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
* UDP palvelin harjoitustyö eka versio
* @author jenni yrjänä
* @version 11 Jun 2020
*/
public class UDPPalvelin implements Runnable {
    
    private final int asiakasPortti;
    
    /**
     * @param asiakasPortti
     */
    public UDPPalvelin (int asiakasPortti) {
        this.asiakasPortti = asiakasPortti;
    }
    
    /**
     * 
     */
    @Override
    public void run() {
        try (DatagramSocket palvelin = new DatagramSocket(9999)){
            for (int i = 0; i<3; i++) {
                String viesti = "Viestin numero on " + i;
                DatagramPacket dp = new DatagramPacket(viesti.getBytes(), viesti.length(), InetAddress.getLocalHost(), asiakasPortti);
                palvelin.send(dp);
            }
        } catch(SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
}
