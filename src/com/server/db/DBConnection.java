
package com.server.db;
import java.sql.*;

/**
 *
 * @author FaZeGa
 */
public class DBConnection 
{
	Connection connexion;
	Statement stmt;

	public DBConnection() throws SQLException
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/leimz", "root", "");
			stmt = connexion.createStatement();
		} catch (Exception e) {
			System.out.print("Impossible to connect with the database");
			System.exit(0);
		}
	}

	public Connection getConnexion() {
		return connexion;
	}

	public void setConnexion(Connection connexion) {
		this.connexion = connexion;
	}

	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}
     
      
    
}
