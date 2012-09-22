package com.client.load;

import java.io.*;

import java.util.ArrayList;

import org.jdom.*;
import org.jdom.input.SAXBuilder;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.loading.LoadingList;

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

			Element[] calques_e = new Element[racine_maps.getChildren("calque").size()];

			for(int i = 0; i < racine_maps.getChildren("calque").size(); i++)
				calques_e[i] = (Element) racine_maps.getChildren("calque").get(i);

			purcent++;
			
			for(int i = 0; i < (Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(calques_e[0].getChildren("tile").size()-1)).getChild("id_x").getText())+1); i++)
			{
				grille.add(new ArrayList<Tile>());
				for(int j = 0; j < (Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(calques_e[0].getChildren("tile").size()-1)).getChild("id_y").getText())+1); j++)
					grille.get(i).add(new Tile(new Vector2f(i , j), null));
			}

			
			LoadingList.setDeferredLoading(true);
			int z = 1;
			for(int u = 0; u < calques_e[0].getChildren("tile").size(); u++)
			{
				if(u == (calques_e[0].getChildren("tile").size()/70)*z)
				{
					purcent++;
					z++;
				}

				if( grille.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_x").getText()))
					.get( Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_y").getText())).getPos().x
					== Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_x").getText())
					&&
					grille.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_x").getText()))
					.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_y").getText())).getPos().y
					== Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_y").getText())
				  )
				{
					for(int k = 0; k < racine_types.getChild("calque1").getChildren("type").size(); k ++)
					{
						if(((Element) calques_e[0].getChildren("tile").get(u)).getChild("type").getText()
								.equals(((Element)racine_types.getChild("calque1").getChildren("type").get(k)).getChild("nom").getText()))
						{
							try {
								grille.get(	Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_x").getText()))
								.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_y").getText()))
								.getTypes().add(Type_tile.getTypesTile(((Element)racine_types.getChild("calque1").getChildren("type").get(k)).getChild("nom").getText()));
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
						}
					}

					grille.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_x").getText()))
						.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_y").getText()))
						.setMonsterHolder( Boolean.parseBoolean(((Element) calques_e[0].getChildren("tile").get(u)).getChild("monsterHolder").getText()));
				}
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
