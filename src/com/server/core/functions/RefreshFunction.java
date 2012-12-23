package com.server.core.functions;

import java.util.ArrayList;

import com.server.entities.managers.EntitiesManager;
import com.map.Tile;
import com.map.server.managers.MapManager;
import com.server.core.Calculator;
import com.server.core.Client;
import com.server.core.GlobalConstant;
import com.server.entities.Entity;
import com.server.entities.Joueur;
import com.server.entities.PNJ;

public class RefreshFunction implements Functionable
{
	public RefreshFunction()
	{
		
	}

	@Override
	public void doSomething(String[] args, Client c) 
	{
		ArrayList<Entity> loaded_entities = c.getCompte().getCurrent_joueur().getLoaded_entities();
		System.out.println(loaded_entities.size());
		ArrayList<ArrayList<Tile>> grille = MapManager.instance.getTilesAutour(c.getCompte().getCurrent_joueur().getTile(), GlobalConstant.nbCaseNear);
		
		ArrayList<Entity> unloaded_entities = new ArrayList<Entity>();
		for(int i = 0; i < grille.size(); i++)
		{
			for(int j = 0; j < grille.get(i).size(); j++)
			{
				for(int k = 0; k < EntitiesManager.instance.getEntities().size(); k++)
				{
					if(EntitiesManager.instance.getEntities().get(k).getTile().equals(grille.get(i).get(j)))
					{
						if(!loaded_entities.contains(EntitiesManager.instance.getEntities().get(k)) && EntitiesManager.instance.getEntities().get(k)!=c.getCompte().getCurrent_joueur())
						{
							unloaded_entities.add(EntitiesManager.instance.getEntities().get(k));
						}
					}
				}
			}
		}
		
		for(int i = 0; i < loaded_entities.size(); i++)
		{
			if(loaded_entities.get(i) instanceof Joueur && loaded_entities.get(i)!=c.getCompte().getCurrent_joueur())
			{
				c.sendToClient(
						"s;j;"
				+((Joueur)loaded_entities.get(i)).getPerso().getNom()+";"
				+loaded_entities.get(i).getPos_real().x+";"
				+loaded_entities.get(i).getPos_real().y+";"
				+loaded_entities.get(i).stringOrientation()
						);
			}
			else if(loaded_entities.get(i) instanceof PNJ)
			{
				c.sendToClient(
						"s;pnj;"
				+((PNJ)loaded_entities.get(i)).getNom()+";"
				+loaded_entities.get(i).getPos_real().x+";"
				+loaded_entities.get(i).getPos_real().y+";"
				+loaded_entities.get(i).stringOrientation()
						);
			}
		}
		
		
		for(int i = 0; i < unloaded_entities.size(); i++)
		{
			if(unloaded_entities.get(i) instanceof Joueur)
			{
				((LoadFunction)Calculator.dictfunctions.get("lo")).loadPerso(c,(Joueur) unloaded_entities.get(i));
				c.getCompte().getCurrent_joueur().getLoaded_entities().add(unloaded_entities.get(i));
			}
			else if(unloaded_entities.get(i) instanceof PNJ)
			{
				((LoadFunction)Calculator.dictfunctions.get("lo")).loadPnj(c,(PNJ) unloaded_entities.get(i));
				c.getCompte().getCurrent_joueur().getLoaded_entities().add(unloaded_entities.get(i));
			}
		}
		
	}

}
