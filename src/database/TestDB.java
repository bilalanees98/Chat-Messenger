package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.sql.Statement;

public class TestDB {

	/** The name of the MySQL account to use (or empty for anonymous) */
	private final String userName = "root";

	/** The password for the MySQL account (or empty for anonymous) */
	private final String password = "lalib202";

	/** The name of the computer running MySQL */
	private final String serverName = "localhost";

	/** The port of the MySQL server (default is 3306) */
	private final int portNumber = 3306;

	/**
	 * The name of the database we are testing with (this default is installed with
	 * MySQL)
	 */
	private final String dbName = "chatbotdb";

	/** The name of the table we are testing with */
	private final String clientTable = "Client";
	private Statement stmt;

	/**
	 * Get a new database connection
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);
		System.out.println("trying to get connection!! ");
		conn = DriverManager.getConnection(
				"jdbc:mysql://" + this.serverName + ":" + this.portNumber + "/" + this.dbName, connectionProps);
		System.out.println(" Connection achieved!! ");
		return conn;
	}

	public boolean executeUpdate(Connection conn, String command) throws SQLException {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(command); // This will throw a SQLException if it fails
			return true;
		} finally {

			// This will run whether we throw an exception or not
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public void MakeDB(Connection conn) {
		stmt = null;
		String sql = "CREATE DATABASE ChatBotDB";
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////// CHATBOT
	///////////////////////////////////////////////////////////////////////////////// FUNCTIONS////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	public ArrayList<String> getOnlineFriends(Connection conn, String username) {
		Statement stmt = null;
		ArrayList<String> friends = new ArrayList<String>();
		try {
			stmt = conn.createStatement();
			int id = getUserID(username,conn);
			String sql = "Select Name from Client where id in(Select MID from contactlist where id= " + id + ") and status=1";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String name = rs.getString("NAME");
				friends.add(name);
			}
			return friends;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return friends;
		}

	}

	public boolean checkUser(String userName, String password, Connection conn) {
		boolean y = false;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			String sql = "Select count(*) from client " + "where name = " + "'" + userName + "'" + " and password= "
					+ "'" + password + "'";
			ResultSet rs = stmt.executeQuery(sql);
			int count = 0;
			while (rs.next()) {
				count = rs.getInt("Count(*)");
				// System.out.println(rs.getInt("Count(*)"));
			}
			if (count > 0) {
				y = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return y;
	}

	public boolean checkFriendship(int ID1, int ID2, Connection conn) {
		boolean y = false;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			String sql = "Select count(*) from contactlist " + "where ID= " + ID1 + " and MID= " + ID2;

			ResultSet rs = stmt.executeQuery(sql);
			int count = 0;
			while (rs.next()) {
				count = rs.getInt("Count(*)");
			}
			if (count > 0) {
				y = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return y;
	}

	public void addFriend(int ID1, int ID2, Connection conn) throws SQLException {
		String input = "INSERT IGNORE INTO contactlist(ID,MID) " + "VALUES (" + ID1 + "," + ID2 + ")";
		this.executeUpdate(conn, input);
	}

	public void registerUser(String name, String Password, Connection conn) throws SQLException {
		String input = "INSERT IGNORE INTO client(name,password) " + "VALUES (" + "'" + name + "'" + "," + "'"
				+ Password + "'" + ")";
		this.executeUpdate(conn, input);
	}

	public int getUserID(String username, Connection conn) throws SQLException {
		Statement stmt = null;
		stmt = conn.createStatement();
		String sql = "Select id from client " + "where name = " + "'" + username + "'";
		ResultSet rs = stmt.executeQuery(sql);
		int id = 0;
		while (rs.next()) {
			id = rs.getInt("id");
		}
		return id;
	}

	public void run() {

		// Connect to MySQL
		Connection conn = null;
		try {
			// this.MakeDB(conn);
			conn = this.getConnection();
			System.out.println("connection name is :: " + conn.getClass().getName());
			System.out.println("Connected to database");
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}

		// Create a table
		try {
			// CREATION OF TABLES
			String createClient = "CREATE TABLE if not exists Client (\r\n" + "    ID int NOT NULL AUTO_INCREMENT,\r\n"
					+ "    name varchar(50) UNIQUE,\r\n" + "    status int default 0,\r\n"
					+ "    Password varchar(10),\r\n" + "    PRIMARY KEY(ID)\r\n" + "   )auto_increment=10001";
			this.executeUpdate(conn, createClient);
			String createContactList = "CREATE TABLE IF NOT EXISTS ContactList (\r\n" + "    ID int NOT NULL,\r\n"
					+ "	MID int NOT NULL,\r\n" + "    FOREIGN KEY (MID) REFERENCES client(ID),\r\n"
					+ "    PRIMARY KEY(ID,MID)\r\n" + "    \r\n" + "   )";
			this.executeUpdate(conn, createContactList);

			// INSERTION OF SAMPLE CLIENTS
			String input = "Insert ignore into client(name,status,Password) " + "Values('Ahmed',1,'123')";
			this.executeUpdate(conn, input);
			input = "Insert ignore into client(name,status,Password) " + "Values('Kashif',1,'456')";
			this.executeUpdate(conn, input);
			input = "Insert ignore into client(name,status,Password) " + "Values('Ali',1,'789')";
			this.executeUpdate(conn, input);
			input = "Insert ignore into client(name,status,Password) " + "Values('Atif',1,'1122')";
			this.executeUpdate(conn, input);
			// INSERTION OF SAMPLE CONTACT LIST ENTRIES
			input = " Insert ignore into contactlist(ID,MID) " + "values(10001,10002)";
			this.executeUpdate(conn, input);
			input = "Insert ignore into contactlist(ID,MID)" + "values(10001,10003)";
			this.executeUpdate(conn, input);
			input = "Insert ignore into contactlist(ID,MID)" + "values(10001,10004)";
			this.executeUpdate(conn, input);
			
			input = " Insert ignore into contactlist(ID,MID) " + "values(10002,10001)";
			this.executeUpdate(conn, input);
			//input = "Insert ignore into contactlist(ID,MID)" + "values(10001,10003)";
			//this.executeUpdate(conn, input);
			//input = "Insert ignore into contactlist(ID,MID)" + "values(10001,10004)";
			//this.executeUpdate(conn, input);

			// this.displayOnlineFriends(conn, 10001);
			// System.out.println( this.findUser(10001, "abcd123", conn));
			// this.addFriend(10001, 10007, conn);
			// this.registerUser("Hyder Abbas","JiyoGG", conn);
			// System.out.println(this.checkFriendship(10001, 10008, conn));

		} catch (Exception e) {
			System.out.println("ERROR: Could not create the table");
			e.printStackTrace();
			return;
		}

		// Drop the table
		/*
		 * try { String dropString = "DROP TABLE " + this.albumTable;
		 * this.executeUpdate(conn, dropString);
		 * System.out.println("Dropped the table"); } catch (Exception e) {
		 * System.out.println("ERROR: Could not drop the table"); e.printStackTrace();
		 * return; }
		 */
	}

	public static void main(String[] args) {
		TestDB app = new TestDB();
		// app.run();

		//System.out.println(app.checkUser("Ahmed", "abcd123", app.getConnection()));
		app.run();
	}
}
