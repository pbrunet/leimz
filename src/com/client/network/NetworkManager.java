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

import com.game_entities.Joueur;
import com.game_entities.Orientation;
import com.game_entities.managers.EntitiesManager;
import com.gameplay.entities.Personnage;
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

	private ArrayList<NetworkListener> listeners;

	private ArrayList<String> message_recu_serveur;
	private Timer t;

	private EntitiesManager visible_entities_manager;
	private MapManager mapManager;

	private Thread checkListenersSending, handleServerMessages;

	private static long timeout = 5000;

	public boolean modifManager = false;
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

	public void init(MapManager mapManager2, EntitiesManager ent, ArrayList<NetworkListener> listeners2)
	{
		this.listeners = listeners2;
		this.mapManager = mapManager2;
		this.visible_entities_manager = ent;

		checkListenersSending = new Thread(new Runnable() {

			@Override
			public void run() 
			{
				while(true)
				{
					for(int i = 0; i < listeners.size(); i++)
					{
						if(listeners.get(i).wantSending())
						{
							sendToServer(listeners.get(i).messageToSend());
							listeners.get(i).setSentOk();
						}
					}
				}
			}
		});
		checkListenersSending.start();

		handleServerMessages = new Thread(new Runnable() {

			@Override
			public void run() 
			{
				while(true)
				{
					modifManager = false;
					String message = receiveFromServer();
					if(message != null)
					{
						String[] temp = message.split(";");

						if(temp[0].equals("s"))
						{
							if(!visible_entities_manager.getPlayers_manager().getMain_player().getPerso().getNom().equals(temp[1]))
							{
								modifManager = true;
								boolean contains = false;
								int index = 0;
								for(int i = 0; i < visible_entities_manager.getPlayers_manager().getJoueurs().size(); i++)
								{
									if(visible_entities_manager.getPlayers_manager().getJoueurs().get(i).getPerso().getNom().equals(temp[1]))
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
									visible_entities_manager.getPlayers_manager().getJoueurs().get(index).setPos_real(new Vector2f(Float.parseFloat(temp[3]), Float.parseFloat(temp[4])));
									visible_entities_manager.getPlayers_manager().getJoueurs().get(index).setOrientation(Joueur.parseStringOrientation(temp[5]));				
								}
							}
						}

						if(temp[0].equals("i"))
						{
							System.out.println("infos : "+message);
							System.out.println("infos recues !");
							visible_entities_manager.getPlayers_manager().addNewPlayer(new Joueur(new Personnage(temp[1], null, null, null, null), null, Orientation.BAS));
							visible_entities_manager.getPlayers_manager().getJoueurs().get(visible_entities_manager.getPlayers_manager().getJoueurs().size()-1).initImgs();
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

	public EntitiesManager getVisible_entities_manager() {
		return visible_entities_manager;
	}

	public void setVisible_entities_manager(EntitiesManager visibleEntitiesManager) {
		visible_entities_manager = visibleEntitiesManager;
	}

	public MapManager getMapManager() {
		return mapManager;
	}

	public void setMapManager(MapManager mapManager) {
		this.mapManager = mapManager;
	}
}
