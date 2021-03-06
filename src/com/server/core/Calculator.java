package com.server.core;

import java.io.IOException;


import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

import com.map.server.managers.MapManager;
import com.server.core.functions.ConnectFunction;
import com.server.core.functions.Functionable;
import com.server.core.functions.InfoFunction;
import com.server.core.functions.LoadFunction;
import com.server.core.functions.SayFunction;
import com.server.core.functions.StateFunction;

public class Calculator implements Runnable
{
    private static HashMap <String,Functionable> dictfunctions = new HashMap<String,Functionable>();
    private List<ClientList> cl;
    private Thread t;
    
   public Calculator(MapManager mapManager, List<ClientList> cl)
   {
	   this.cl = cl;
	   
       //On ajoute les fonctions
     /* dictfunctions.put("s",new StateFunction());
      dictfunctions.put("a",new AttackFunction());
      dictfunctions.put("l",new LootFunction());
      dictfunctions.put("t",new TakeFunction());
      dictfunctions.put("m",new MoveFunction());
      dictfunctions.put("i",new InfoFunction());
      dictfunctions.put("o",new InvitFunction());
      dictfunctions.put("r",new ResultInvitFunction());
      dictfunctions.put("c",new ConnectFunction());
      dictfunctions.put("gl",new ListInvitFunction());
      dictfunctions.put("sa",new SayFunction());*/
	   
	   dictfunctions.put("s",new StateFunction());
	   dictfunctions.put("i",new InfoFunction());
	   dictfunctions.put("c",new ConnectFunction());
	   dictfunctions.put("sa",new SayFunction());
	   dictfunctions.put("lo",new LoadFunction());
	   
	   this.t = new Thread(this);
	   t.start();
   }
   
   public void submit(Client source, String mess)
   {
       
       if(mess!=null && !mess.equals("none") && !mess.isEmpty())
       {
           System.out.println("Reception du message :" + mess);
           String[] temp = mess.split(";");
           Functionable f = dictfunctions.get(temp[0]);
           ArrayList<String> tmp = f.doSomething(temp,source.getCompte().getClient_id());
           for(int i=0;i<tmp.size();i++)
           {
           		String s = tmp.get(i);
           		if(temp[0].equals("c") && s.matches("^[0-9]+$"))
           		{
           			source.getCompte().setClient_id(Integer.parseInt(s));
           		}
           		else
           		{
           			source.sendToClient(s) ;
	            	if(mess.equals("CONNECT_FAIL"))
	            		ServerSingleton.getInstance().deconnexion(source);
           		}
           }
       }
   }

   @Override
   public void run() 
   {
	  while(true)
	  {
		  try
          {
              // Pour chaque client connecte
              for(int i = 0; i < cl.size(); i++)
              {
            	  for(int j = 0; j < cl.get(i).getClients().size(); j++)
                  {
            		  Client c = cl.get(i).getClients().get(j);
                      try {
    					this.
    					submit(
    							c,
    							c.receiveFromClient());
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
                  }
                 
              }
          }

          catch (ConcurrentModificationException e)
          {
              /*On eteint le serveur si on a une erreur de modification concurrente.
              *Car ca voudra dire que j'ai du boulot
              */
              ServerSingleton.getInstance().seeToServer(e.getMessage());
              //l.fatal(e.getMessage());
          }
	  }
   }

   public List<ClientList> getCl() {
	   return cl;
   }

   public void setCl(List<ClientList> cl) {
	   this.cl = cl;
   }
}
