package tietoverkot;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
*
* @author jenni
* @version 25 May 2020
*/
public class HTTPPalvelin2 {

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        
        @SuppressWarnings("resource")
        ServerSocket sr =  new ServerSocket(25000);
        @SuppressWarnings("resource")
        Socket ss = sr.accept();
        @SuppressWarnings("resource")
        Scanner sc = new Scanner(ss.getInputStream());
        String sana = sc.nextLine();
        
        sana = "Jennin palvelin;" + sana;
        
        PrintStream p = new PrintStream(ss.getOutputStream());
        p.println(sana);
        sr.close();
        ss.close();
    }
}
