package com.client.display;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f; 
import com.client.entities.Joueur;
import com.client.entities.MainJoueur;
import com.client.entities.Monstre;
import com.client.entities.PNJ;
import com.client.entities.Entity.Etat;
import com.client.gamestates.Base;
import com.client.utils.Data;
import com.game_entities.managers.EntitiesManager;
import com.game_entities.managers.MonstersManager;
import com.game_entities.managers.PNJsManager;
import com.gameplay.managers.CombatManager;
import com.map.Grille;
import com.map.client.managers.MapManager;
import com.map.Type_tile;

public class DisplayManager 
{

	//*******************Infos contenues dans ce gestionnaire********************
	//[permet de ne pas avoir a ecrire les getters et setters a chaque fois, du type 
	//map_visible_manager.getPlayersManager().getJoueurs().get(0) pour le joueur 0]

	//La grille visible
	private Grille current_map;
	private Camera camera;
	private Graphics graphics;

	public DisplayManager(Camera camera)
	{
		this.camera = camera;
	}
	
	private void drawEntities(float scale)
	{
		current_map = MapManager.instance.getMap_visible().getGrille();
		
		//Joueur principal
		Joueur main_player = EntitiesManager.instance.getPlayers_manager().getMain_player();

		ArrayList<PNJ> list_pnj = new ArrayList<PNJ>();
		for(int i =0; i < camera.getVisible_entities().size(); i++)
		{
			if(camera.getVisible_entities().get(i) instanceof PNJ)
			{
				list_pnj.add((PNJ) camera.getVisible_entities().get(i));
			}
		}
		
		//On recupere les monstres et pnjs de la grille visible
		PNJsManager pnjs_manager = new PNJsManager();
		pnjs_manager.setPnjs(list_pnj);
		MonstersManager mm = new MonstersManager();

		//On associe e chaque monstre un booleen qui indique si il a deje ete affiche
		HashMap<Monstre, Boolean> monstresAffiche = new HashMap<Monstre, Boolean>();

		//On associe e chaque pnj un booleen qui indique si il a deje ete affiche
		HashMap<PNJ, Boolean> pnjsAffiche = new HashMap<PNJ, Boolean>();
		for(int i = 0; i < pnjs_manager.getPnjs().size(); i++)
		{
			pnjsAffiche.put(pnjs_manager.getPnjs().get(i), false);
		}

		//On associe e chaque joueur (il n'y en a qu'un pour l'instant) un booleen qui indique si il a deje ete affiche
		boolean joueurAffiche = false;

		//On passe en boucle de l'indice de depart a l'indice de fin
		for(int i = 0; i < current_map.size(); i++) 
		{
			for(int j = 0; j < current_map.get(i).size(); j++)
			{
				//On affiche l'objet si au moins il existe !
				if(current_map.get(i).get(j).getTypes().size() > 1)
				{
					//On cree la position d'affichage
					Vector2f pos_aff = new Vector2f();
					//La position vaut la position de la tile moins la position de la base, creee auparavant par le level designer pour chaque objet
					pos_aff.x = current_map.get(i).get(j).getPos_screen().x-((Type_tile) current_map.get(i).get(j).getTypes().get(1)).getBase().getX();
					pos_aff.y = current_map.get(i).get(j).getPos_screen().y-((Type_tile) current_map.get(i).get(j).getTypes().get(1)).getBase().getY();


					//On cree la shape associee pour les collisions
					Rectangle ob = new Rectangle(pos_aff.x, pos_aff.y, (((Type_tile) current_map.get(i).get(j).getTypes().get(1)).getImg().getWidth()), (((Type_tile) current_map.get(i).get(j).getTypes().get(1)).getImg().getHeight())-Base.Tile_y/2);

					Rectangle mpps = new Rectangle(main_player.getPos_real_on_screen().x+main_player.getPieds().getX(), main_player.getPos_real_on_screen().y+main_player.getPieds().getY(), main_player.getPieds().getWidth(), main_player.getPieds().getHeight());

					//--------------------------------JOUEUR---------------------------

					//Si on est en intersection
					if(ob.intersects(mpps))
					{
						//-----------CAS OU LE JOUEUR EST DERRIERE L'OBJET------------------

						//Si le joueur n'a pas encore ete affiche
						if(joueurAffiche == false)
						{
							//On affiche le joueur
							main_player.draw(scale);
							//On indique que le joueur a ete affiche
							joueurAffiche = true;
						}
					}
							
					//-----------------------------MONSTRES--------------------
					for(int k = 0; k < mm.getMonsters().size(); k++)
					{
						if(ob.intersects(mm.getMonsters().get(k).getPieds_screen()))
						{
							//Si le monstre n'a pas encore ete affiche
							if(!monstresAffiche.get(mm.getMonsters().get(k)))
							{
								//On affiche le monstre
								mm.getMonsters().get(k).draw();
								//On indique que le monstre a ete affiche
								monstresAffiche.put(mm.getMonsters().get(k), true);
							}
						}
					}

					//-----------------------------PNJs--------------------

					for(int k = 0; k < pnjs_manager.getPnjs().size(); k++)
					{
						if(pnjs_manager.getPnjs().get(k).getImgs_repos()!=null)
						{
							Rectangle pnps = new Rectangle(pnjs_manager.getPnjs().get(k).getPos_real_on_screen().x+pnjs_manager.getPnjs().get(k).getPieds().getX(),
									pnjs_manager.getPnjs().get(k).getPos_real_on_screen().y+pnjs_manager.getPnjs().get(k).getPieds().getY(), 
									pnjs_manager.getPnjs().get(k).getPieds().getWidth(), pnjs_manager.getPnjs().get(k).getPieds().getHeight());
							if(ob.intersects(pnps))
							{
								if(!pnjsAffiche.get(pnjs_manager.getPnjs().get(k)))
								{
									pnjs_manager.getPnjs().get(k).draw();
									pnjsAffiche.put(pnjs_manager.getPnjs().get(k), true);
								}
							}
						}
					}
							
					((Type_tile) current_map.get(i).get(j).getTypes().get(1)).getImg().draw(pos_aff.x, pos_aff.y);	
				}
			}
		}

				for(int k = 0; k < EntitiesManager.instance.getPlayers_manager().getJoueurs().size(); k++)
				{
					if(EntitiesManager.instance.getPlayers_manager().getJoueurs().get(k).getCurrent_img_repos() != null)
						EntitiesManager.instance.getPlayers_manager().getJoueurs().get(k).draw();
					else
						EntitiesManager.instance.getPlayers_manager().getJoueurs().get(k).initImgs();
				}

				//Si le joueur n'a finalement pas ete affiche (dans le cas ou, par ex, il n'y a aucun objet la ou il est)
				if(joueurAffiche == false)
				{
					//On l'affiche
					main_player.draw(scale);
				}
				//Si les pnjs n'ont finalement pas ete affiches (dans le cas ou, par ex, il n'y a aucun objet la ou ils sont)
				Iterator<PNJ> i = pnjsAffiche.keySet().iterator();
				int k = 0;
				while (i.hasNext())
				{
					if(!pnjsAffiche.get(i.next()) && pnjs_manager.getPnjs().get(k).getImgs_repos()!=null)
						pnjs_manager.getPnjs().get(k).draw();
					k++;
				}
	}
	
