package com.client.network;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.client.entities.MainJoueur;
import com.client.utils.gui.PrincipalGui;
import com.game_entities.managers.EntitiesManager;
import com.gameplay.Caracteristique;
import com.gameplay.managers.CombatManager;

public class NetworkManager 
{
	private BufferedReader br;
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
	
	public void startRefreshMessages()
	{
		t.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				 pw.println("re;");
			     pw.flush();
			}
		}, 0, 50);
	}

	public void init()
	{
		t = new Timer();
		t.scheduleAtFixedRate(new StayConnectedTask(pw),0,800);

		handleServerMessages = new Thread(new Runnable() {

			@Override
			public void run() 
			{
				while(true)
				{
					receiveFromServerPossible();
					
					String state_message = receiveFromServer("s"); 
					if(state_message != null)
					{
						EntitiesManager.instance.receiveMessage("s;"+state_message);
					}
						
					String combat_message = receiveFromServer("fi"); 
					if(combat_message != null)
					{
						CombatManager.instance.receiveMessage(combat_message);
					}
					
					String attack_message = receiveFromServer("a"); 
					if(attack_message != null)
					{
						String[] temp = attack_message.split(";");
						String sender = temp[0];
						String nom_sort = temp[1];
						int degats = Integer.parseInt(temp[2]);
						
						MainJoueur.instance.getPerso().getCaracs().put(Caracteristique.VIE, MainJoueur.instance.getPerso().getCaracs().get(Caracteristique.VIE)-degats);
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
							EntitiesManager.instance.receiveMessage("lo;"+load_message);
						}
					}
				}
			}
		});
		handleServerMessages.start();

	}
}
