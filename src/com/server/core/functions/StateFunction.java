package com.server.core.functions;

import java.sql.SQLException;

import java.sql.Statement;
import org.newdawn.slick.geom.Vector2f;
import com.server.entities.Joueur;
import com.map.Tile;
import com.map.server.managers.MapManager;
import com.server.core.Calculator;
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
				Tile tile = MapManager.instance.getTileReal(new Vector2f(Float.parseFloat(args[2]), Float.parseFloat(args[3])));
				if(tile != client.getCompte().getCurrent_joueur().getTile())
				{
					String[] loargs = new String[2];
					loargs[0]="lo";
					loargs[1]="ent";
					Calculator.dictfunctions.get("lo").doSomething(loargs, client);
				}
				Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
				stmt.executeUpdate("UPDATE personnage " +
						" SET posx="+tile.getPos().x+" , posy="+tile.getPos().y+
						"WHERE name='"+client.getCompte().getCurrent_joueur().getPerso().getNom()+"'");
				client.getCompte().getCurrent_joueur().setPos_real(new Vector2f(Float.parseFloat(args[2]), Float.parseFloat(args[3])));
				client.getCompte().getCurrent_joueur().setTile(MapManager.instance.getTileReal(new Vector2f(Float.parseFloat(args[2]), Float.parseFloat(args[3]))));
				client.getCompte().getCurrent_joueur().setOrientation(Joueur.parseStringOrientation(args[4]));
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else if(args[1].equals("vie"))
		{
			//Check
		}
	}
}
