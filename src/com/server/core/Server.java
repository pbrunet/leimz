package com.server.core;

import com.server.db.DBConnection;
import com.server.misc.Logging;

import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;

import java.sql.SQLException;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

/**
 * Classe du serveur! La connexion d'un client se passe ici
 * @author kratisto
 */
public class Server
{
	private ExecutorService pool = Executors.newCachedThreadPool();
	private ServerSocket ss = null;
	private List<ClientList> cl = new CopyOnWriteArrayList<ClientList>();
	private int nbclients = 0;
	private int maxClientperThreads = 100;

	private DBConnection dbConnexion;
	private Logger l = Logging.getLogger(Server.class);


	/* private World world;
    private EntitiesManager entities_manager;*/

	public void start()
	{
		//   ess = new IHM(this);
		// ess.setVisible(true);
		//On initialise le serveur
		initServer();
		//On lance le gestionnaire de commandes
		pool.submit(new Commandes());
		try {
			dbConnexion = new DBConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//On attend la connexion des joueurs*/
		waitPlayers();
	}

	/**
	 * Fonction d'initialisation du socket serveur
	 */
	public void seeToServer(String message) 
	{
		System.out.println(message);
	}
	private void initServer()
	{

		seeToServer("Lancement du serveur de Leimz");
		seeToServer("-----------");
		try
		{
			// On lance le socket
			ss = new ServerSocket(17000);
			seeToServer("Lancement reussi");
		}
		catch (Exception e)
		{
			//On informe de l'échec et on affiche l'erreur et on eteint le serveur
			seeToServer("Lancement echoue");
			l.fatal(e.getMessage());
			System.exit(0);
		}
		//world = new World(entire_map);
	}


	private void waitPlayers()
	{
		Socket player;
		while (true) // attente en boucle de connexion (bloquant sur ss.accept)
		{
			try
			{
				player = ss.accept();
				//On cherche a mettre le client

				ClientList clientl = cltouse();
				//On l'ajoute
				clientl.add(new Client(cl.size()-1,player));
				nbclients++;
				seeToServer("Joueur connecte. Actuellement il y a "+this.getNbclients()+" joueur");
				//On incremente le compteur du nombre de joueur connecté

			}
			catch (IOException ex)
			{
				//On affiche l'erreur
				seeToServer(ex.toString());
				l.error(ex.getMessage());
			}
			catch( Exception e)
			{
				//On affiche l'exception
				seeToServer(e.toString());
				l.error(e.getMessage());
			}

		}

	}
	/**
	 * Fonction nettoyant une liste de client ( la vide entièrement)
	 */
	private void cleanList()
	{
		for(ClientList c : getCl())
		{
			if(c.getNumberClient()==0)
				getCl().remove(c);
		}
	}

	/**
	 *Envoie un message à tous les clients
	 * @param message Message à envoyer
	 */
	public void sendToClient(List<Client> c,String message)
	{
		for(Client client : c)
		{
			client.sendToClient(message);
		}
	}
	public void sendAllClient(String message)
	{
		for(ClientList c:getCl())
		{
			c.sendAllClientList(message);
		}
	}

	/**
	 * On cherche le thread où on pourrait mettre le client
	 * @return Le clientlist pouvant contenir le client
	 * @see ClientList
	 */
	private ClientList cltouse()
	{
		ClientList toreturn = null;
		for(ClientList c : getCl())
		{
			// Si le nombre de client présent dans une liste est inférieur au nombre maximum de client par thread.On se prépare à retourner la liste
			if(c.getNumberClient()<this.getMaxClientperThreads())
				toreturn=c;
		}
		// Si aucune liste n'est prêt à etre retourné on créait une liste.
		if(toreturn == null)
		{
			toreturn = new ClientList();
			this.cl.add(toreturn);
			pool.submit(toreturn);
		}
		return toreturn;
	}

	public void deconnexion(Client client) 
	{
		nbclients --;
		cleanClient();
		cleanList();
		seeToServer("Joueur deconnecte! Actuellement il y a "+this.getNbclients()+" joueur");
	}

	public void save() {
		//      ess.jTextArea1.append("Not yet implemented\n");
	}

	/**
	 * @return the cl
	 */
	public List<ClientList> getCl() {
		return Collections.synchronizedList(cl);
	}

	/**
	 * @return the nbclients
	 */
	private int getNbclients()
	{
		return nbclients;
	}

	/**
	 * @return the maxClientperThreads
	 */
	public int getMaxClientperThreads() {
		return maxClientperThreads;
	}

	/**
	 * @param maxClientperThreads the maxClientperThreads to set
	 */
	public void setMaxClientperThreads(int maxClientperThreads) {
		this.maxClientperThreads = maxClientperThreads;
	}

	void removeACL(ClientList aThis) 
	{
		this.cl.remove(aThis);
	}

	public DBConnection getDbConnexion() {
		return dbConnexion;
	}
	public void setDbConnexion(DBConnection dbConnexion) {
		this.dbConnexion = dbConnexion;
	}

	private void cleanClient() {
		for(ClientList cl : this.cl)
		{
			for(Client c : cl.getClients())
			{
				if(c.getS().isClosed())
				{
					cl.getClients().remove(c);
				}
			}
		}
	}



	public Client getClient(String nompersonnage) {
		Client cli = null ;
		for(int i =0; i<this.cl.size() && cli == null;i++)
		{
			for(int j = 0;j<this.cl.get(i).getClients().size() && cli == null ;j++)
			{
				if(this.cl.get(i).getClients().get(j).getCompte().getCurrent_joueur().getPerso().getNom().equals(nompersonnage))
				{
					cli = this.cl.get(i).getClients().get(j);
				}
					
			}
		}
		return cli;	
	}

	public List<Client> getClientnear(double posx, double posy) {
		//ArrayList<Client> listCli = new ArrayList<Client>();
		
		return null;
	}

}