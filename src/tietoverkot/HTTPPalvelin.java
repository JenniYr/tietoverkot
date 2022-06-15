package tietoverkot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

import javax.net.ServerSocketFactory;

/**
 *
 * @author jenni yrjänä
 * @version 20 May 2020
 */
public class HTTPPalvelin {

    /**
     * @param args ei käytössä
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {

        ServerSocketFactory ssf  = ServerSocketFactory.getDefault();
        Charset charset = Charset.forName("ISO-8859-1");

        try (ServerSocket srvSocket = ssf.createServerSocket(26000)){
            
            
            while(true) {
                try(
                        
                        Socket socket =  srvSocket.accept();
                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),charset))
                        ){

                    
                    if(br.readLine() != null) {
                        System.out.println(br.readLine());
                        bw.write("Jenni; " + br.readLine());
                        bw.flush();
                    }
                    if("EXIT".equalsIgnoreCase(br.readLine())) break;
                    socket.close();  
                }
            }
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }
}