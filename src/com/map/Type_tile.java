package com.map;

import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import com.client.network.NetworkManager;



public class Type_tile 
{
	private String nom;
	private Image img;
	private boolean collidable;
	private int calque;
	private Rectangle base;
	
	private boolean multiTiles = false;
	
	private static HashMap<String, Type_tile> types = new HashMap<String, Type_tile>();
	
	public static Type_tile getTypesTile(String name)
	{
		if(types.get(name) != null)
			return types.get(name);

		//Caracteristiques de race
		NetworkManager.instance.sendToServer("lo;tt;" + name); //load joueur, type tile, name
		NetworkManager.instance.waitForNewMessage("tt");
		String[] info_tile= NetworkManager.instance.receiveFromServer("tt").split(";"); //nom, img adresse, collidable, x, y, calque
		
		try {
			return new Type_tile(info_tile[0], new Image(info_tile[1]),new Rectangle(Integer.parseInt(info_tile[3]),Integer.parseInt(info_tile[4]),80,40),Boolean.parseBoolean(info_tile[2]),Integer.parseInt(info_tile[5]));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Type_tile(String nom, Image img, Rectangle base, boolean collidable, int calque)
	{
		this.nom = nom;
		this.img = img;
		this.collidable = collidable;
		this.calque = calque;
		this.base = base;
		types.put(nom, this);
	}
	
	public Type_tile(String nom, Image img, Rectangle base, boolean collidable, int calque, boolean multiTiles)
	{
		this.nom = nom;
		this.img = img;
		this.collidable = collidable;
		this.calque = calque;
		this.base = base;
		
		this.multiTiles = true;
	}
	
	public Type_tile(String nom, Rectangle base, boolean collidable, int calque)
	{
		this.nom = nom;
		this.collidable = collidable;
		this.calque = calque;
		this.base = base;
	}
	
	public Type_tile(String nom, Rectangle base, boolean collidable, int calque, boolean multiTiles)
	{
		this.nom = nom;
		this.collidable = collidable;
		this.calque = calque;
		this.base = base;
		
		this.multiTiles = multiTiles;
	}

	public int getCalque() {
		return calque;
	}

	public void setCalque(int calque) {
		this.calque = calque;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	public boolean isCollidable() {
		return collidable;
	}

	public void setCollidable(boolean collidable) {
		this.collidable = collidable;
	}

	public Rectangle getBase() {
		return base;
	}

	public void setBase(Rectangle base) {
		this.base = base;
	}

	public boolean isMultiTiles() {
		return multiTiles;
	}

	public void setMultiTiles(boolean multiTiles) {
		this.multiTiles = multiTiles;
	}
}
