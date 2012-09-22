package com.server.core;

import com.map.Grille;
import com.map.Map;
import com.map.Tile;
import com.map.Type_tile;
import com.map.server.managers.MapManager;
import com.server.db.DBConnection;
import com.server.misc.Logging;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.loading.LoadingList;


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

	private Calculator calculator;

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

	private Map loadMap()
	{
		org.jdom.Document doc_map = null, doc_types = null;
		Element racine_maps, racine_types;
		Grille grille;
		SAXBuilder sxb = new SAXBuilder();
		try
		{
			//On cree un nouveau document JDOM avec en argument le fichier XML
			//Le parsing est termine ;)
			doc_map = sxb.build(new File("data/Maps/map_constance.xml"));
			doc_types = sxb.build(new File("data/Maps/types_tiles.xml"));
		}
		catch(Exception e){
			System.out.println("Erreur de parsing XML de la carte");
		}

		//On initialise un nouvel element racine avec l'element racine du document.
		racine_maps = doc_map.getRootElement();
		racine_types = doc_types.getRootElement();

		grille = new Grille();

		Element[] calques_e = new Element[racine_maps.getChildren("calque").size()];

		for(int i = 0; i < racine_maps.getChildren("calque").size(); i++)
			calques_e[i] = (Element) racine_maps.getChildren("calque").get(i);

		for(int i = 0; i < (Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(calques_e[0].getChildren("tile").size()-1)).getChild("id_x").getText())+1); i++)
		{
			grille.add(new ArrayList<Tile>());
			for(int j = 0; j < (Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(calques_e[0].getChildren("tile").size()-1)).getChild("id_y").getText())+1); j++)
				grille.get(i).add(new Tile(new Vector2f(i , j), null));
		}

		for(int u = 0; u < calques_e[0].getChildren("tile").size(); u++)
		{

			if( grille.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_x").getText()))
					.get( Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_y").getText())).getPos().x
					== Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_x").getText())
					&&
					grille.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_x").getText()))
					.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_y").getText())).getPos().y
					== Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_y").getText())
					)
			{
				for(int k = 0; k < racine_types.getChild("calque1").getChildren("type").size(); k ++)
				{
					if(((Element) calques_e[0].getChildren("tile").get(u)).getChild("type").getText()
							.equals(((Element)racine_types.getChild("calque1").getChildren("type").get(k)).getChild("nom").getText()))
					{
						String[] str_n = ((Element)racine_types.getChild("calque1").getChildren("type").get(k)).getChild("base").getText().split(",");
						Rectangle base = new Rectangle(Integer.parseInt(str_n[0]), Integer.parseInt(str_n[1]),80,40);

						try {
							grille.get(	Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_x").getText()))
							.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_y").getText()))
							.getTypes().add(new Type_tile(
									((Element)racine_types.getChild("calque1").getChildren("type").get(k)).getChild("nom").getText(),
									base
									, Boolean.getBoolean(((Element)racine_types.getChild("calque1").getChildren("type").get(k)).getChild("collidable").getText())
									,1));
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} 
					}
				}

				grille.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_x").getText()))
				.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_y").getText()))
				.setMonsterHolder( Boolean.parseBoolean(((Element) calques_e[0].getChildren("tile").get(u)).getChild("monsterHolder").getText()));
			}
		}

		LoadingList.setDeferredLoading(true);

		for(int u = 0; u < calques_e[1].getChildren().size(); u++)
		{
			if(((Element) calques_e[1].getChildren().get(u)).getText().equals("tile"))
			{
				for(int k = 0; k < racine_types.getChild("calque2").getChildren("type").size(); k ++)
				{
					if(((Element) calques_e[1].getChildren("tile").get(u)).getChild("type").getText()
							.equals(((Element)racine_types.getChild("calque2").getChildren("type").get(k)).getChild("nom").getText()))
					{
						String[] str_n = ((Element)racine_types.getChild("calque2").getChildren("type").get(k)).getChild("base").getText().split(",");
						Rectangle base = new Rectangle(Integer.parseInt(str_n[0]), Integer.parseInt(str_n[1]),80,40);

						try {
							grille.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_x").getText()))
							.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_y").getText()))
							.getTypes().add(new Type_tile(
									((Element)racine_types.getChild("calque2").getChildren("type").get(k)).getChild("nom").getText(),
									base
									, Boolean.getBoolean(((Element)racine_types.getChild("calque2").getChildren("type").get(k)).getChild("collidable").getText())
									,2));
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} 
					}
				}
			}
			else if(((Element) calques_e[1].getChildren().get(u)).getText().equals("group_tiles"))
			{
				for(int k = 0; k < racine_types.getChild("calque2").getChildren("type").size(); k ++)
				{
					if(((Element) calques_e[1].getChildren().get(u)).getChild("type").getText()
							.equals(((Element)racine_types.getChild("calque2").getChildren("type").get(k)).getChild("nom").getText()))
					{
						String[] str_n = ((Element)racine_types.getChild("calque2").getChildren("type").get(k)).getChild("base").getText().split(",");
						Rectangle base = new Rectangle(Integer.parseInt(str_n[0]), Integer.parseInt(str_n[1]),Integer.parseInt(str_n[2]),Integer.parseInt(str_n[3]));

						try {

							if(((Element)racine_types.getChild("calque2").getChildren("type").get(k)).getAttributes().size() > 0)
							{
								grille.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_x").getText()))
								.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_y").getText()))
								.getTypes().add(new Type_tile(
										((Element)racine_types.getChild("calque2").getChildren("type").get(k)).getChild("nom").getText(),
										base
										, Boolean.getBoolean(((Element)racine_types.getChild("calque2").getChildren("type").get(k)).getChild("collidable").getText())
										,2
										, Boolean.getBoolean(((Element)racine_types.getChild("calque2").getChildren("type").get(k)).getAttributeValue("multiTiles"))));
							}
							else
							{
								grille.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_x").getText()))
								.get(Integer.parseInt(((Element) calques_e[0].getChildren("tile").get(u)).getChild("id_y").getText()))
								.getTypes().add(new Type_tile(
										((Element)racine_types.getChild("calque2").getChildren("type").get(k)).getChild("nom").getText(),
										base
										, Boolean.getBoolean(((Element)racine_types.getChild("calque2").getChildren("type").get(k)).getChild("collidable").getText())
										,2));
							}
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		ArrayList<String> monstres = new ArrayList<String>();
		monstres.add("bouftou");
		monstres.add("tofu");

		Map map = new Map(grille, monstres);

		return map;
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
		Map entire_map = loadMap();
		MapManager mapManager = new MapManager(entire_map);
		//world = new World(entire_map);

		calculator = new Calculator(mapManager, cl);
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
		calculator.setCl(cl);
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
}