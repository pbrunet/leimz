package com.gameplay.managers;

import java.util.ArrayList;
import java.util.HashMap;

import com.client.display.gui.GUI_Manager;
import com.client.gamestates.Base;
import com.client.network.NetworkListener;
import com.client.network.NetworkManager;
import com.client.utils.gui.PrincipalGui;
import com.game_entities.Joueur;
import com.game_entities.MainJoueur;
import com.game_entities.managers.EntitiesManager;
import com.gameplay.Caracteristique;
import com.gameplay.Combat;
import com.gameplay.Equipe;
import com.map.Tile;
import com.map.client.managers.MapManager;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ResizableFrame;


public class CombatManager implements NetworkListener
{
	private ArrayList<Combat> mainJoueurCombats;
	private ArrayList<Combat> combats;
	private Combat current_combat;
	
	private ResizableFrame waitingAskFrame, answerFrame;
	
	public static CombatManager instance;
	
	public CombatManager()
	{
		instance = this;
		mainJoueurCombats = new ArrayList<Combat>();
		combats = new ArrayList<Combat>();
	}
	
	public void askCombat(Joueur j)
	{
		final String nom = j.getPerso().getNom();
		NetworkManager.instance.sendToServer("co;ask;"+nom);
		
		waitingAskFrame = PrincipalGui.getDialogPane("En attente de réponse pour votre demande de combat à "+j.getPerso().getNom(),  PrincipalGui.CANCEL_PANE);
		((Button)waitingAskFrame.getChild(0).getChild(1)).addCallback(new Runnable() {
			
			@Override
			public void run() {
				waitingAskFrame.setVisible(false);
				NetworkManager.instance.sendToServer("co;can;"+nom);
			}
		});
		GUI_Manager.instance.getRoot().add(waitingAskFrame);
		waitingAskFrame.adjustSize();
	    waitingAskFrame.setPosition((Base.sizeOfScreen_x/2)-(waitingAskFrame.getWidth()/2), (Base.sizeOfScreen_y/2)-(waitingAskFrame.getHeight()/2));
	}
	
	public void stopCurrentCombat(Equipe perdante)
	{
		current_combat.stop();
		current_combat = null;
		
		ResizableFrame resultsFrame = new ResizableFrame();
		resultsFrame.setTheme("/resizableframe");
		resultsFrame.setTitle("Résultats du combat formel");
		
		GUI_Manager.instance.getRoot().add(resultsFrame);
	}
	
	public void refresh()
	{
		if(current_combat != null)
		{
			Equipe perdante = null;
			ArrayList<HashMap<Joueur, Boolean>> checkinglist = new ArrayList<HashMap<Joueur, Boolean>>();
			for(int i = 0; i < current_combat.getEquipes().size(); i++)
			{
				for(int j = 0; j < current_combat.getEquipes().get(i).getMembres().size(); j++)
				{
					System.out.println(current_combat.getEquipes().get(i).getMembres().get(j).getPerso().getNom()+" : vie;"+current_combat.getEquipes().get(i).getMembres().get(j).getPerso().getCaracs().get(Caracteristique.VIE));
					checkinglist.add(new HashMap<Joueur, Boolean>());
					if(current_combat.getEquipes().get(i).getMembres().get(j).getPerso().isDead())
					{
						checkinglist.get(checkinglist.size()-1).put(current_combat.getEquipes().get(i).getMembres().get(j), true);
						System.out.println(current_combat.getEquipes().get(i).getMembres().get(j).getPerso().getNom()+" est mort !");
					}
					else
					{
						checkinglist.get(checkinglist.size()-1).put(current_combat.getEquipes().get(i).getMembres().get(j), false);
					}
				}
			}
			for(int i = 0; i < checkinglist.size(); i++)
			{
				if(!checkinglist.get(i).containsValue(false))
				{
					perdante = current_combat.getEquipes().get(i);
				}
			}
			if(perdante != null)
			{
				System.out.println("Equipe perdante, equipe de : "+perdante.getMembres().get(0).getPerso().getNom());
				stopCurrentCombat(perdante);
			}
			else
			{
				System.out.println("Pas d'équipe perdante !");
			}
		}
		
	}

