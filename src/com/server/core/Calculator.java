package com.server.core;

import java.util.HashMap;
import java.util.concurrent.Callable;

import com.server.core.functions.ConnectFunction;
import com.server.core.functions.Functionable;
import com.server.core.functions.LoadFunction;
import com.server.core.functions.SayFunction;
import com.server.core.functions.StateFunction;

public class Calculator implements Callable<Void>
{
	private static HashMap <String,Functionable> dictfunctions = new HashMap<String,Functionable>();
	private Client cli;
	private String mess;

	public Calculator(String receiveFromClient, Client c) {
	

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


}
