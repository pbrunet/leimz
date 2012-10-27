package com.server.core;

import java.io.IOException;


import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;


import com.server.core.functions.AttackFunction;
import com.server.core.functions.CombatFunction;
import com.server.core.functions.ConnectFunction;
import com.server.core.functions.Functionable;
import com.server.core.functions.InfoFunction;
import com.server.core.functions.LoadFunction;
import com.server.core.functions.SayFunction;
import com.server.core.functions.StateFunction;

public class Calculator implements Runnable
{
	private static HashMap <String,Functionable> dictfunctions = new HashMap<String,Functionable>();
	private Thread t;

	public Calculator()
	{

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
		dictfunctions.put("i",new InfoFunction());
		dictfunctions.put("c",new ConnectFunction());
		dictfunctions.put("sa",new SayFunction());
		dictfunctions.put("lo",new LoadFunction());
		dictfunctions.put("co",new CombatFunction());
		dictfunctions.put("a",new AttackFunction());

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
			try{
				f.doSomething(temp,source);
			}
			catch(RuntimeException e){
				source.sendToClient("REQUEST_FAIL");
				e.printStackTrace();
				ServerSingleton.getInstance().deconnexion(source);
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
