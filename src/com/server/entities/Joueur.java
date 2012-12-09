package com.server.entities;

import java.util.ArrayList;

import com.server.entities.Personnage;
import com.map.Tile;

public class Joueur extends Entity
{
	protected Personnage perso;
	
	protected ArrayList<Tile> loaded_zone;
	
	public Joueur(Personnage perso, Tile tile, Orientation orientation)
	{
		super(orientation, tile);
		this.perso = perso;

		loaded_zone = new ArrayList<Tile>();
	}
	
	protected String stringOrientation()
	{
		String o_m = null;
		switch(orientation)
		{
			case DROITE:
				o_m = "d";
				break;
			case GAUCHE:
				o_m = "g";
				break;
			case HAUT:
				o_m = "h";
				break;
			case BAS:
				o_m = "b";
				break;
			case HAUT_DROITE:
				o_m = "hd";
				break;
			case HAUT_GAUCHE:
				o_m = "hg";
				break;
			case BAS_DROITE:
				o_m = "bd";
				break;
			case BAS_GAUCHE:
				o_m = "bg";
				break;
		}
		return o_m;
	}
	
	public static Orientation parseStringOrientation(String o_m)
	{
		Orientation o = null;
		
		if(o_m.equals("d"))
			o = Orientation.DROITE;
		else if(o_m.equals("g"))
			o = Orientation.GAUCHE;
		else if(o_m.equals("h"))
			o = Orientation.HAUT;
		else if(o_m.equals("b"))
			o = Orientation.BAS;
		else if(o_m.equals("hd"))
			o = Orientation.HAUT_DROITE;
		else if(o_m.equals("hg"))
			o = Orientation.HAUT_GAUCHE;
		else if(o_m.equals("bd"))
			o = Orientation.BAS_DROITE;
		else if(o_m.equals("bg"))
			o = Orientation.BAS_GAUCHE;
		
		return o;
	}
	
	public Personnage getPerso() {
		return perso;
	}

	public void setPerso(Personnage perso) {
		this.perso = perso;
	}

	public ArrayList<Tile> getLoaded_zone() {
		return loaded_zone;
	}

	public void setLoaded_zone(ArrayList<Tile> loaded_zone) {
		this.loaded_zone = loaded_zone;
	}
	
	
}
