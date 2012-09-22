package com.server.core; 
import com.server.misc.Logging;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import org.apache.log4j.Logger;

/**
 *
 * @author kratisto
 */
public class Commandes implements Callable<Void> {
    private Logger l = Logging.getLogger(Commandes.class);
    /**
     *
     */
    public BufferedReader in;
    /**
     *
     */
    public Commandes()
        {
       
            in = new BufferedReader(new InputStreamReader(System.in));
	}
     /**
     *
     */
    @Override
    public Void call()  {
            String commande;
		try
		{
			while ((commande = in.readLine())!=null)
			{
				if (commande.equalsIgnoreCase("quit")) // commande "quit" detect�e ...
					System.exit(0); // ... on ferme alors le serveur
                        }
		}
		catch (Exception e) {
                    l.warn(e.getMessage());
                    
                }
                return null;
                
	}

}
