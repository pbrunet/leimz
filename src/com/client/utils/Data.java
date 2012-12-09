package com.client.utils;

import java.util.ArrayList;


import org.newdawn.slick.Font;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class Data
{
	public static ArrayList<UnicodeFont> font;
	private static Data instance;
	
	/*private ArrayList<Race> races;
	private ArrayList<Classe>*/
	
	@SuppressWarnings("unchecked")
	public static void loadData()
	{
		font = new ArrayList<UnicodeFont>();
		
		for(int i = 10; i < 35; i++)
		{
			java.awt.Font f = new java.awt.Font("Trebuchet MS", 25, java.awt.Font.BOLD);
			UnicodeFont label = null;
			label = new UnicodeFont(f, i, true, false);
			label.addAsciiGlyphs();
			label.addGlyphs(400,600);
			label.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
			try {
				label.loadGlyphs();
			} catch (SlickException e) {
				e.printStackTrace();
			}
			font.add(label);
		}
	}
	
	public static Font getFont(String nom, int size)
	{
		return font.get(size-10);
	}

	private static void createAnInstance() {
		 if(null == instance)
         { 
                   instance = new Data(); 
         } 
	}

	public static Data getInstance() {
		 if(null == instance) 
         {
                   createAnInstance();
         }
         return instance; 
	}
}
