package tietoverkot;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
*
* @author Jenni Yrjänä
* @version 19 May 2020
*/
public class HTTPAsiakas2 {
    
    /**
     * @param args ei käytössä
     * @throws IOException 
     * @throws UnknownHostException 
     */
    public static void main(String[] args) throws UnknownHostException, IOException {
        
        String teksti, temp;
        @SuppressWarnings("resource")
        Scanner sc = new Scanner(System.in);
        @SuppressWarnings("resource")
        Socket s =  new Socket("127.0.0.1", 25000);
        @SuppressWarnings("resource")
        Scanner sc1  =  new Scanner(s.getInputStream());
        System.out.println("Kirjoita jotain");
        teksti = sc.nextLine();
        PrintStream p = new PrintStream(s.getOutputStream());
        p.println(teksti);
   
        temp = sc1.nextLine();
        
        String[] palat = temp.split("[;]");
        System.out.println("Palvelin: " + palat[0]);
        StringBuilder sb = new StringBuilder("teksti: ");
        for(int i = 1; i<palat.length; i++) {
            sb.append(palat[i] + " ");
        }
        System.out.println(sb.toString());
        
        s.close();
    }
}
