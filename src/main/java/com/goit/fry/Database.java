package com.goit.fry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Database {

	private static final Logger logger = LogManager.getRootLogger();
	private static Database instance = null;
	private final Connection conn;

	private Database() throws SQLException {

		try {
			conn = DriverManager.getConnection("jdbc:h2:./megasoft");
		}
		catch (SQLException e) {

			logger.error(e.getMessage());
			throw e;
		}
	}

	public static Database getInstance() throws SQLException {

		if (instance == null)
			instance = new Database();
		return instance;
	}

	public Connection getConnection() {

		return conn;
	}

	static void reset() {

		instance = null;
	}
}
