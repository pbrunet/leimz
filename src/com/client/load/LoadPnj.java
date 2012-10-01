package com.client.load;

import java.util.ArrayList;

import com.client.network.NetworkManager;

import com.game_entities.Orientation;
import com.game_entities.PNJ;
import com.game_entities.managers.PNJsManager;

import com.gameplay.PNJ_discours;
import com.map.client.managers.MapManager;

/**
 * 
 * @author chelendil
 * Classe chargeant les informations de classe et de race du joueur (sort, competences ...)
 */
public class LoadPnj implements Runnable
{
	private Thread looper;
	private int purcent;
	private boolean running;
	private PNJsManager pnjs_manager;

	public LoadPnj()
	{
		this.purcent = 0;
		pnjs_manager = new PNJsManager();
		looper = new Thread(this);
		looper.start();
		running = true;
	}

	@Override
	public void run() 
	{
		if(running)
		{
			NetworkManager network = NetworkManager.instance;

			purcent += 13;

			//-------------------GESTION DES PNJs-----------------------

			network.sendToServer("lo;pnj"); //load pnjs
			network.waitForNewMessage();
			String s = network.getMessage_recu_serveur();
			String[] args_pnj = s.split("new;");
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

	public PNJsManager getPnjs_manager() {
		return pnjs_manager;
	}

	public void setPnjs_manager(PNJsManager pnjs_manager) {
		this.pnjs_manager = pnjs_manager;
	}
}
