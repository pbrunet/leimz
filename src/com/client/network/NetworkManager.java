package com.client.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.UnknownHostException;

import java.util.ArrayList;
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
import com.map.client.managers.MapManager;

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

	private ArrayList<String> message_recu_serveur;
	private Timer t;


	private Thread handleServerMessages;

	private static long timeout = 5000;

	public static NetworkManager instance;

	public NetworkManager(String ip, int port) throws UnknownHostException, IOException
	{
		message_recu_serveur = new ArrayList<String>();
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

	public void waitForNewMessage()
	{
		long start = System.currentTimeMillis();
		while(receiveFromServer() == null){
			if((System.currentTimeMillis() - start) > timeout)
				break;
		}
	}

	public String receiveFromServer()
	{
		try
		{
			if(receiveFromServerPossible())
				return message_recu_serveur.get(message_recu_serveur.size()-1);
			else
				return null;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	public boolean receiveFromServerPossible() 
	{
		try
		{
			message_recu_serveur.add(br.readLine());
			return true;
		}
		catch(Exception e)
		{
			return false;
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
					String message = receiveFromServer();
					if(message != null)
					{
						String[] temp = message.split(";");

						if(temp[0].equals("s"))
						{
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

						else if(temp[0].equals("i"))
						{
							System.out.println("infos : "+message);
							EntitiesManager.instance.getPlayers_manager().addNewPlayer(new Joueur(new Personnage(temp[1], temp[2], temp[3]), null, Orientation.BAS));
							EntitiesManager.instance.getPlayers_manager().getJoueurs().get(EntitiesManager.instance.getPlayers_manager().getJoueurs().size()-1).initImgs();
						}
						
						else if(temp[0].equals("co"))
						{
							CombatManager.instance.receiveMessage(message);
						}
						else if(temp[0].equals("a"))
						{
							MainJoueur.instance.getPerso().getCaracs().put(Caracteristique.VIE, MainJoueur.instance.getPerso().getCaracs().get(Caracteristique.VIE)-Integer.parseInt(temp[2]));
							NetworkManager.instance.sendToServer("s;vie;"+MainJoueur.instance.getPerso().getCaracs().get(Caracteristique.VIE)+";"+temp[1]);
						}
						else if(temp[0].equals("sa"))
						{
							PrincipalGui.instance.getChat_frame().receiveMessage(message);
						}
						else if(temp[0].equals("lo"))
						{
							//Loading dynamique
							if(temp[1].equals("map"))
							{
								
							}
							else if(temp[1].equals("ent"))
							{
								EntitiesManager.instance.receiveMessage(message);
							}
						}
					}
				}
			}
		});
		handleServerMessages.start();

	}

	public String getMessage_recu_serveur() 
	{
		if(!message_recu_serveur.isEmpty())
		{
			 String message = message_recu_serveur.remove(0);
			return message;
		}
		return "";
	}

	public void setMessage_recu_serveur(String messageRecuServeur) 
	{
		message_recu_serveur.add(messageRecuServeur);
	}
}
