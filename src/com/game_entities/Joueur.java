package com.game_entities;


import java.io.File;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import org.newdawn.slick.geom.Vector2f;

import com.gameplay.entities.Personnage;
import com.map.Tile;

public class Joueur extends Entity
{
	private Personnage perso;
	protected Vector2f absolute;
	
	private String str_file;
	private Element racine;
	private Document doc = null;
	
	public Joueur(Personnage perso, Tile tile, Orientation orientation)
	{
		super(orientation, tile);
		this.perso = perso;
		
		this.str_file = perso.getEntity_file();
		
		SAXBuilder sxb = new SAXBuilder();
	    try
	    {
	       doc = sxb.build(new File(str_file));
	    }
	    catch(Exception e){}
	    racine = doc.getRootElement();
	    
		this.absolute = new Vector2f(0,0);
	}

	public void initImgs()
	{
		imgs_repos = new Image[racine.getChild("imgs_repos").getChildren().size()];
		
		try {
			for(int i = 0; i < racine.getChild("imgs_repos").getChildren().size(); i++)
			{
				imgs_repos[i] = new Image(((Element) racine.getChild("imgs_repos").getChildren().get(i)).getText());
			}
		} catch (SlickException e) {
			e.printStackTrace();
		}
		 
		current_img_repos = returnImgOrientation(orientation);

		this.size = new Vector2f(imgs_repos[0].getWidth(), imgs_repos[0].getHeight());
		
		pos_real_on_screen = new Vector2f();
		pos_real_on_screen.x = (pos_real.x+absolute.x)-(current_img_repos.getWidth()/2);
		pos_real_on_screen.y = (pos_real.y+absolute.y)-65;
		
		int[] numbers = new int[4];
		String[] str = (racine.getChild("shapes").getChild("pieds").getText().split(","));
		for(int i = 0; i < 4; i++)
		{
			numbers[i] = Integer.parseInt(str[i]);
		}
		this.corps = new Rectangle(0, 0, size.x, size.y);
		this.pieds = new Rectangle(numbers[0], numbers[1], numbers[2], numbers[3]);
	}
	
	public void refresh()
	{
		current_img_repos = returnImgOrientation(getOrientation());
		
		pos_real_on_screen.x = (pos_real.x+absolute.x)-(current_img_repos.getWidth()/2);
		pos_real_on_screen.y = (pos_real.y+absolute.y)-65;
		
		//QUETES
		perso.getQuetes_manager().testQuetes(this);
	}
	
	public void draw()
	{
		current_img_repos.draw(pos_real_on_screen.x, pos_real_on_screen.y);
	}
	
	public void draw(float scale)
	{
		current_img_repos.draw((pos_real.x+absolute.x)-((current_img_repos.getWidth()*scale)/2), (pos_real.y+absolute.y)-(65*scale), scale);
	}
	
	protected String stringOrientation()
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
	
	public Personnage getPerso() {
		return perso;
	}

	public void setPerso(Personnage perso) {
		this.perso = perso;
	}
	
	public Vector2f getAbsolute() {
		return absolute;
	}

	public void setAbsolute(Vector2f absolute) {
		this.absolute = absolute;
	}
}
