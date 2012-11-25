package com.server.core;

import java.util.ArrayList;
import java.util.concurrent.Callable;
/**
 * Classe représentant une liste de client. La liste est de taille variable
 * @author kratisto
 */
public class ClientList implements Callable<Void> 
{
	private ArrayList<Client> clients;
	/**
	 * Constructeur de la classe
	 **/
	public ClientList()
	{
		super();
		clients = new ArrayList<Client>();

	}
	/**
	 * Ajout d'un client
	 * @param Client le client à ajouter
	 * @see Client
	 **/
	public void add(Client c)
	{
		c.sendToClient("co;CONNECT_SERVER");
		clients.add(c);
	}

	/**
	 * Le nombre de client connecte
	 * @return Le nombre de client connecte
	 **/
	public int getNumberClient()
	{
		return getClients().size();
	}

	/**
	 * fonction principale du thread
	 */
	/*
    @Override
    public Void call() throws IOException
    {

        //On boucle tout le temps
        while(true)
        {

            try
            {
                // Pour chaque client connecte
                for(int i = 0; i < clients.size(); i++)
                {
                    Client c = clients.get(i);
                    Calcul ca = new Calcul(c.receiveFromClient(), c, s);
                    pool.submit(ca);
                }
            }

            catch (ConcurrentModificationException e)
            {
                /*On eteint le serveur si on a une erreur de modification concurrente.
	 *Car ca voudra dire que j'ai du boulot
	 *//*
                ServerSingleton.getInstance().seeToServer(e.getMessage());
                l.fatal(e.getMessage());

            }
        }



    }*/

	/**
	 * Envoie un message à tous les clients connectés.
	 * @param message Le message a envoyé
	 */
	public void sendAllClientList(String message) 
	{
		for(Client c: clients)
		{
			c.sendToClient(message);
		}
	}

	/**
	 * @return the client
	 */
	public ArrayList<Client> getClients() {
		return clients;
	}
	@Override
	public Void call() throws Exception {
		return null;
	}
}
