package com.map.server.managers;

import java.util.ArrayList;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import com.client.entities.Entity;
import com.map.Grille;
import com.map.Tile;

public class CollisionManager 
{

	private ArrayList<Entity> entities;
	private ArrayList<Tile> tiles_collidable;

	private Grille grille;

	public CollisionManager(Grille grille, ArrayList<Entity> entities)
	{
		this.setGrille(grille);

		tiles_collidable = new ArrayList<Tile>();
		for(int i = 0; i < grille.size(); i++)
		{
			for(int j = 0; j < grille.get(i).size(); j++)
			{
				if(grille.get(i).get(j).isCollidable())
				{
					tiles_collidable.add(grille.get(i).get(j));
				}
			}
		}
		/*for(int i = 0; i < entities.size();i++)
		{
			entities.get(i).setCollision_manager(this);
		}*/

		this.entities = entities;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public void setEntities(ArrayList<Entity> entities) 
	{
		this.entities = entities;

		/*for(int i = 0; i < entities.size();i++)
		{
			entities.get(i).setCollision_manager(this);
		}*/
	}

	public ArrayList<Tile> getTiles_collidable() {
		return tiles_collidable;
	}

	public void setTiles_collidable(ArrayList<Tile> tilesCollidable) {
		tiles_collidable = tilesCollidable;
	}

	public void refreshGrille(Grille grille)
	{
		tiles_collidable = new ArrayList<Tile>();
		for(int i = 0; i < grille.size(); i++)
		{
			for(int j = 0; j < grille.get(i).size(); j++)
			{
				if(grille.get(i).get(j).isCollidable())
				{
					tiles_collidable.add(grille.get(i).get(j));
				}
			}
		}
	}

	public void addEntity(Entity entity)
	{
		this.entities.add(entity);
	}

	public void checkCollision(Entity entity)
	{

	}

	public boolean checkMoving(Entity entity)
	{

		ArrayList<Boolean> collision_possible = new ArrayList<Boolean>();

		//Cration d'une liste de boolen, qui va permettre de savoir si oui ou non il peut avancer
		ArrayList<Boolean> ok = new ArrayList<Boolean>();


		//On rcupre les pieds de l'entit
		Shape shape = new Rectangle(entity.getPos_real_on_screen().x+entity.getPieds().getX(), entity.getPos_real_on_screen().y+entity.getPieds().getY(), entity.getPieds().getWidth(), entity.getPieds().getHeight());

		//Forme qui va servir pour la collision, en fonction de l'orientation
		Shape s = null;

		switch(entity.getOrientation())
		{
		case HAUT:
			//Le move.y est forcment ngatif
			s = new Rectangle(shape.getX(), shape.getY()-1, shape.getWidth(), shape.getHeight());
			break;
		case BAS:
			//Le move.y est forcment positif
			s = new Rectangle(shape.getX(), shape.getY()+1, shape.getWidth(), shape.getHeight());
			break;
		case GAUCHE:
			//Le move.x est forcment ngatif
			s = new Rectangle(shape.getX()-1, shape.getY(), shape.getWidth(), shape.getHeight());
			break;
		case DROITE:
			//Le move.x est forcment positif
			s = new Rectangle(shape.getX()+1, shape.getY(), shape.getWidth(), shape.getHeight());
			break;
		case HAUT_DROITE:
			s = new Rectangle(shape.getX()+1, shape.getY()-0.5f, shape.getWidth(), shape.getHeight());
			break;
		case HAUT_GAUCHE:
			s = new Rectangle(shape.getX()-1, shape.getY()-0.5f, shape.getWidth(), shape.getHeight());
			break;
		case BAS_DROITE:
			s = new Rectangle(shape.getX()+1, shape.getY()+0.5f, shape.getWidth(), shape.getHeight());
			break;
		case BAS_GAUCHE:
			s = new Rectangle(shape.getX()-1, shape.getY()+0.5f, shape.getWidth(), shape.getHeight());
			break;

		}

		/*
		//Tiles situes autour du joueur (pour gagner en performance)
		ArrayList<Tile> tiles_base = new ArrayList<Tile>();

		//Si la pos en x du joueur est paire
		if(entity.getTile().getPos_x() % 2 == 0)
		{
			//On rcupre les tiles autour de lui (faire un schma pour comprendre)
			tiles_base.add(grille.get((int) (entity.getTile().getPos_x()-1)).get((int) entity.getTile().getPos_y()-1));
			tiles_base.add(grille.get((int) (entity.getTile().getPos_x()-1)).get((int) entity.getTile().getPos_y()));
			tiles_base.add(grille.get((int) (entity.getTile().getPos_x())).get((int) entity.getTile().getPos_y()-1));
			tiles_base.add(grille.get((int) (entity.getTile().getPos_x())).get((int) entity.getTile().getPos_y()+1));
			tiles_base.add(grille.get((int) (entity.getTile().getPos_x()+2)).get((int) entity.getTile().getPos_y()));
			tiles_base.add(grille.get((int) (entity.getTile().getPos_x()-2)).get((int) entity.getTile().getPos_y()));
			tiles_base.add(grille.get((int) (entity.getTile().getPos_x()+1)).get((int) entity.getTile().getPos_y()-1));
			tiles_base.add(grille.get((int) (entity.getTile().getPos_x()+1)).get((int) entity.getTile().getPos_y()));
		}

		//Sinon
		else
		{
			//On rcupre aussi les tiles autour de lui (ce ne sont pas les mmes)
			tiles_base.add(grille.get((int) (entity.getTile().getPos_x()-1)).get((int) entity.getTile().getPos_y()));
			tiles_base.add(grille.get((int) (entity.getTile().getPos_x()-1)).get((int) entity.getTile().getPos_y()+1));
			tiles_base.add(grille.get((int) (entity.getTile().getPos_x())).get((int) entity.getTile().getPos_y()-1));
			tiles_base.add(grille.get((int) (entity.getTile().getPos_x())).get((int) entity.getTile().getPos_y()+1));
			tiles_base.add(grille.get((int) (entity.getTile().getPos_x()+2)).get((int) entity.getTile().getPos_y()));
			tiles_base.add(grille.get((int) (entity.getTile().getPos_x()-2)).get((int) entity.getTile().getPos_y()));
			tiles_base.add(grille.get((int) (entity.getTile().getPos_x()+1)).get((int) entity.getTile().getPos_y()));
			tiles_base.add(grille.get((int) (entity.getTile().getPos_x()+1)).get((int) entity.getTile().getPos_y()+1));
		}*/


		//System.out.println("taille de la liste avant purage : " + tiles_base.size());

		//Tiles que l'on va garder
		ArrayList<Tile> tiles = new ArrayList<Tile>();

		/*int size = tiles_base.size();
		for(int i = 0; i < size; i++)
		{
			//Si la tile de base a un type et est collidable, alors on garde
			if(tiles_base.get(i).getTypes() != null)
			{
				if(tiles_base.get(i).isCollidable())
					tiles.add(tiles_base.get(i));
			}
		}*/

		//System.out.println("taille de la liste aprs purage : " + tiles.size());
		//Si on a gard au moins une tile
		if(tiles.size() != 0)
		{
			for(int i = 0; i < tiles.size(); i++)
			{		
				//On cre le losange associ
				Polygon ob = new Polygon();
				/*ob.addPoint(tiles.get(i).getPos_x_real(), tiles.get(i).getPos_y_real()+20);
				ob.addPoint(tiles.get(i).getPos_x_real()+40, tiles.get(i).getPos_y_real());
				ob.addPoint(tiles.get(i).getPos_x_real()+80, tiles.get(i).getPos_y_real()+20);
				ob.addPoint(tiles.get(i).getPos_x_real()+40, tiles.get(i).getPos_y_real()+40);*/

				//Si il y a intersection
				ok.add(!s.intersects(ob));
			}
		}	

		//Si il y a au moins une collisions, alors renvoyer false
		collision_possible.add(ok.contains(false));		

		if(!collision_possible.contains(true))
		{
			for(int i = 0; i < entities.size(); i++)
			{
				if(entity != entities.get(i))
				{
					collision_possible.add(s.intersects(new Rectangle(entities.get(i).getPos_real_on_screen().x+entities.get(i).getPieds().getX(), entities.get(i).getPos_real_on_screen().y+entities.get(i).getPieds().getY(), entities.get(i).getPieds().getWidth(), entities.get(i).getPieds().getHeight())));
				}
			}
		}

		return !(collision_possible.contains(true));
	}

	public Grille getGrille() {
		return grille;
	}

	public void setGrille(Grille grille) {
		this.grille = grille;
	}
}
