package com.server.core.functions;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.server.entities.PNJ;
import com.gameplay.Caracteristique;
import com.gameplay.Inventaire;
import com.gameplay.PNJ_discours;
import com.gameplay.Sort;
import com.gameplay.items.Item;
import com.gameplay.items.SimpleItem;
import com.map.Grille;
import com.map.Tile;
import com.map.server.managers.MapManager;
import com.server.core.Client;
import com.server.core.GlobalConstant;
import com.server.core.ServerSingleton;
import com.server.entities.managers.EntitiesManager;

/**
 * classe envoyant les informations de chargement d'informations
 */
public class LoadFunction implements Functionable
{
	public LoadFunction()
	{      
	}

	//fonction repondant aux requetes du client
	@Override
	public void doSomething(String[] args, Client client)
	{
		switch(args[1])
		{
		case "pos":
			askPos(client);
			break;
		case "j":
			askJoueur(client, args);
			break;
		case "ent":
			askEntities(client);
			break;
		case "tt":
			askTypeTiles(client,args[2]);
			break;
		case "map":
			askMap(client);
			break;
		case "mapc":
			askMapContent(client);
			break;
		case "mon":
			askMonster(client);
			break;
		default:
			throw new RuntimeException("Unimplemented");
		}
	}

	private void askJoueur(Client client, String[] args)
	{
		switch(args[2])
		{
		case "rc":
			askRaceCaracteristic(client);
			break;
		case "cc":
			askClassCaracteristic(client);
			break;
		case "jc":
			askPlayerCaracteristic(client);
			break;
		case "jcv":
			askPlayerCaracteristicValue(client);
			break;
		case "rs":
			askRaceSort(client);
			break;
		case "cs":
			askClassSort(client);
			break;
		case "in":
			askInventory(client);
			break;
		}
	}
	
	private void askPos(Client client) 
	{
		ResultSet rs = null;
		try {
			String sql = "SELECT Personnage.pos_x, Personnage.pos_y " +
					"FROM Personnage " +
					"WHERE personnage.joueur=" + client.getCompte().getClient_id();
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			int pos_x = 0, pos_y = 0;
			while(rs.next())
			{
				pos_x= rs.getInt("Personnage.pos_x");
				pos_y = rs.getInt("Personnage.pos_y");
			}
			client.getCompte().getCurrent_joueur().setTile(MapManager.instance.getEntire_map().getGrille().get(pos_x).get(pos_y));
			client.sendToClient(pos_x+";"+pos_y);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Race caracteristic");
		}
	}

	public void askRaceCaracteristic(Client client)
	{
		ResultSet rs = null;
		try {
			String sql = "SELECT caracteristiques.name, caracteristiques_race.value " +
					"FROM caracteristiques,caracteristiques_race,personnage " +
					"WHERE caracteristiques.id=caracteristiques_race.id_caracteristique " +
					"AND caracteristiques_race.id_race=personnage.race " +
					"AND personnage.joueur=" + client.getCompte().getClient_id();
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			HashMap<Caracteristique,Integer> caracs_race = new HashMap<Caracteristique,Integer>();
			String rc = "";
			while(rs.next())
			{
				rc += rs.getString("caracteristiques.name") + ";";
				rc += rs.getInt("caracteristiques_race.value") + ";";
				caracs_race.put(Caracteristique.valueOf(rs.getString("caracteristiques.name").toUpperCase()), rs.getInt("caracteristiques_race.value"));
			}
			client.getCompte().getCurrent_joueur().getPerso().getRace().setCarac(caracs_race);
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Race caracteristic");
		}
	}

	public void askClassCaracteristic(Client client)
	{
		ResultSet rs;
		try {
			String sql = "SELECT caracteristiques.name, caracteristiques_classe.value " +
					"FROM caracteristiques,caracteristiques_classe,personnage " +
					"WHERE caracteristiques.id=caracteristiques_classe.id_caracteristique " +
					"AND caracteristiques_classe.id_classe=personnage.classe " +
					"AND personnage.joueur=" + client.getCompte().getClient_id();
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			HashMap<Caracteristique,Integer> caracs_classe = new HashMap<Caracteristique,Integer>();
			String rc = "";
			while(rs.next())
			{
				rc += rs.getString("caracteristiques.name") + ";";
				rc += rs.getInt("caracteristiques_classe.value") + ";";
				caracs_classe.put(Caracteristique.valueOf(rs.getString("caracteristiques.name").toUpperCase()), rs.getInt("caracteristiques_classe.value"));
			}
			client.getCompte().getCurrent_joueur().getPerso().getClasse().setCaracs(caracs_classe);
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Class caracteristic");
		}
	}

