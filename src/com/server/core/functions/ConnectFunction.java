
package com.server.core.functions;

import com.server.entities.Joueur;
import com.server.entities.Personnage;
import com.map.server.managers.MapManager;
import com.server.core.Account;
import com.server.core.Client;
import com.server.core.ServerSingleton;
import com.server.entities.Orientation;
import com.server.entities.managers.EntitiesManager;

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

			Statement stmt;
			try {
				stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			} catch (SQLException e) {
				throw new RuntimeException("Finding account statement's creation failed");
			}
			try {
				rsj = stmt.executeQuery("SELECT currjoueur,connected FROM Account " +
						"WHERE nom_de_compte='"+ndc+"' " +
						"AND mot_de_passe='"+mdp+"'");
			} catch (SQLException e) {
				throw new RuntimeException("Issue with executing query (syntax ?) for finding account");
			}

			try {
				rsj.next();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			
			String name="";
			try 
			{
				name = rsj.getString("currjoueur");
			
				boolean connected = rsj.getBoolean("connected");
				if(connected)
					throw new RuntimeException("Account already connected");

				if(name.isEmpty())
					throw new RuntimeException("No player for account "+ndc);
			} 
			catch (SQLException e) {
				c.sendToClient("c;CONNECT_FAILED;INCORRECT_NDC_PASS");
				throw new RuntimeException("Connection failed : information set doesn't return any account");
			}
			
			try {
				rsj.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				stmt.executeUpdate("UPDATE Account SET connected=true WHERE nom_de_compte='"+ndc+"'");
			} catch (SQLException e) {
				throw new RuntimeException("Finding player statement's creation failed");
			}
			
			ResultSet rsp;
			try {
				rsp = stmt.executeQuery("SELECT race,classe,posx,posy,orientation " +
						"FROM personnage " +
						"WHERE name='"+name+"'");
			} catch (SQLException e) {
				throw new RuntimeException("Issue with executing query (syntax ?) for finding player");
			}
			
			try {
				rsp.next();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String race =null, classe=null, ori=null;
			int posx=0, posy=0;
			try {
				race = rsp.getString("race");
				classe = rsp.getString("classe");
				posx = rsp.getInt("posx");
				posy = rsp.getInt("posy");
				ori = rsp.getString("orientation");
			} catch (SQLException e) {
				throw new RuntimeException("Connection failed : information set doesn't return any player");
			}

			c.sendToClient("c;CONNECT_SUCCEED");
			c.setCompte(new Account(ndc,mdp));
			c.getCompte().setCurrent_joueur(new Joueur(new Personnage(name), MapManager.instance.getEntire_map().getGrille().get((int)posx).get((int)posy), Orientation.BAS));
			c.getCompte().getCurrent_joueur().getPerso().getRace().setNom(race);
			c.getCompte().getCurrent_joueur().getPerso().getClasse().setNom(classe);
			EntitiesManager.instance.getPlayers_manager().addNewPlayer(c.getCompte().getCurrent_joueur());
			
			c.sendToClient("ci;"+name+";"+race+";"+classe+";"+posx+";"+posy+";"+ori);
			ServerSingleton.getInstance().sendAllClient("lo;ent;j"+name+";"+race+";"+classe+";"+posx+";"+posy+";"+ori);
		
			try {
				rsp.close();
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
}
