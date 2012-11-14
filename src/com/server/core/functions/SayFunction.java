package com.server.core.functions;

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
       ServerSingleton.getInstance().sendAllClient("sa;"+args[1]+";"+args[2]);
    }
}
