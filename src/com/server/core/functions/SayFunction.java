package com.server.core.functions;

import java.util.ArrayList;

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
    public ArrayList<String> doSomething(String[] args,int id)
    {
       ArrayList<String> mess = new ArrayList<String>();
       mess.add(args[0]+";"+args[1]+";"+args[2]);
		return mess;
    }
}
