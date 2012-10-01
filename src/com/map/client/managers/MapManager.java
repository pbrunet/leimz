package com.map.client.managers;

import org.newdawn.slick.geom.Vector2f;

import com.client.gamestates.Base;
import com.map.Map;
import com.map.Tile;

public class MapManager 
{
	private CollisionManager collision_manager;

	private Vector2f absolute = new Vector2f(0,0);	
	private Map map_visible, entire_map;

	public static MapManager instance;

	public MapManager(Map entire_map)
	{
		this.entire_map = entire_map;

		this.init();

		if(instance == null)
			instance = this;
		else
			System.out.println("Erreur : impossible d'instancier un deuxieme MapManager");
	}

	public void refresh()
	{
	}

	public void init()
	{		
		//On parcourt alors les lignes et les colonnes
		for(int i = 0; i < entire_map.getGrille().size(); i++)
		{
			for(int j = 0; j <entire_map.getGrille().get(i).size(); j++)
			{
				int shift = 0;
				if(i%2!=0) 
					shift = 20;
				//On dtermine la "vraie" position de la tile par rapport aux coordonnes
				entire_map.getGrille().get(i).get(j).setPos_screen(
						new Vector2f((entire_map.getGrille().get(i).get(j).getPos().x*Base.Tile_x), entire_map.getGrille().get(i).get(j).getPos().y*Base.Tile_x+shift));
				entire_map.getGrille().get(i).get(j).setPos_real(
						new Vector2f((entire_map.getGrille().get(i).get(j).getPos().x*Base.Tile_x), entire_map.getGrille().get(i).get(j).getPos().y*Base.Tile_x+shift));
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

	public Map getMap_visible() {
		return map_visible;
	}

	public void setMap_visible(Map mapVisible) {
		map_visible = mapVisible;
	}

	public Map getEntire_map() {
		return entire_map;
	}

	public void setEntire_map(Map entireMap) {
		entire_map = entireMap;
	}

	public Vector2f getAbsolute() {
		return absolute;
	}

	public void setAbsolute(Vector2f absolute) {
		this.absolute = absolute;
	}
}
