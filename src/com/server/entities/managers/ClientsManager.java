package com.server.entities.managers;

import java.util.ArrayList;

import com.server.core.Client;

public class ClientsManager 
{
	private ArrayList<Client> clients;
	
	public ClientsManager(ArrayList<Client> clients)
	{
		this.clients = clients;
	}
	
	public ClientsManager()
	{
		this.clients = new ArrayList<>();
	}

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
