package com.server.core;

import java.util.ArrayList;

import com.game_entities.Joueur;



public class Account 
{
	private int clientId;
	private String name;
	private String passwd;
	private ArrayList<Joueur> joueurs;
	private Joueur current_joueur;
	
	public Account()
	{
		
	}
	public Account(String ndc,String mpd)
	{
		name = ndc;
		passwd = mpd;
	}
	public void setMdp(String mdp) 
	{
		passwd = mdp;
	}
	public void setClient_id(int parseInt) 
	{
		this.clientId = parseInt;
		
	}
	
	public int getClient_id() {
		return this.clientId;
	}
	
	public ArrayList<Joueur> getJoueurs() {
		return joueurs;
	}
	
	public void setJoueurs(ArrayList<Joueur> joueurs) {
		this.joueurs = joueurs;
	}
	
	public Joueur getCurrent_joueur() {
		return current_joueur;
	}
	
	public void setCurrent_joueur(Joueur currentJoueur) {
		current_joueur = currentJoueur;
	}
	
	public String getMdp() {
		return passwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
