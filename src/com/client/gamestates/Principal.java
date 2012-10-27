package com.client.gamestates;

import java.io.File;

import java.net.MalformedURLException;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.client.display.Camera;
import com.client.display.DisplayManager;
import com.client.display.gui.GUI_Manager;
import com.client.events.EventListener;
import com.client.events.MainEventListener;
import com.client.network.NetworkManager;
import com.client.utils.Data;
import com.client.utils.gui.ChatFrame;
import com.client.utils.gui.InventairePanel;
import com.client.utils.gui.InventaireUI;
import com.client.utils.gui.PnjDialogFrame;
import com.client.utils.gui.PrincipalGui;
import com.client.utils.pathfinder.PathFinder;
import com.game_entities.Joueur;
import com.game_entities.MainJoueur;
import com.game_entities.PNJ;
import com.game_entities.managers.EntitiesManager;
import com.gameplay.Combat;
import com.gameplay.Equipe;
import com.gameplay.Combat.EtatCombat;
import com.gameplay.managers.CombatManager;
import com.map.Tile;
import com.map.client.managers.MapManager;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.ResizableFrame;

public class Principal extends BasicGameState
{
	
	//----------------------------Map-----------------------------------
	private MapManager map_manager;
	private DisplayManager disp;
	
	//------------------GUI----------------
    private PrincipalGui maingui;
    
    //EVENTS
    private MainEventListener event_listener;
    
    //Entites du jeu
    private EntitiesManager entities_manager;
    
    //Joueur principal
    private MainJoueur main_player;
    
    //Gestionnaire de recherche de chemin
    private PathFinder pathfinder;
    
    //Camera
    private Camera camera;
    
    private float current_scale = 1;
    
    //------------COMBAT-------------
    private CombatManager combatManager;
    

	@Override
	public int getID() 
	{
		return 3;
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg)
			throws SlickException 
	{
		entities_manager =  EntitiesManager.instance;
		
		GUI_Manager.instance.getRoot().removeAllChildren();
		
		map_manager = MapManager.instance;
		main_player = entities_manager.getPlayers_manager().getMain_player();
		main_player.initImgs();
		
		for(int i = 0; i < entities_manager.getPnjs_manager().getPnjs().size(); i++)
		{
			entities_manager.getPnjs_manager().getPnjs().get(i).initImgs();
		}
		
		camera = new Camera();
		disp = new DisplayManager(camera, entities_manager);
		
		combatManager = new CombatManager();
		
		NetworkManager.instance.init();
		
		pathfinder = new PathFinder(map_manager.getEntire_map());
		
		event_listener = new MainEventListener(pathfinder);
		
		maingui = new PrincipalGui();
	}

	public MainJoueur getMain_player() {
		return main_player;
	}

	public void setMain_player(MainJoueur mainPlayer) {
		main_player = mainPlayer;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException 
	{
		Data.loadData();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics gr)
			throws SlickException 
	{
		Combat todraw = null;
		for(int i = 0; i < combatManager.getMainJoueurCombats().size(); i++)
		{
			if(!combatManager.getMainJoueurCombats().get(i).getEtat().equals(EtatCombat.FINI))
			{
				todraw = combatManager.getMainJoueurCombats().get(i);
			}
		}
		if(todraw != null)
		{
			disp.drawAllCombat(gr, new Vector2f(gc.getInput().getMouseX(), gc.getInput().getMouseY()), todraw);
		}
		else
		{
			disp.drawAll(gr, new Vector2f(gc.getInput().getMouseX(), gc.getInput().getMouseY()));
		}
		
		
		GUI_Manager.instance.getTwlInputAdapter().render();
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException 
	{		
		gc.setMinimumLogicUpdateInterval(10);
		gc.setMaximumLogicUpdateInterval(10);
		

		
		for(int i = 0; i < entities_manager.getPlayers_manager().getJoueurs().size(); i++)
		{
			if(entities_manager.getPlayers_manager().getJoueurs().get(i).getPos_real() != null)
			{
				entities_manager.getPlayers_manager().getJoueurs().get(i).setTile(map_manager.getTileReal(
						entities_manager.getPlayers_manager().getJoueurs().get(i).getPos_real()));
				entities_manager.getPlayers_manager().getJoueurs().get(i).setAbsolute(map_manager.getAbsolute());
			}
			
		}
		main_player.setTile(
				map_manager.getTileReal(main_player.getPos_real())
				);
		main_player.setAbsolute(map_manager.getAbsolute());
		main_player.move();
		
		camera.focusOn(main_player.getTile(), main_player.getTile().getPos_real().copy().sub(main_player.getPos_real()));
		camera.zoom(current_scale);
		
		main_player.refresh();
		for(int i = 0; i < entities_manager.getPlayers_manager().getJoueurs().size(); i++)
		{
			if(entities_manager.getPlayers_manager().getJoueurs().get(i).getPos_real_on_screen()!=null)
				entities_manager.getPlayers_manager().getJoueurs().get(i).refresh();
		}
		for(int i = 0; i < entities_manager.getPnjs_manager().getPnjs().size(); i++)
		{
			if(entities_manager.getPnjs_manager().getPnjs().get(i).getPos_real_on_screen()!=null)
				entities_manager.getPnjs_manager().getPnjs().get(i).refresh();
		}
		disp.refresh(entities_manager);
		
		combatManager.refresh();
		
		GUI_Manager.instance.getTwlInputAdapter().update();
		if(!GUI_Manager.instance.getTwlInputAdapter().isOn_gui_event())
		{
			event_listener.pollEvents(gc.getInput());
			main_player.pollEvents(gc.getInput());
			for(int i = 0; i < entities_manager.getPnjs_manager().getPnjs().size(); i++)
			{
				entities_manager.getPnjs_manager().getPnjs().get(i).pollEvents(gc.getInput());
			}
		}
    }
}
