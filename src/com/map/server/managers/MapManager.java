package com.map.server.managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;


import com.map.Map;
import com.map.SimpleType_tile;
import com.map.Tile;
import com.map.Type_tile;
import com.server.core.ServerSingleton;

public class MapManager 
{

	private CollisionManager collision_manager;

	private Map entire_map;
    public static MapManager instance;

    private static HashMap<String, SimpleType_tile> types = new HashMap<String, SimpleType_tile>();
    
    public static SimpleType_tile getTypesTile(String name) 
    {
    	SimpleType_tile type = null;
    	
    	ResultSet rs;
		//Chargement des informations d'une tile
		try {
			String sql = "SELECT tiles_map.nom, tiles_map.image, tiles_map.collidable, tiles_map.base_x, tiles_map.base_y " +
					"FROM tiles_map " +
					"WHERE tiles_map.nom='" + name + "'";
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			String rc = "";
			while(rs.next())
			{
				type = new SimpleType_tile(rs.getString("tiles_map.nom"), rs.getBoolean("tiles_map.collidable"), 0);
			}
			if(rc.equals(""))
			{
				sql = "SELECT tiles_map_content.nom, tiles_map_content.image, tiles_map_content.collidable, tiles_map_content.base_x, tiles_map_content.base_y " +
						"FROM tiles_map_content " +
						"WHERE tiles_map_content.nom='" + name + "'";
				rs = stmt.executeQuery(sql);
				rc = "";
				while(rs.next())
				{
					type = new SimpleType_tile(rs.getString("tiles_map_content.nom"), rs.getBoolean("tiles_map_content.collidable"), 1);
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		types.put(type.getNom(), type);
	return type;
    	
	}
    
	public MapManager(Map entire_map)
	{
		this.entire_map = entire_map;

		this.init();
		if(instance == null)
		{
			 instance = this;
		}
		else
		{
			System.err.println("Erreur : impossible d'instancier un deuxieme MapManager");
		}
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
	
	public ArrayList<ArrayList<Tile>> getTilesAutour(Tile tile, int spread)
	{
		ArrayList<ArrayList<Tile>> grille = new ArrayList<ArrayList<Tile>>();
		
		//R�cup�ration de l'index de d�part en x et y des Tile � afficher.
		int indiceStart_x = (int) (tile.getPos().x - spread);
		int indiceStart_y = (int) (tile.getPos().y - spread);
		
		//R�cup�ration de l'index de fin en x et y des Tile � afficher.
		int indiceFin_x = indiceStart_x + spread*2;
		int indiceFin_y = indiceStart_y + spread*2;

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

	public Map getEntire_map() {
		return entire_map;
	}

	public void setEntire_map(Map entireMap) {
		entire_map = entireMap;
	}

	
}
