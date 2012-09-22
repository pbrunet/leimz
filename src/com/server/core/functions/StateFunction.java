package com.server.core.functions;

import java.sql.ResultSet;
import java.sql.SQLException;

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
				ServerSingleton.getInstance().getDbConnexion().getStmt().executeUpdate("" +
						"UPDATE personnage SET pos='"+args[2]+";"+args[3]+"'");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}


			String toSend = "s;";
			ResultSet rss = null;
			try {
				rss = ServerSingleton.getInstance().getDbConnexion().getStmt().executeQuery("SELECT * from personnage where joueur=" + client.getCompte().getClient_id());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				while(rss.next())
				{
					toSend += (rss.getString("name")+";");
					String[] s = rss.getString("pos").split(";");
					toSend += (s[0]+";");
					toSend += (s[1]+";");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			client.sendToClient(toSend);
		}
	}
}
