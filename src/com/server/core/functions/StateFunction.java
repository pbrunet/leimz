package com.server.core.functions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.newdawn.slick.geom.Vector2f;

import com.game_entities.Joueur;
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
				client.getCompte().getCurrent_joueur().setPos_real(new Vector2f(Float.parseFloat(args[2]), Float.parseFloat(args[3])));
				client.getCompte().getCurrent_joueur().setOrientation(Joueur.parseStringOrientation(args[4]));
				
				String toSend = "s;";
				ResultSet rss = stmt.executeQuery("SELECT personnage.name, personnage.pos " +
						"FROM personnage " +
						"WHERE joueur=" + client.getCompte().getClient_id());

				while(rss.next())
				{
					toSend += (rss.getString("name")+";");
					toSend += ("pos;");
					String[] s = rss.getString("pos").split(";");
					toSend += (s[0]+";");
					toSend += (s[1]+";");
					toSend += (args[4]+";");
				}
				ServerSingleton.getInstance().sendAllClient(toSend);
				rss.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
