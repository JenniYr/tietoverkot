package tietoverkot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import javax.net.SocketFactory;

/**
*
* @author Jenni Yrjänä
* @version 19 May 2020
*/
public class HTTPasiakas {
    
    /**
     * @param args ei käytössä
     * @throws IOException 
     * @throws UnknownHostException 
     */
    public static void main(String[] args) throws UnknownHostException, IOException {
        
        Charset charset = Charset.forName("ISO-8859-1");
        SocketFactory sf = SocketFactory.getDefault();
        
        
        try (
            Socket socket = sf.createSocket("localhost", 26000);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charset)))
        {
            bw.write("GET / HTTP/1.1\r\nHost: localhost\r\n\r\n");
            bw.flush();
            String rivi;
            int count = 0;
            StringBuilder sb = new StringBuilder();
            
            while((rivi = br.readLine()) != null) {
                byte[] bt = new byte[2048];
                bt = rivi.getBytes();
                count = count + bt.length; // Ei laske tavuja oikein
                
                if(rivi.length() == 0) sb.append('\n');
                if(rivi.startsWith("<")) sb.append(rivi + '\n');
                
            }
            
            System.out.println("Tavuja vastaanotettiin: " + count);
            System.out.println(sb.toString());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
