package com.gameplay;

import java.util.ArrayList;

import org.jdom.Element;
import org.newdawn.slick.geom.Vector2f;

import com.game_entities.Joueur;

public class Quete 
{
	private String nom, commanditaire;
	private ArrayList<Objectif> objectifs;
	
	public Quete(String commanditaire, Element root)
	{
		this.nom = root.getChild("nom").getText();
		this.commanditaire = commanditaire;
		
		this.objectifs = new ArrayList<Objectif>();
		for(int i = 0; i < root.getChild("objectifs").getChildren("objectif").size(); i++)
		{
			this.objectifs.add(new Objectif(
					((Element) root.getChild("objectifs").getChildren("objectif").get(i)).getChild("description").getText()
					, ((Element) root.getChild("objectifs").getChildren("objectif").get(i)).getChild("but").getText())
					);
		}
	}
	
	public void testObjectifs(Joueur main_player)
	{
		for(int i = 0; i < objectifs.size(); i++)
		{
			if(!objectifs.get(i).isAccompli())
			{
				if(objectifs.get(i).getType().equals("endroit"))
				{
					if(((float)main_player.getTile().getPos().x) == (((Vector2f)objectifs.get(i).getObjectif()).getX()) && ((float)main_player.getTile().getPos().y) == (((Vector2f)objectifs.get(i).getObjectif()).getY()))
					{
						objectifs.get(i).setAccompli(true);
						System.out.println("Objectif reussie !");
					}
				}
			}
			
		}
	}
	
	class Objectif
	{
		private String description;
		private boolean accompli;
		private Object objectif;
		private String type;
		
		public Objectif(String description, String text_objectif)
		{
			this.description = description;

			if(text_objectif.contains("tile"))
			{
				String[] pos = text_objectif.substring(5, text_objectif.length()-1).split(",");
				this.objectif = new Vector2f(Float.parseFloat(pos[0]), Float.parseFloat(pos[1]));
				type = "endroit";
			}
		}
		
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public boolean isAccompli() {
			return accompli;
		}
		public void setAccompli(boolean accompli) {
			this.accompli = accompli;
		}

		public Object getObjectif() {
			return objectif;
		}

		public void setObjectif(Object objectif) {
			this.objectif = objectif;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getCommanditaire() {
		return commanditaire;
	}

	public void setCommanditaire(String commanditaire) {
		this.commanditaire = commanditaire;
	}

	public ArrayList<Objectif> getObjectifs() {
		return objectifs;
	}

	public void setObjectifs(ArrayList<Objectif> objectifs) {
		this.objectifs = objectifs;
	}
}
