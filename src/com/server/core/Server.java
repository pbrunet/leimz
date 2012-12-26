package com.server.core;

import com.server.db.DBConnection;
import com.server.entities.managers.EntitiesManager;
import com.server.misc.Logging;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
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

	private DBConnection dbConnexion;
	private Logger l = Logging.getLogger(Server.class);


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
		@SuppressWarnings("unused")
		Load load = new Load();
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
		seeToServer("");
		
		new ClientsManager();
		new Calculator();
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

				ClientList clientl = ClientsManager.instance.findProperList();
				//On l'ajoute
				clientl.add(new Client(ClientsManager.instance.getLists().size(),player));

				seeToServer("Joueur connecte. Actuellement il y a "+ClientsManager.instance.getClients().size()+" joueur");
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
	 *Envoie un message à tous les clients
	 * @param message Message à envoyer
	 */
	public void sendToClients(List<Client> c,String message)
	{
		for(Client client : c)
		{
			client.sendToClient(message);
		}
	}
	public void sendAllClient(String message)
	{
		for(ClientList c: ClientsManager.instance.getLists())
		{
			c.sendAllClientList(message);
		}
	}


	public void deconnexion(Client client) 
	{
		try {
			Statement stmt = ServerSingleton.getInstance().getDbConnexion().getConnexion().createStatement();
			String sql = "UPDATE account SET connected=false WHERE nom_de_compte='"+client.getCompte().getName()+"'";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		EntitiesManager.instance.getPlayers_manager().getJoueurs().remove(client.getCompte().getCurrent_joueur());
		ClientsManager.instance.removeClient(client);
		seeToServer("Joueur deconnecte! Actuellement il y a "+ClientsManager.instance.getClients().size()+" joueur");
	}

	public DBConnection getDbConnexion() {
		return dbConnexion;
	}
	
	public void setDbConnexion(DBConnection dbConnexion) {
		this.dbConnexion = dbConnexion;
	}
}