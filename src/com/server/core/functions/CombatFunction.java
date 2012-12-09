package com.server.core.functions;

import com.gameplay.Caracteristique;
import com.server.core.Client;
import com.server.core.ServerSingleton;

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
		System.out.println("answer asking");
		Client receiver = null;
		for(int i = 0; i < ServerSingleton.getInstance().getCl().size(); i++)
		{
			for(int j = 0; j < ServerSingleton.getInstance().getCl().get(i).getClients().size(); j++)
			{
				System.out.println(ServerSingleton.getInstance().getCl().get(i).getClients().get(j).getCompte().getCurrent_joueur().getPerso().getNom());
				if(ServerSingleton.getInstance().getCl().get(i).getClients().get(j).getCompte().getCurrent_joueur().getPerso().getNom().equals(args[3]))
				{
					receiver = ServerSingleton.getInstance().getCl().get(i).getClients().get(j);
					System.out.println("Client trouvé !");
				}
			}
		}
		
		if(args[2].equals("y"))
		{
			receiver.sendToClient("fi;an;y;"+c.getCompte().getCurrent_joueur().getPerso().getNom());
			receiver.sendToClient("s;"+c.getCompte().getCurrent_joueur().getPerso().getNom()+";vie;"+c.getCompte().getCurrent_joueur().getPerso().getCaracs().get(Caracteristique.VIE));
			c.sendToClient("s;"+receiver.getCompte().getCurrent_joueur().getPerso().getNom()+";vie;"+receiver.getCompte().getCurrent_joueur().getPerso().getCaracs().get(Caracteristique.VIE));
		}
		else if(args[2].equals("n"))
		{
			receiver.sendToClient("fi;an;n;"+c.getCompte().getCurrent_joueur().getPerso().getNom());
		}
		
	}

	private void askingFunction(Client c, String[] args)
	{
		System.out.println("asking");
		Client receiver = null;
		for(int i = 0; i < ServerSingleton.getInstance().getCl().size(); i++)
		{
			for(int j = 0; j < ServerSingleton.getInstance().getCl().get(i).getClients().size(); j++)
			{
				System.out.println(ServerSingleton.getInstance().getCl().get(i).getClients().get(j).getCompte().getCurrent_joueur().getPerso().getNom());
				if(ServerSingleton.getInstance().getCl().get(i).getClients().get(j).getCompte().getCurrent_joueur().getPerso().getNom().equals(args[2]))
				{
					receiver = ServerSingleton.getInstance().getCl().get(i).getClients().get(j);
					System.out.println("Client trouvé !");
				}
			}
		}
		
		receiver.sendToClient("fi;ask;"+c.getCompte().getCurrent_joueur().getPerso().getNom());
	}
	
	
	private void cancelAskingFunction(Client c, String[] args)
	{
		System.out.println("cancel asking");
		Client receiver = null;
		for(int i = 0; i < ServerSingleton.getInstance().getCl().size(); i++)
		{
			for(int j = 0; j < ServerSingleton.getInstance().getCl().get(i).getClients().size(); j++)
			{
				System.out.println(ServerSingleton.getInstance().getCl().get(i).getClients().get(j).getCompte().getCurrent_joueur().getPerso().getNom());
				if(ServerSingleton.getInstance().getCl().get(i).getClients().get(j).getCompte().getCurrent_joueur().getPerso().getNom().equals(args[2]))
				{
					receiver = ServerSingleton.getInstance().getCl().get(i).getClients().get(j);
					System.out.println("Client trouvé !");
				}
			}
		}
		
		receiver.sendToClient("fi;can;"+c.getCompte().getCurrent_joueur().getPerso().getNom());
	}

}
