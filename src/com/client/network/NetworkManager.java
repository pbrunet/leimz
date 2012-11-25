package com.client.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import org.newdawn.slick.geom.Vector2f;
import com.client.entities.Joueur;
import com.client.entities.MainJoueur;
import com.client.entities.Orientation;
import com.client.utils.gui.PrincipalGui;
import com.game_entities.managers.EntitiesManager;
import com.gameplay.Caracteristique;
import com.gameplay.entities.Personnage;
import com.gameplay.managers.CombatManager;

/**
 * @author Kratisto
 */
public class NetworkManager 
{
	/**
	 *Objet permettant de lire ce que le serveur envoie
	 *@see BufferedReader
	 */
	private BufferedReader br;
	/**
	 * Objet permettant d'ecrire au server
	 * @see PrintWriter
	 */
	private PrintWriter pw; 
	private Socket s;

	private HashMap<String,String> message_recu_serveur;
	private Timer t;


	private Thread handleServerMessages;

	private static long timeout = 10000;

	public static NetworkManager instance;

	public NetworkManager(String ip, int port) throws UnknownHostException, IOException
	{
		message_recu_serveur = new HashMap<String,String>();
		s = new Socket(ip, port);
		s.setSoTimeout(10);
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		pw = new PrintWriter(s.getOutputStream());
		t = new Timer();
		t.scheduleAtFixedRate(new StayConnectedTask(pw),0,800);
		instance = this;
	}

	public void sendToServer(String message)
	{
		pw.println(message);
		pw.flush();
	}

	public void waitForNewMessage(String name)
	{
		long start = System.currentTimeMillis();
		do {
			receiveFromServerPossible();
			if((System.currentTimeMillis() - start) > timeout)
			{
				System.err.println("Message "+ name + " du serveur non reçu");
				System.err.println("Message en stock : " + message_recu_serveur.toString());
				System.exit(1);
			}
		} while(!message_recu_serveur.containsKey(name));
	}

	public String receiveFromServer(String name)
	{
		String res = null;
		try
		{
			if(message_recu_serveur.containsKey(name))
			{
				res = message_recu_serveur.get(name);
				message_recu_serveur.remove(name);
			}
		}
		catch(Exception e){}
		return res;
	}

	public void receiveFromServerPossible() 
	{
		try
		{
			String res[] = br.readLine().split(";", 2);
			message_recu_serveur.put(res[0],res[1]);
		}
		catch(Exception e){
		}
	}

	public void init()
	{
		handleServerMessages = new Thread(new Runnable() {

			@Override
			public void run() 
			{
				while(true)
				{
					receiveFromServerPossible();
					
					String state_message = receiveFromServer("s"); 
					if(state_message != null && false)
					{
						String[] temp = state_message.split(";");
						if(!EntitiesManager.instance.getPlayers_manager().getMain_player().getPerso().getNom().equals(temp[1]))
						{
							boolean contains = false;
							int index = 0;
							for(int i = 0; i < EntitiesManager.instance.getPlayers_manager().getJoueurs().size(); i++)
							{
								if(EntitiesManager.instance.getPlayers_manager().getJoueurs().get(i).getPerso().getNom().equals(temp[1]))
								{
									contains = true;
									index = i;
								}
							}

							if(contains == false)
							{
								sendToServer("i;"+temp[1]);
							}
								else
								{
									if(temp[2].equals("pos"))
									{
										EntitiesManager.instance.getPlayers_manager().getJoueurs().get(index).setPos_real(new Vector2f(Float.parseFloat(temp[3]), Float.parseFloat(temp[4])));
										EntitiesManager.instance.getPlayers_manager().getJoueurs().get(index).setOrientation(Joueur.parseStringOrientation(temp[5]));				
									}
									
									else if(temp[2].equals("vie"))
									{
										int vie = Integer.parseInt(temp[3]);
										System.out.println("Vie : "+vie);
										EntitiesManager.instance.getPlayers_manager().getJoueur(temp[1]).getPerso().getCaracs().put(Caracteristique.VIE, vie);
									}
								}
							}
							else
							{
								if(temp[2].equals("vie"))
								{
									int vie = Integer.parseInt(temp[3]);
									System.out.println("Vie : "+vie);
									MainJoueur.instance.getPerso().getCaracs().put(Caracteristique.VIE, vie);
								}
							}
					}
				
					String message = receiveFromServer("i"); 
					if(message != null)
					{
						String[] temp = message.split(";");
							System.out.println("infos : "+message);
							EntitiesManager.instance.getPlayers_manager().addNewPlayer(new Joueur(new Personnage(temp[0], temp[1], temp[2]), null, Orientation.BAS));
							EntitiesManager.instance.getPlayers_manager().getJoueurs().get(EntitiesManager.instance.getPlayers_manager().getJoueurs().size()-1).initImgs();
						
					}
						
					String combat_message = receiveFromServer("co"); 
					if(combat_message != null)
					{
						CombatManager.instance.receiveMessage(message);
					}
					
					String attack_message = receiveFromServer("a"); 
					if(attack_message != null)
					{
						String[] temp = attack_message.split(";");
						MainJoueur.instance.getPerso().getCaracs().put(Caracteristique.VIE, MainJoueur.instance.getPerso().getCaracs().get(Caracteristique.VIE)-Integer.parseInt(temp[1]));
						NetworkManager.instance.sendToServer("s;vie;"+MainJoueur.instance.getPerso().getCaracs().get(Caracteristique.VIE)+";"+temp[0]);
					}
					
					String say_message = receiveFromServer("sa");
					if(say_message != null)
					{
						PrincipalGui.instance.getChat_frame().receiveMessage(say_message);
					}
					
					String load_message = receiveFromServer("lo");
					if(load_message != null)
					{
						String[] temp = load_message.split(";");
						//Loading dynamique
						if(temp[0].equals("map"))
						{
							
						}
						else if(temp[0].equals("ent"))
						{
							System.out.println("message transmitted to entities manager");
							EntitiesManager.instance.receiveMessage(load_message);
						}
					}
				}
			}
		});
		handleServerMessages.start();

	}
}
