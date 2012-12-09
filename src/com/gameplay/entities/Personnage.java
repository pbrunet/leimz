package com.gameplay.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import com.gameplay.Caracteristique;
import com.gameplay.Classe;
import com.gameplay.Combat;
import com.gameplay.Inventaire;
import com.gameplay.Race;
import com.gameplay.Sort;
import com.gameplay.managers.QuetesManager;

public class Personnage 
{
	private String nom;
	private Race race;
	private Classe classe;
	private HashMap<Caracteristique,Integer> caracs_values;
	private HashMap<Caracteristique,Integer> caracs;
	@SuppressWarnings("unused")
	private ArrayList<Sort> sorts;
	
	private Sort current_sort;

	//------------------STATIC CARACS--------------------
	public enum caracteristique { VIE, ENERGIE, DEPLACEMENT, ENDURANCE, PRECISION, DOMMAGE_CAC, DOMMAGE_MAGIE };
	
	private QuetesManager quetes_manager;
	private Inventaire inventaire;
	private String entity_file;
	
	private ArrayList<Combat> combats;
	private Combat current_combat;
	
	public Personnage(String nom, Race race, Classe classe, HashMap<Caracteristique,Integer> carac_val, HashMap<Caracteristique,Integer> carac)
	{
		this.race = race ;
		this.classe = classe;
		this.nom = nom;
		this.setEntity_file("data/Gameplay/Races/"+race.getNom()+"/Classes/"+"entity_"+classe.getNom().toLowerCase()+".xml");
		this.caracs = carac;
		this.caracs_values = carac_val;
		this.quetes_manager = new QuetesManager();
		this.inventaire = new Inventaire();
		this.combats = new ArrayList<>();
	}
	
	public Personnage(String nom, String race, String classe)
	{
		this.nom = nom;
		this.setEntity_file("data/Gameplay/Races/"+race+"/Classes/"+"entity_"+classe.toLowerCase()+".xml");

		this.race = new Race(race);
		this.classe = new Classe(classe);
		this.caracs = new HashMap<>();
		this.caracs_values = new HashMap<>();
		this.quetes_manager = new QuetesManager();
		this.inventaire = new Inventaire();
		this.combats = new ArrayList<>();
	}
	
	public Personnage()
	{
		this.race = new Race("");
		this.classe = new Classe("");
		
		this.caracs = new HashMap<>();
		this.caracs_values = new HashMap<>();
		this.quetes_manager = new QuetesManager();
		this.inventaire = new Inventaire();
		this.combats = new ArrayList<>();
	}
	
	public Inventaire getInventaire() {
		return inventaire;
	}

	public void setInventaire(Inventaire inventaire) {
		this.inventaire = inventaire;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race clan) {
		this.race = clan;
	}

	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	public ArrayList<Sort> getSorts() {
		ArrayList<Sort> tmp = new ArrayList<Sort>();
		tmp.addAll(race.getSorts());
		tmp.addAll(classe.getSorts());
		return tmp;
	}

	public HashMap<Caracteristique,Integer> getCaracsBase() {
		
		HashMap<Caracteristique,Integer> caracs_base = new HashMap<Caracteristique,Integer>(classe.getCaracs());
		for(Entry<Caracteristique, Integer> entry : race.getCarac().entrySet()) {
			if(caracs_base.containsKey(entry.getKey()))
				caracs_base.put(entry.getKey(),caracs_base.get(entry.getKey()) + entry.getValue());
			else
				caracs_base.put(entry.getKey(), entry.getValue());
		}
		return caracs_base;
	}
	
	public HashMap<Caracteristique,Integer> getCaracs() {
		return caracs;
	}

	public void setCaracs(HashMap<Caracteristique,Integer> caracs) {
		this.caracs = caracs;
	}

	public QuetesManager getQuetes_manager() {
		return quetes_manager;
	}

	public void setQuetes_manager(QuetesManager quetesManager) {
		quetes_manager = quetesManager;
	}

	public HashMap<Caracteristique,Integer> getCaracs_values() {
		return caracs_values;
	}

	public void setCaracs_values(HashMap<Caracteristique,Integer> caracs_values) {
		this.caracs_values = caracs_values;
	}

	public String getEntity_file() {
		return entity_file;
	}

	public void setEntity_file(String entity_file) {
		this.entity_file = entity_file;
	}

	public Sort getCurrent_sort() {
		return current_sort;
	}

	public void setCurrent_sort(Sort current_sort) {
		this.current_sort = current_sort;
	}

	public boolean isDead() {
		return this.getCaracs().get(Caracteristique.VIE)<=0;
	}

	public void setSorts(ArrayList<Sort> sorts) {
		this.sorts = sorts;
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