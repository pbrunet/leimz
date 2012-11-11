package com.client.display;

import java.util.Iterator;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f; 

import com.client.gamestates.Base;
import com.client.gamestates.Principal.GameState;
import com.client.utils.Data;
import com.game_entities.Joueur;
import com.game_entities.Monstre;
import com.game_entities.PNJ;
import com.game_entities.Entity.Etat;
import com.game_entities.managers.EntitiesManager;
import com.game_entities.managers.MonstersManager;
import com.game_entities.managers.PNJsManager;
import com.map.Grille;
import com.map.client.managers.MapManager;

public class DisplayManager 
{

	//*******************Infos contenues dans ce gestionnaire********************
	//[permet de ne pas avoir a ecrire les getters et setters a chaque fois, du type 
	//map_visible_manager.getPlayersManager().getJoueurs().get(0) pour le joueur 0]

	//La grille visible
	private Grille current_map;
	private EntitiesManager entities_manager;
	private Camera camera;
	private Graphics graphics;
	private GameState game_state;

	public DisplayManager(Camera camera, EntitiesManager entities_manager)
	{
		this.camera = camera;
		this.entities_manager = entities_manager;
	}

	//Rafraichissement du display_manager
	public void refresh(GameState game_state, EntitiesManager ent)
	{
		//On recupere la grille visible, qui a pu changer entre temps
		current_map = MapManager.instance.getMap_visible().getGrille();
		this.entities_manager = ent;

		this.game_state = game_state;
	}
	
	public void drawGround()
	{
		float scale = camera.getZoomScale();
		Color filtre;
		
		if(game_state.equals(GameState.COMBAT))
			filtre = new Color(255,255,255,0.5f);
		else
			filtre = new Color(255,255,255,1f);
		
		//On dessine la carte
		for(int j = 0; j < current_map.get(0).size(); j++)
		{
			/*On veut ici dessiner la carte ligne par ligne en affichant une tile sur 2
			 * pair ou impaire en fonction de la premiere tile en haut a gauche
			 */
			int first = (int) (current_map.get(0).get(0).getPos().x%2);
			for(int k=0;k<2;k++)
			{
				for(int i = first; i < current_map.size(); i+=2) 
				{
					//On cree la position d'affichage
					Vector2f pos_aff = new Vector2f();
					//La position vaut la position de la tile moins la position de la base, creee auparavant par le level designer pour chaque objet
					//De plus, on ajoute une petite formule demontrable simplement en repere orthonorme pour le zoom
					pos_aff.x = (current_map.get(i).get(j).getPos_screen().x-current_map.get(i).get(j).getTypes().get(0).getBase().getX())+((1-scale)*Base.Tile_x);
					pos_aff.y = (current_map.get(i).get(j).getPos_screen().y-current_map.get(i).get(j).getTypes().get(0).getBase().getY())+((1-scale)*Base.Tile_y/2);

					current_map.get(i).get(j).getTypes().get(0).getImg().draw(pos_aff.x, pos_aff.y, scale, filtre);
					current_map.get(i).get(j).setDrawn(true);
				}
				first=(first==1)?0:1;
			}
		}
		
	}

	//Dessin complet
	public void drawAll(Graphics g)
	{
		this.graphics = g;

		float scale = camera.getZoomScale();

		drawGround();

		//Joueur principal
		Joueur main_player = entities_manager.getPlayers_manager().getMain_player();

		//On recupere les monstres et pnjs de la grille visible
		PNJsManager pnjs_manager = entities_manager.getPnjs_manager();
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
					pos_aff.x = current_map.get(i).get(j).getPos_screen().x-current_map.get(i).get(j).getTypes().get(1).getBase().getX();
					pos_aff.y = current_map.get(i).get(j).getPos_screen().y-current_map.get(i).get(j).getTypes().get(1).getBase().getY();


					//On cree la shape associee pour les collisions
					Rectangle ob = new Rectangle(pos_aff.x, pos_aff.y, (current_map.get(i).get(j).getTypes().get(1).getImg().getWidth()), (current_map.get(i).get(j).getTypes().get(1).getImg().getHeight())-Base.Tile_y/2);

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
					
					current_map.get(i).get(j).getTypes().get(1).getImg().draw(pos_aff.x, pos_aff.y);	
				}
			}
		}

		for(int k = 0; k < entities_manager.getPlayers_manager().getJoueurs().size(); k++)
		{
			if(entities_manager.getPlayers_manager().getJoueurs().get(k).getCurrent_img_repos() != null)
				entities_manager.getPlayers_manager().getJoueurs().get(k).draw();
			else
				entities_manager.getPlayers_manager().getJoueurs().get(k).initImgs();
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
			if(!pnjsAffiche.get(i.next()))
				pnjs_manager.getPnjs().get(k).draw();
			k++;
		}

		if(main_player.getEtat().equals(Etat.OVER) || main_player.getEtat().equals(Etat.CLICKED))
		{
			main_player.getCurrent_img_repos().setAlpha(0.8f);
			Font font = Data.getFont("Trebuchet MS", 15);
			graphics.setFont(font);
			graphics.drawString(main_player.getPerso().getNom(), main_player.getPos_real_on_screen().x+(main_player.getSize().x/2)-(font.getWidth(main_player.getPerso().getNom())/2), main_player.getPos_real_on_screen().y-20);
		}
		else
		{
			main_player.getCurrent_img_repos().setAlpha(1f);
		}
	}
}
