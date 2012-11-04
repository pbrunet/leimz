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
