package com.client.load;

import java.awt.Font;
import com.client.entities.MainJoueur;
import com.client.entities.Orientation;
import com.client.gamestates.Base;
import com.game_entities.managers.EntitiesManager;
import com.game_entities.managers.PNJsManager;
import com.game_entities.managers.PlayersManager;
import com.map.Map;
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
	/*private LoadPnj load_pnj;
	private LoadMonster load_monster;*/
	private Image fond, barre;
	private UnicodeFont label;
	private float purcent = 0;

	private boolean loadMapFinished =  false, loadJoueurFinished = false;

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
		
		load_joueur = new LoadJoueur();
		load_map = new LoadMap();

		loadMaps();
			}


	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
			throws SlickException
			{
		//FIXME mettre des coordonnées qui ne dépendes que des images (donc refaire image)
		purcent=(purcent>=100)?100:purcent;
		fond.draw();
		barre.getSubImage(0, 0, (int) ((purcent/100)*barre.getWidth()), barre.getHeight()).draw(237, 321);

		label.drawString(458, 325, (int)purcent + "%", Color.yellow);
			}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int arg2)
			throws SlickException 
			{

		if(!load_map.getT().isAlive() && !loadMapFinished)
		{
			new MapManager(new Map(load_map.getGrille(),null));
			loadJoueur();
			loadMapFinished=true;
		}
		
		if(!load_joueur.getT().isAlive() && loadMapFinished)
		{
			loadJoueurFinished=true;
		}

		//purcent = (load_pnj != null)?load_pnj.getPurcent():0;

		purcent = load_map.getPurcent();

		purcent += load_joueur.getPurcent();

		if(loadJoueurFinished && loadMapFinished)
		{
			MainJoueur j = new MainJoueur(load_joueur.getJoueur().getPerso(), MapManager.instance.getEntire_map().getGrille().get((int)load_joueur.getJoueur().getTile().getPos().x).get((int)load_joueur.getJoueur().getTile().getPos().y), load_joueur.getJoueur().getOrientation());

			EntitiesManager e_m = new EntitiesManager();
			e_m.setPnjs_manager(new PNJsManager());
			e_m.setPlayers_manager(new PlayersManager(j,load_joueur.getPlayers()));

			sbg.enterState(Base.PRINCIPAL);
		}
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
	}

	private void loadJoueur()
	{
		load_joueur.start();
	}

	private void loadMaps()
	{
		load_map.start();
	}
}
