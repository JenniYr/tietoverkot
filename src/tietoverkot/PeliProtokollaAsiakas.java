package tietoverkot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
* Peliprotokollaharjoitus, Asiakasohjelma
* Peli vaikuttaisi toimivan, mutta virheille ei tässä versiossa ole tehty mitään isompaa
* Switch case käsittely on aivan kamala ja väkisin miettii miten saisi olioilla parannettua,
* mutta ajanpuutteen takia en ala käyttämään enempää aikaa tähän vaikka se oppimisen kannalta hyöydyllistä olisi ollutkin.
* 
* Aliohjelmat toimivat, mutta olisihan näistä saanut fiksummatkin väkerrettyä.
* 
* Jos ohjelmassa on täydennettävää, toivotaan pikaista palautetta!
* 
* @author jenni yrjänä
* @version 2 Jul 2020
*/
public class PeliProtokollaAsiakas {
    
    /**
     * @param str on kayttajan syöte
     * @param kayttajaSoketti on datagrammisoketti
     * @param ipAdd 
     * @throws IOException 
     * 
     */
    public static void laheta(String str, DatagramSocket kayttajaSoketti, InetAddress ipAdd) throws IOException {
         byte[] outData = new byte[1024];
         outData = str.getBytes();
         DatagramPacket paketti = new DatagramPacket(outData, outData.length, ipAdd, 9999);
         kayttajaSoketti.send(paketti);
    }

    /**
     * @param kayttajaSoketti
     * @param ipAdd 
     * @return taulukon erotelluista sanoista
     * @throws IOException 
     * 
     */
    public static String[] vastaanota (DatagramSocket kayttajaSoketti, InetAddress ipAdd) throws IOException {
         byte[] inData = new byte[1024];
         DatagramPacket paketti2 = new DatagramPacket(inData, inData.length, ipAdd, 9999 );
         kayttajaSoketti.receive(paketti2);
         String vastaanotettu = new String(paketti2.getData());
         String[] palat = vastaanotettu.split("[ ;]");
         return  palat;
         
    }
    /**
     * @param args ei käytössä
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {

        @SuppressWarnings("resource")
        BufferedReader kayttajaIn  = new BufferedReader(new InputStreamReader(System.in));
        @SuppressWarnings("resource")
        DatagramSocket kayttajaSoketti = new DatagramSocket();
        InetAddress ipAdd = InetAddress.getByName("localhost");
        
        System.out.println("Aloittaaksesi pelin kirjoita: JOIN nimesi");
        
        laheta(kayttajaIn.readLine(), kayttajaSoketti, ipAdd);
        
        boolean on = true;
        
        String tila = "JOIN";
        
        while(on) 
        {
            String[] palat = vastaanota(kayttajaSoketti, ipAdd);
            switch (tila) 
            {
                case "JOIN" :
                    switch(palat[0]) 
                    {
                        case "ACK":
                            switch(palat[1]) 
                            {
                                case "201":
                                    System.out.println("Odotetaan toista pelaajaa");
                                    break; // break for case 201                                    
                                case "202":
                                    System.out.println("Vastustajasi on " + palat[2] + "");
                                    System.out.println("Anna numero kokonaisluku, joka asetetaan pelialueeksi: ");
                                    String luku = kayttajaIn.readLine();
                                    laheta("ACK 300 ", kayttajaSoketti, ipAdd);
                                    laheta("DATA " + luku, kayttajaSoketti, ipAdd);
                                    tila = "GAME";
                                    break; // break for case 202
                                
                                case"203":
                                    System.out.println("Vastustajasi on " + palat[2]);
                                    System.out.println("Vastustaja saa aloittaa!");
                                    tila = "GAME";
                                    break; // break for case 203
                                
                                default:
                                System.out.println("virhe " + palat[1] );
                            } // palat [1] loppu
                        break;
                    default:
                        break;
                    }// palat [0] loppu
                    break; // break for case JOIN
                case "GAME" :
                    switch (palat[0]) {
                        case "DATA" :
                            laheta("ACK 300 ", kayttajaSoketti, ipAdd);
                            System.out.println("Anna numero");
                            String luku  = kayttajaIn.readLine();
                            laheta("DATA " + luku, kayttajaSoketti, ipAdd);
                            break; // break for case DATA
                        
                        case "ACK":
                            switch(palat[1]) {
                                case"300":
                                    break; //break for case 300
                                case"400":
                                    System.out.println("Määrittelemätön virhe");
                                    break; // break for case 400
                                case"401":
                                    System.out.println("JOIN ei jostain syystä onnistu");
                                    break; // break for case 401
                                case"402":
                                    System.out.println("Ei ole sinun vuorosi");
                                    break; // break for case 402
                                case"403":
                                    System.out.println("ACK viesti virheellinen");
                                    break; // break for case 403
                                case"404":
                                    System.out.println("Väärä kehysrakenne");
                                    break; // break for case 404
                                case"407":
                                    System.out.println("Arvaus ei ollut numero");
                                    break; // break for case 407
                                case"409":
                                    System.out.println("Lukualueen asettaminen ei onnistunut");
                                    break;
                            default:
                                    break;
                            }// switch palat[1]
                            break; // break for case ACK
                        
                        case "QUIT":
                            switch(palat[1]) {
                                case"501":
                                    for(int i = 2; i<palat.length; i++) {
                                        System.out.print(palat[i].toString());
                                    }
                                    System.out.println("Lopetetaan peli");
                                    laheta("ACK 500 ", kayttajaSoketti, ipAdd);
                                    on = false;
                                    break; // break for case 501
                                
                                case"502":
                                    for(int i = 2; i<palat.length; i++) {
                                        System.out.print(palat[i].toString() + " ");
                                    }
                                    System.out.println("Lopetetaan peli");
                                    laheta("ACK 500 ", kayttajaSoketti, ipAdd);
                                    on = false;
                                    break; // break for case 502
                            default:
                                break;
                                    
                            } // QUIT switch(palat[1])
                            
                            break; // break for case QUIT
                    
                        default:
                            break;
                    }
                    break; // break for case GAME
                default:
                    break;
            } // tila loppuu
        }
        
        kayttajaSoketti.close();
        kayttajaIn.close();
    }
}
