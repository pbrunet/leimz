package com.map;

import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import com.client.network.NetworkManager;



public class Type_tile extends SimpleType_tile
{
	private Image img;
	private Rectangle base;
	
	
	public Type_tile(String nom, Image img, Rectangle base, boolean collidable, int calque)
	{
		super(nom, collidable, calque);
		
		this.img = img;
		this.base = base;
	}
	
	public Type_tile(String nom, Image img, Rectangle base, boolean collidable, int calque, boolean multiTiles)
	{
		super(nom, collidable, calque);
		this.img = img;
		this.base = base;
		
		this.multiTiles = true;
	}

	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	public Rectangle getBase() {
		return base;
	}

	public void setBase(Rectangle base) {
		this.base = base;
	}
}
