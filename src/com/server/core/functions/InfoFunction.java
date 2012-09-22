package com.server.core.functions;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
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
	public ArrayList<String> doSomething(String[] args,int id)
	{
		ArrayList<String> answer = new ArrayList<String>();

		ResultSet rsp = null;
		try {
			rsp = ServerSingleton.getInstance().getDbConnexion().getStmt().executeQuery("select * from Personnage where name='"+args[1]+"'");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("premiere etape ok");
		int id_race = 0, id_classe = 0;
		try {
			while(rsp.next())
			{
				id_race = rsp.getInt("race");
				id_classe = rsp.getInt("classe");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		System.out.println("deuxieme etape ok");

		ResultSet rspr = null;
		try {
			rspr = ServerSingleton.getInstance().getDbConnexion().getStmt().executeQuery("select * from Race where id="+id_race);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String race = null;
		try {
			while(rspr.next())
			{
				race = rspr.getString("name");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		ResultSet rspc = null;
		try {
			rspc = ServerSingleton.getInstance().getDbConnexion().getStmt().executeQuery("select * from Classe where id="+id_classe);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String classe = null;
		try {
			while(rspc.next())
			{
				classe = rspc.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("troisieme etape ok");
		answer.add("i;"+args[1]+";"+race+";"+classe);

		System.out.println("infos envoyees !");

		return answer;
	}
}