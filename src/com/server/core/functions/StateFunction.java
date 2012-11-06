package com.server.core.functions;

import java.sql.SQLException;
import java.sql.Statement;

import com.server.core.Client;
import com.server.core.ServerSingleton;


/**
 * Write a description of class StateFunction here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class StateFunction implements Functionable
{

	public StateFunction()
	{

	}

	@Override
	public void doSomething(String[] args, Client client)
	{
		
		if(args[1].equals("pos"))
		{
			try {

				Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
				stmt.executeUpdate("UPDATE personnage " +
						"SET posx='"+args[2]+"' , posy='"+args[3]+"' where id_compte='"+client.getCompte().getClient_id()+"'");
				
				String toSend = "s;";

					toSend += (client.getCompte().getCurrent_joueur().getPerso().getNom()+";");
					toSend += (args[2]+";");
					toSend += (args[3]+";");
		
				ServerSingleton.getInstance().sendAllClient(toSend);
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
