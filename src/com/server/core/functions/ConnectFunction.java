
package com.server.core.functions;

import com.server.entities.Joueur;

import com.server.entities.Personnage;
import com.map.server.managers.MapManager;
import com.server.core.Account;
import com.server.core.Client;
import com.server.core.ServerSingleton;
import com.server.entities.Orientation;

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
			rsj = stmt.executeQuery("SELECT currjoueur,connected FROM Account " +
					"WHERE nom_de_compte='"+ndc+"' " +
					"AND mot_de_passe='"+mdp+"'");

			rsj.next();
			String name = rsj.getString("currjoueur");
			boolean connected = rsj.getBoolean("connected");
			if(connected)
				throw new RuntimeException("Player already connected");

			if(name.isEmpty())
				throw new RuntimeException("Searching player");

			rsj.close();
			stmt.executeUpdate("UPDATE Account SET connected=true WHERE nom_de_compte='"+ndc+"'");
			ResultSet rsp = stmt.executeQuery("SELECT race,classe,posx,posy,orientation " +
					"FROM personnage " +
					"WHERE name='"+name+"'");
			rsp.next();
			String race = rsp.getString("race");
			String classe = rsp.getString("classe");
			int posx = rsp.getInt("posx");
			int posy = rsp.getInt("posy");
			String ori = rsp.getString("orientation");

			c.sendToClient("c;CONNECT_SUCCEED");
			c.setCompte(new Account(ndc,mdp));
			c.getCompte().setCurrent_joueur(new Joueur(new Personnage(name), MapManager.instance.getEntire_map().getGrille().get((int)posx).get((int)posy), Orientation.BAS));
			c.sendToClient("ci;"+name+";"+race+";"+classe+";"+posx+";"+posy+";"+ori);
			ServerSingleton.getInstance().sendAllClient("aj;"+name+";"+race+";"+classe+";"+posx+";"+posy+";"+ori);
			rsp.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
