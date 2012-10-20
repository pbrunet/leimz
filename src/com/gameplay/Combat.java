package com.gameplay;

import java.util.ArrayList;

import com.map.Tile;

public class Combat
{
	private ArrayList<Equipe> equipes;
	private ArrayList<Tile> zone;
	public enum EtatCombat
	{
		INIT, EN_COURS, FINI
	};
	private EtatCombat etat;
	
	public Combat(ArrayList<Equipe> equipes, ArrayList<Tile> zone)
	{
		this.equipes = equipes;
		this.zone = zone;
		
		etat =  EtatCombat.INIT;
	}

	public ArrayList<Equipe> getEquipes() {
		return equipes;
	}

	public void setEquipes(ArrayList<Equipe> equipes) {
		this.equipes = equipes;
	}

	public ArrayList<Tile> getZone() {
		return zone;
	}

	public void setZone(ArrayList<Tile> zone) {
		this.zone = zone;
	}

	public EtatCombat getEtat() {
		return etat;
	}

	public void setEtat(EtatCombat etat) {
		this.etat = etat;
	}
	
}
