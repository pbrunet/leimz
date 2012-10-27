package com.client.events;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.client.display.gui.GUI_Manager;
import com.client.gamestates.Base;
import com.client.network.NetworkManager;
import com.client.utils.gui.PnjDialogFrame;
import com.client.utils.gui.PrincipalGui;
import com.client.utils.pathfinder.PathFinder;
import com.game_entities.Joueur;
import com.game_entities.MainJoueur;
import com.game_entities.PNJ;
import com.game_entities.managers.EntitiesManager;
import com.gameplay.Combat.EtatCombat;
import com.gameplay.managers.CombatManager;
import com.map.Tile;
import com.map.client.managers.MapManager;

public class MainEventListener extends EventListener
{
	public static MainEventListener instance;
	
	private PathFinder pf;
	
	public MainEventListener(PathFinder pf)
	{
		this.pf = pf;
	}
	
	@Override
	public void pollEvents(Input input)
	{
		if(input.isKeyPressed(Input.KEY_ESCAPE))
			PrincipalGui.instance.getMenu().setVisible(!PrincipalGui.instance.getMenu().isVisible());
		
		if(input.isKeyPressed(Input.KEY_I))
			PrincipalGui.instance.getInventaireUI().setVisible(!PrincipalGui.instance.getInventaireUI().isVisible());
		
		
		if(input.isKeyPressed(Input.KEY_SPACE))
		{
			if(MainJoueur.instance.getPerso().getSorts().get(0).equals(MainJoueur.instance.getPerso().getCurrent_sort()))
			{
				MainJoueur.instance.getPerso().setCurrent_sort(null);
			}
			else
			{
				MainJoueur.instance.getPerso().setCurrent_sort(MainJoueur.instance.getPerso().getSorts().get(0));
			}
		}
		
		if(input.isMousePressed(0))
		{
			boolean entity_pressed = false;
			//System.out.println(input.getMouseX()+":"+input.getMouseY()+ "      "+map_manager.getEntire_map().getGrille().get(21).get(20).getPos_screen().x+":"+map_manager.getEntire_map().getGrille().get(21).get(20).getPos_screen().y);
			for(int i = 0; i < EntitiesManager.instance.getEntities().size(); i++)
			{
				Rectangle c = new Rectangle(
						EntitiesManager.instance.getEntities().get(i).getCorps().getX()+EntitiesManager.instance.getEntities().get(i).getPos_real_on_screen().x,
						EntitiesManager.instance.getEntities().get(i).getCorps().getY()+EntitiesManager.instance.getEntities().get(i).getPos_real_on_screen().y,
						EntitiesManager.instance.getEntities().get(i).getCorps().getWidth(),
						EntitiesManager.instance.getEntities().get(i).getCorps().getHeight());
						
				if(c.contains(input.getMouseX(), input.getMouseY()))
				{
					entity_pressed = true;
					if((EntitiesManager.instance.getEntities().get(i)) instanceof PNJ)
					{
						PnjDialogFrame dialog = new PnjDialogFrame(EntitiesManager.instance.getPnjs_manager().getPnjs().get(i));
						
						GUI_Manager.instance.getRoot().add(dialog);
						dialog.setSize(400, 400);
						dialog.setPosition((Base.sizeOfScreen_x/2)-(dialog.getWidth()/2), (Base.sizeOfScreen_y/2)-(dialog.getHeight()/2));
					}
					
					else if((EntitiesManager.instance.getEntities().get(i)) instanceof Joueur)
					{
						if(MainJoueur.instance.getPerso().getCurrent_sort() != null)
						{
							String nom_receveur = ((Joueur)(EntitiesManager.instance.getEntities().get(i))).getPerso().getNom();
							String nom_sort = MainJoueur.instance.getPerso().getCurrent_sort().getNom();
							NetworkManager.instance.sendToServer("a;"+nom_receveur+";"+nom_sort);
							System.out.println("attaque envoyée !");
						}
						else
						{
							boolean encours = false;
							for(int k = 0; k < CombatManager.instance.getMainJoueurCombats().size(); k++)
							{
								if(CombatManager.instance.getMainJoueurCombats().get(k).getEtat().equals(EtatCombat.EN_COURS))
								{
									encours = true;
								}
							}
							if(!encours)
							{
								CombatManager.instance.askCombat((Joueur)EntitiesManager.instance.getEntities().get(i));
							}
						}
					}
					else
					{
						Tile tile_pressed = MapManager.instance.getTileScreen(new Vector2f(input.getMouseX(), input.getMouseY()));
						if(tile_pressed != null)
						{
							MainJoueur.instance.startMoving(pf.calculateChemin(MainJoueur.instance.getTile(), tile_pressed));
						}
					}
					
				}
			}
			
			if(!entity_pressed)
			{
				Tile tile_pressed = MapManager.instance.getTileScreen(new Vector2f(input.getMouseX(), input.getMouseY()));
				if(tile_pressed != null)
				{
					MainJoueur.instance.startMoving(pf.calculateChemin(MainJoueur.instance.getTile(), tile_pressed));
				}
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
}
