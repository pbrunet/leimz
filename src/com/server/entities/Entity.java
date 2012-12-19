package com.server.entities;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import com.map.Tile;
import com.map.client.managers.CollisionManager;

public abstract class Entity 
{
	
	
	//Position de l'entite sur la carte
	//En repere (0;80;40)
	protected Tile tile;
	//En repere (0;1;1)
	protected Vector2f pos_real;
	
	//Position correspondant au barycentre de la tile sur laquelle l'entite est
	protected Vector2f pos_real_on_screen;

	//Taille de l'entite
	protected Vector2f size;
	
	//Formes avec la taille + la position
	protected Rectangle pieds, corps;
	
	//Orientation de l'entite
	protected Orientation orientation;
	
	//Etat
	public enum Etat
	{
		NORMAL, OVER, CLICKED
	};
	protected Etat etat;
	
	//Booleen indiquant si l'entite est en collision ou non
	protected boolean on_collision = false;
	
	//Gestionnaire de collisions
	protected CollisionManager collision_manager;
	
	//PAS DE GRAPHIQUE POUR LE SERVEUR
	
	//*******************************ANNEXES********************************
	
	//La taille relative de l'entite
	protected float scaleSize = 1;
	
	//La vitesse de l'entite
	protected float speed = 1.0f;
	
	public Entity(Orientation orientation, Tile tile)
	{
		this.orientation = orientation;
		this.tile = tile;
		
		this.pos_real = new Vector2f(0,0);
		if(tile != null)
		{
			this.pos_real.x = tile.getPos_real().x;
			this.pos_real.y = tile.getPos_real().y;
		}
		
		this.etat = Etat.NORMAL;
	}
	
	//Methode permettant de dessiner l'entite
	public void draw()
	{
		/**
		 * A voir en fonction de l'entite
		 */
	}
	
	//Methode permettant de rafraichir des elements de l'entite (formes par exemple)
	public void refresh()
	{
		/**
		 * A voir en fonction de l'entite
		 */
	}
	
	public String stringOrientation()
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
	
	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile = tile;
	}

	public Vector2f getPos_real_on_screen() {
		return pos_real_on_screen;
	}

	public void setPos_real_on_screen(Vector2f posRealOnScreen) {
		pos_real_on_screen = posRealOnScreen;
	}

	public Vector2f getSize() {
		return size;
	}

	public void setSize(Vector2f size) {
		this.size = size;
	}

	public Rectangle getPieds() {
		return pieds;
	}

	public void setPieds(Rectangle pieds) {
		this.pieds = pieds;
	}

	public Rectangle getCorps() {
		return corps;
	}

	public void setCorps(Rectangle corps) {
		this.corps = corps;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

	public float getScaleSize() {
		return scaleSize;
	}

	public void setScaleSize(float scaleSize) {
		this.scaleSize = scaleSize;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public boolean isOn_collision() {
		return on_collision;
	}

	public void setOn_collision(boolean onCollision) {
		on_collision = onCollision;
	}

	public CollisionManager getCollision_manager() {
		return collision_manager;
	}

	public void setCollision_manager(CollisionManager collisionManager) {
		collision_manager = collisionManager;
	}

	public Vector2f getPos_real() {
		return pos_real;
	}

	public void setPos_real(Vector2f posReal) {
		pos_real = posReal;
	}

	public Etat getEtat() {
		return etat;
	}
	
	public void setEtat(Etat etat) {
		this.etat = etat;
	}
}
