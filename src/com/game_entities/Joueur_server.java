package com.game_entities;

import com.gameplay.entities.Personnage_serveur;

public class Joueur_server
{
	private Personnage_serveur perso;
	private float posx;
	private float posy;
	private Orientation dir;
	
	public Joueur_server(Personnage_serveur perso, float posx, float posy, Orientation orientation)
	{
		this.perso = perso;
		this.dir = orientation;
		this.setPosx(posx);
		this.setPosy(posy);
	}
	
	public Personnage_serveur getPerso() {
		return perso;
	}

	public void setPerso(Personnage_serveur perso) {
		this.perso = perso;
	}

	public Orientation getDir() {
		return dir;
	}

	public void setDir(Orientation dir) {
		this.dir = dir;
	}

	public float getPosx() {
		return posx;
	}

	public void setPosx(float posx) {
		this.posx = posx;
	}

	public float getPosy() {
		return posy;
	}

	public void setPosy(float posy) {
		this.posy = posy;
	}
}
