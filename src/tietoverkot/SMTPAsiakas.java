package tietoverkot;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
* SMTPasiakasohjelma, Tietoverkot harjoitusty�.
* Teht�v�n tekeminen meni muuten hyvin, mutta en oikein ymm�rt�nyt sit� kohtaa
* miss� sanottiin, ett� sama miss� tilassa ollaan, niin palvelimelle pit�isi pysty� l�hett�m��n
* QUIT komento. Eik� ekan switchin default tee t�m�n?
* @author Jenni Yrj�n�
* @version 11.6.2020
*/
public class SMTPAsiakas {
    
    /**
     * @param args ei k�yt�ss�
     * @throws IOException 
     * @throws UnknownHostException 
     */
    public static void main(String[] args) throws UnknownHostException, IOException {
        
        String temp;
       
        @SuppressWarnings("resource")
        Socket s =  new Socket("127.0.0.1", 28000);
        @SuppressWarnings("resource")
        Scanner sc1  =  new Scanner(s.getInputStream());
         @SuppressWarnings("resource")
        PrintStream p = new PrintStream(s.getOutputStream());
        
        boolean on = true;
   
        while(on) {
            
        temp = sc1.nextLine();
        String[] palat = temp.split("[ ;]");
        
        switch (palat[0]) {
        case "220":
            on = true;
            p.println("HELO jyu.fi");
            break;
        
        case "250":
            switch(palat[1]) {
            
            case "2.0.0":
                p.println("QUIT");
                break;
                
            case "2.1.0":
                p.println("RCPT TO:" + " minttu.raikkonen@outlook.com");
                break;
            case "2.1.5":
                p.println("DATA");
                break;
            default:
                p.println("MAIL FROM:" + " kimi.raikkonen@ferrari.fi");
                break;
            }// switch2 lopetus
            break; // case250 break
        
        case "221":
            on = false;
            break;
        case "354":
            p.println("Tein juuri miljoonasopimuksen!");
            p.println(".");
            break;

        default:
            System.out.println("Virhe, ohjelma lopetetaan");
            p.println("QUIT");
            break;
        
        } // switch lopetus
        p.flush();
        } // while-silmukan loppusulkku

        
        s.close();
        sc1.close();
        p.close();
    }
}