	public void askPlayerCaracteristic(Client client)
	{
		ResultSet rs;
		try {
			String sql = "SELECT caracteristiques.name, caracteristiques_joueur.value " +
					"FROM caracteristiques,caracteristiques_joueur " +
					"WHERE caracteristiques.id=caracteristiques_joueur.id_caracteristique " +
					"AND caracteristiques_joueur.id_joueur=" + client.getCompte().getClient_id();
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			HashMap<Caracteristique,Integer> caracs = new HashMap<Caracteristique,Integer>();
			String rc = "";
			while(rs.next())
			{
				rc += rs.getString("caracteristiques.name") + ";";
				rc += rs.getInt("caracteristiques_joueur.value") + ";";
				caracs.put(Caracteristique.valueOf(rs.getString("caracteristiques.name").toUpperCase()), rs.getInt("caracteristiques_joueur.value"));
			}
			client.getCompte().getCurrent_joueur().getPerso().setCaracs(caracs);
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Player Caracteristic");
		}
	}

	public void askPlayerCaracteristicValue(Client client)
	{
		ResultSet rs;
		//Valeurs des caracteristiques du joueur
		try {
			String sql = "SELECT caracteristiques.name, caracteristiques_joueur.current_value " +
					"FROM caracteristiques,caracteristiques_joueur " +
					"WHERE caracteristiques.id=caracteristiques_joueur.id_caracteristique " +
					"AND caracteristiques_joueur.id_joueur=" + client.getCompte().getClient_id();
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			HashMap<Caracteristique,Integer> caracs_value = new HashMap<Caracteristique,Integer>();
			String rc = "";
			while(rs.next())
			{
				rc += rs.getString("caracteristiques.name") + ";";
				rc += rs.getInt("caracteristiques_joueur.current_value") + ";";
				caracs_value.put(Caracteristique.valueOf(rs.getString("caracteristiques.name").toUpperCase()), rs.getInt("caracteristiques_joueur.current_value"));
			}
			client.getCompte().getCurrent_joueur().getPerso().setCaracs_values(caracs_value);
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Player Caracteristic values");
		}
	}

	public void askRaceSort(Client client)
	{
		ResultSet rs;
		//sorts de race
		try {
			String sql = "SELECT sorts_race.nom, sorts_race.value_min, sorts_race.value_max, sorts_race.description" +
					" FROM sorts_race,personnage,race_sort " +
					"WHERE sorts_race.id=race_sort.id_sort " +
					"AND race_sort.id_race=personnage.race " +
					"AND personnage.joueur=" + client.getCompte().getClient_id();
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			ArrayList<Sort> sorts_race = new ArrayList<Sort>();
			String rc = "";
			while(rs.next())
			{
				rc += rs.getString("sorts_race.nom") + ";";
				rc += rs.getInt("sorts_race.value_min") + ";";
				rc += rs.getInt("sorts_race.value_max") + ";";
				rc += rs.getString("sorts_race.description") + ";";
				sorts_race.add(new Sort(rs.getString("sorts_race.nom"), rs.getString("sorts_race.description"), rs.getInt("sorts_race.value_min"), rs.getInt("sorts_race.value_max"), null));
			}
			client.
			getCompte().
			getCurrent_joueur().
			getPerso().
			getRace().
			setSorts(sorts_race);
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("race sort");
		}
	}

