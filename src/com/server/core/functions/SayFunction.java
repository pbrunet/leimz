package com.server.core.functions;

import java.util.List;

import com.server.core.Client;
import com.server.core.ServerSingleton;

/**
 * Write a description of class SayFunction here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SayFunction implements Functionable
{
    
    public SayFunction()
    {
    	
    }
    
    @Override
    public void doSomething(String[] args,Client client)
    {
    	  //Format des messages envoyé aux clients s;types_com;nom_personnage_qui_parle;message
    	  //Types_com correspond aux channels de discussions ( mp , world ,region)
    	  //On parle à tout le monde
    	  if(args[2].equals("a"))	   
    		  ServerSingleton.getInstance().sendAllClient("sa"+args[0]+";"+args[1]+";"+client.getCompte().getCurrent_joueur().getPerso().getNom()+";"+args[2]);
    	  else
    		  //ON chuchotte
    		  if(args[2].equals("w"))
    			  ServerSingleton.getInstance().getClient(args[2]).sendToClient("sa"+args[0]+";"+args[1]+";"+client.getCompte().getCurrent_joueur().getPerso().getNom()+";"+args[2]);
    		  else
    			  ServerSingleton.getInstance().sendAllClient("sa"+args[0]+";"+client.getCompte().getCurrent_joueur().getPerso().getNom()+";"+args[2]);
    	  //TODO a finir
    	  //On parle a proximité
    	  if(args[1].equals("p"))
    	  {
    		  List<Client> listCli = ServerSingleton.getInstance().getClientnear(client.getCompte().getCurrent_joueur().getTile().getPos().x,client.getCompte().getCurrent_joueur().getTile().getPos().y);
    			  for(Client c : listCli)
    			  {
    				  c.sendToClient("sa"+args[0]+";"+args[1]+";"+client.getCompte().getCurrent_joueur().getPerso().getNom()+";"+args[2]);
    			  }
    	  }
    }
}
