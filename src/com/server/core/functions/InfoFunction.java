package com.server.core.functions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.server.core.Client;
import com.server.core.ServerSingleton;

/**
 * Write a description of class InvitFunction here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class InfoFunction implements Functionable
{
	public InfoFunction()
	{
	}

	@Override
	public void doSomething(String[] args,Client client)
	{
		ResultSet rsp = null;
		try {
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rsp = stmt.executeQuery("SELECT classe, race " +
					"FROM personnage " +
					"WHERE name='"+args[1]+"'");

			rsp.next();
			String race = rsp.getString("race");
			String classe = rsp.getString("classe");
			rsp.close();
			client.sendToClient("i;"+args[1]+";"+race+";"+classe);

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}