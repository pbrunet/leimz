package com.client.load;

import java.io.*;

import java.util.ArrayList;

import org.jdom.*;
import org.jdom.input.SAXBuilder;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.loading.LoadingList;

import com.client.network.NetworkManager;
import com.map.Grille;
import com.map.Map;
import com.map.Tile;
import com.map.Type_tile;

public class LoadMap implements Runnable
{
	private org.jdom.Document doc_map, doc_types;
	private Element racine_maps, racine_types;
	private Grille grille;
	private Thread looper;
	private int purcent;
	private boolean running;

	private Map map;


	public LoadMap(int purcent)
	{
		this.purcent = purcent;
		looper = new Thread(this);
		looper.start();
		running = true;
	}


	public Thread getLooper() {
		return looper;
	}

	public void setLooper(Thread looper) {
		this.looper = looper;
	}

	public int getPurcent() {
		return purcent;
	}

	public void setPurcent(int purcent) {
		this.purcent = purcent;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void run()
	{
		if(running)
		{
			System.out.println("Loading map...");
			//On cree une instance de SAXBuilder
			SAXBuilder sxb = new SAXBuilder();
			try
			{
				//On cree un nouveau document JDOM avec en argument le fichier XML
				//Le parsing est termine ;)
				doc_map = sxb.build(new File("data/Maps/map_constance.xml"));
				doc_types = sxb.build(new File("data/Maps/types_tiles.xml"));
			}
			catch(Exception e){
				System.out.println("Erreur de parsing XML de la carte");
			}

			//On initialise un nouvel element racine avec l'element racine du document.
			racine_maps = doc_map.getRootElement();
			racine_types = doc_types.getRootElement();

			purcent+=2;

			grille = new Grille();

			NetworkManager.instance.sendToServer("lo;map"); //load map
			NetworkManager.instance.waitForNewMessage();
			String[] args_map = NetworkManager.instance.getMessage_recu_serveur().split(";");

			int max_x=Integer.parseInt(args_map[0]);
			int max_y=Integer.parseInt(args_map[1]);

			Element[] calques_e = new Element[2];

			calques_e[1] = (Element) racine_maps.getChildren("calque").get(1);

			purcent++;

			for(int i = 0; i < max_x+1 ; i++)
			{
				grille.add(new ArrayList<Tile>());
				for(int j = 0; j < max_y+1; j++)
					grille.get(i).add(new Tile(new Vector2f(i , j), null));
			}

			LoadingList.setDeferredLoading(true);
			for(int u = 0; u < args_map.length/4; u++)
			{
				if(u%(args_map.length/278)==0)
					purcent++;
				
				int id_x=Integer.parseInt(args_map[4*u+2]);
				int id_y=Integer.parseInt(args_map[4*u+3]);

				grille.get(id_x).get(id_y).getTypes().add(Type_tile.getTypesTile(args_map[4*u+4]));
				grille.get(id_x).get(id_y).setMonsterHolder( Boolean.parseBoolean(args_map[4*u+5]));
			}



			for(int u = 0; u < calques_e[1].getChildren().size(); u++)
			{
				if(((Element) calques_e[1].getChildren().get(u)).getText().equals("tile"))
				{
					for(int k = 0; k < racine_types.getChild("calque2").getChildren("type").size(); k ++)
					{
						if(((Element) calques_e[1].getChildren("tile").get(u)).getChild("type").getText()
								.equals(((Element)racine_types.getChild("calque2").getChildren("type").get(k)).getChild("nom").getText()))
						{
							try {
								grille.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_x").getText()))
								.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_y").getText()))
								.getTypes().add(Type_tile.getTypesTile(((Element)racine_types.getChild("calque2").getChildren("type").get(k)).getChild("nom").getText()));
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
						}
					}
				}
				else if(((Element) calques_e[1].getChildren().get(u)).getText().equals("group_tiles"))
				{
					for(int k = 0; k < racine_types.getChild("calque2").getChildren("type").size(); k ++)
					{
						if(((Element) calques_e[1].getChildren().get(u)).getChild("type").getText()
								.equals(((Element)racine_types.getChild("calque2").getChildren("type").get(k)).getChild("nom").getText()))
						{
							try {

								if(((Element)racine_types.getChild("calque2").getChildren("type").get(k)).getAttributes().size() > 0)
								{
									grille.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_x").getText()))
									.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_y").getText()))
									.getTypes().add(Type_tile.getTypesTile(
											((Element)racine_types.getChild("calque2").getChildren("type").get(k)).getChild("nom").getText()));
								}
								else
								{
									grille.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_x").getText()))
									.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_y").getText()))
									.getTypes().add(Type_tile.getTypesTile(((Element)racine_types.getChild("calque2").getChildren("type").get(k)).getChild("nom").getText()));
								}
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			System.out.println("FIn du loading");
			LoadingList.setDeferredLoading(false);

			purcent+=7;
			System.out.println("purcent ok");

			ArrayList<String> monstres = new ArrayList<String>();
			monstres.add("bouftou");
			monstres.add("tofu");

			map = new Map(grille, monstres);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			purcent=81;
			running = false;
		}
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
}
