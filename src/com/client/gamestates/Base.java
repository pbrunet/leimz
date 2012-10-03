package com.client.gamestates;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.client.load.Loading;

//La base du jeu, elle gere les differents etats de jeux, et possede le main
public class Base extends StateBasedGame 
{

	//Les entiers correspondants aux etats de jeu
	public static int IDENTIFICATION = 1, LOADING = 2, PRINCIPAL = 3;
	
	//Taille de la fenetre de jeu (largeur (x) et hauteur (y) )
	public static int sizeOfScreen_x = 900;
	public static int sizeOfScreen_y = 650;
	
	public Base() 
	{
		//Appel du constructeur de la classe mere, avec le titre de la fenetre en parametre
		super("Leimz");
	}
 
	//Methode d'initialisation des etats de jeu
	public void initStatesList(GameContainer gc) throws SlickException 
	{
		//Ajouts des etats de jeu, chaque etat correspond a une classe fille de BasicGameState
		this.addState(new Identification());
		this.addState(new Loading());
		this.addState(new Principal());
	}
 
	public static void main(String[] args) throws SlickException
    {
		Base b = new Base();
		
		//Creation de la fenetre/appli, qui contient la base, maitre des etats de jeu
        AppGameContainer app = new AppGameContainer(b);
        
        //app.setSmoothDeltas(true);
        app.setShowFPS(false);
        
        //Ajout de la taille de la fenetre
        app.setDisplayMode(sizeOfScreen_x, sizeOfScreen_y, false);
        
        //On demarre l'appli et on entre dans le premier etat
        app.start();
	}
}