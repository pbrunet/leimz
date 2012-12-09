package com.map;
import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

public class Tile 
{
	private Vector2f pos; //Ces positions sont les coordonnees
	private Vector2f pos_screen; //Ces positions sont les positions par rapport e l'origine (0;0)
	private Vector2f pos_real;


	private ArrayList<SimpleType_tile> types;
	private int state; //On definit son etat (peu utilise)
	public static int NONE = 0, OVER = 1, CLICKED = 2;
	private boolean monsterHolder = false;

	private boolean collidable, drawn;

	private int groupId;


	/**
	 * Constructeur utilise pour l'initilisation des Tiles dans une grille/map.
	 * @param pos_x Position en X de la tyle dans le tableau (ex: 0)
	 * @param pos_y Position en Y de la tyle dans le tableau (ex: 3)
	 * @param type Type de la tyle
	 */
	public Tile(Vector2f pos, SimpleType_tile type)
	{
		this.types = new ArrayList<SimpleType_tile>();

		this.pos = pos;

		if(type != null)
			this.types.add(type);
	}

	public Tile(Vector2f pos, SimpleType_tile type, boolean monsterHolder)
	{

		this.types = new ArrayList<SimpleType_tile>();

		this.pos = pos;

		if(type != null)
			this.types.add(type);
		this.monsterHolder = monsterHolder;

	}

	/**
	 * Constructeur utilise pour l'initilisation des Tyles dans la liste des Type de tile dispo pour
	 * forger une map. Ici, pas besoin de preciser la position car le Tyle n'est pas dans la map.
	 * @param type Type de la tyle
	 */
	public Tile(SimpleType_tile type)
	{
		this.types = new ArrayList<SimpleType_tile>();

		this.pos = new Vector2f(-1, -1);

		if(type != null)
			this.types.add(type);
	}

	public void checkCollidable()
	{
		for(int i = 0; i < types.size(); i++)
		{
			if(types.get(i).isCollidable())
			{
				collidable = true;
			}
		}
	}

	public void refreshEvent()
	{
		//RIEN, (pour l'instant)
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public int getState()
	{
		return state;
	}

	public boolean isMonsterHolder() {
		return monsterHolder;
	}

	public void setMonsterHolder(boolean monsterHolder) {
		this.monsterHolder = monsterHolder;
	}



	public boolean isPointed(Vector2f pos)
	{

		//Cette methode renvoie si oui ou non, la tile est pointee par la souris.


		//ATTENTION, cette methode est assez compliquee mathematiquement
		//Pour la demontrer, dessiner sur un schema un losange dont les diagonales font 80 et 40 (UA)
		//Trouver alors la condition pour qu'un point (x;y) soit dans le losange.


		int m_x, m_y; //On definit de nouvelles coordonnees pour la souris
		m_x = (int) ((pos.x - pos_screen.x) - (80/2)); //La coordonnee x par rapport au centre de l'image
		m_y = (int) ((pos.y - pos_screen.y) - (40/2)); //La coordonnee y par rapport au centre de l'image

		//Si la souris est dans la tile en x, et que |x/2|+|y| < height/2 soit 20
		return (m_x > -40 && m_x < 40 && ((Math.abs(m_x/2)+Math.abs(m_y))<=20));
	}

	public boolean isPointed(Vector2f pos, Vector2f pos_absolute)
	{

		//Cette methode renvoie si oui ou non, la tile est pointee par la souris.


		//ATTENTION, cette methode est assez compliquee mathematiquement
		//Pour la demontrer, dessiner sur un schema un losange dont les diagonales font 80 et 40 (UA)
		//Trouver alors la condition pour qu'un point (x;y) soit dans le losange.


		int m_x, m_y; //On definit de nouvelles coordonnees pour la souris
		m_x = (int) ((pos.x - pos_absolute.x) - (80/2)); //La coordonnee x par rapport au centre de l'image
		m_y = (int) ((pos.y - pos_absolute.y) - (40/2)); //La coordonnee y par rapport au centre de l'image

		//Si la souris est dans la tile en x, et que |x/2|+|y| < height/2 soit 20
		return (m_x > -40 && m_x < 40 && ((Math.abs(m_x/2)+Math.abs(m_y))<=20));
	}

	public Vector2f getPos() {
		return pos;
	}

	public void setPos(Vector2f pos) {
		this.pos = pos;
	}

	public Vector2f getPos_screen() {
		return pos_screen;
	}

	public void setPos_screen(Vector2f posScreen) {
		pos_screen = posScreen;
	}

	public Vector2f getPos_real() {
		return pos_real;
	}

	public void setPos_real(Vector2f posReal) {
		pos_real = posReal;
	}

	public ArrayList<SimpleType_tile> getTypes() {
		return types;
	}
	
	public void addTypes(SimpleType_tile t)
	{
		types.add(t);
	}

	public void setTypes(ArrayList<SimpleType_tile> types) {
		this.types = types;
	}

	public boolean isCollidable() {
		return collidable;
	}

	public void setCollidable(boolean collidable) {
		this.collidable = collidable;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public boolean isDrawn() {
		return drawn;
	}

	public void setDrawn(boolean drawn) {
		this.drawn = drawn;
	}

	public Vector2f getPos_screen_barycentre() {
		return new Vector2f(pos_screen.x+40, pos_screen.y+20);
	}

	public Vector2f getPos_real_barycentre() {
		return new Vector2f(pos_real.x+40, pos_real.y+20);
	}
}
