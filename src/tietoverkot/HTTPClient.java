package tietoverkot;

import java.io.*;
import java.net.*;

class HTTPClient
{
    public static void main(String[] args) throws Exception
    {
        @SuppressWarnings("resource")
        Socket s = new Socket(InetAddress.getByName("localhost"), 25000);
        
        String snd = "GET / HTTP/1.1\r\nHost: localhost\r\n\r\n";
        
        byte[] msg = new byte[snd.length()];
        
        msg = snd.getBytes();
        
        @SuppressWarnings("resource")
        OutputStream os = s.getOutputStream();
        
        os.write(msg);

         

        @SuppressWarnings("resource")
        InputStream is = s.getInputStream();
        //int count = 0;

        
        String sana = "";
        
        do {
        byte[] rec = new byte[2048];
        is.read(rec); 
        sana = (new String(rec));
        System.out.println(sana);
        } while (sana.length() != 0);
        

        s.close();
    }
}