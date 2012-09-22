package com.server.core.functions;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

import com.server.core.ServerSingleton;

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
    public ArrayList<String> doSomething(String[] args,int client_id)
    {
    	ArrayList<String> answer = new ArrayList<String>();
		ResultSet rs = null;
    	if(args[1].equals("rc"))
    	{
    		//Caracteristiques de race
    		try {
    			String sql = "SELECT caracteristiques.name, caracteristiques_race.value " +
    					"FROM caracteristiques,caracteristiques_race,personnage " +
    					"WHERE caracteristiques.id=caracteristiques_race.id_caracteristique " +
    					"AND caracteristiques_race.id_race=personnage.race " +
    					"AND personnage.joueur=" + client_id;
				rs = ServerSingleton.getInstance().getDbConnexion().getStmt().executeQuery(sql);
				String rc = "";
				while(rs.next())
				{
					rc += rs.getString("caracteristiques.name") + ";";
					rc += rs.getInt("caracteristiques_race.value") + ";";
				}
				answer.add(rc);
			} catch (SQLException e) {
				answer.add("REQUEST_FAIL");
				e.printStackTrace();
			}
    	}
    	else if(args[1].equals("cc"))
    	{
    		//Caracteristiques de classe
    		try {
    			String sql = "SELECT caracteristiques.name, caracteristiques_classe.value " +
    					"FROM caracteristiques,caracteristiques_classe,personnage " +
    					"WHERE caracteristiques.id=caracteristiques_classe.id_caracteristique " +
    					"AND caracteristiques_classe.id_classe=personnage.classe " +
    					"AND personnage.joueur=" + client_id;
				rs = ServerSingleton.getInstance().getDbConnexion().getStmt().executeQuery(sql);
				String rc = "";
				while(rs.next())
				{
					rc += rs.getString("caracteristiques.name") + ";";
					rc += rs.getInt("caracteristiques_classe.value") + ";";
				}
				answer.add(rc);
			} catch (SQLException e) {
				answer.add("REQUEST_FAIL");
				e.printStackTrace();
			}
    	}
    	else if(args[1].equals("jc"))
    	{
    		//Caracteristiques de joueur
    		try {
    			String sql = "SELECT caracteristiques.name, caracteristiques_joueur.value " +
    					"FROM caracteristiques,caracteristiques_joueur " +
    					"WHERE caracteristiques.id=caracteristiques_joueur.id_caracteristique " +
    					"AND caracteristiques_joueur.id_joueur=" + client_id;
				rs = ServerSingleton.getInstance().getDbConnexion().getStmt().executeQuery(sql);
				String rc = "";
				while(rs.next())
				{
					rc += rs.getString("caracteristiques.name") + ";";
					rc += rs.getInt("caracteristiques_joueur.value") + ";";
				}
				answer.add(rc);
			} catch (SQLException e) {
				answer.add("REQUEST_FAIL");
				e.printStackTrace();
			}
    	}
    	else if(args[1].equals("jcv"))
    	{
    		//Valeurs des caracteristiques du joueur
    		try {
    			String sql = "SELECT caracteristiques.name, caracteristiques_joueur.current_value " +
    					"FROM caracteristiques,caracteristiques_joueur " +
    					"WHERE caracteristiques.id=caracteristiques_joueur.id_caracteristique " +
    					"AND caracteristiques_joueur.id_joueur=" + client_id;
				rs = ServerSingleton.getInstance().getDbConnexion().getStmt().executeQuery(sql);
				String rc = "";
				while(rs.next())
				{
					rc += rs.getString("caracteristiques.name") + ";";
					rc += rs.getInt("caracteristiques_joueur.current_value") + ";";
				}
				answer.add(rc);
			} catch (SQLException e) {
				answer.add("REQUEST_FAIL");
				e.printStackTrace();
			}
    	}
    	else if(args[1].equals("rs"))
        {
    		//sorts de race
        	try {
        		String sql = "SELECT sorts_race.nom, sorts_race.value_min, sorts_race.value_max, sorts_race.description" +
        			" FROM sorts_race,personnage,race_sort " +
        			"WHERE sorts_race.id=race_sort.id_sort " +
        			"AND race_sort.id_race=personnage.race " +
					"AND personnage.joueur=" + client_id;
    			rs = ServerSingleton.getInstance().getDbConnexion().getStmt().executeQuery(sql);
    			String rc = "";
    			while(rs.next())
    			{
    				rc += rs.getString("sorts_race.nom") + ";";
    				rc += rs.getInt("sorts_race.value_min") + ";";
    				rc += rs.getInt("sorts_race.value_max") + ";";
    				rc += rs.getString("sorts_race.description") + ";";
    			}
    			answer.add(rc);
    		} catch (SQLException e) {
    			answer.add("REQUEST_FAIL");
    			e.printStackTrace();
    		}
        }
    	else if(args[1].equals("cs"))
        {
    		//sorts de classe
        	try {
        		String sql = "SELECT sorts_classe.nom, sorts_classe.value_min, sorts_classe.value_max, sorts_classe.description" +
        			" FROM sorts_classe,personnage,classe_sort " +
        			"WHERE sorts_classe.id=classe_sort.id_sort " +
        			"AND classe_sort.id_classe=personnage.classe " +
					"AND personnage.joueur=" + client_id;
    			rs = ServerSingleton.getInstance().getDbConnexion().getStmt().executeQuery(sql);
    			String rc = "";
    			while(rs.next())
    			{
    				rc += rs.getString("sorts_classe.nom") + ";";
    				rc += rs.getInt("sorts_classe.value_min") + ";";
    				rc += rs.getInt("sorts_classe.value_max") + ";";
    				rc += rs.getString("sorts_classe.Description") + ";";
    			}
    			answer.add(rc);
    		} catch (SQLException e) {
    			answer.add("REQUEST_FAIL");
    			e.printStackTrace();
    		}
        }
    	else if(args[1].equals("in"))
    	{
        	try {
        		String sql = "SELECT item.nom, item.description, item.type, item.icone, item.apercu, item.effets" +
        			" FROM item,inventaire,personnage " +
        			"WHERE item.id=inventaire.id_objet " +
        			"AND inventaire.id_joueur=personnage.joueur " +
					"AND personnage.joueur=" + client_id;
    			rs = ServerSingleton.getInstance().getDbConnexion().getStmt().executeQuery(sql);
    			String rc = "";
    			while(rs.next())
    			{
    				rc += rs.getString("item.nom") + ";";
    				rc += rs.getString("item.description") + ";";
    				rc += rs.getString("item.type") + ";";
    				rc += rs.getString("item.icone") + ";";
    				rc += rs.getString("item.apercu") + ";";
    				rc += rs.getString("item.effets") + ";";
    			}
    			System.out.println("/" + rc + "/");
    			answer.add(rc);
    		} catch (SQLException e) {
    			answer.add("REQUEST_FAIL");
    			e.printStackTrace();
    		}
    	}
    	else if(args[1].equals("pnj"))
    	{
        	try {
        		String sql = "SELECT pnj.nom " +
        			"FROM pnj";
    			rs = ServerSingleton.getInstance().getDbConnexion().getStmt().executeQuery(sql);
    			String rc = "";
    			while(rs.next())
    			{
    				rc += rs.getString("pnj.nom") + ";";
    			}
    			
    			String sql_d = "SELECT pnj_discours.discours, pnj_discours.after_answer " +
    			"FROM pnj_discours,pnj " +
    			"WHERE pnj_discours.id_pnj = pnj.id";
    			rs = ServerSingleton.getInstance().getDbConnexion().getStmt().executeQuery(sql_d);
    			while(rs.next())
    			{
    				rc += rs.getString("pnj_discours.discours") + ";";
    				rc += rs.getString("pnj_discours.after_answer") + ";";
    			}
    			rc = rc.substring(0, rc.length()-1);
    			rc+="|s|;";
    			
    			String sql_r = "SELECT pnj_reponses.reponse, pnj_reponses.id_discours " +
    			"FROM pnj_reponses,pnj " +
    			"";//"WHERE pnj_reponses.id=" + "1";
    			rs = ServerSingleton.getInstance().getDbConnexion().getStmt().executeQuery(sql_r);
    			while(rs.next())
    			{
    				rc += rs.getString("pnj_reponses.id_discours") + ";";
    				rc += rs.getString("pnj_reponses.reponse") + ";";
    			}
    			rc = rc.substring(0, rc.length()-1);
    			rc+="|s|;";
    			
    			String sql_pos = "SELECT pnj.pos_x, pnj.pos_y " +
    			"FROM pnj";
    			rs = ServerSingleton.getInstance().getDbConnexion().getStmt().executeQuery(sql_pos);
    			while(rs.next())
    			{
    				rc += rs.getInt("pnj.pos_x") + ";";
    				rc += rs.getInt("pnj.pos_y") + ";";
    			}
    			System.out.println("message pnj : "+rc);
    			answer.add(rc);
    		} catch (SQLException e) {
    			answer.add("REQUEST_FAIL");
    			e.printStackTrace();
    		}
    	}
    	
    	else if(args[1].equals("tt"))
        {
    		//Chargement des informations d'une tile
        	try {
        		String sql = "SELECT tiles_map.nom, tiles_map.image, tiles_map.collidable, tiles_map.base_x, tiles_map.base_y " +
        			"FROM tiles_map " +
        			"WHERE tiles_map.nom='" + args[2]+"'";
    			rs = ServerSingleton.getInstance().getDbConnexion().getStmt().executeQuery(sql);
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
            			"WHERE tiles_map_content.nom=" + args[2];
        			rs = ServerSingleton.getInstance().getDbConnexion().getStmt().executeQuery(sql);
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
    			answer.add(rc);
    		} catch (SQLException e) {
    			answer.add("REQUEST_FAIL");
    			e.printStackTrace();
    		}
        }
		return answer;
    }
}
