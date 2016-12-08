import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class MorpionServeur {
	
	//1 = Coordon�e invalide
	//2 = win
	//3 = lose
	//4 = draw #sacamerde
	
	 private DatagramSocket dg;
	 private boolean tourDuServeur = false;
	 private int[][] grille;

	    /**
	     * Le serveur cr�e une socket pour �couter sur le port pr�cis� en param�tre
	     */
	    public MorpionServeur (int port){
		try{
		    dg = new DatagramSocket (port);
		}
		catch (Exception e){
		    System.out.println ("Constructeur :"+e);
		}
	    }

	    public void start (){
		DatagramPacket dpr;
		System.out.println ("D�marrage du serveur");
		int i = 1;
		
		grille = new int[3][3];
		String recu ="";
		int x;
		int y;
		int erreur;
		
		// D�marrage de la boucle d'attente des connexions clients
		while (true) {
		    // Cr�ation du DatagramPacket de r�ception
		    byte [] buf = new byte [1000];
		    dpr = new DatagramPacket(buf, buf.length);
		    try {
			// Attente de la r�ception d'un DatagramPacket
			dg.receive (dpr);
			i++;
			System.out.println ("R�ception d'un message de "+dpr.getAddress().getHostName()+"/"+dpr.getPort());
			recu = new String(dpr.getData(), 0, dpr.getLength());
			System.out.println ("Message re�u : "+recu);
			buf = Integer.toString (i).getBytes ();

		        //System.out.println(new String(buf));
			
			if(recu.length() == 2){
				
				x = Integer.parseInt(recu.substring(0, 1));
				y = Integer.parseInt(recu.substring(1, 2));
				
				System.out.println("COUCOU : " + y);
				
				if(grille[x][y] != 0) {
					
					//Renvoi d'un message au client avec le num�ro attribu�
					
					erreur =1;
					
				}
				else {
					grille[x][y] = 1;
					
					if(isOk() == 2){
						
						erreur = 2;
					}
					else if(isOk() == 4) {
						
						erreur = 4;
					}
					else {
					
						//Tour du serveur
						
						tourDuServeur = false;
						
						while(!tourDuServeur) {
							
							int xServ = (int) (Math.random()*3);
							int yServ = (int) (Math.random()*3);
							
							if(grille[xServ][yServ] == 0) {
								grille[xServ][yServ] = 2;
								tourDuServeur = true;
							}
						}
						
						if (isOk() == 3){
							
							erreur = 3;
							
						}
						else if(isOk() == 4) {
							
							erreur = 4;
							
						}
						else {
							
							erreur = 0;
							
						}
						
						//
					}
				}
				
			}
			else {
				
				erreur = 1;
				
				}
			
			String retour = erreur +"," + encoder();
			
			System.out.println(retour);
			
			buf = retour.getBytes ();
			
			DatagramPacket dps = new DatagramPacket (buf, buf.length, dpr.getAddress(), dpr.getPort());
			System.out.println ("Num�ro attribu� par le serveur : "+new String (buf,0,buf.length));
			System.out.println ("Num�ro attribu� au client :" + i);
			dg.send (dps);
			
		    }
		    //catch(Error e){System.out.println(""+e);}
		    catch (Exception e) {
			//System.err.println (e);
			System.out.println (e.toString ());		
			System.exit(0);
		    }
		}
		
	    }

	    private int isOk(){
	        if(grille[0][0] == 1 && grille[0][1] == 1 && grille[0][2] == 1) return 2;
	        else if(grille[1][0] == 1 && grille[1][1] == 1 && grille[1][2] == 1) return 2;
	        else if(grille[2][0] == 1 && grille[2][1] == 1 && grille[2][2] == 1) return 2;
	        else if(grille[0][0] == 1 && grille[1][0] == 1 && grille[2][0] == 1) return 2;
	        else if(grille[0][1] == 1 && grille[1][1] == 1 && grille[2][1] == 1) return 2;
	        else if(grille[0][2] == 1 && grille[1][2] == 1 && grille[2][2] == 1) return 2;
	        else if(grille[0][0] == 1 && grille[1][1] == 1 && grille[2][2] == 1) return 2;
	        else if(grille[0][2] == 1 && grille[1][1] == 1 && grille[2][0] == 1) return 2;
	        else if(grille[0][0] == 2 && grille[0][1] == 2 && grille[0][2] == 2) return 3;
	        else if(grille[1][0] == 2 && grille[1][1] == 2 && grille[1][2] == 2) return 3;
	        else if(grille[2][0] == 2 && grille[2][1] == 2 && grille[2][2] == 2) return 3;
	        else if(grille[0][0] == 2 && grille[1][0] == 2 && grille[2][0] == 2) return 3;
	        else if(grille[0][1] == 2 && grille[1][1] == 2 && grille[2][1] == 2) return 3;
	        else if(grille[0][2] == 2 && grille[1][2] == 2 && grille[2][2] == 2) return 3;
	        else if(grille[0][0] == 2 && grille[1][1] == 2 && grille[2][2] == 2) return 3;
	        else if(grille[0][2] == 2 && grille[1][1] == 2 && grille[2][0] == 2) return 3;
	        else if(isGrilleFull()) return 4;
	        else return 0;
	    }

	    private boolean isGrilleFull(){
	        for(int i=0;i<3;i++){
	            for(int j=0;j<3;j++){
	                if(grille[i][j] == 0) return false;
	            }
	        }
	        return true;
	    }
	    
	    private String encoder() {
	    	
	    	String s ="";
	    	
	    	for(int i = 0; i<3;i++)
	    		for(int j =0; j<3; j++)
	    			s += grille[i][j] +",";
	    	
	    	return s;
	    }

		public static void main (String args []){
	    
		MorpionServeur hs = null;
		int port = 5555; // Par d�faut le port est 5000
		if (args.length == 1){
		    // sinon r�cup�ration en ligne de commande du num�ro de port
		    port = Integer.parseInt (args [0]);
		}
		hs = new MorpionServeur (port);
	        hs.start();
	}
}
