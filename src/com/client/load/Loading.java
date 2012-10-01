package com.client.load;

import java.awt.Font;


import com.client.gamestates.Base;
import com.client.gamestates.Principal;

import com.game_entities.MainJoueur;
import com.game_entities.Orientation;
import com.game_entities.managers.EntitiesManager;
import com.game_entities.managers.PlayersManager;

import com.map.client.managers.MapManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Loading extends BasicGameState
{
	private LoadMap load_map;
	private LoadJoueur load_joueur;
	private LoadEntities load_entities;
	private Image fond, barre;
	private UnicodeFont label;
	private float purcent = 0;

	private boolean loadM =  false, loadJ = false;

	@Override
	public int getID() 
	{
		return 2;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg)
			throws SlickException
			{
		Font f = new Font("Trebuchet MS", 25, Font.BOLD);

		label = new UnicodeFont(f, 25, true, false);
		label.addAsciiGlyphs();
		label.addGlyphs(400,600);
		label.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
		label.loadGlyphs();

		fond = new Image("data/Images/Loading/fond_v2.png");
		barre = new Image("data/Images/Loading/Barre_remplie.png");

		loadJoueur();
			}


	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
			throws SlickException
			{
		//FIXME mettre des coordonnées qui ne dépendes que des images (donc refaire image)
		fond.draw();
		barre.getSubImage(0, 0, (int) ((purcent/100)*barre.getWidth()), barre.getHeight()).draw(237, 321);

		label.drawString(458, 325, (int)purcent + "%", Color.yellow);
			}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int arg2)
			throws SlickException 
			{

		
		if(load_joueur != null && !load_joueur.isRunning() && !loadJ)
		{
			loadMaps();
			loadJ=true;
		}
		
		if(load_map != null && !load_map.isRunning() && !loadM)
		{
			((Principal) sbg.getState(Base.PRINCIPAL)).setMap_manager(new MapManager(load_map.getMap()));

			loadEntities();
			loadM=true;
		}

		if(load_entities != null && load_entities.isRunning())
			purcent = load_entities.getPurcent();
		else
			purcent = 0;

		if(load_map != null && load_map.isRunning())
			purcent += load_map.getPurcent();
		else
			purcent += 0;

		purcent += load_joueur.getPurcent();

		if(load_entities != null && !load_entities.isRunning() && load_joueur != null && !load_joueur.isRunning() && load_map != null && !load_map.isRunning())
		{
			MainJoueur j = new MainJoueur(load_joueur.getPerso(), MapManager.instance.getEntire_map().getGrille().get(20).get(20), Orientation.DROITE);

			EntitiesManager e_m = load_entities.getEntities_manager();
			PlayersManager players_manager = new PlayersManager(j);
			e_m.setPlayers_manager(players_manager);

			((Principal) sbg.getState(Base.PRINCIPAL)).setEntities_manager(e_m);

			sbg.enterState(Base.PRINCIPAL);
		}
			}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {

	}

	private void loadJoueur()
	{
		load_joueur = new LoadJoueur(0);
	}

	private void loadMaps()
	{
		load_map = new LoadMap(0);
	}

	private void loadEntities()
	{
		load_entities = new LoadEntities(0);
	}
}
