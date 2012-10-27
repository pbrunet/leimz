package com.game_entities;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.client.network.NetworkManager;
import com.client.utils.pathfinder.Chemin;
import com.client.utils.pathfinder.Noeud;
import com.game_entities.Entity.Etat;
import com.gameplay.entities.Personnage;
import com.map.Tile;

public class MainJoueur extends Joueur
{
	//Ev�nements
	private ArrayList<String> events = new ArrayList<String>();
	
	//PATHFINDING
	private Chemin current_chemin;
	private ArrayList<Tile> list_tiles_done;
	private Tile next_tile;
	
	public static MainJoueur instance;
	
	public MainJoueur(Personnage perso, Tile tile, Orientation orientation) 
	{
		super(perso, tile, orientation);
		


		this.pos_real.x += 40;
		this.pos_real.y += 20;

		
		this.list_tiles_done = new ArrayList<Tile>();
		
		NetworkManager.instance.sendToServer("s;pos;"+pos_real.x+";"+pos_real.y+";"+stringOrientation());
		
		if(instance == null)
		{
			instance = this;
		}
		
	}

	
	
	
	public void pollEvents(Input input)
	{
		/**
		 * Gestion des �v�nements, pour le d�placement du perso
		 * 
		 */
		if(input.isKeyDown(Input.KEY_D))
		{
			if(!events.contains("DROITE"))
				events.add("DROITE");
		}
		if(input.isKeyDown(Input.KEY_Z))
		{
			if(!events.contains("HAUT"))
				events.add("HAUT");
		}
		if(input.isKeyDown(Input.KEY_Q))
		{
			if(!events.contains("GAUCHE"))
				events.add("GAUCHE");
		}
		if(input.isKeyDown(Input.KEY_S))
		{
			if(!events.contains("BAS"))
				events.add("BAS");
		}
		if(input.isMousePressed(0))
		{
			if(!events.contains("PRESSED"))
				events.add("PRESSED");
		}
		
		
		if(!input.isKeyDown(Input.KEY_D))
		{
			if(events.contains("DROITE"))
				events.remove("DROITE");
		}
		if(!input.isKeyDown(Input.KEY_Z))
		{
			if(events.contains("HAUT"))
				events.remove("HAUT");
		}
		if(!input.isKeyDown(Input.KEY_Q))
		{
			if(events.contains("GAUCHE"))
				events.remove("GAUCHE");
		}
		if(!input.isKeyDown(Input.KEY_S))
		{
			if(events.contains("BAS"))
				events.remove("BAS");
		}
		if(!input.isMouseButtonDown(0))
		{
			if(events.contains("PRESSED"))
				events.remove("PRESSED");
		}
		
		
		if(events.contains("GAUCHE") && !(events.contains("DROITE") && events.contains("HAUT") && events.contains("BAS")))
		{
			setOrientation(Orientation.GAUCHE);
		}
		if(events.contains("DROITE") && !(events.contains("DROITE") && events.contains("HAUT") && events.contains("BAS")))
		{
			setOrientation(Orientation.DROITE);
		}
		if(events.contains("HAUT") && !(events.contains("DROITE") && events.contains("HAUT") && events.contains("BAS")))
		{
			setOrientation(Orientation.HAUT);
		}
		if(events.contains("BAS") && !(events.contains("DROITE") && events.contains("HAUT") && events.contains("BAS")))
		{
			setOrientation(Orientation.BAS);
		}
		if(events.contains("GAUCHE") && events.contains("HAUT") && !(events.contains("DROITE") && events.contains("BAS")))
		{
			setOrientation(Orientation.HAUT_GAUCHE);
		}
		if(events.contains("DROITE") && events.contains("HAUT") && !(events.contains("GAUCHE") && events.contains("BAS")))
		{
			setOrientation(Orientation.HAUT_DROITE);
		}
		if(events.contains("GAUCHE") && events.contains("BAS") && !(events.contains("DROITE") && events.contains("HAUT")))
		{
			setOrientation(Orientation.BAS_GAUCHE);
		}
		if(events.contains("DROITE") && events.contains("BAS") && !(events.contains("GAUCHE") && events.contains("HAUT")))
		{
			setOrientation(Orientation.BAS_DROITE);
		}
		
		if(events.contains("BAS") || events.contains("HAUT") || events.contains("DROITE") || events.contains("GAUCHE"))
		{
			moveKey();
		}
	
		
		/**
		 * Gestion des �v�nements, pour la vitesse du perso
		 * 
		 */
		
		if(input.isKeyDown(Input.KEY_LSHIFT))
		{
			events.add("SPEED+");
			setSpeed(2.0f);
		}
		if(!input.isKeyDown(Input.KEY_LSHIFT))
		{
			if(events.contains("SPEED+"))
			{
				events.remove("SPEED+");
				setSpeed(1.0f);
			}
		}
		
		float mouseX = input.getMouseX();
		float mouseY = input.getMouseY();
		
		if((new Rectangle(this.pos_real_on_screen.x+this.corps.getX(), this.pos_real_on_screen.y+this.corps.getY(), this.corps.getWidth(), this.corps.getHeight())).contains(mouseX, mouseY))
		{
			etat = Etat.OVER;
			if(input.isMouseButtonDown(0))
			{
				etat = Etat.CLICKED;
			}
		}
		else
		{
			etat = Etat.NORMAL;
		}
	}
	
	
	public void startMoving(Chemin chemin)
	{
		this.current_chemin = new Chemin(chemin.getObjectif(), chemin.getDepart());
		ArrayList<Noeud> noeuds = new ArrayList<Noeud>();
		for(int i = chemin.getNoeuds().size()-1; i >= 0; i--)
		{
			noeuds.add(chemin.getNoeuds().get(i));
		}
		current_chemin.setNoeuds(noeuds);
		
		next_tile = current_chemin.getDepart().getTile();
		
		list_tiles_done = new ArrayList<Tile>();
	}
	
