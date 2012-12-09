package com.server.entities;

import java.util.ArrayList;
import com.gameplay.PNJ_discours;
import com.gameplay.Quete;
import com.map.Tile;

public class PNJ extends Entity
{
	private String nom;
	
	private ArrayList<Quete> quetes;
	private PNJ_discours discours;
	
	public PNJ(String nom, PNJ_discours discours, Orientation orientation, Tile tile)
	{
		super(orientation, tile);
	
		this.nom = nom;
		this.discours = discours;
	}
	
	public PNJ(String nom, PNJ_discours discours, ArrayList<Quete> quetes, Orientation orientation, Tile tile)
	{
		super(orientation, tile);
		
		this.nom = nom;
		this.discours = discours;
		this.quetes = quetes;
	}


	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public ArrayList<Quete> getQuetes() {
		return quetes;
	}

	public void setQuetes(ArrayList<Quete> quetes) {
		this.quetes = quetes;
	}

	public PNJ_discours getPnjDiscours() {
		return discours;
	}

	public void setPnjDiscours(PNJ_discours discours) {
		this.discours = discours;
	}
}
