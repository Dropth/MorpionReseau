import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;


public class MorpionClient {

	private DatagramSocket ds;
    private String server_address;
    private int server_port;
    private int code=0;

    public MorpionClient (String server_address, int server_port){
	this.server_address = server_address;
	this.server_port = server_port;
	// Cr�ation d'une socket sur le port 5001 pour le client
	try{
	    ds = new DatagramSocket (5001);
	}
	catch (Exception e){
	    System.out.println (e);
	}
    }

    public void start (){
	DatagramPacket dpr;
	int i=0;
	byte [] buf=null;
	try{
	    // Cr�ation d'un DatagramPacket pour envoi au serveur dont le nom et le port ont �t� pr�cis� en entr�e
	    //System.out.println ("Envoi d'un message � "+server_address);
		
		while (code != 2 && code !=3 && code!=4) {
			
		    System.out.println("A toi de jouer (Yuuuuuuuuuuuuuu...)");
		    Scanner sc = new Scanner(System.in);
		    String envoyer = sc.nextLine();
		    
		    
		    
		    
		    buf = (envoyer.getBytes());
		    DatagramPacket dps = new DatagramPacket (buf,buf.length,InetAddress.getByName(server_address),server_port);
		  //  System.out.println ("Cr�ation du Datagram et envoi ...");
		    // L'envoi se fait � partir de la socket client, l'adresse 
		    // de destination �tant pr�cis�e dans le DatagramPacket
		    ds.send (dps);
		    System.out.println ("Envoi effectu�");
		    // Cr�ation d'une socket pour recevoir les informations du serveur
		    buf = new byte [1000];
		    dpr = new DatagramPacket(buf,buf.length);
		    System.out.println ("Cr�ation du Datagram pour r�ception");
		    ds.receive (dpr);
		    decoder(new String (dpr.getData(),0,dpr.getLength()));
		    System.out.println ("Num�ro attribu� par le serveur : '"+new String (dpr.getData(),0,dpr.getLength())+"'");
	    
		}
	}
	catch (Exception e){
	    System.out.println (e);
	}
    }
    
    public void decoder(String code) {
    	
    	String[] message = code.split(",");
    	
    	int num = Integer.parseInt(message[0]);
    	this.code = num;
    	
    	if(num == 0) {
    		System.out.println(afficherGrille(message));
    	}
    	else if (num == 1) {
    		System.out.println("Erreur dans votre saisie !");
    		System.out.println(afficherGrille(message));
    	}
    	else if (num == 2) {
    		System.out.println("Vous avez gagn� GG !");
    		System.out.println(afficherGrille(message));
    	}
    	else if (num == 3){
    		System.out.println("Vous avez perdu... Dommage !");
    		System.out.println(afficherGrille(message));
    	}
    	else {
    		System.out.println("Egalite !");
    		System.out.println(afficherGrille(message));
    	}
    }
    
    public String afficherGrille (String[] tabM) {
    	
    	int cpt = 1;
    	String ret = "";
    	
    	for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                ret+=tabM[cpt]+" | ";
                cpt++;
            }
            ret+="\n";
        }
        return ret;
    }

    public static void main (String args []){
	//toi :
    
    int port = 5555;
    String ip = "192.168.43.178";
	MorpionClient hc = new MorpionClient (ip, port);
        hc.start();
    }
}
