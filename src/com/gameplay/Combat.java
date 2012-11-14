package com.gameplay;

import java.util.ArrayList;
import java.util.Timer;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.client.entities.MainJoueur;
import com.client.gamestates.Base;
import com.client.utils.Chrono;
import com.client.utils.Data;
import com.map.Tile;
import com.map.Type_tile;

public class Combat
{
	private ArrayList<Equipe> equipes;
	private ArrayList<Tile> zone;
	public enum EtatCombat
	{
		INIT, EN_COURS, FINI
	};
	private EtatCombat etat;
	private Chrono timer_start;
	private static long time_start = 3000;
	
	public Combat(ArrayList<Equipe> equipes, ArrayList<Tile> zone)
	{
		this.equipes = equipes;
		this.zone = zone;
		
		etat = EtatCombat.INIT;
		
		timer_start = new Chrono(time_start);
	}
	
	public void stop()
	{
		this.etat = EtatCombat.FINI;
	}
	
	public void draw(Graphics g, Vector2f mousePos, float scale)
	{
		if(this.getEtat().equals(EtatCombat.INIT))
		{
			this.getTimer_start().start();
			this.setEtat(EtatCombat.EN_COURS);
		}

		if(this.getEtat().equals(EtatCombat.EN_COURS))
		{
			for(int i = 0; i < this.getZone().size(); i++)
			{
				//On cree la position d'affichage
				Vector2f pos_aff = new Vector2f();
				//La position vaut la position de la tile moins la position de la base, creee auparavant par le level designer pour chaque objet
				//De plus, on ajoute une petite formule demontrable simplement en repere orthonorme pour le zoom
				pos_aff.x = (this.getZone().get(i).getPos_screen().x-((Type_tile) this.getZone().get(i).getTypes().get(0)).getBase().getX())+((1-scale)*Base.Tile_x);
				pos_aff.y = (this.getZone().get(i).getPos_screen().y-((Type_tile) this.getZone().get(i).getTypes().get(0)).getBase().getY())+((1-scale)*Base.Tile_y/2);

				((Type_tile) this.getZone().get(i).getTypes().get(0)).getImg().draw(pos_aff.x, pos_aff.y, scale);
			}
			
			if(this.getTimer_start().getTime() > 0)
			{
				Font font2 = Data.getFont("Trebuchet MS", 30);
				g.setFont(font2);
				String txt = "";
				txt += "Le combat commence dans ";
				txt += ((int)(this.getTimer_start().getTime()/1000))+1;
				g.drawString(txt, Base.sizeOfScreen_x/2-(font2.getWidth(txt)/2), Base.sizeOfScreen_y/2-(font2.getHeight(txt)/2));
			}
			
			if(MainJoueur.instance.getPerso().getCurrent_sort() != null)
			{
				g.setColor(new Color(60,0,255));
				g.drawLine(MainJoueur.instance.getPos_real_on_screen().x+(MainJoueur.instance.getSize().x/2), MainJoueur.instance.getPos_real_on_screen().y+(MainJoueur.instance.getSize().y/2), mousePos.x, mousePos.y);
				g.setColor(Color.white);
			}
		}
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

	public Chrono getTimer_start() {
		return timer_start;
	}

	public void setTimer_start(Chrono timer_start) {
		this.timer_start = timer_start;
	}

	
}
