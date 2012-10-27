package com.map;

import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import com.client.network.NetworkManager;

public class SimpleType_tile 
{
	protected String nom;
	protected boolean collidable;
	protected int calque;

	
	protected boolean multiTiles = false;
	
	
	public SimpleType_tile(String nom, boolean collidable, int calque)
	{
		this.nom = nom;
		this.collidable = collidable;
		this.calque = calque;
	}
	
	public SimpleType_tile(String nom, boolean collidable, int calque, boolean multiTiles)
	{
		this.nom = nom;
		this.collidable = collidable;
		this.calque = calque;
		
		this.multiTiles = true;
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

	public boolean isCollidable() {
		return collidable;
	}

	public void setCollidable(boolean collidable) {
		this.collidable = collidable;
	}


	public boolean isMultiTiles() {
		return multiTiles;
	}

	public void setMultiTiles(boolean multiTiles) {
		this.multiTiles = multiTiles;
	}
}

