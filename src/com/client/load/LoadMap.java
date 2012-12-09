package com.client.load;

import java.util.ArrayList;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.loading.LoadingList;
import com.client.network.NetworkManager;
import com.map.Grille;
import com.map.Tile;
import com.map.client.managers.MapManager;

public class LoadMap implements Runnable
{
	private Grille grille;
	private Thread t;
	private int purcent;

	public LoadMap()
	{
		this.purcent = 0;
		t = new Thread(this);
	}
	
	public void start()
	{
		t.start();
	}

	public int getPurcent() {
		return purcent;
	}

	public void setPurcent(int purcent) {
		this.purcent = purcent;
	}
	
	public Thread getT() {
		return t;
	}

	public void setT(Thread t) {
		this.t = t;
	}

	@Override
	public void run()
	{
			purcent+=2;

			grille = new Grille();

			NetworkManager.instance.sendToServer("lo;map"); //load map
			NetworkManager.instance.waitForNewMessage("map");
			String[] args_map = NetworkManager.instance.receiveFromServer("map").split(";");
			if(args_map.length<3)
				throw new RuntimeException("Map loading error");

			int max_x=Integer.parseInt(args_map[0]);
			int max_y=Integer.parseInt(args_map[1]);

			purcent++;

			for(int i = 0; i < max_x+1 ; i++)
			{
				grille.add(new ArrayList<Tile>());
				for(int j = 0; j < max_y+1; j++)
					grille.get(i).add(new Tile(new Vector2f(i , j), null));
			}

			LoadingList.setDeferredLoading(true);
			for(int u = 0; u < args_map.length-4; u+=4)
			{
				if(u%(args_map.length/70)==0)
					purcent++;

				int id_x=Integer.parseInt(args_map[u+2]);
				int id_y=Integer.parseInt(args_map[u+3]);
				grille.get(id_x).get(id_y).addTypes(MapManager.getTypesTile(args_map[u+4]));
				grille.get(id_x).get(id_y).setMonsterHolder( Boolean.parseBoolean(args_map[u+5]));
			}

			NetworkManager.instance.sendToServer("lo;mapc"); //load map content
			NetworkManager.instance.waitForNewMessage("mapc");
			String[] args_mapc = NetworkManager.instance.receiveFromServer("mapc").split(";");

			if(args_map.length>2)
			{
				for(int u = 0; u < args_mapc.length; u+=3)
				{
					grille.get(Integer.parseInt(args_mapc[u]))
					.get(Integer.parseInt(args_mapc[u+1]))
					.addTypes(MapManager.getTypesTile(args_mapc[u+2]));
				}
			}
			LoadingList.setDeferredLoading(false);

			purcent+=7;

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	public Grille getGrille() {
		return grille;
	}

	public void setGrille(Grille grille) {
		this.grille = grille;
	}
}
