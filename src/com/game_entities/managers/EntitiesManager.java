package com.game_entities.managers;

import java.util.ArrayList;

import com.client.entities.Entity;
import com.client.entities.PNJ;
import com.client.load.LoadPnj;
import com.client.network.NetworkListener;

public class EntitiesManager implements NetworkListener
{
	private MonstersManager monsters_manager;
	private PlayersManager players_manager;
	private PNJsManager pnjs_manager;
	private ArrayList<Entity> entities;
	
	public static EntitiesManager instance;
	
	public EntitiesManager()
	{
		instance = this;
		entities = new ArrayList<Entity>();
	}
	
	private void refreshEntities()
	{
		this.entities.removeAll(entities);
		for(int i = 0; i < players_manager.getJoueurs().size(); i++)
		{
			this.entities.add(players_manager.getJoueurs().get(i));
		}
		/*for(int i = 0; i < monsters_manager.getMonsters().size(); i++)
		{
			this.entities.add(monsters_manager.getMonsters().get(i));
		}*/
		for(int i = 0; i < pnjs_manager.getPnjs().size(); i++)
		{
			this.entities.add(pnjs_manager.getPnjs().get(i));
		}
	}
	

	@Override
	public void receiveMessage(String str) 
	{
		String[] temp = str.split(";");
		
		if(temp[2].contains("pnj"))
		{
			PNJ pnj = LoadPnj.loadPnj(str.substring(temp[0].length()+1+temp[1].length()+1+temp[2].length()+1, str.length()));
			pnjs_manager.add(pnj);
		}
	}
	
	public MonstersManager getMonsters_manager() {
		return monsters_manager;
	}

	public void setMonsters_manager(MonstersManager monstersManager) {
		monsters_manager = monstersManager;
	}

	public PlayersManager getPlayers_manager() {
		return players_manager;
	}

	public void setPlayers_manager(PlayersManager playersManager) {
		players_manager = playersManager;
	}

	public PNJsManager getPnjs_manager() {
		return pnjs_manager;
	}

	public void setPnjs_manager(PNJsManager pnjsManager) {
		pnjs_manager = pnjsManager;
	}


	public ArrayList<Entity> getEntities() {
		this.refreshEntities();
		return entities;
	}


	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}

	
	
	
}
