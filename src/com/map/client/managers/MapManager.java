package com.map.client.managers;


import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.client.network.NetworkManager;
import com.game_entities.Entity;
import com.game_entities.Joueur;
import com.game_entities.managers.MonstersManager;
import com.game_entities.managers.PNJsManager;
import com.game_entities.managers.PlayersManager;
import com.map.Grille;
import com.map.Map;
import com.map.Tile;
import com.map.Type_tile;

public class MapManager 
{
	
	private CollisionManager collision_manager;
	
	
	private Vector2f absolute = new Vector2f(0,0);	
	private Map map_visible, entire_map;
	
    public static MapManager instance;
    
    private static HashMap<String, Type_tile> types = new HashMap<String, Type_tile>();
	
	public static Type_tile getTypesTile(String name)
	{
		if(types.get(name) != null)
			return types.get(name);

		//Caracteristiques de race
		NetworkManager.instance.sendToServer("lo;tt;" + name); //load joueur, type tile, name
		NetworkManager.instance.waitForNewMessage();
		String[] info_tile= NetworkManager.instance.getMessage_recu_serveur().split(";"); //nom, img adresse, collidable, x, y, calque
		Type_tile t = null;
		try {
			t= new Type_tile(info_tile[0], new Image(info_tile[1]),new Rectangle(Integer.parseInt(info_tile[3]),Integer.parseInt(info_tile[4]),80,40),Boolean.parseBoolean(info_tile[2]),Integer.parseInt(info_tile[5]));
			types.put(info_tile[0], t);
			return t;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public MapManager(Map entire_map)
	{
		this.entire_map = entire_map;
		
		this.init();
		
		//this.collision_manager = new CollisionManager(map_visible.getGrille(), null);
		if(instance == null)
		{
			 instance = this;
		}
		else
		{
			System.err.println("Erreur : impossible d'instancier un deuxieme MapManager");
		}
		 
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
	    				//On d�termine la "vraie" position de la tile par rapport aux coordonn�es
	    				entire_map.getGrille().get(i).get(j).setPos_screen(
	    						new Vector2f((entire_map.getGrille().get(i).get(j).getPos().x/2)*80, entire_map.getGrille().get(i).get(j).getPos().y*40));
	    				entire_map.getGrille().get(i).get(j).setPos_real(
	    						new Vector2f((entire_map.getGrille().get(i).get(j).getPos().x/2)*80, entire_map.getGrille().get(i).get(j).getPos().y*40));
	    			}
	    			
	    			else //Si la ligne est impaire
	    			{
	    				//On d�termine la "vraie" position de la tile par rapport aux coordonn�es
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
	
	public ArrayList<ArrayList<Tile>> getTilesAutour(Tile tile, int spread)
	{
		ArrayList<ArrayList<Tile>> grille = new ArrayList<ArrayList<Tile>>();
		
		//R�cup�ration de l'index de d�part en x et y des Tile � afficher.
		int indiceStart_x = (int) (tile.getPos().x - (spread / 2));
		int indiceStart_y = (int) (tile.getPos().y - (spread / 2));
		
		//R�cup�ration de l'index de fin en x et y des Tile � afficher.
		int indiceFin_x = indiceStart_x + spread;
		int indiceFin_y = indiceStart_y + spread;

		int h = 0;
			//On parcourt alors les lignes et les colonnes
			for(int i = indiceStart_x; i < indiceFin_x; i++) 
	    	{
				if(i >= 0 && i < this.getEntire_map().getGrille().size())
				{
					grille.add(new ArrayList<Tile>());
				}
				
	    		for(int j = indiceStart_y; j < indiceFin_y; j++)
	    		{
	    			//Attention aux cas o� la tile voulue se situe sur une extr�mit� de la map.
    				if(i >= 0 && j >=0 && i < this.getEntire_map().getGrille().size() && j < this.getEntire_map().getGrille().get(i).size())
    				{
		    			grille.get(h).add(this.getEntire_map().getGrille().get(i).get(j));
    				}
	    		}
	    		if(i >= 0 && i < this.getEntire_map().getGrille().size())
				{
	    			h++;
				}
	    	}
		return grille;
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