	public void move()
	{
		if(current_chemin != null)
		{
			if(!this.getPos_real().equals(current_chemin.getObjectif().getTile().getPos_real_barycentre()))
			{
				if(this.tile.equals(next_tile) && !this.tile.equals(current_chemin.getObjectif().getTile()))
				{
					list_tiles_done.add(next_tile);
					next_tile = current_chemin.getNoeuds().get(list_tiles_done.size()).getTile();
				}
				else
				{
					if(next_tile.getPos_real_barycentre().x > this.getPos_real().x && next_tile.getPos_real_barycentre().y > this.getPos_real().y)
					{
						this.orientation = Orientation.BAS_DROITE;
						this.pos_real.x += 1f*speed;
						this.pos_real.y += 0.5f*speed;
					}
					else if(next_tile.getPos_real_barycentre().x < this.getPos_real().x && next_tile.getPos_real_barycentre().y < this.getPos_real().y)
					{
						this.orientation = Orientation.HAUT_GAUCHE;
						this.pos_real.x -= 1f*speed;
						this.pos_real.y -= 0.5f*speed;
					}
					else if(next_tile.getPos_real_barycentre().x > this.getPos_real().x && next_tile.getPos_real_barycentre().y < this.getPos_real().y)
					{
						this.orientation = Orientation.HAUT_DROITE;
						this.pos_real.x += 1f*speed;
						this.pos_real.y -= 0.5f*speed;
					}
					else if(next_tile.getPos_real_barycentre().x < this.getPos_real().x && next_tile.getPos_real_barycentre().y > this.getPos_real().y)
					{
						this.orientation = Orientation.BAS_GAUCHE;
						this.pos_real.x -= 1f*speed;
						this.pos_real.y += 0.5f*speed;
					}
					else if(next_tile.getPos_real_barycentre().x > this.getPos_real().x)
					{
						this.orientation = Orientation.DROITE;
						this.pos_real.x += 1*speed;
					}
					else if(next_tile.getPos_real_barycentre().x < this.getPos_real().x)
					{
						this.orientation = Orientation.GAUCHE;
						this.pos_real.x -= 1*speed;
					}
					else if(next_tile.getPos_real_barycentre().y > this.getPos_real().y)
					{
						this.orientation = Orientation.BAS;
						this.pos_real.y += 1*speed;
					}
					else if(next_tile.getPos_real_barycentre().y < this.getPos_real().y)
					{
						this.pos_real.y -= 1*speed;
						this.orientation = Orientation.HAUT;
					}
					NetworkManager.instance.sendToServer("s;pos;"+pos_real.x+";"+pos_real.y+";"+stringOrientation());
				}
			}
		}
		
	}
	
	
	public void moveKey()
	{
		current_chemin = null;
			switch(orientation)
			{
				case HAUT:
					pos_real.y -= 1 * speed;
					break;
				case BAS:
					pos_real.y += 1 * speed;
					break;
				case GAUCHE:
					pos_real.x -= 1 * speed;
					break;
				case DROITE:
					pos_real.x += 1 * speed;
					break;
				case HAUT_DROITE:
					pos_real.x += 1 * speed;
					pos_real.y -= 0.5f * speed;
					break;
				case HAUT_GAUCHE:
					pos_real.x -= 1 * speed;
					pos_real.y -= 0.5f * speed;
					break;
				case BAS_DROITE:
					pos_real.x += 1 * speed;
					pos_real.y += 0.5f * speed;
					break;
				case BAS_GAUCHE:
					pos_real.x -= 1 * speed;
					pos_real.y += 0.5f * speed;
					break;
				
			}
			
			NetworkManager.instance.sendToServer("s;pos;"+pos_real.x+";"+pos_real.y+";"+stringOrientation());
			
		
	}
	/*
	@Override
	public void listenToServerMessage(String message) 
	{
		
	}

	@Override
	public String messageToSend() 
	{
		return messageToSend;
	}

	@Override
	public boolean wantSending() 
	{
		return wantToSend;
	}

	@Override
	public void sendMessageToServer(String message) 
	{
		wantToSend = true;
		messageToSend = message;
	}

	@Override
	public void setSentOk()
	{
		wantToSend = false;
		messageToSend = "";
	}
	*/
	@Override
	public void refresh()
	{
		super.refresh();
		
		//QUETES
		perso.getQuetes_manager().testQuetes(this);
	}

}
