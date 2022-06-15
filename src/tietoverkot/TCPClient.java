package tietoverkot;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

class TCPClient
{
    public static void main(String argv[]) throws Exception
    {
     String sentence;
     String modifiedSentence;
     //BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
     @SuppressWarnings("resource")
    Socket clientSocket = new Socket("localhost", 25000);
     DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
     
     BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
        clientSocket.getInputStream()));
     
     sentence = "GET / HTTP/1.1\r\nHost:localhost\r\n\r\n";
     outToServer.writeBytes(sentence + '\n');
     
     int count = 0;
    
     modifiedSentence = inFromServer.readLine() + 'n';
     System.out.println("FROM SERVER: " + 
             modifiedSentence);
     
     while (count >= 0) {
         
         count = inFromServer.read();
         
         modifiedSentence = inFromServer.readLine();
         if(modifiedSentence != null) System.out.println(modifiedSentence);
     }
     
     //System.out.println("Tavuja vastaanotettu " + count);

     clientSocket.close();
    }
   }
