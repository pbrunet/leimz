package com.server.core.functions;

import com.server.core.Client;
import com.server.core.ServerSingleton;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Write a description of class ConnectFunction here.
 * 
 * @author chelendil 
 */
public class ConnectFunction implements Functionable
{
	public ConnectFunction()
	{    
	}

	@Override
	public void doSomething(String[] args,Client c)
	{
		
		if(args.length <2)
			throw new RuntimeException("Connection");
		
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
				throw new RuntimeException("Searching player");

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
			c.getCompte().setClient_id(id);
			c.sendToClient("CONNECT_SUCCEED");
			c.sendToClient(nom+";"+race+";"+classe);

		} catch (SQLException e) {
			throw new RuntimeException("Connection");
		}
	}
}
