package com.server.core.functions;

import com.server.core.ServerSingleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Write a description of class ConnectFunction here.
 * 
 * @author chelendil 
 * @version (a version number or a date)
 */
public class ConnectFunction implements Functionable
{
	public ConnectFunction()
	{    
	}

	@Override
	public ArrayList<String> doSomething(String[] args,int client_id)
	{
		ArrayList<String> answer = new ArrayList<String>();
		
		if(args.length <2)
		{
			answer.add("CONNECT_FAIL");
			return answer;
		}
		
		String ndc = args[1];
		String mdp = args[2];

		ResultSet rsj = null;
		try {
			rsj = ServerSingleton.getInstance().getDbConnexion().getStmt().executeQuery("SELECT id FROM joueur WHERE nom_de_compte='"+ndc+"' AND mot_de_passe='"+mdp+"'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {

			int id = 0;
			while(rsj.next())
			{
				id = rsj.getInt("id");
			}
			
			if(id == 0)
			{
				answer.add("CONNECT_FAIL");
				return answer;
			}

			ResultSet rsp = null;
			try {
				rsp = ServerSingleton.getInstance().getDbConnexion().getStmt().executeQuery("SELECT personnage.name,race.name,classe.name FROM personnage, race, classe WHERE personnage.joueur="+id+" AND race.id=personnage.race AND classe.id=personnage.classe");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			String race = null, classe = null, nom = null;
			while(rsp.next())
			{
				nom = rsp.getString("personnage.name");
				race = rsp.getString("race.name");
				classe = rsp.getString("classe.name");
			}
			answer.add((new Integer(id)).toString());
			answer.add("CONNECT_SUCCEED");
			answer.add(nom+";"+race+";"+classe);

		} catch (SQLException e) {
			answer.add(0, "CONNECT_FAIL");
			e.printStackTrace();
		}
		System.out.println(answer);
		return answer;
	}
}
