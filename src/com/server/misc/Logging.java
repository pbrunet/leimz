/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.misc;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 *
 * @author 9104172P
 */
public class Logging {
    
    public static Logger getLogger(Class<?> c)
    {
        Logger logRoot = Logger.getLogger(c);
        FileAppender appender = null;
        try {
        appender = new FileAppender();
        StringBuilder motif = new StringBuilder();
        motif.append("Date/heure : %d{yyyy-MM-dd HH:mm:ss.SSS} %n");
        motif.append("Classe emettrice : %C %n");
        motif.append("Niveau : %p %n");
        motif.append("Localisation : %l %n");
        motif.append("Message: %m %n");
        motif.append("%n");
        PatternLayout layout = new PatternLayout(motif.toString());
        appender.setLayout(layout);
        appender.setFile("log"+c.getName());
        appender.activateOptions();
        logRoot.addAppender(appender);
        logRoot.setLevel(Level.DEBUG);
        } catch (Exception e) {
        e.printStackTrace();
        }
        return logRoot;
    }
    
}
