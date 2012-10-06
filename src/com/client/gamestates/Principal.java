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
import com.client.network.NetworkListener;
import com.client.network.NetworkManager;
import com.client.utils.Data;
import com.client.utils.gui.ChatFrame;
import com.client.utils.gui.InventairePanel;
import com.client.utils.gui.InventaireUI;
import com.client.utils.gui.PnjDialogFrame;
import com.client.utils.pathfinder.PathFinder;
import com.game_entities.MainJoueur;
import com.game_entities.managers.EntitiesManager;
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
    private GUI_Manager gui_manager;
    private ChatFrame frame;
    private ResizableFrame menu;
    private InventaireUI inventaireUI;
    
    //EVENTS
    private EventListener event_listener;
    
    //Entites du jeu
    private EntitiesManager entities_manager;
    
    //Joueur principal
    private MainJoueur main_player;
    
    //Gestionnaire de recherche de chemin
    private PathFinder pathfinder;
    
    //Camera
    private Camera camera;
    
    private float current_scale = 1;
    
    public enum GameState
    {
    	NORMAL, COMBAT
    };
    private GameState game_state;

	@Override
	public int getID() 
	{
		return 3;
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg)
			throws SlickException 
	{
		
		main_player = entities_manager.getPlayers_manager().getMain_player();
		main_player.initImgs();
		
		for(int i = 0; i < entities_manager.getPnjs_manager().getPnjs().size(); i++)
		{
			entities_manager.getPnjs_manager().getPnjs().get(i).initImgs();
		}
		
		camera = new Camera(map_manager);
		disp = new DisplayManager(camera, entities_manager);
		
		ArrayList<NetworkListener> listeners = new ArrayList<NetworkListener>();
		listeners.add(main_player);
		
		NetworkManager.instance.init(map_manager, entities_manager, listeners);
		
		pathfinder = new PathFinder(map_manager.getEntire_map());
		
		//--------------------------GUI-----------------------
        try {
			gui_manager = new GUI_Manager(new File("data/GUI/Theme/projet.xml").toURI().toURL(), gc);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
        frame = new ChatFrame(null, main_player);
        frame.setTheme("/resizableframe");
        frame.setTitle("Leimzochat");
        frame.setPosition(50, 300);
        frame.appendRow("default","Bienvenue sur Leimz !! /n/nTapez votre message sur la barre en dessous de ce message :)/n/n");
        
        inventaireUI = new InventaireUI(main_player.getPerso().getInventaire());
        inventaireUI.setTheme("/resizableframe");
        inventaireUI.setPosition(100, 100);
        gui_manager.getRoot().add(inventaireUI);
        
        menu = new ResizableFrame();
        menu.setTheme("/resizableframe");
        menu.setTitle("Menu");
        
        Button options = new Button("Options");
        options.setTheme("/button");
        Button deconnexion = new Button("Se Deconnecter");
        deconnexion.setTheme("/button");
        Button retour = new Button("Retour au Jeu");
        retour.setTheme("/button");
        retour.addCallback(new Runnable() {
			
			@Override
			public void run() {
				menu.setVisible(false);
			}
		});
        Button quitter = new Button("Quitter");
        quitter.setTheme("/button");
        quitter.addCallback(new Runnable() {
			
			@Override
			public void run() {
				System.exit(0);
			}
		});
        
        DialogLayout l = new DialogLayout();
        l.setTheme("/dialoglayout");
        l.setHorizontalGroup(l.createParallelGroup(options, deconnexion, retour, quitter));
        l.setVerticalGroup(l.createSequentialGroup(options, deconnexion, retour, quitter));
        menu.add(l);
        
        menu.setPosition((Base.sizeOfScreen_x/2)-(menu.getWidth()/2), (Base.sizeOfScreen_y/2)-(menu.getHeight()/2));
        menu.setVisible(false);
        
        gui_manager.getRoot().add(frame);
        gui_manager.getRoot().add(menu);
        
        game_state = GameState.NORMAL;
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
		
		event_listener = new EventListener()
		{
			@Override
			public void pollEvents(Input input)
			{
				if(input.isKeyPressed(Input.KEY_ESCAPE))
					menu.setVisible(!menu.isVisible());
				
				if(input.isKeyPressed(Input.KEY_I))
					inventaireUI.setVisible(!inventaireUI.isVisible());
				
				if(input.isKeyPressed(Input.KEY_SPACE))
				{
					//START COMBAT
					if(game_state != GameState.COMBAT)
						setGame_state(GameState.COMBAT);
					else
						setGame_state(GameState.NORMAL);
				}
				
				if(input.isMousePressed(0))
				{
					for(int i = 0; i < entities_manager.getPnjs_manager().getPnjs().size(); i++)
					{
						Rectangle c = new Rectangle(
								entities_manager.getPnjs_manager().getPnjs().get(i).getCorps().getX()+entities_manager.getPnjs_manager().getPnjs().get(i).getPos_real_on_screen().x,
								entities_manager.getPnjs_manager().getPnjs().get(i).getCorps().getY()+entities_manager.getPnjs_manager().getPnjs().get(i).getPos_real_on_screen().y,
								entities_manager.getPnjs_manager().getPnjs().get(i).getCorps().getWidth(),
								entities_manager.getPnjs_manager().getPnjs().get(i).getCorps().getHeight());
								
						if(c.contains(input.getMouseX(), input.getMouseY()))
						{
							PnjDialogFrame dialog = new PnjDialogFrame(entities_manager.getPnjs_manager().getPnjs().get(i));
							
							gui_manager.getRoot().add(dialog);
							dialog.setSize(400, 400);
							dialog.setPosition((Base.sizeOfScreen_x/2)-(dialog.getWidth()/2), (Base.sizeOfScreen_y/2)-(dialog.getHeight()/2));
						}
					}
					
					Tile tile_pressed = map_manager.getTileScreen(new Vector2f(input.getMouseX(), input.getMouseY()));
					if(tile_pressed != null)
					{
						main_player.startMoving(pathfinder.calculateChemin(main_player.getTile(), tile_pressed));
					}
				}
			}

			@Override
			public void mousePressed(int button, int x, int y) {
			}

			@Override
			public void mouseReleased(int button, int x, int y) {
			}

			@Override
			public void mouseWheelMoved(int change) {
			}
		};
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics gr)
			throws SlickException 
	{
		disp.drawAll(gr);
		gui_manager.getTwlInputAdapter().render();
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException 
	{		
		gc.setMinimumLogicUpdateInterval(10);
		gc.setMaximumLogicUpdateInterval(10);
		
		inventaireUI.refresh(main_player.getPerso().getInventaire());
		InventairePanel inventory_panel = inventaireUI.getInventory_panel();
		for(int i = 0; i < inventory_panel.getSlots().size(); i++)
		{
			for(int j = 0; j < inventory_panel.getSlots().get(i).size();j++)
			{
				if(inventory_panel.getSlots().get(i).get(j).isFenOk())
				{
					 ResizableFrame panInfo = InventaireUI.panInfo(inventory_panel.getSlots().get(i).get(j).getItem());
					
                     gui_manager.getRoot().add(panInfo);
                     
                     panInfo.setSize(300, 250);
                     panInfo.setPosition((inventory_panel.getX()+(inventory_panel.getWidth()/2))-(panInfo.getWidth()/2), (inventory_panel.getY()+(inventory_panel.getHeight()/2))-(panInfo.getHeight()/2));
                     inventory_panel.getSlots().get(i).get(j).setFenOk(false);
                    
				}
			}
		}
		frame.refresh(main_player);
		
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
		
		camera.focusOn(main_player.getTile(), new Vector2f(-(main_player.getPos_real().x-main_player.getTile().getPos_real().x), -(main_player.getPos_real().y-main_player.getTile().getPos_real().y)));
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
		disp.refresh(game_state, entities_manager);
		
		gui_manager.getTwlInputAdapter().update();
		if(!gui_manager.getTwlInputAdapter().isOn_gui_event())
		{
			event_listener.pollEvents(gc.getInput());
			main_player.pollEvents(gc.getInput());
			for(int i = 0; i < entities_manager.getPnjs_manager().getPnjs().size(); i++)
			{
				entities_manager.getPnjs_manager().getPnjs().get(i).pollEvents(gc.getInput());
			}
		}
		
		if(NetworkManager.instance.modifManager)
		{
			entities_manager = NetworkManager.instance.getVisible_entities_manager();
			NetworkManager.instance.setVisible_entities_manager(entities_manager);
		}
		
    }

	public MapManager getMap_manager() {
		return map_manager;
	}

	public void setMap_manager(MapManager mapManager) {
		map_manager = mapManager;
	}

	public EntitiesManager getEntities_manager() {
		return entities_manager;
	}

	public void setEntities_manager(EntitiesManager entitiesManager) {
		entities_manager = entitiesManager;
	}

	public GameState getGame_state() {
		return game_state;
	}

	public void setGame_state(GameState gameState) {
		game_state = gameState;
	}
}
