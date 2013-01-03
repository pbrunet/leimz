package com.server.entities.managers;

import java.util.ArrayList;

import com.map.Tile;
import com.map.server.managers.MapManager;
import com.server.core.GlobalConstant;
import com.server.entities.Entity;
import com.server.entities.Joueur;

public class EntitiesManager 
{
	private MonstersManager monsters_manager;
	private PNJsManager pnjs_manager;
	private PlayersManager players_manager;
	
	
	private ArrayList<Entity> entities;
	public static EntitiesManager instance;
	
	public EntitiesManager()
	{
		instance = this;
		entities = new ArrayList<Entity>();
		players_manager = new PlayersManager();
	}

	private void refreshEntities()
	{
		this.entities.removeAll(entities);
		for(int i = 0; i < players_manager.getJoueurs().size(); i++)
		{
			this.entities.add(players_manager.getJoueurs().get(i));
		}
		for(int i = 0; i < pnjs_manager.getPnjs().size(); i++)
		{
			this.entities.add(pnjs_manager.getPnjs().get(i));
		}
	}
	
	public ArrayList<Entity> getEntitiesAround(Entity entity)
	{
		refreshEntities();
		ArrayList<Entity> list_around = new ArrayList<Entity>();
		
		ArrayList<ArrayList<Tile> > grille = MapManager.instance.getTilesAutour(entity.getTile(), GlobalConstant.nbCaseNear);
		for(int i = 0; i < grille.size(); i++)
		{
			for(int j = 0; j < grille.get(i).size(); j++)
			{
				for(int u = 0; u < entities.size(); u++)
				{
					if(entities.get(u).getTile().equals(grille.get(i).get(j)))
					{
						list_around.add(entities.get(u));
					}
				}
			}
		}
		
		return list_around;
	}
	
	public ArrayList<Joueur> getPlayersAround(Entity entity)
	{
		ArrayList<Joueur> list_around = new ArrayList<Joueur>();
		ArrayList<Entity> entities_around = getEntitiesAround(entity);
		for(int i = 0; i < entities_around.size(); i++)
		{
			if(entities_around.get(i) instanceof Joueur)
			{
				list_around.add((Joueur) entities_around.get(i));
			}
		}
		
		return list_around;
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

	public void setPlayers_manager(PlayersManager players_manager) {
		this.players_manager = players_manager;
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
