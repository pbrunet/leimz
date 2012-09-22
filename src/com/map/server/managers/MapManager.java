package com.map.server.managers;

import org.newdawn.slick.geom.Vector2f;

import com.map.Map;
import com.map.Tile;

public class MapManager 
{

	private CollisionManager collision_manager;

	private Map entire_map;

	public MapManager(Map entire_map)
	{
		this.entire_map = entire_map;

		this.init();

		//this.collision_manager = new CollisionManager(map_visible.getGrille(), null);
	}

	/*public void init(Tile tile)
	{
		Grille g = new Grille();

		for(int i = 0; i < )


		this.map_visible = new Map(g, entire_map.getDataMonstres());

	}*/


	public void refresh()
	{
		/*for(int i = 0; i < pnjs_manager.getPnjs().size(); i++)
		{
			pnjs_manager.getPnjs().get(i).refresh();
		}*/
	}

	/*public void refreshCollisionManager()
	{
		ArrayList<Entity> entities = new ArrayList<Entity>();
		for(int i = 0; i < players_manager.getJoueurs().size();i++)5
			entities.add(players_manager.getJoueurs().get(i).getEntity());
		for(int i = 0; i < pnjs_manager.getPnjs().size();i++)
			entities.add(pnjs_manager.getPnjs().get(i));
		this.collision_manager.setEntities(entities);
	}*/

	public void init()
	{		
		//On parcourt alors les lignes et les colonnes
		for(int i = 0; i < entire_map.getGrille().size(); i++)
		{
			for(int j = 0; j <entire_map.getGrille().get(i).size(); j++)
			{
				if(i%2 ==0) //Si la ligne est paire
				{
					//On determine la "vraie" position de la tile par rapport aux coordonnees
					entire_map.getGrille().get(i).get(j).setPos_screen(
							new Vector2f((entire_map.getGrille().get(i).get(j).getPos().x/2)*80, entire_map.getGrille().get(i).get(j).getPos().y*40));
					entire_map.getGrille().get(i).get(j).setPos_real(
							new Vector2f((entire_map.getGrille().get(i).get(j).getPos().x/2)*80, entire_map.getGrille().get(i).get(j).getPos().y*40));
				}
				else //Si la ligne est impaire
				{
					//On determine la "vraie" position de la tile par rapport aux coordonnees
					entire_map.getGrille().get(i).get(j).setPos_screen(
							new Vector2f((entire_map.getGrille().get(i).get(j).getPos().x*80)/2, (entire_map.getGrille().get(i).get(j).getPos().y*40)+20));
					entire_map.getGrille().get(i).get(j).setPos_real(
							new Vector2f((entire_map.getGrille().get(i).get(j).getPos().x*80)/2, (entire_map.getGrille().get(i).get(j).getPos().y*40)+20));
				}
			}
		}
	}

	/**
	 * 
	 * Retourne la tile en fonction du (x;y)
	 * 
	 * 
	 */
	public Tile getTileScreen(Vector2f posScreen)
	{
		for(int i=0; i < this.entire_map.getGrille().size(); i++)
		{ //Parcours toute la grille visible
			for(int j=0; j < this.entire_map.getGrille().get(i).size(); j++)
			{ 
				if(this.entire_map.getGrille().get(i).get(j).isPointed(posScreen) && this.entire_map.getGrille().get(i).get(j).isDrawn())
					return this.entire_map.getGrille().get(i).get(j);
			}
		}

		return null;
	}

	public Tile getTileReal(Vector2f posReal)
	{
		for(int i=0; i < this.entire_map.getGrille().size(); i++)
		{ //Parcours toute la grille visible
			for(int j=0; j < this.entire_map.getGrille().get(i).size(); j++)
			{ 
				if(this.entire_map.getGrille().get(i).get(j).isPointed(posReal, this.entire_map.getGrille().get(i).get(j).getPos_real()))
					return this.entire_map.getGrille().get(i).get(j);
			}
		}

		return null;
	}

	public CollisionManager getCollision_manager() {
		return collision_manager;
	}

	public void setCollision_manager(CollisionManager collisionManager) {
		collision_manager = collisionManager;
	}

	public Map getEntire_map() {
		return entire_map;
	}

	public void setEntire_map(Map entireMap) {
		entire_map = entireMap;
	}
}
