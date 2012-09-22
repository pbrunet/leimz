package com.gameplay;

import java.util.ArrayList;
import java.util.HashMap;

public class Classe 
{
	private String nom;
	private ArrayList<Sort> sorts;
	private HashMap<Caracteristique,Integer> carac;
	
	public Classe(String nom, ArrayList<Sort> sorts, HashMap<Caracteristique,Integer> carac)
	{
		this.nom = nom;
		this.sorts = sorts;
		this.carac = carac;
	}

	public ArrayList<Sort> getSorts() {
		return sorts;
	}

	public void setSorts(ArrayList<Sort> sorts) {
		this.sorts = sorts;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public HashMap<Caracteristique,Integer> getCarac() {
		return carac;
	}

	public void setCarac(HashMap<Caracteristique,Integer> carac) {
		this.carac = carac;
	}
}
