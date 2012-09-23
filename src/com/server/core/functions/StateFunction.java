package com.server.core.functions;

import java.sql.ResultSet;
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
						"SET pos='"+args[2]+";"+args[3]+"'");

				String toSend = "s;";
				ResultSet rss = stmt.executeQuery("SELECT personnage.name, personnage.pos " +
						"FROM personnage " +
						"WHERE joueur=" + client.getCompte().getClient_id());

				while(rss.next())
				{
					toSend += (rss.getString("name")+";");
					String[] s = rss.getString("pos").split(";");
					toSend += (s[0]+";");
					toSend += (s[1]+";");
				}
				client.sendToClient(toSend);
				rss.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
