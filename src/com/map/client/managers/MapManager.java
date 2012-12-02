package com.map.client.managers;


import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import com.client.gamestates.Base;
import com.client.network.NetworkListener;
import com.client.network.NetworkManager;
import com.map.Map;
import com.map.Tile;
import com.map.Type_tile;

public class MapManager implements NetworkListener
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
		NetworkManager.instance.waitForNewMessage("tt");
		String[] info_tile= NetworkManager.instance.receiveFromServer("tt").split(";"); //nom, img adresse, collidable, x, y, calque
		try {
			Type_tile t = new Type_tile(info_tile[0], new Image(info_tile[1]),new Rectangle(Integer.parseInt(info_tile[3]),Integer.parseInt(info_tile[4]),80,40),Boolean.parseBoolean(info_tile[2]),Integer.parseInt(info_tile[5]));
			types.put(name, t);
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
	
	public void drawMap(Map map, Color filter, float scale)
	{
		//On dessine la carte
		for(int j = 0; j < map.getGrille().get(0).size(); j++)
		{
			/*On veut ici dessiner la carte ligne par ligne en affichant une tile sur 2
			 * pair ou impaire en fonction de la premiere tile en haut a gauche
			 */
			int first = (int) (map.getGrille().get(0).get(0).getPos().x%2);
			for(int k=0;k<2;k++)
			{
				for(int i = first; i < map.getGrille().size(); i+=2) 
				{
					//On cree la position d'affichage
					Vector2f pos_aff = new Vector2f();
					//La position vaut la position de la tile moins la position de la base, creee auparavant par le level designer pour chaque objet
					//De plus, on ajoute une petite formule demontrable simplement en repere orthonorme pour le zoom
					pos_aff.x = (map.getGrille().get(i).get(j).getPos_screen().x-((Type_tile) map.getGrille().get(i).get(j).getTypes().get(0)).getBase().getX())+((1-scale)*Base.Tile_x);
					pos_aff.y = (map.getGrille().get(i).get(j).getPos_screen().y-((Type_tile) map.getGrille().get(i).get(j).getTypes().get(0)).getBase().getY())+((1-scale)*Base.Tile_y/2);
					if(filter != null)
					{
						((Type_tile) map.getGrille().get(i).get(j).getTypes().get(0)).getImg().draw(pos_aff.x, pos_aff.y, scale, filter);
					}
					else
					{
						((Type_tile) map.getGrille().get(i).get(j).getTypes().get(0)).getImg().draw(pos_aff.x, pos_aff.y, scale);
					}
					
					map.getGrille().get(i).get(j).setDrawn(true);
				}
				first=(first==1)?0:1;
			}
		}		
	}
	
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
	

	@Override
	public void receiveMessage(String str) {
		// TODO Auto-generated method stub
		
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
