package com.server.core.functions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import com.gameplay.Caracteristique;
import com.server.core.Client;
import com.server.core.ServerSingleton;
import com.server.core.functions.Functionable;

/**
 * Fonction d'attaque
 * 
 * @author Kratisto
 * @version 20/03/2012
 */
public class AttackFunction implements Functionable
{
    
    public AttackFunction()
    {
        
    }

	@Override
	public void doSomething(String[] args, Client c) {
		
		ResultSet rsp = null;
		try {
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			rsp = stmt.executeQuery("SELECT Sorts_classe.value_min, Sorts_race.value_min, Sorts_race.value_max, Sorts_classe.value_max " +
					"FROM Sorts_classe, Sorts_race, classe_sort, race_sort, personnage " +
					"WHERE personnage.name=\""+c.getCompte().getCurrent_joueur().getPerso().getNom()+"\""+
					" AND (sorts_classe.nom=\""+args[2]+"\" OR sorts_race.nom=\""+args[2]+"\") "+
					" AND race_sort.id_race=personnage.race"+
					" AND classe_sort.id_classe=personnage.classe");
			
			int value_min = 0, value_max = 0;
			while(rsp.next())
			{
				value_min = rsp.getInt("value_min");
				value_max = rsp.getInt("value_max");
			}
			
			Random random = new Random();
			int degats = value_min+random.nextInt(value_max-value_min);
			System.out.println("Degats : "+degats);
			
			int vie = c.getCompte().getCurrent_joueur().getPerso().getCaracs().get(Caracteristique.VIE)-degats;
			c.getCompte().getCurrent_joueur().getPerso().getCaracs().put(Caracteristique.VIE, vie);
			
			rsp.close();
			c.sendToClient("a;"+args[2]+";"+degats);
			ServerSingleton.getInstance().sendAllClient("s;"+c.getCompte().getCurrent_joueur().getPerso().getNom()+";"+"vie;"+vie);

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
