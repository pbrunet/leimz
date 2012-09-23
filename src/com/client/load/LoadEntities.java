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
			String[] args_pnj = s.split("new;");
			PNJsManager pnjs_manager = new PNJsManager();
			for( int i=1;i<args_pnj.length;i++)
			{
				String[] pnj = args_pnj[i].split(";");
				PNJ_discours pnj_discours = new PNJ_discours(pnj[3], null, new ArrayList<PNJ_discours>(),0);
				pnj_discours.fillTree(4, pnj);
				pnjs_manager.add(new PNJ(
						pnj[2], 
						pnj_discours,
						Orientation.BAS, 
						MapManager.instance.getEntire_map().getGrille().get(Integer.parseInt(pnj[0])).get(Integer.parseInt(pnj[1]))));
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
