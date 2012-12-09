package com.server.core.functions;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.server.entities.PNJ;
import com.gameplay.PNJ_discours;
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
		case "j":
			askJoueur(client, args);
			break;
		case "ent":
			askEntities(client);
			break;
		case "tt":
			askTypeTiles(client,args[2],args[1]);
			break;
		case "map":
			askMap(client,args[1]);
			break;
		case "mapc":
			askMapContent(client,args[1]);
			break;
		case "mon":
			askMonster(client);
			break;
		case "pj":
			askPerso(client,args[1]);
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
			askRaceCaracteristic(client, args[2]);
			break;
		case "cc":
			askClassCaracteristic(client, args[2]);
			break;
		case "jc":
			askPlayerCaracteristic(client, args[2]);
			break;
		case "jcv":
			askPlayerCaracteristicValue(client, args[2]);
			break;
		case "rs":
			askRaceSort(client, args[2]);
			break;
		case "cs":
			askClassSort(client, args[2]);
			break;
		case "in":
			askInventory(client, args[2]);
			break;
		}
	}
	
	public void askPerso(Client client,String tag)
	{
		ResultSet rs;
		try {
			String sql = "SELECT name,race,classe,posx,posy,orientation " +
					"FROM personnage,Account " +
					"WHERE personnage.name=Account.currjoueur " +
					"AND Account.connected=1";
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			String rc = tag + ";";
			while(rs.next())
			{
				rc += "new;";
				rc += rs.getString("name") + ";";
				rc += rs.getString("race") + ";";
				rc += rs.getString("classe") + ";";
				rc += rs.getInt("posx") + ";";
				rc += rs.getInt("posy") + ";";
				rc += rs.getString("orientation") + ";";
			}

			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Other players");
		}
	}
	
	public void askRaceCaracteristic(Client client,String tag)
	{
		ResultSet rs = null;
		try {
			String sql = "SELECT caracteristiques_race.caracteristique, caracteristiques_race.value " +
					"FROM caracteristiques_race,personnage " +
					"WHERE caracteristiques_race.race=personnage.race " +
					"AND personnage.name='" + client.getCompte().getCurrent_joueur().getPerso().getNom()+"'";
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			String rc = tag + ";";
			while(rs.next())
			{
				rc += rs.getString("caracteristiques_race.caracteristique") + ";";
				rc += rs.getInt("caracteristiques_race.value") + ";";
			}
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Race caracteristic");
		}
	}

	public void askClassCaracteristic(Client client,String tag)
	{
		ResultSet rs;
		try {
			String sql = "SELECT caracteristiques_classe.caracteristique, caracteristiques_classe.value " +
					"FROM caracteristiques_classe,personnage " +
					"WHERE caracteristiques_classe.classe=personnage.classe " +
					"AND personnage.name='" + client.getCompte().getCurrent_joueur().getPerso().getNom()+"'";
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			String rc = tag + ";";
			while(rs.next())
			{
				rc += rs.getString("caracteristiques_classe.caracteristique") + ";";
				rc += rs.getInt("caracteristiques_classe.value") + ";";
			}
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Class caracteristic");
		}
	}

	public void askPlayerCaracteristic(Client client,String tag)
	{
		ResultSet rs;
		try {
			String sql = "SELECT caracteristique, value " +
					"FROM caracteristiques_joueur " +
					"WHERE nom_joueur='" + client.getCompte().getCurrent_joueur().getPerso().getNom()+"'";
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			String rc = tag + ";";
			while(rs.next())
			{
				rc += rs.getString("caracteristique") + ";";
				rc += rs.getInt("value") + ";";
			}
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Player Caracteristic");
		}
	}

	public void askPlayerCaracteristicValue(Client client,String tag)
	{
		ResultSet rs;
		//Valeurs des caracteristiques du joueur
		try {
			String sql = "SELECT caracteristique, current_value " +
					"FROM caracteristiques_joueur " +
					"WHERE nom_joueur='" + client.getCompte().getCurrent_joueur().getPerso().getNom()+"'";
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			String rc = tag + ";";
			while(rs.next())
			{
				rc += rs.getString("caracteristique") + ";";
				rc += rs.getInt("current_value") + ";";
			}
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Player Caracteristic values");
		}
	}

	public void askRaceSort(Client client,String tag)
	{
		ResultSet rs;
		//sorts de race
		try {
			String sql = "SELECT sorts_race.nom, sorts_race.value_min, sorts_race.value_max, sorts_race.description" +
					" FROM sorts_race,personnage,race_sort " +
					"WHERE sorts_race.nom=race_sort.sort " +
					"AND race_sort.race=personnage.race " +
					"AND personnage.name='" + client.getCompte().getCurrent_joueur().getPerso().getNom()+"'";
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			String rc = tag + ";";
			while(rs.next())
			{
				rc += rs.getString("sorts_race.nom") + ";";
				rc += rs.getInt("sorts_race.value_min") + ";";
				rc += rs.getInt("sorts_race.value_max") + ";";
				rc += rs.getString("sorts_race.description") + ";";
			}
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("race sort");
		}
	}

	public void askClassSort(Client client,String tag)
	{
		ResultSet rs;
		//sorts de classe
		try {
			String sql = "SELECT sorts_classe.nom, sorts_classe.value_min, sorts_classe.value_max, sorts_classe.description" +
					" FROM sorts_classe,personnage,classe_sort " +
					"WHERE sorts_classe.nom=classe_sort.sort " +
					"AND classe_sort.classe=personnage.classe " +
					"AND personnage.name='" + client.getCompte().getCurrent_joueur().getPerso().getNom()+"'";
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			String rc = tag + ";";
			while(rs.next())
			{
				rc += rs.getString("sorts_classe.nom") + ";";
				rc += rs.getInt("sorts_classe.value_min") + ";";
				rc += rs.getInt("sorts_classe.value_max") + ";";
				rc += rs.getString("sorts_classe.Description") + ";";
			}
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Class sort");
		}
	}

	public void askInventory(Client client,String tag)
	{
		ResultSet rs;
		try {
			String sql = "SELECT item.nom, item.description, item.type, item.icone, item.apercu " +
					"FROM item,inventaire " +
					"WHERE item.nom=inventaire.objet " +
					"AND inventaire.joueur='" + client.getCompte().getCurrent_joueur().getPerso().getNom()+"'";
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			String rc = tag + ";";
			while(rs.next())
			{
				ResultSet rs2 = null;
				rc += rs.getString("item.nom") + ";";
				rc += rs.getString("item.description") + ";";
				rc += rs.getString("item.type") + ";";
				rc += rs.getString("item.icone") + ";";
				rc += rs.getString("item.apercu") + ";";
				sql = "SELECT value,caracteristique " +
						"FROM caracteristiques_objet " +
						"WHERE objet='" + rs.getString("item.nom")+"'";
				Statement stmt2 = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
				rs2 = stmt2.executeQuery(sql);
				while(rs2.next())
				{
					rc += rs2.getString("caracteristique") + ";";
					rc += rs2.getInt("value") + ";";
				}
				rs2.close();
				stmt2.close();
			}
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Bag");
		}
	}
	
	private void askEntities(Client client)
	{
		System.out.println("asking entities");
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


	public void askTypeTiles(Client client, String name,String tag)
	{
		ResultSet rs;
		//Chargement des informations d'une tile
		try {
			String sql = "SELECT nom, image, collidable, base_x, base_y " +
					"FROM tiles_map " +
					"WHERE nom='" + name + "'";
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			String rc = tag + ";";
			while(rs.next())
			{
				rc += rs.getString("nom") + ";";
				rc += rs.getString("image") + ";";
				rc += rs.getBoolean("collidable") + ";";
				rc += rs.getInt("base_x") + ";";
				rc += rs.getInt("base_y") + ";";
				rc += "0";
			}
			if(rc.equals(tag + ";"))
			{
				sql = "SELECT nom, image, collidable, base_x, base_y " +
						"FROM tiles_map_content " +
						"WHERE nom='" + name + "'";
				rs = stmt.executeQuery(sql);
				while(rs.next())
				{
					rc += rs.getString("nom") + ";";
					rc += rs.getString("image") + ";";
					rc += rs.getBoolean("collidable") + ";";
					rc += rs.getInt("base_x") + ";";
					rc += rs.getInt("base_y") + ";";
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

	public void askMap(Client client,String tag)
	{
		ResultSet rs;
		//Chargement des informations de la map
		try {
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			String sql = "SELECT MAX(map.x), MAX(map.y)" +
					"FROM map ";
			rs = stmt.executeQuery(sql);
			String rc = tag + ";";
			rs.next();
			rc += rs.getInt(1) + ";";
			rc += rs.getInt(2) + ";";
			sql = "SELECT x, y, monsterHolder, type " +
					"FROM map ";
			rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				rc += rs.getInt("x") + ";";
				rc += rs.getInt("y") + ";";
				rc += rs.getString("type") + ";";
				rc += rs.getBoolean("monsterHolder") + ";";
			}
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Map");
		}
	}

	public void askMapContent(Client client,String tag)
	{
		ResultSet rs;
		//Chargement des informations de la map
		try {
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			String rc = tag + ";";
			String sql = "SELECT x, y, type " +
					"FROM map_content ";
			rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				rc += rs.getInt("x") + ";";
				rc += rs.getInt("y") + ";";
				rc += rs.getString("type") + ";";
			}
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Map Content");
		}
	}

	public void askMonster(Client client,String tag)
	{
		ResultSet rs;
		//Chargement des informations des monstres
		try {
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			String rc = tag + ";";
			String sql = "SELECT name " +
					"FROM monster ";
			rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				rc += rs.getString("name") + ";";
			}
			client.sendToClient(rc);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Monster");
		}
	}
}
