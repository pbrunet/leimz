package com.server.entities.managers;

public class EntitiesManager 
{
	private MonstersManager monsters_manager;
	private ClientsManager clients_manager;
	private PNJsManager pnjs_manager;
	public static EntitiesManager instance;
	
	public EntitiesManager()
	{
		instance = this;
		clients_manager = new ClientsManager();
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
	
	
}
