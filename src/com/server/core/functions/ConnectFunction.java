package com.server.core.functions;

import com.game_entities.Joueur_server;
import com.game_entities.Orientation;
import com.gameplay.entities.Personnage_serveur;
import com.server.core.Account;
import com.server.core.Client;
import com.server.core.ServerSingleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


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
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rsj = stmt.executeQuery("SELECT id_account FROM Account " +
					"WHERE nom_de_compte='"+ndc+"' " +
					"AND mot_de_passe='"+mdp+"'");

			int id = 0;
		
			while(rsj.next())
			{
				id = rsj.getInt("id_account");
			}
			if(id == 0)
				throw new RuntimeException("Searching player");

			rsj.close();
			ResultSet rsp = stmt.executeQuery("SELECT personnage.name,race.name,classe.name " +
					"FROM personnage, race, classe " +
					"WHERE personnage.id_compte="+id+" " +
					"AND race.id=personnage.race " +
					"AND classe.id=personnage.classe");
			String race = null, classe = null, nom = null;
			while(rsp.next())
			{
				nom = rsp.getString("personnage.name");
				race = rsp.getString("race.name");
				classe = rsp.getString("classe.name");
			}
			c.sendToClient("c;CONNECT_SUCCEED");
			c.setCompte(new Account(ndc,mdp));
			c.getCompte().setClient_id(id);
			c.getCompte().setCurrent_joueur(new Joueur_server(new Personnage_serveur(nom), 0,0, Orientation.BAS));
			c.sendToClient("ci;"+nom+";"+race+";"+classe);
			rsp.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException("Connection");
		}
	}
}
