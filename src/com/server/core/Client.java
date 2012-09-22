package com.server.core ;

import com.server.misc.Logging;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import org.apache.log4j.Logger;

/**
 *  Classe qui correspond à un client ( en même temps vu le nom). Une instance est créé à chaque connection accepté par ServerSocket.
 *
 * @author kratisto
 */
public class Client {

	/**
	 * Le socket pour communiquer avec le client
	 * @see Socket 
	 */
	private Socket s;
	/**
	 *Objet permettant de lire ce que le client envoie
	 *@see BufferedReader
	 */
	private BufferedReader br;
	/**
	 * Objet permettant d'écrire au client
	 * @see PrintWriter
	 */
	private PrintWriter pw;
	/**
	 * Entier representant le temps de non réponse du client
	 */
	private int noresponse=0;
	private int acces;

	private int list;

	private  Logger l = Logging.getLogger(Client.class);
	private Account compte;

	/**
	 * Constructeur du client
	 * @param nbclients
	 * @param player
	 * @throws IOException
	 */
	public Client(int list, Socket player) throws IOException
	{
		this.list = list;
		s = player;
		br = new BufferedReader(new InputStreamReader(getS().getInputStream()));
		pw = new PrintWriter(getS().getOutputStream());
		compte = new Account();
	}

	public String receiveFromClient() throws IOException
	{
		String tmp = null;
		try 
		{
			this.getS().setSoTimeout(1);
			tmp = this.br.readLine();
			setNoresponse(0);
		} 

		catch (SocketTimeoutException ex) 
		{
			// Si on chope l'erreur comme quoi le socket n'a pas répondu
			if(this.getNoresponse()<1000)
			{
				//On incremente un compteur
				this.setNoresponse(this.getNoresponse() + 1);
			}
			else
			{
				this.disconnect();
			}
			//System.out.println(this.getNoresponse());
		}
		catch (IOException ex) 
		{
			this.disconnect();
		}
		return tmp; 
	}

	public void sendToClient(String message)
	{
		pw.println(message);
		pw.flush();
	}

	public boolean connect(String nom, String pass) {
		// TODO : verification de l'existance du compte
		return true;
	}

	public void disconnect()
	{
		try {
			this.br.close();
			this.pw.close();
			this.s.close();
			ServerSingleton.getInstance().deconnexion(this);
		} catch (IOException ex) {
			l.fatal(ex.getMessage());
		}
	}

	/**
	 * @return the s
	 */
	public Socket getS() {
		return s;
	}

	/**
	 * @param s the s to set
	 */
	public void setS(Socket s) {
		this.s = s;
	}

	/**
	 * @return the br
	 */
	public BufferedReader getBr() {
		return br;
	}

	/**
	 * @param br the br to set
	 */
	public void setBr(BufferedReader br) {
		this.br = br;
	}

	/**
	 * @return the pw
	 */
	public PrintWriter getPw() {
		return pw;
	}

	/**
	 * @param pw the pw to set
	 */
	public void setPw(PrintWriter pw) {
		this.pw = pw;
	}

	/**
	 * @return the noresponse
	 */
	public int getNoresponse() {
		return noresponse;
	}

	/**
	 * @param noresponse the noresponse to set
	 */
	public void setNoresponse(int noresponse) {
		this.noresponse = noresponse;
	}

	/**
	 * @return the acces
	 */
	public int getAcces() {
		return acces;
	}

	/**
	 * @param acces the acces to set
	 */
	public void setAcces(int acces) {
		this.acces = acces;
	}

	public int getList() {
		return list;
	}

	public void setList(int list) {
		this.list = list;
	}

	public Account getCompte() {
		return compte;
	}

	public void setCompte(Account compte) {
		this.compte = compte;
	}
}

