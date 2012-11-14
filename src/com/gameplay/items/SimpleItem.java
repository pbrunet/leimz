package com.gameplay.items;

import java.util.HashMap;

import org.newdawn.slick.Image;

import com.gameplay.Caracteristique;

public class SimpleItem 
{
	private String nom, description;
	private String icon_file, apercu_file;
	private int poids;
	private HashMap<Caracteristique,Integer> effets;
	
	public SimpleItem(String nom, String description, String icon_file, String desc_file, HashMap<Caracteristique,Integer> effets, int poids)
	{
		this.nom = nom;
		this.description = description;
		this.icon_file = icon_file;
		this.apercu_file = desc_file;
		this.effets = effets;
		this.poids = poids;
	}
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon_file() {
		return icon_file;
	}

	public void setIcon_file(String icon_file) {
		this.icon_file = icon_file;
	}

	public String getApercu_file() {
		return apercu_file;
	}

	public void setApercu_file(String apercu_file) {
		this.apercu_file = apercu_file;
	}

	public HashMap<Caracteristique, Integer> getEffets() {
		return effets;
	}

	public void setEffets(HashMap<Caracteristique, Integer> effets) {
		this.effets = effets;
	}

	public int getPoids() {
		return poids;
	}

	public void setPoids(int poids) {
		this.poids = poids;
	}
}
