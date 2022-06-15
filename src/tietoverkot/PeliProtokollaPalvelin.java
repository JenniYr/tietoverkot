package tietoverkot;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Random;

/**
 * Tietoverkot harkkatyön tehtävä 5
 * Peliprotokollan palvelin osuus
 * 
 * Ohjelma ei ole täysin tilakoneen mukainen, koska tässä kysytään käyttäjältä numeroalue jolla pelataan
 * ja tämän jälkeen siirrytään heti waitack tilaan ja vasta sitten game tilaan
 * 
 * HUOM! Jostain syystä split ei jaa palasia kuten pitäisi.
 * Jos testaat ohjelmaa UDP asiakasohjelman kanssa, lyö ylimääräinen välilyönti viestin perään
 * 
 * Ohjelmaa ei ole testattu automaattisesti, joten jos työn läpi meneminen sen vaatii, toivotaan pikaista palautetta.
 * 
 * @author jenni yrjänä
 * @version 7 Jul 2020
 */
public class PeliProtokollaPalvelin {

    private static boolean tarkistaVuoro(DatagramPacket pakettiIn, int vuoro, int[] portit) {

        int portti = pakettiIn.getPort(); 
        if(portti == portit[vuoro]) return true;
        return false;
    }

    /**
     * @param str on kayttajan syöte
     * @param kayttajaSoketti on datagrammisoketti
     * @throws IOException 
     * 
     */
    public static void laheta(String str, DatagramSocket kayttajaSoketti) throws IOException {
        byte[] outData = new byte[1024];
        outData = str.getBytes();
        DatagramPacket paketti = new DatagramPacket(outData, outData.length, kayttajaSoketti.getInetAddress(), kayttajaSoketti.getPort());
        kayttajaSoketti.send(paketti);
    }

    /**
     * @param str on kayttajan syöte
     * @param ds 
     * @param dp 
     * @param portti 
     * @throws IOException 
     * 
     */
    public static void laheta(String str, DatagramSocket ds, DatagramPacket dp, int portti) throws IOException {
        byte[] outData = new byte[1024];
        outData = str.getBytes();
        DatagramPacket paketti = new DatagramPacket(outData, outData.length, dp.getAddress(), portti);
        ds.send(paketti);
    }

    /**
     * @param vuoro on tällä hetkellä vuorossa oleva indeksi
     * @return palauttaa vaihdetun indeksin
     */
    public static int vaihdaVuoro(int vuoro) {

        int vuoronVaihto  = vuoro;

        if(vuoro == 0) { 
            vuoronVaihto = 1; // En äkkiseltään keksinyt fiksumpaa ratkaisua

        }else vuoronVaihto = 0;

        return vuoronVaihto;
    }

    /**
     * @param portti 
     * @param portit on taulukko porttinumeroista
     * @param numerointi on seuraavan vapaa alkio
     * @return palauttaa taulukon porteista 
     * @throws IOException 
     */
    public static  int[] paivitaPortit(int portti, int[] portit, int numerointi) throws IOException {

        int[] uudetPortit = portit;

        for(int i = 0; i< portit.length; i++) {
            int porttinen = portit[i];
            if(portti == porttinen) return uudetPortit;
        }

        uudetPortit[numerointi] = portti;

        return uudetPortit;
    }

    /**
     * @param kayttajaSoketti
     * @param dp 
     * @return taulukon erotelluista sanoista
     * @throws IOException 
     * 
     */
    public static String[] vastaanota (DatagramSocket kayttajaSoketti, DatagramPacket dp ) throws IOException {

        kayttajaSoketti.receive(dp);
        String vastaanotettu = new String(dp.getData());
        String[] palat = vastaanotettu.split("[ ;]");
        return  palat;

    }