	public void askClassSort(Client client)
	{
		ResultSet rs;
		//sorts de classe
		try {
			String sql = "SELECT sorts_classe.nom, sorts_classe.value_min, sorts_classe.value_max, sorts_classe.description" +
					" FROM sorts_classe,personnage,classe_sort " +
					"WHERE sorts_classe.id=classe_sort.id_sort " +
					"AND classe_sort.id_classe=personnage.classe " +
					"AND personnage.joueur=" + client.getCompte().getClient_id();
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			ArrayList<Sort> sorts_classe = new ArrayList<Sort>();
			String rc = "";
			while(rs.next())
			{
				rc += rs.getString("sorts_classe.nom") + ";";
				rc += rs.getInt("sorts_classe.value_min") + ";";
				rc += rs.getInt("sorts_classe.value_max") + ";";
				rc += rs.getString("sorts_classe.Description") + ";";
				sorts_classe.add(new Sort(rs.getString("sorts_classe.nom"), rs.getString("sorts_classe.description"), rs.getInt("sorts_classe.value_min"), rs.getInt("sorts_classe.value_max"), null));
			}
			client.getCompte().getCurrent_joueur().getPerso().getClasse().setSorts(sorts_classe);
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Class sort");
		}
	}

	public void askInventory(Client client)
	{
		ResultSet rs;
		try {
			String sql = "SELECT item.id, item.nom, item.description, item.type, item.icone, item.apercu " +
					"FROM item,inventaire " +
					"WHERE item.id=inventaire.id_objet " +
					"AND inventaire.id_joueur=" + client.getCompte().getClient_id();
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			ArrayList<SimpleItem> items = new ArrayList<SimpleItem>();
			String rc = "";
			while(rs.next())
			{
				ResultSet rs2 = null;
				rc += rs.getString("item.nom") + ";";
				rc += rs.getString("item.description") + ";";
				rc += rs.getString("item.type") + ";";
				rc += rs.getString("item.icone") + ";";
				rc += rs.getString("item.apercu") + ";";
				sql = "SELECT caracteristiques_objet.value,caracteristiques.name " +
						"FROM caracteristiques_objet, caracteristiques " +
						"WHERE caracteristiques_objet.id_caracteristique=caracteristiques.id " +
						"AND caracteristiques_objet.id_objet=" + rs.getString("item.id");
				Statement stmt2 = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
				rs2 = stmt2.executeQuery(sql);
				HashMap<Caracteristique, Integer> effets = new HashMap<Caracteristique, Integer>();
				while(rs2.next())
				{
					rc += rs2.getString("caracteristiques.name") + ";";
					rc += rs2.getString("caracteristiques_objet.value") + ";";
					effets.put(Caracteristique.valueOf(rs2.getString("caracteristiques.name").toUpperCase()), rs2.getInt("caracteristiques_objet.value"));
				}
				items.add(new SimpleItem(rs.getString("item.nom"), rs.getString("item.description"), rs.getString("item.icone"), rs.getString("item.apercu"), effets, 10));
				rs2.close();
				stmt2.close();
			}
			client.getCompte().getCurrent_joueur().getPerso().setInventaire(new Inventaire(items));
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	
	
	
	
	
	private void askEntities(Client client)
	{
		ArrayList<ArrayList<Tile>> grille = MapManager.instance.getTilesAutour(client.getCompte().getCurrent_joueur().getTile(), GlobalConstant.nbCaseNear);
		ArrayList<Tile> tiles_to_test = new ArrayList<Tile>();
		
		//Un peu gore, à améliorer
		for(int i = 0; i < grille.size(); i++)
		{
			for(int j = 0; j < grille.get(i).size(); j++)
			{
				tiles_to_test.add(grille.get(i).get(j));
				for(int u = 0; u < client.getCompte().getCurrent_joueur().getLoaded_zone().size(); u++)
				{
					if(grille.get(i).get(j).equals(client.getCompte().getCurrent_joueur().getLoaded_zone().get(u)))
					{
						tiles_to_test.remove(grille.get(i).get(j));
					}
				}
			}
		}
		
		
		//Idem
		for(int i = 0; i < EntitiesManager.instance.getPnjs_manager().getPnjs().size(); i++)
		{
			for(int k = 0; k < tiles_to_test.size(); k++)
			{
				if(tiles_to_test.get(k).equals(EntitiesManager.instance.getPnjs_manager().getPnjs().get(i).getTile()))
				{
					loadPnj(client, EntitiesManager.instance.getPnjs_manager().getPnjs().get(i));
				}
				client.getCompte().getCurrent_joueur().getLoaded_zone().add(tiles_to_test.get(k));
			}
		}
	}
	
	private void loadPnj(Client client, PNJ pnj)
	{
		String rc = "lo;ent;pnj;";
		rc += (int)pnj.getTile().getPos().x + ";";
		rc += (int)pnj.getTile().getPos().y + ";";
		rc += pnj.getNom() + ";";
		rc += pnj.getPnjDiscours().getDiscours() + ";";
		rc += getPnjAnswer(pnj.getPnjDiscours(), 0);
		System.out.println(rc);
		client.sendToClient(rc);
	}
	
	private String getPnjAnswer(PNJ_discours pnj_discours, int depth)
	{
		String rc = "";
		depth++;
		for(int i = 0; i < pnj_discours.getReponses().size(); i++)
		{
			rc += depth + ";";
			rc += pnj_discours.getReponses().get(i).getDiscours() + ";";
			rc += getPnjAnswer(pnj_discours.getReponses().get(i),depth);
		}
		return rc;
	}


	public void askTypeTiles(Client client, String name)
	{
		ResultSet rs;
		//Chargement des informations d'une tile
		try {
			String sql = "SELECT tiles_map.nom, tiles_map.image, tiles_map.collidable, tiles_map.base_x, tiles_map.base_y " +
					"FROM tiles_map " +
					"WHERE tiles_map.nom='" + name + "'";
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			String rc = "";
			while(rs.next())
			{
				rc += rs.getString("tiles_map.nom") + ";";
				rc += rs.getString("tiles_map.image") + ";";
				rc += rs.getBoolean("tiles_map.collidable") + ";";
				rc += rs.getInt("tiles_map.base_x") + ";";
				rc += rs.getInt("tiles_map.base_y") + ";";
				rc += "0";
			}
			if(rc.equals(""))
			{
				sql = "SELECT tiles_map_content.nom, tiles_map_content.image, tiles_map_content.collidable, tiles_map_content.base_x, tiles_map_content.base_y " +
						"FROM tiles_map_content " +
						"WHERE tiles_map_content.nom='" + name + "'";
				rs = stmt.executeQuery(sql);
				rc = "";
				while(rs.next())
				{
					rc += rs.getString("tiles_map_content.nom") + ";";
					rc += rs.getString("tiles_map_content.image") + ";";
					rc += rs.getBoolean("tiles_map_content.collidable") + ";";
					rc += rs.getInt("tiles_map_content.base_x") + ";";
					rc += rs.getInt("tiles_map_content.base_y") + ";";
					rc += "1";
				}
			}
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Type tiles");
		}
	}

	public void askMap(Client client)
	{
		ResultSet rs;
		//Chargement des informations de la map
		try {
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			String sql = "SELECT MAX(map.x), MAX(map.y)" +
					"FROM map ";
			rs = stmt.executeQuery(sql);
			String rc = "";
			rs.next();
			rc += rs.getInt(1) + ";";
			rc += rs.getInt(2) + ";";
			sql = "SELECT map.x, map.y, map.monsterHolder, tiles_map.nom " +
					"FROM tiles_map,map " +
					"WHERE tiles_map.id=map.type";
			rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				rc += rs.getInt("map.x") + ";";
				rc += rs.getInt("map.y") + ";";
				rc += rs.getString("tiles_map.nom") + ";";
				rc += rs.getBoolean("map.monsterHolder") + ";";
			}
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Map");
		}
	}

	public void askMapContent(Client client)
	{
		ResultSet rs;
		//Chargement des informations de la map
		try {
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			String rc = "";
			String sql = "SELECT map_content.x, map_content.y, tiles_map_content.nom " +
					"FROM tiles_map_content,map_content " +
					"WHERE tiles_map_content.id=map_content.type";
			rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				rc += rs.getInt("map_content.x") + ";";
				rc += rs.getInt("map_content.y") + ";";
				rc += rs.getString("tiles_map_content.nom") + ";";
			}
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Map Content");
		}
	}

	public void askMonster(Client client)
	{
		ResultSet rs;
		//Chargement des informations des monstres
		try {
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			String rc = "";
			String sql = "SELECT monster.name " +
					"FROM monster ";
			rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				rc += rs.getString("monster.name") + ";";
			}
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Monster");
		}
	}
}
