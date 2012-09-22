package com.client.load;

import java.util.ArrayList;

import com.client.network.NetworkManager;

import com.game_entities.Orientation;
import com.game_entities.PNJ;
import com.game_entities.managers.EntitiesManager;
import com.game_entities.managers.PNJsManager;

import com.gameplay.PNJ_discours;
import com.map.client.managers.MapManager;

/**
 * 
 * @author chelendil
 * Classe chargeant les informations de classe et de race du joueur (sort, competences ...)
 */
public class LoadEntities implements Runnable
{
	private Thread looper;
	private int purcent;
	private boolean running;
	private EntitiesManager entities_manager;

	public LoadEntities(int purcent)
	{
		this.purcent = purcent;
		looper = new Thread(this);
		looper.start();
		running = true;
	}

	@Override
	public void run() 
	{
		if(running)
		{
			entities_manager = new EntitiesManager();
			
			System.out.println("Loading entities ...");

			NetworkManager network = NetworkManager.instance;

			purcent += 13;
			
			//-------------------GESTION DES PNJs-----------------------
			
			network.sendToServer("lo;pnj"); //load pnjs
			network.waitForNewMessage();
			String s = network.getMessage_recu_serveur();
			System.out.println(s);
			String[] args_pnj = s.split(";");
			
			PNJsManager pnjs_manager = new PNJsManager();
			int i=0;
			while(i < args_pnj.length-1)
			{
				ArrayList<PNJ_discours> pnj_discours = new ArrayList<PNJ_discours>();
				int n=-1;
				do
				{
					n+=2;
					String[] t = args_pnj[i+n+1].split("-");
					ArrayList<Integer> tree = new ArrayList<Integer>();
					for(int k = 0; k < t.length; k++)
					{
						if(t[k].contains("|s|"))
						{
							tree.add(Integer.parseInt(t[k].substring(0,1)));
						}
						else
						{
							//tree.add(Integer.parseInt(t[k]));
						}
					}
					pnj_discours.add(new PNJ_discours(args_pnj[i+n], tree, null));
					System.out.println(args_pnj[i+n+1]);
				}
				while(!args_pnj[i+n+1].contains("|s|"));
				
				ArrayList<ArrayList<String>> reponses = new ArrayList<ArrayList<String>>();
				reponses.add(new ArrayList<String>());
				int id_pnj = 1, index = 0;
				do
				{
					n+=2;
					if(Integer.parseInt(args_pnj[i+n]) != id_pnj)
					{
						pnj_discours.get(id_pnj-1).setReponses(reponses.get(index));
						reponses.add(new ArrayList<String>());
						index++;
					}
					id_pnj = Integer.parseInt(args_pnj[i+n]);			
					
					if(Integer.parseInt(args_pnj[i+n]) != id_pnj)
					{
						pnj_discours.get(id_pnj-1).setReponses(reponses.get(index));
						reponses.add(new ArrayList<String>());
						index++;
					}
					id_pnj = Integer.parseInt(args_pnj[i+n]);
					if(args_pnj[i+n+1].contains("|s|"))
					{
						reponses.get(index).add(args_pnj[i+n+1].substring(0, args_pnj[i+n+1].length()-3));
						pnj_discours.get(id_pnj-1).setReponses(reponses.get(index));
					}
					else
					{
						reponses.get(index).add(args_pnj[i+n+1]);
					}
					
					System.out.println(args_pnj[i+n+1]);
				}
				while(!args_pnj[i+n+1].contains("|s|"));
				
				pnjs_manager.add(new PNJ(
						args_pnj[0], 
						pnj_discours,
						Orientation.BAS, 
						MapManager.instance.getEntire_map().getGrille().get(Integer.parseInt(args_pnj[args_pnj.length-2])).get(Integer.parseInt(args_pnj[args_pnj.length-1]))));
				i+=(2+n+20);
			
			}
			entities_manager.setPnjs_manager(pnjs_manager);
			
			try {
				//TODO Pourquoi un sleep?
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			running = false;
		}
	}

	public int getPurcent() {
		return purcent;
	}

	public void setPurcent(int purcent) {
		this.purcent = purcent;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public EntitiesManager getEntities_manager() {
		return entities_manager;
	}

	public void setEntities_manager(EntitiesManager entitiesManager) {
		entities_manager = entitiesManager;
	}
}
