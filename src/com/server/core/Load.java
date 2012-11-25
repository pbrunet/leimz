package com.server.core;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import com.gameplay.PNJ_discours;
import com.map.Grille;
import com.map.Map;
import com.map.Tile;
import com.map.server.managers.MapManager;
import com.server.entities.Orientation;
import com.server.entities.PNJ;
import com.server.entities.managers.EntitiesManager;
import com.server.entities.managers.PNJsManager;

public class Load implements Runnable
{
	@SuppressWarnings("unused")
	private MapManager mapmanager;
	@SuppressWarnings("unused")
	private EntitiesManager entitiesmanager;
	private Thread t;
	public Load()
	{
		t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() 
	{
		loadMap();
		loadEntities();
		System.out.println("load ok");
	}
	
	private void loadEntities()
	{
		entitiesmanager = new EntitiesManager();
		loadPnjs();
		loadMonsters();
	}
	
	private void loadPnjs()
	{
		PNJsManager pnjs_manager= new PNJsManager();
		
		ResultSet rs;
		try {
			String sql = "SELECT pnj.pos_x, pnj.pos_y, pnj.nom, pnj_discours.discours, pnj_discours.id " +
					"FROM pnj, pnj_discours " +
					"WHERE pnj.nom=pnj_discours.nom_pnj " +
					"AND pnj_discours.after_answer IS NULL";
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				PNJ_discours discours = new PNJ_discours(rs.getString("pnj_discours.discours"), null, new ArrayList<PNJ_discours>(), 0);
				discours.setReponses(getPnjAnswer(discours, rs.getInt("pnj_discours.id"), 0));
				
				pnjs_manager.add(
						new PNJ(rs.getString("pnj.nom"),
								discours,
								Orientation.BAS,
								MapManager.instance.getEntire_map().getGrille().get(rs.getInt("pnj.pos_x")).get(rs.getInt("pnj.pos_y"))
						        ));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		EntitiesManager.instance.setPnjs_manager(pnjs_manager);
	}
	

	private ArrayList<PNJ_discours> getPnjAnswer(PNJ_discours parent, int id_discour, int depth) throws SQLException {
		ArrayList<PNJ_discours> list = new ArrayList<PNJ_discours>();
		
		ResultSet rs;
		String sql = "SELECT pnj_discours.discours, pnj_discours.id " +
				"FROM pnj_discours " +
				"WHERE pnj_discours.after_answer=" + id_discour;
		Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
		rs = stmt.executeQuery(sql);
		depth++;
		while(rs.next())
		{
			PNJ_discours d = new PNJ_discours(rs.getString("pnj_discours.discours"), parent, new ArrayList<PNJ_discours>(), depth);
			d.setReponses(getPnjAnswer(d,rs.getInt("pnj_discours.id"), depth));
			list.add(d);
		}
		return list;
	}
	
	private void loadMonsters()
	{
		
	}
	
	private void loadMap()
	{
		Map entire_map = null;
		Grille grille = new Grille();
		ResultSet rs;
		//Chargement des informations de la map
		try {
			int max_x = 0, max_y = 0;
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			String sql = "SELECT MAX(map.x), MAX(map.y)" +
					"FROM map ";
			rs = stmt.executeQuery(sql);
			rs.next();
			max_x = rs.getInt(1);
			max_y = rs.getInt(2);
			for(int i = 0; i < max_x+1 ; i++)
			{
				grille.add(new ArrayList<Tile>());
				for(int j = 0; j < max_y+1; j++)
					grille.get(i).add(new Tile(new Vector2f(i , j), null));
			}
			sql = "SELECT x, y, monsterHolder, type " +
					"FROM map ";
			rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				grille.get(rs.getInt("map.x")).get(rs.getInt("map.y")).addTypes(MapManager.getTypesTile(rs.getString("type")));
				grille.get(rs.getInt("map.x")).get(rs.getInt("map.y")).setMonsterHolder(rs.getBoolean("map.monsterHolder"));
				
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		entire_map = new Map(grille, null);
		
		
		mapmanager = new MapManager(entire_map);
	}

}
