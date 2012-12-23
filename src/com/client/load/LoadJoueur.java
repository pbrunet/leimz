package com.client.load;

import java.util.ArrayList;

import java.util.HashMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;
import com.client.entities.Joueur;
import com.client.entities.Orientation;
import com.client.network.NetworkManager;
import com.gameplay.Caracteristique;
import com.gameplay.Classe;
import com.gameplay.Inventaire;
import com.gameplay.Race;
import com.gameplay.Sort;
import com.gameplay.entities.Personnage;
import com.gameplay.items.Equipement;
import com.map.client.managers.MapManager;

/**
 * 
 * @author chelendil
 * Classe chargeant les informations de classe et de race du joueur (sort, competences ...)
 */
public class LoadJoueur implements Runnable
{
	private Thread t;
	private int purcent;
	private Joueur joueur;
	private ArrayList<Joueur> list_joueur;

	public LoadJoueur()
	{
		this.list_joueur = new ArrayList<Joueur>();
		this.purcent = 0;
		t = new Thread(this);
	}
	
	public void start()
	{
		t.start();
	}

	@Override
	public void run() 
	{
			NetworkManager.instance.waitForNewMessage("ci");

			//On recupere la chaine avec les infos sur le perso
			String[] args_perso = NetworkManager.instance.receiveFromServer("ci").split(";");
			if(args_perso.length<6)
				throw new RuntimeException("Incorrect login message from server");

			String nom_perso = args_perso[0];
			String nom_race = args_perso[1];
			String nom_classe = args_perso[2];
			int posx = Integer.parseInt(args_perso[3]);
			int posy = Integer.parseInt(args_perso[4]);
			Orientation ori = Joueur.parseStringOrientation(args_perso[5]);
			System.out.println("Pos : "+posx+","+posy);

			//-------------------GESTION DE LA RACE-----------------------

			Race race = new Race(nom_race, getSorts("lo;j;rs"), getCaracteristic("lo;j;rc"));

			//-------------------GESTION DE LA CLASSE-----------------------

			Classe classe = new Classe(nom_classe, getSorts("lo;j;cs"), getCaracteristic("lo;j;cc"));

			//---------------GESTION DU PERSONNAGE------------------

			joueur = new Joueur(new Personnage(
					nom_perso, 
					race, 
					classe,
					getCaracteristic("lo;j;jcv"),
					getCaracteristic("lo;j;jc")), 
					MapManager.instance.getEntire_map().getGrille().get(posx).get(posy), 
					ori);

			//----------------GESTION DE L'INVENTAIRE---------------------

			LoadingList.setDeferredLoading(true);

			Inventaire inventaire = new Inventaire();

			NetworkManager.instance.sendToServer("lo;j;in"); //load joueur, joueur caracteristiques
			NetworkManager.instance.waitForNewMessage("in");
			String[] str_i = NetworkManager.instance.receiveFromServer("in").split(";");

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

			joueur.getPerso().setInventaire(inventaire);

			purcent+=6;
	}

	public int getPurcent() {
		return purcent;
	}

	public void setPurcent(int purcent) {
		this.purcent = purcent;
	}

	@SuppressWarnings("unchecked")
	private HashMap<Caracteristique,Integer>getCaracteristic(String message)
	{
		NetworkManager.instance.sendToServer(message);
		NetworkManager.instance.waitForNewMessage(message.split(";",3)[2]);
		String[] caract = NetworkManager.instance.receiveFromServer(message.split(";",3)[2]).split(";");
		if(caract.length<2)
			throw new RuntimeException("Incorrect caracteristic loading message from server");

		HashMap<Caracteristique,Integer> caracs = new HashMap<Caracteristique,Integer>();
		for(int i = 0; i < caract.length; i+=2)
			caracs.put(Caracteristique.valueOf(caract[i].toUpperCase()), Integer.parseInt(caract[i+1]));
		return (HashMap<Caracteristique, Integer>) caracs.clone();
	}

	@SuppressWarnings("unchecked")
	private ArrayList<Sort>getSorts(String message)
	{
		ArrayList<Sort> sorts = new ArrayList<Sort>();
		NetworkManager.instance.sendToServer(message);
		NetworkManager.instance.waitForNewMessage(message.split(";")[2]);
		String[] sort = NetworkManager.instance.receiveFromServer(message.split(";")[2]).split(";");
		if(sort.length<4)
			throw new RuntimeException("Incorrect sorts loading message from server");

		for(int i = 0; i < sort.length; i+=4)
			sorts.add(new Sort(sort[i],sort[i+3],Integer.parseInt(sort[i+1]),Integer.parseInt(sort[i+2]),null));

		return (ArrayList<Sort>) sorts.clone();
	}
	
	public Joueur getJoueur() {
		return joueur;
	}

	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}
	
	public ArrayList<Joueur> getPlayers() {
		return list_joueur;
	}

	public Thread getT() {
		return t;
	}

	public void setT(Thread t) {
		this.t = t;
	}
	
	

}
