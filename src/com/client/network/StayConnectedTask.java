/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.network;

import java.io.PrintWriter;
import java.util.TimerTask;

/**
 *
 * @author kratisto
 */
public class StayConnectedTask extends TimerTask
{

    PrintWriter pw;
    
    
    public StayConnectedTask(PrintWriter ec)
    {
        pw = ec;
    }
    
    @Override
    public void run() 
    {
        pw.println("none");
        pw.flush();
    }
    
}
