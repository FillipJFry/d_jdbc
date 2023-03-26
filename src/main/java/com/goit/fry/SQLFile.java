package com.goit.fry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class SQLFile {

	private static final int MAX_LEN = 30;
	private final SimpleSQLFileParser parser;
	private final Logger logger;

	SQLFile(Logger logger) {

		parser = new SimpleSQLFileParser();
		this.logger = logger;
	}

	public void addDDLPatterns() {

		parser.addPattern("CREATE TABLE ");
		parser.addPattern("DROP TABLE ");
		parser.addPattern("ALTER TABLE ");
	}

	public void addDMLUpdatePatterns() {

		parser.addPattern("DELETE FROM ");
		parser.addPattern("INSERT INTO ");
	}

	public void addDMLSelectPattern() {

		parser.addPattern("SELECT ");
	}

	public boolean addPattern(String pattern_str) {

		return parser.addPattern(pattern_str);
	}

	public String loadCommand(String sql_file_path) throws Exception {

		logger.info("parsing the file '" + sql_file_path + "'");
		try (BufferedReader in = new BufferedReader(new FileReader(sql_file_path))) {

			String cmd = parser.findNext(in);
			if (cmd != null) {
				logger.info("the command extracted: " + cmd);
				return cmd;
			}
		}
		throw new Exception("the sql-file " + sql_file_path + " is empty");
	}

	public List<String> loadMultipleCommands(String sql_file_path) throws Exception {

		logger.info("parsing the file '" + sql_file_path + "'");
		ArrayList<String> commands = new ArrayList<>();

		try (BufferedReader in = new BufferedReader(new FileReader(sql_file_path))) {
			String cmd;
			while ((cmd = parser.findNext(in)) != null) {
				commands.add(cmd);
				logger.info("adding a sql-command: " + cmd);
			}
		}
		logger.info(sql_file_path + " - parsing finished");

		return commands;
	}

	public void executeCommand(String command, IQueryResult query_result) throws SQLException {

		assert command != null;
		try (Connection conn = Database.getInstance().getConnection()) {
			try (Statement stmt = conn.createStatement()) {

				ResultSet rs = stmt.executeQuery(command);
				int records = 0;
				while (rs.next()) {
					query_result.addRecord(rs);
					records++;
				}

				logger.info("the command " +
						command.substring(0, Integer.min(MAX_LEN, command.length())) +
						"... returned " + records + " record(s)");
			}
		}
	}

	public void executeMultipleCommands(List<String> commands) throws SQLException {

		logger.info("executing the commands");
		try (Connection conn = Database.getInstance().getConnection()) {
			try (Statement stmt = conn.createStatement()) {
				for (String command : commands) {
					int r = stmt.executeUpdate(command);
					logger.info("the command " +
							command.substring(0, Integer.min(MAX_LEN, command.length())) +
							"... returned " + r);
				}
			}
		}
	}
}
