package com.game_entities.server.managers;

public class EntitiesManager 
{
	private MonstersManager monsters_manager;
	private ClientsManager clients_manager;
	private PNJsManager pnjs_manager;
	
	public EntitiesManager()
	{
		
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
