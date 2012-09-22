package com.game_entities.server.managers;

import java.util.ArrayList;

import com.server.core.Client;

public class ClientsManager 
{
	private ArrayList<Client> clients;
	
	public ClientsManager(ArrayList<Client> clients)
	{
		this.clients = clients;
	}
	
	/*public void refresh()
	{
		for(int i = 0; i < clients.size(); i++)
		{
			joueurs.get(i).refresh();
		}
	}*/

	public void addNewClient(Client client)
	{
		this.clients.add(client);
	}

	public ArrayList<Client> getClients() {
		return clients;
	}

	public void setClients(ArrayList<Client> clients) {
		this.clients = clients;
	}
}
