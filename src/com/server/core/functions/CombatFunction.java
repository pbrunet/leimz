package com.server.core.functions;

import com.gameplay.Caracteristique;
import com.server.core.Client;
import com.server.core.ClientsManager;

public class CombatFunction implements Functionable
{
	
	public CombatFunction()
	{
		
	}
	
	@Override
	public void doSomething(String[] args, Client c) 
	{
		System.out.println("entree dans la fonction combat");
		
		switch(args[1])
		{
		case "ask":
			askingFunction(c, args);
			break;
		case "can":
			cancelAskingFunction(c, args);
			break;
		case "an":
			answerAskingFunction(c, args);
			break;
		default:
			throw new RuntimeException ("Unimplemented");
		}
		
	}

	private void answerAskingFunction(Client c, String[] args) 
	{
		Client receiver = ClientsManager.instance.getClient(args[3]);
		
		if(args[2].equals("y"))
		{
			receiver.sendToClient("s;j;"+c.getCompte().getCurrent_joueur().getPerso().getNom()+";vie;"+c.getCompte().getCurrent_joueur().getPerso().getCaracs_values().get(Caracteristique.VIE));
			c.sendToClient("s;j;"+receiver.getCompte().getCurrent_joueur().getPerso().getNom()+";vie;"+receiver.getCompte().getCurrent_joueur().getPerso().getCaracs_values().get(Caracteristique.VIE));
			receiver.sendToClient("fi;an;y;"+c.getCompte().getCurrent_joueur().getPerso().getNom());
		}
		else if(args[2].equals("n"))
		{
			receiver.sendToClient("fi;an;n;"+c.getCompte().getCurrent_joueur().getPerso().getNom());
		}
		
	}

	private void askingFunction(Client c, String[] args)
	{
		Client receiver = ClientsManager.instance.getClient(args[2]);
		
		receiver.sendToClient("fi;ask;"+c.getCompte().getCurrent_joueur().getPerso().getNom());
	}
	
	
	private void cancelAskingFunction(Client c, String[] args)
	{
		Client receiver = ClientsManager.instance.getClient(args[2]);
		
		receiver.sendToClient("fi;can;"+c.getCompte().getCurrent_joueur().getPerso().getNom());
	}

}
