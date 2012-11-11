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

import com.client.gamestates.Base;
import com.game_entities.Joueur;
import com.game_entities.Orientation;
import com.game_entities.managers.EntitiesManager;
import com.gameplay.Classe;
import com.gameplay.Race;
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

	private HashMap<String,String> message_recu_serveur;
	private Timer t;

	private EntitiesManager visible_entities_manager;
	private MapManager mapManager;

	private Thread checkListenersSending, handleServerMessages;

	private static long timeout = 10000;

	public boolean modifManager = false;
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
					receiveFromServerPossible();
					modifManager = false;
					String message = receiveFromServer("s"); 
					if(message != null && false)
					{
						String[] temp = message.split(";");
						if(!visible_entities_manager.getPlayers_manager().getMain_player().getPerso().getNom().equals(temp[0]))
						{
							modifManager = true;
							boolean contains = false;
							int index = 0;
							for(int i = 0; i < visible_entities_manager.getPlayers_manager().getJoueurs().size(); i++)
							{
								if(visible_entities_manager.getPlayers_manager().getJoueurs().get(i).getPerso().getNom().equals(temp[0]))
								{
									contains = true;
									index = i;
								}
							}

							if(contains == false)
							{
								sendToServer("i;s");
							}
							else
							{
								visible_entities_manager.getPlayers_manager().getJoueurs().get(index).setPos_real(new Vector2f(Float.parseFloat(temp[2]), Float.parseFloat(temp[3])));
								visible_entities_manager.getPlayers_manager().getJoueurs().get(index).setOrientation(Joueur.parseStringOrientation(temp[4]));				
							}
						}
					}

					message = receiveFromServer("aj"); 
					if(message != null)
					{
						String[] temp = message.split(";");
						String name = temp[0];
						String race = temp[1];
						String classe = temp[2];
						int posx = Integer.parseInt(temp[3]);
						int posy = Integer.parseInt(temp[4]);
						Orientation ori = Orientation.valueOf(temp[5]);

						System.out.println("infos : "+message);
						System.out.println("infos recues !");
						if(!visible_entities_manager.getPlayers_manager().getMain_player().getPerso().getNom().equals(name))
						{
							visible_entities_manager.getPlayers_manager().addNewPlayer(new Joueur(new Personnage(name, new Race(race), new Classe(classe), null, null), MapManager.instance.getEntire_map().getGrille().get(posx/Base.Tile_x).get(posy/Base.Tile_y), ori));
							visible_entities_manager.getPlayers_manager().getJoueurs().get(visible_entities_manager.getPlayers_manager().getJoueurs().size()-1).initImgs();
						}
					}
				}
			}
		});
		handleServerMessages.start();

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
