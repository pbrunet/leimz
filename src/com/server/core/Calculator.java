package com.server.core;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import com.server.core.functions.AttackFunction;
import com.server.core.functions.CombatFunction;
import com.server.core.functions.ConnectFunction;
import com.server.core.functions.Functionable;
import com.server.core.functions.LoadFunction;
import com.server.core.functions.SayFunction;
import com.server.core.functions.StateFunction;

public class Calculator implements Callable<Void>
{
	public static HashMap <String,Functionable> dictfunctions = new HashMap<String,Functionable>();
	private Thread t;

	public Calculator()
	{

		cli = c;
		mess = receiveFromClient;
		//On ajoute les fonctions
		/*
      dictfunctions.put("a",new AttackFunction());
      dictfunctions.put("l",new LootFunction());
      dictfunctions.put("t",new TakeFunction());
      dictfunctions.put("m",new MoveFunction());
      dictfunctions.put("o",new InvitFunction());
      dictfunctions.put("r",new ResultInvitFunction());
      dictfunctions.put("gl",new ListInvitFunction());*/

		dictfunctions.put("s",new StateFunction());
		dictfunctions.put("c",new ConnectFunction());
		dictfunctions.put("sa",new SayFunction());
		dictfunctions.put("lo",new LoadFunction());
		dictfunctions.put("fi",new CombatFunction());
		dictfunctions.put("a",new AttackFunction());

	
	}

	public Void call()
	{

		if(mess!=null && !mess.equals("none") && !mess.isEmpty())
		{
			String[] temp = mess.split(";");
			Functionable f = dictfunctions.get(temp[0]);
			try{
				f.doSomething(temp,cli);
			}
			catch(Exception e){
				cli.sendToClient("REQUEST_FAIL");
				System.out.println("W : " + e.getMessage() + " request failed");
				ServerSingleton.getInstance().deconnexion(cli);
			}
		}
		return null;
	}

	@Override
	public void run() 
	{
		while(true)
		{
			try
			{
				// Pour chaque client connecte
				for(int i = 0; i < ServerSingleton.getInstance().getCl().size(); i++)
				{
					for(int j = 0; j < ServerSingleton.getInstance().getCl().get(i).getClients().size(); j++)
					{
						Client c = ServerSingleton.getInstance().getCl().get(i).getClients().get(j);
						try {
							this.submit(c,c.receiveFromClient());
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
}
