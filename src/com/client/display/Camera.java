package com.client.display;

import java.util.ArrayList;


import org.newdawn.slick.geom.Vector2f;

import com.client.gamestates.Base;
import com.map.Grille;
import com.map.Map;
import com.map.Tile;
import com.map.client.managers.MapManager;

public class Camera 
{
	private MapManager map_manager;
	private Map map_visible;
	
	private static int NB_TILES_X = (Base.sizeOfScreen_x / 40);
	private static int NB_TILES_Y = (Base.sizeOfScreen_y / 40);
	
	private Tile lastFocus = null;
	private Vector2f lastDecalage = null;
	
	private float zoomScale = 1.0f;
	private float fuzzy = 1.0f;
	
	public Camera(MapManager map_manager)
	{
		this.map_manager = map_manager;
	}

	public void focusOn(Tile tile, Vector2f decalage)
	{	
		if(lastFocus == null && lastDecalage == null)
		{
			lastFocus = tile;
			lastDecalage = decalage;
		}
		else
		{
			if(lastFocus == tile && lastDecalage == decalage)
			{
				return;
			}
		}
		
		int decalage_x = (int) decalage.x - 80*2;
		int decalage_y = (int) decalage.y - 40*2;
		
		//Récupération de l'index de départ en x et y des Tile à afficher.
		int indiceStart_x = (int) (tile.getPos().x - (NB_TILES_X / 2));
		int indiceStart_y = (int) (tile.getPos().y - (NB_TILES_Y / 2));
		
		//Récupération de l'index de fin en x et y des Tile à afficher.
		int indiceFin_x = indiceStart_x + NB_TILES_X;
		int indiceFin_y = indiceStart_y + NB_TILES_Y;
		
		//Maintenant, on ajoute x pour faire "déborder" la map pour avoir une map rempli sur l'écran de jeu
		indiceStart_x -= 4;
		indiceStart_y -= 2;
		indiceFin_x += 4;
		indiceFin_y += 2;

		//System.out.println("Indice de départ : "+indiceStart_x+" ; "+indiceStart_y + "  --- Indice de fin : "+indiceFin_x+" ; "+indiceFin_y+"  ---  Decalage : "+decalage_x+" ; "+decalage_y);
			
		Grille current_map = new Grille();
		
			//Ces deux compteur vont nous permettre de simulé la position de la Tile sur l'écran du joueur.
			// y : Simulation de la position en X
			// z : Simulation de la position en Y
			int y = 0, z = 0, h = 0;

			//On parcourt alors les lignes et les colonnes
			for(int i = indiceStart_x; i < indiceFin_x; i++) 
	    	{
				if(i >= 0 && i < map_manager.getEntire_map().getGrille().size())
				{
					current_map.add(new ArrayList<Tile>());
				}
				
	    		for(int j = indiceStart_y; j < indiceFin_y; j++)
	    		{
	    			//Attention aux cas où le joueur se situe sur une extrémité de la map.
    				if(i >= 0 && j >=0 && i < map_manager.getEntire_map().getGrille().size() && j < map_manager.getEntire_map().getGrille().get(i).size())
    				{
    						if(i%2 ==0) //Si la ligne est paire
    		    			{
    							if(indiceStart_x % 2 == 0)
    							{
    								//On détermine la "vraie" position de la tile par rapport aux coordonnées
        		    				map_manager.getEntire_map().getGrille().get(i).get(j).setPos_screen(
        		    						new Vector2f(((y*80)/2) + decalage_x, (z*40) + decalage_y));
    							}
    							else
    							{
    								//On détermine la "vraie" position de la tile par rapport aux coordonnées
        		    				map_manager.getEntire_map().getGrille().get(i).get(j).setPos_screen(
        		    						new Vector2f(((y*80)/2) + decalage_x, ((z*40)+20) + decalage_y));
    							}
    		    			}
    		    			else //Si la ligne est impaire
    		    			{	
    		    				if(indiceStart_x % 2 == 0)
    							{
    		    					//On détermine la "vraie" position de la tile par rapport aux coordonnées
    		    					map_manager.getEntire_map().getGrille().get(i).get(j).setPos_screen(
    		    							new Vector2f(((y*80)/2) + decalage_x, ((z*40)+20) + decalage_y));
    							}
    		    				else
    		    				{
    		    					//On détermine la "vraie" position de la tile par rapport aux coordonnées
    		    					map_manager.getEntire_map().getGrille().get(i).get(j).setPos_screen(
    		    							new Vector2f(((y*80)/2) + decalage_x, ((z*40)+40) + decalage_y));
    		    				}
    		    			}
		    			//System.out.println("Tile "+map_manager.getEntire_map().getGrille().get(i).get(j).getPos().x+";"+map_manager.getEntire_map().getGrille().get(i).get(j).getPos().y+"  ---> "+ map_manager.getEntire_map().getGrille().get(i).get(j).getPos_screen().x+" : "+map_manager.getEntire_map().getGrille().get(i).get(j).getPos_screen().y);
		    			current_map.get(h).add(map_manager.getEntire_map().getGrille().get(i).get(j));
    				}
	    			z++;
	    		}
	    		if(i >= 0 && i < map_manager.getEntire_map().getGrille().size())
				{
	    			h++;
				}
	    		
	    		y++;
	    		z = 0;
	    	}
			
			map_manager.setAbsolute(new Vector2f(-(current_map.get(0).get(0).getPos_real().x-current_map.get(0).get(0).getPos_screen().x), -(current_map.get(0).get(0).getPos_real().y-current_map.get(0).get(0).getPos_screen().y)));
			
			
			map_visible = new Map(current_map, map_manager.getEntire_map().getDataMonstres());
			map_manager.setMap_visible(map_visible);
			
		
	}
	
	public void zoom(float scale)
	{
		this.zoomScale = scale;
	}
	
	public void fuzzy(float fuzzy)
	{
		this.fuzzy = fuzzy;
	}

	

	public float getFuzzy() {
		return fuzzy;
	}

	public void setFuzzy(float fuzzy) {
		this.fuzzy = fuzzy;
	}

	public float getZoomScale() {
		return zoomScale;
	}

	public void setZoomScale(float zoomScale) {
		this.zoomScale = zoomScale;
	}

	public Map getMap_visible() {
		return map_visible;
	}

	public void setMap_visible(Map mapVisible) {
		map_visible = mapVisible;
	}

}