    /**
     * @param args ei käytössä
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {

        int portti = 9999;
        @SuppressWarnings("resource")
        DatagramSocket socket = new DatagramSocket(portti);   


        String tila = "WAIT";        
        boolean on = true;
        int vuoro = -1;
        int pelaajat = 0;
        //int quitAck = 0;
        int luku = -1;        
        int[] portit = new int[2];
        String[] nimi = new String[2];        
        int numerointi = 0;

        while(on) {

            byte[] inData = new byte[1024];
            DatagramPacket pakettiIn = new DatagramPacket(inData, inData.length);

            String[] palat = vastaanota(socket, pakettiIn);
            portit = paivitaPortit(pakettiIn.getPort(), portit, numerointi);
            for(int i = 0; i< palat.length; i++) {
                System.out.println(palat[i].toString());
            }

            switch(tila) {
            case "WAIT":
                switch(palat[0]) {
                case"JOIN":                              
                    nimi[numerointi] = palat[1];
                    pelaajat++;
                    numerointi++;
                    if(pelaajat == 1) {
                        laheta("ACK 201 ", socket, pakettiIn, portit[0]); // Tämä olikin kumma juttu, ilman merkkijono perässä olevaa välilyöntiä split ei toiminut oikein ja sain aina tulokseksi defaultin. 
                    }
                    if(pelaajat == 2) {
                        Random rand = new Random();
                        vuoro = rand.nextInt(2);

                        laheta("ACK 202 " + nimi[vaihdaVuoro(vuoro)].toString() + " ", socket, pakettiIn, portit[vuoro]);
                        if(vuoro == 0)laheta("ACK 203 " + nimi[vuoro].toString() + " ", socket, pakettiIn, portit[1]);
                        if(vuoro == 1)laheta("ACK 203 " + nimi[vuoro].toString() + " ", socket, pakettiIn, portit[0]);
                        tila = "WAITACK"; // kuvasta poiketen siirrytään WAITACK tilaan, koska tässä aloittavalta pelaajalta kysytään lukualue jolla pelataan.                                 
                    }
                    break; // break for case JOIN
                default:
                    laheta("ACK 401 ", socket, pakettiIn, portit[vuoro]);
                    break;
                }
                break; // break for case WAIT
            case "GAME":
                if(tarkistaVuoro(pakettiIn, vuoro, portit) == false) {
                    if(vuoro == 1) {
                        laheta("ACK 402", socket, pakettiIn, portit[0]);
                    } else laheta("ACK 402", socket, pakettiIn, portit[1]);
                    break;
                }
                switch(palat[0]) {
                case"DATA":

                    if(luku == -1) {
                        try{                                    
                            luku = Integer.parseInt(palat[1].trim());
                            Random rand = new Random();
                            luku = rand.nextInt(luku);
                        } catch (Exception e) {
                            e.getMessage();
                        }finally {
                            laheta("DATA ", socket, pakettiIn, portit[vuoro]); 
                        }
                    } else {
                        try {
                            int apuluku = Integer.parseInt(palat[1].trim());
                            if(apuluku != luku) {
                                vuoro= vaihdaVuoro(vuoro);                                              
                                laheta("DATA ", socket, pakettiIn, portit[vuoro]);
                                tila = "WAITACK";
                            }else {
                                laheta("QUIT 501 ", socket, pakettiIn, portit[vuoro]);
                                vuoro = vaihdaVuoro(vuoro);
                                laheta("QUIT 502 ", socket, pakettiIn, portit[vuoro]);
                                tila = "END";
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            laheta("ACK 407 ", socket, pakettiIn, portit[vuoro]);
                            laheta("DATA ", socket, pakettiIn, portit[vuoro]);
                            tila = "WAITACK";
                        }

                    } // elsen lopetus
                    break; // break for case DATA
                default:
                    laheta("ACK 404 ", socket, pakettiIn, portit[vuoro]);
                    break;               
                }
                break; // break for case GAME
            case"WAITACK":
                switch(palat[0]) {
                case"ACK":
                    switch(palat[1]) {
                    case"300":
                        tila = "GAME";
                        break;
                    default:
                        laheta("ACK 403 ", socket, pakettiIn, portit[vuoro]);
                        break;
                    
                    } // end for WAITACK ACK switch
                    break;
                default:
                    laheta("ACK 404 Väärä kehysrakenne ", socket, pakettiIn, portit[vuoro]);
                    break;
                } // end for WAITACK switch
                break; // break for case WAITACK
            case"END":
                switch(palat[0]) {
                case"ACK":
                    switch(palat[1]) {
                    case"500":
                        pelaajat--;
                        if(pelaajat == 0) on = false;
                        break;
                    default:
                        laheta("ACK 403 ", socket, pakettiIn, portit[vuoro]);
                        break;
                    }// end for ACK switch

                    break;
                default:
                    laheta("ACK 404 ", socket, pakettiIn, portit[vuoro]);
                    break;

                } // end for END switch
                break; // break for fase END
            default:
                laheta("ACK 400 ", socket, pakettiIn, portit[vuoro]);
                break;
            }           
        } // while silmukkan lopetus
        socket.close();
    }
}
