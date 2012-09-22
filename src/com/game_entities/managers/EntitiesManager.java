package com.game_entities.managers;

public class EntitiesManager 
{
	private MonstersManager monsters_manager;
	private PlayersManager players_manager;
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
	
	
}
