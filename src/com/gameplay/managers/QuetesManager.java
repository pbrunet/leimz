package com.gameplay.managers;

import java.util.ArrayList;


import com.client.entities.Joueur;
import com.gameplay.Quete;

public class QuetesManager 
{
	private ArrayList<Quete> quetes;
	
	public QuetesManager(ArrayList<Quete> quetes)
	{
		this.quetes = quetes;
	}
	
	public QuetesManager(Quete quete)
	{
		this.quetes = new ArrayList<Quete>();
		quetes.add(quete);
	}
	
	public QuetesManager()
	{
		this.quetes = new ArrayList<Quete>();
	}
	
	public void addQuetes(Quete quete)
	{
		quetes.add(quete);
	}
	
	public void testQuetes(Joueur joueur)
	{
		for(int i = 0; i < quetes.size(); i++)
		{
			quetes.get(i).testObjectifs(joueur);
		}
	}

	public ArrayList<Quete> getQuetes() {
		return quetes;
	}

	public void setQuetes(ArrayList<Quete> quetes) {
		this.quetes = quetes;
	}
}
