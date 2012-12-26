package com.server.core;

import java.util.ArrayList;
import com.server.core.ClientList;

public class ClientsManager 
{
	private ArrayList<Client> clients;
	private ArrayList<ClientList> lists;
	
	public static ClientsManager instance;
	
	public ClientsManager()
	{
		instance = this;
		this.clients = new ArrayList<Client>();
		this.lists = new ArrayList<ClientList>();
	}

	public ClientList findProperList()
	{
		ClientList toreturn = null;
		for(ClientList c : lists)
		{
			// Si le nombre de client présent dans une liste est inférieur au nombre maximum de client par thread.On se prépare à retourner la liste
			if(c.getNumberClient()<100)
				toreturn=c;
		}
		// Si aucune liste n'est prêt à etre retourné on crée une liste.
		if(toreturn == null)
		{
			toreturn = new ClientList();
			this.lists.add(toreturn);
		}
		return toreturn;
	}
	
	public void removeClient(Client client)
	{
		for(int i = 0; i < lists.size(); i++)
		{
			if(lists.get(i).getClients().contains(client))
			{
				lists.get(i).getClients().remove(client);
				return;
			}
		}
	}

	private void refreshClients() 
	{
		clients.removeAll(clients);
		for(int i = 0; i < lists.size(); i++)
		{
			for(int j = 0; j < lists.get(i).getClients().size(); j++)
			{
				clients.add(lists.get(i).getClients().get(j));
			}
		}
	}
	
	public Client getClient(String nompersonnage)
	{
		Client cli = null ;
		for(int i =0; i<this.lists.size() && cli == null;i++)
		{
			for(int j = 0;j<this.lists.get(i).getClients().size() && cli == null ;j++)
			{
				if(this.lists.get(i).getClients().get(j).getCompte().getCurrent_joueur().getPerso().getNom().equals(nompersonnage))
				{
					cli = this.lists.get(i).getClients().get(j);
				}
			}
		}
		return cli;	
	}
	
	public ArrayList<Client> getClients() {
		refreshClients();
		return clients;
	}

	public void setClients(ArrayList<Client> clients) {
		this.clients = clients;
	}

	public ArrayList<ClientList> getLists() {
		return lists;
	}

	public void setLists(ArrayList<ClientList> lists) {
		this.lists = lists;
	}
}
