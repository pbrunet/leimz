package com.server.core.functions;

import com.server.core.Client;

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
       client.sendToClient("sa"+args[0]+";"+args[1]+";"+args[2]);
    }
}
