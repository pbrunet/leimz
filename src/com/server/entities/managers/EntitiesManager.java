package com.server.entities.managers;

import java.util.ArrayList;


import com.server.entities.Entity;

public class EntitiesManager 
{
	private MonstersManager monsters_manager;
	private ClientsManager clients_manager;
	private PNJsManager pnjs_manager;
	private ArrayList<Entity> entities;
	public static EntitiesManager instance;
	
	public EntitiesManager()
	{
		instance = this;
		clients_manager = new ClientsManager();
		entities = new ArrayList<Entity>();
	}

	private void refreshEntities()
	{
		this.entities.removeAll(entities);
		for(int i = 0; i < clients_manager.getClients().size(); i++)
		{
			this.entities.add(clients_manager.getClients().get(i).getCompte().getCurrent_joueur());
		}
		for(int i = 0; i < pnjs_manager.getPnjs().size(); i++)
		{
			this.entities.add(pnjs_manager.getPnjs().get(i));
		}
	}
	
	public MonstersManager getMonsters_manager() {
		return monsters_manager;
	}

	public void setMonsters_manager(MonstersManager monstersManager) {
		monsters_manager = monstersManager;
	}
	
	public ClientsManager getClients_manager() {
		return clients_manager;
	}

	public void setClients_manager(ClientsManager clientsManager) {
		clients_manager = clientsManager;
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