	public void drawAll(Graphics g, Vector2f mousePos)
	{
		this.graphics = g;

		float scale = camera.getZoomScale();

		if(CombatManager.instance.getCurrent_combat() != null)
		{
			CombatManager.instance.getCurrent_combat().draw(g, mousePos, scale);
			MapManager.instance.drawMap(MapManager.instance.getMap_visible(), new Color(255,255,255,0.4f), scale);
		}
		else
		{
			MapManager.instance.drawMap(MapManager.instance.getMap_visible(), null, scale);
		}
		drawEntities(scale);
		
		if(MainJoueur.instance.getPerso().getCurrent_sort() != null)
		{
			g.setColor(new Color(60,0,255));
			g.drawLine(MainJoueur.instance.getPos_real_on_screen().x+(MainJoueur.instance.getSize().x/2), MainJoueur.instance.getPos_real_on_screen().y+(MainJoueur.instance.getSize().y/2), mousePos.x, mousePos.y);
			g.setColor(Color.white);
		}

		if(MainJoueur.instance.getEtat().equals(Etat.OVER) || MainJoueur.instance.getEtat().equals(Etat.CLICKED))
		{
			MainJoueur.instance.getCurrent_img_repos().setAlpha(0.8f);
			Font font = Data.getFont("Trebuchet MS", 15);
			graphics.setFont(font);
			graphics.drawString(MainJoueur.instance.getPerso().getNom(), MainJoueur.instance.getPos_real_on_screen().x+(MainJoueur.instance.getSize().x/2)-(font.getWidth(MainJoueur.instance.getPerso().getNom())/2), MainJoueur.instance.getPos_real_on_screen().y-20);
		}
		else
		{
			MainJoueur.instance.getCurrent_img_repos().setAlpha(1f);
		}
	}
}
