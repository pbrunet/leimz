package com.client.load;

import java.util.ArrayList;

import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import com.client.network.NetworkManager;

import com.game_entities.Joueur;

import com.gameplay.Caracteristique;
import com.gameplay.Classe;
import com.gameplay.Inventaire;
import com.gameplay.Race;
import com.gameplay.Sort;
import com.gameplay.entities.Personnage;
import com.gameplay.items.Equipement;

/**
 * 
 * @author chelendil
 * Classe chargeant les informations de classe et de race du joueur (sort, competences ...)
 */
public class LoadJoueur implements Runnable
{
	private Thread looper;
	private int purcent;
	private boolean running;
	private Personnage perso;
	private Joueur joueur;

	public LoadJoueur(int purcent)
	{
		this.purcent = purcent;
		looper = new Thread(this);
		looper.start();
		running = true;
	}

	@Override
	public void run() 
	{
		if(running)
		{
			System.out.println("Loading player ...");

			NetworkManager network = NetworkManager.instance;
			network.waitForNewMessage();

			//On recupere la chaine avec les infos sur le perso
			String str_perso = network.getMessage_recu_serveur();
			String[] args_perso = str_perso.split(";");
			if(args_perso.length<3)
				throw new RuntimeException("Incorrect login message from server");

			String nom_perso = args_perso[0];
			String nom_race = args_perso[1];
			String nom_classe = args_perso[2];

			//-------------------GESTION DE LA RACE-----------------------

			//Caracteristiques de race
			network.sendToServer("lo;rc"); //load, race caracteristiques
			network.waitForNewMessage();
			String[] race_caract = network.getMessage_recu_serveur().split(";");
			if(race_caract.length<2)
				throw new RuntimeException("Incorrect race caracteristic loading message from server");

			HashMap<Caracteristique,Integer> caracs_race = new HashMap<Caracteristique,Integer>();
			for(int i = 0; i < race_caract.length; i+=2)
				caracs_race.put(Caracteristique.valueOf(race_caract[i].toUpperCase()), Integer.parseInt(race_caract[i+1]));

			//Sorts de race
			network.sendToServer("lo;rs"); //load, race sort
			network.waitForNewMessage();
			String[] race_sort = network.getMessage_recu_serveur().split(";");
			if(race_sort.length<4)
				throw new RuntimeException("Incorrect race sort loading message from server");

			ArrayList<Sort> sorts_race = new ArrayList<Sort>();
			for(int i = 0; i < race_sort.length; i+=4)
				sorts_race.add(new Sort(race_sort[i],race_sort[i+3],Integer.parseInt(race_sort[i+1]),Integer.parseInt(race_sort[i+2]),null));

			Race race = new Race(nom_race, sorts_race, caracs_race);

			//-------------------GESTION DE LA CLASSE-----------------------

			//Caracteristiques de classe
			HashMap<Caracteristique,Integer> caracs_classe = new HashMap<Caracteristique,Integer>();

			network.sendToServer("lo;cc"); //load, classe caracteristiques
			network.waitForNewMessage();
			String[] classe_caract = network.getMessage_recu_serveur().split(";");
			if(classe_caract.length<2)
				throw new RuntimeException("Incorrect classe caracteristic loading message from server");

			for(int i = 0; i < classe_caract.length; i+=2)
				caracs_classe.put(Caracteristique.valueOf(classe_caract[i].toUpperCase()), Integer.parseInt(classe_caract[i+1]));

			//Sorts de classe
			ArrayList<Sort> sorts_classe = new ArrayList<Sort>();

			network.sendToServer("lo;cs"); //load joueur, classe sorts
			network.waitForNewMessage();
			String[] classe_sort = network.getMessage_recu_serveur().split(";");
			if(classe_sort.length<4)
				throw new RuntimeException("Incorrect classe sorts loading message from server");

			for(int i = 0; i < classe_sort.length; i+=4)
				sorts_classe.add(new Sort(classe_sort[i],classe_sort[i+3],Integer.parseInt(classe_sort[i+1]),Integer.parseInt(classe_sort[i+2]),null));

			Classe classe = new Classe(nom_classe, sorts_classe, caracs_classe);


			//---------------GESTION DU PERSONNAGE------------------

			//Caracteristiques du joueur
			HashMap<Caracteristique,Integer> caracs_joueur = new HashMap<Caracteristique,Integer>();

			network.sendToServer("lo;jc"); //load, joueur caracteristiques
			network.waitForNewMessage();
			String[] joueur_caract = network.getMessage_recu_serveur().split(";");
			if(joueur_caract.length<2)
				throw new RuntimeException("Incorrect joueur caracteristic loading message from server");

			for(int i = 0; i < joueur_caract.length; i+=2)
				caracs_joueur.put(Caracteristique.valueOf(joueur_caract[i].toUpperCase()), Integer.parseInt(joueur_caract[i+1]));

			//Valeurs des caracteristiques du joueur
			HashMap<Caracteristique,Integer> caracsval_joueur = new HashMap<Caracteristique,Integer>();

			network.sendToServer("lo;jcv"); //load, joueur caracteristiques values
			network.waitForNewMessage();
			String[] joueur_caractval = network.getMessage_recu_serveur().split(";");
			if(joueur_caractval.length<2)
				throw new RuntimeException("Incorrect joueur caracteristic value loading message from server");

			for(int i = 0; i < joueur_caractval.length; i+=2)
				caracsval_joueur.put(Caracteristique.valueOf(joueur_caractval[i].toUpperCase()), Integer.parseInt(joueur_caractval[i+1]));

			//GESTION DE L'INVENTAIRE

			LoadingList.setDeferredLoading(true);

			Inventaire inventaire = new Inventaire();

			network.sendToServer("lo;in"); //load joueur, joueur caracteristiques
			network.waitForNewMessage();
			String[] str_i = network.getMessage_recu_serveur().split(";");

			//Do nothing if inventaire is empty
			if(str_i.length>6)
			{
				for(int i = 0; i < str_i.length; i+=7)
				{
					HashMap<Caracteristique, Integer> h = new HashMap<Caracteristique, Integer>();
					h.put(Caracteristique.valueOf(str_i[i+5].toUpperCase()), Integer.parseInt(str_i[i+6]));
					try {
						inventaire.addItem(new Equipement(str_i[i], str_i[i+2], str_i[i+1], new Image(str_i[i+3]), new Image(str_i[i+4]), h, 20));
					} catch (SlickException e) {
						System.out.println("E: SlickException : this Image doesn't existe");
					}
				}
			}
			LoadingList.setDeferredLoading(false);

			perso = new Personnage(nom_perso, race, classe,caracsval_joueur,caracs_joueur);
			perso.setInventaire(inventaire);

			purcent+=6;

			try {
				//TODO Pourquoi un sleep?
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			running = false;
		}
	}

	public Joueur getJoueur() {
		return joueur;
	}

	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}

	public int getPurcent() {
		return purcent;
	}

	public void setPurcent(int purcent) {
		this.purcent = purcent;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Personnage getPerso() {
		return perso;
	}

	public void setPerso(Personnage perso) {
		this.perso = perso;
	}
}