	@Override
	public void receiveMessage(String str) 
	{
		String[] temp = str.split(";");
		if(temp[0].equals("co"))
		{
			if(temp[1].contains("ask"))
			{
				final String nom = temp[2];
				answerFrame = PrincipalGui.getDialogPane(nom+" vous propose un combat formel. Acceptez-vous ?", PrincipalGui.YES_NO_PANE);
				((Button)answerFrame.getChild(0).getChild(1)).addCallback(new Runnable() {
					
					@Override
					public void run() 
					{
						NetworkManager.instance.sendToServer("co;an;y;"+nom);
						answerFrame.setVisible(false);
						
						Equipe equipe1 = new Equipe("Equipe 1", MainJoueur.instance);
						Equipe equipe2 = new Equipe("Equipe 2", EntitiesManager.instance.getPlayers_manager().getJoueur(nom));
						ArrayList<Equipe> equipes = new ArrayList<Equipe>();
						equipes.add(equipe1);
						equipes.add(equipe2);
						
						ArrayList<Tile> zone= new ArrayList<Tile>();
						ArrayList<ArrayList<Tile>> tiles_autour = MapManager.instance.getTilesAutour(MainJoueur.instance.getTile(), 10);
						for(int k = 0; k < tiles_autour.size(); k++)
						{
							zone.addAll(tiles_autour.get(k));
						}
						Combat combat = new Combat(equipes, zone);
						getMainJoueurCombats().add(combat);
						current_combat = combat;
					}
				});
				((Button)answerFrame.getChild(0).getChild(2)).addCallback(new Runnable() {
					
					@Override
					public void run() {
						NetworkManager.instance.sendToServer("co;an;n;"+nom);
						answerFrame.setVisible(false);
					}
				});
				
				GUI_Manager.instance.getRoot().add(answerFrame);
				answerFrame.adjustSize();
				answerFrame.setPosition((Base.sizeOfScreen_x/2)-(answerFrame.getWidth()/2), (Base.sizeOfScreen_y/2)-(answerFrame.getHeight()/2));
			}
			else if(temp[1].contains("can"))
			{
				answerFrame.setVisible(false);
			}
			else if(temp[1].contains("an"))
			{
				if(temp[2].equals("y"))
				{
					waitingAskFrame.setVisible(false);
					
					String nom_j = temp[3];
					Equipe equipe1 = new Equipe("Equipe 1", MainJoueur.instance);
					Equipe equipe2 = new Equipe("Equipe 2", EntitiesManager.instance.getPlayers_manager().getJoueur(nom_j));
					ArrayList<Equipe> equipes = new ArrayList<Equipe>();
					equipes.add(equipe1);
					equipes.add(equipe2);
					
					ArrayList<Tile> zone= new ArrayList<Tile>();
					ArrayList<ArrayList<Tile>> tiles_autour = MapManager.instance.getTilesAutour(MainJoueur.instance.getTile(), 10);
					for(int k = 0; k < tiles_autour.size(); k++)
					{
						zone.addAll(tiles_autour.get(k));
					}
					Combat combat = new Combat(equipes, zone);
					getMainJoueurCombats().add(combat);
					current_combat = combat;
				}
				else if(temp[2].equals("n"))
				{
					waitingAskFrame.setVisible(false);
				}
			}
		}
		
	}

	public ArrayList<Combat> getMainJoueurCombats() {
		return mainJoueurCombats;
	}

	public void setMainJoueurCombats(ArrayList<Combat> mainJoueurCombats) {
		this.mainJoueurCombats = mainJoueurCombats;
	}

	public ArrayList<Combat> getCombats() {
		return combats;
	}

	public void setCombats(ArrayList<Combat> combats) {
		this.combats = combats;
	}

	public Combat getCurrent_combat() {
		return current_combat;
	}

	public void setCurrent_combat(Combat current_combat) {
		this.current_combat = current_combat;
	}

	
	
	
}
