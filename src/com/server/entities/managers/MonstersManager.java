package com.server.entities.managers;

import java.util.ArrayList;

import com.client.entities.Monstre;


public class MonstersManager
{
	private ArrayList<Monstre> monsters;
	private long tpsReap;
	private int nbMax;
	
	
	public MonstersManager(ArrayList<Monstre> monsters, long tpsReap, int nbMax)
	{
		this.setTpsReap(tpsReap);
		this.setNbMax(nbMax); 
		
		monsters = new ArrayList<Monstre>();
	}
	
	/*public void refresh() 
	{
		
		if(System.currentTimeMillis() - lastReap < tpsReap){}
		else
		{
			lastReap = System.currentTimeMillis();
			
			
			if(monsters.size() < nbMax)
			{
				Random r = new Random();
				int i = r.nextInt(data_monsters.size());
				int u = r.nextInt(pos_reap.size());
			
				Vector2f pos = new Vector2f();
				pos.x = tiles_reap.get(u).getPos_x();
				pos.y = tiles_reap.get(u).getPos_y();
				
				Monstre m = new Monstre(data_monsters.get(i), pos_reap.get(u), pos);
				monsters.add(m);
				System.out.println("new monster !" + "  --> size:" + monsters.size());
			}
			
		}
		
		for(int i = 0; i < monsters.size(); i++)
		{
			monsters.get(i).refresh();
			if(monsters.get(i).getPosOnMap().x >= current_map.get(0).get(0).getPos_x()
					   && monsters.get(i).getPosOnMap().x <= current_map.get(current_map.size()-1).get(0).getPos_x()
					   && monsters.get(i).getPosOnMap().y >= current_map.get(0).get(0).getPos_y()
					   && monsters.get(i).getPosOnMap().y <= current_map.get(current_map.size()-1).get(current_map.get(current_map.size()-1).size()-1).getPos_y())
			{
				Vector2f posMap = new Vector2f();
				posMap.x = current_map.get((int) monsters.get(i).getPosOnMap().x-current_map.get(0).get(0).getPos_x()).get((int) monsters.get(i).getPosOnMap().y-current_map.get(0).get(0).getPos_y()).getPos_x();
				posMap.y = current_map.get((int) monsters.get(i).getPosOnMap().x-current_map.get(0).get(0).getPos_x()).get((int) monsters.get(i).getPosOnMap().y-current_map.get(0).get(0).getPos_y()).getPos_y();
				
				Vector2f posMapOld = new Vector2f();
				Vector2f posRealOld = new Vector2f();
				
				Vector2f pos = new Vector2f();
				pos.x = current_map.get((int) monsters.get(i).getPosOnMap().x-current_map.get(0).get(0).getPos_x()).get((int) monsters.get(i).getPosOnMap().y-current_map.get(0).get(0).getPos_y()).getPos_x_real()-((monsters.get(i).getSize().x-80)/2);
				pos.y = current_map.get((int) monsters.get(i).getPosOnMap().x-current_map.get(0).get(0).getPos_x()).get((int) monsters.get(i).getPosOnMap().y-current_map.get(0).get(0).getPos_y()).getPos_y_real()+35-(monsters.get(i).getSize().y);
				
				if(!monsters.get(i).isMoved())
				{
					posRealOld.x = current_map.get((int) monsters.get(i).getPosOnMap().x-current_map.get(0).get(0).getPos_x()).get((int) monsters.get(i).getPosOnMap().y-current_map.get(0).get(0).getPos_y()).getPos_x_real();
					posRealOld.y = current_map.get((int) monsters.get(i).getPosOnMap().x-current_map.get(0).get(0).getPos_x()).get((int) monsters.get(i).getPosOnMap().y-current_map.get(0).get(0).getPos_y()).getPos_y_real();
					posMapOld.x = current_map.get((int) monsters.get(i).getPosOnMap().x-current_map.get(0).get(0).getPos_x()).get((int) monsters.get(i).getPosOnMap().y-current_map.get(0).get(0).getPos_y()).getPos_x();
					posMapOld.y = current_map.get((int) monsters.get(i).getPosOnMap().x-current_map.get(0).get(0).getPos_x()).get((int) monsters.get(i).getPosOnMap().y-current_map.get(0).get(0).getPos_y()).getPos_y();
					
					monsters.get(i).setPosOnMap(posMap);
					monsters.get(i).setPos_real(pos);
				}
				else
				{
					Tile t = g.getTile((int)(monsters.get(i).getPieds_screen().getX()+(monsters.get(i).getPieds_screen().getWidth()/2)), (int)(monsters.get(i).getPieds_screen().getY()+(monsters.get(i).getPieds_screen().getHeight()/2)));
					monsters.get(i).setPosOnMap(new Vector2f(t.getPos_x(), t.getPos_y()));
					
					/*monsters.get(i).setTrans_for_move(new Vector2f(
					posRealOld.x-current_map.get((int)posMapOld.x-current_map.get(0).get(0).getPos_x()).get((int)posMapOld.y-current_map.get(0).get(0).getPos_y()).getPos_x_real(),
					posRealOld.y-current_map.get((int)posMapOld.x-current_map.get(0).get(0).getPos_x()).get((int)posMapOld.y-current_map.get(0).get(0).getPos_y()).getPos_y_real()
					));*//*
				}
			}
			
			
		}
		
		
		
		if(System.currentTimeMillis() - lastMove < tpsMove){}
		else
		{
			lastMove = System.currentTimeMillis();
			
			System.out.println("move !");

			for(int i = 0; i < monsters.size(); i++)
			{
				monsters.get(i).manageMove();
				monsters.get(i).setMoved(true);
			}
		}*/
		
		
		
	
	
	public ArrayList<Monstre> getMonsters() {
		return monsters;
	}

	public void setMonsters(ArrayList<Monstre> monsters) {
		this.monsters = monsters;
	}
	/*

	public void drawMonsters()
	{
		for(int i = 0; i < monsters.size(); i++)
		{
			//System.out.println("Pos monstre " + i + " :  X --> " + monsters.get(i).getPosOnMap().x + " ;; Y --> " + monsters.get(i).getPosOnMap().y);
			//System.out.println("Pos map : " + " origine (" +  current_map.get(0).get(0).get(0).getPos_x() + " ; " + current_map.get(0).get(0).get(0).getPos_y() + ")   et   fin (" + current_map.get(0).get(current_map.get(0).size()-1).get(0).getPos_x() + " ; " + current_map.get(0).get(current_map.get(0).size()-1).get(current_map.get(0).get(current_map.get(0).size()-1).size()-1).getPos_y() + ")");
			if(monsters.get(i).getPosOnMap().x >= current_map.get(0).get(0).getPos_x()
					   && monsters.get(i).getPosOnMap().x <= current_map.get(current_map.size()-1).get(0).getPos_x()
					   && monsters.get(i).getPosOnMap().y >= current_map.get(0).get(0).getPos_y()
					   && monsters.get(i).getPosOnMap().y <= current_map.get(current_map.size()-1).get(current_map.get(current_map.size()-1).size()-1).getPos_y())
			{
				monsters.get(i).draw();
			}
			
		}
	}*/

	public int getNbMax() {
		return nbMax;
	}

	public void setNbMax(int nbMax) {
		this.nbMax = nbMax;
	}

	public long getTpsReap() {
		return tpsReap;
	}

	public void setTpsReap(long tpsReap) {
		this.tpsReap = tpsReap;
	}
}
