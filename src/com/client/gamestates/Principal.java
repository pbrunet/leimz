package com.client.gamestates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import com.client.display.Camera;
import com.client.display.DisplayManager;
import com.client.display.gui.GUI_Manager;
import com.client.entities.MainJoueur;
import com.client.events.MainEventListener;
import com.client.network.NetworkManager;
import com.client.utils.Data;
import com.client.utils.gui.PrincipalGui;
import com.client.utils.pathfinder.PathFinder;
import com.game_entities.managers.EntitiesManager;
import com.gameplay.managers.CombatManager;
import com.map.client.managers.MapManager;

public class Principal extends BasicGameState
{

	//----------------------------Map-----------------------------------
	private MapManager map_manager;
	private DisplayManager disp;

	//------------------GUI----------------
    @SuppressWarnings("unused")
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
		disp = new DisplayManager(camera);
		
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
		disp.drawAll(gr, new Vector2f(gc.getInput().getMouseX(), gc.getInput().getMouseY()));
		
		
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
		for(int i = 0; i < entities_manager.getPnjs_manager().getPnjs().size(); i++)
		{
			if(entities_manager.getPnjs_manager().getPnjs().get(i).getImgs_repos()==null)
				entities_manager.getPnjs_manager().getPnjs().get(i).initImgs();
		}
		main_player.setTile(
				map_manager.getTileReal(main_player.getPos_real())
				);
		main_player.setAbsolute(map_manager.getAbsolute());
		main_player.movePath();

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
