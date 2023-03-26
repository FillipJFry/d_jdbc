package com.goit.fry;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseInitService {

	private static final Logger logger = LogManager.getRootLogger();

	public static void main(String[] args) {

		SQLFile sql_file = new SQLFile(logger);
		sql_file.addDDLPatterns();

		try {
			List<String> ddl_commands = sql_file.loadMultipleCommands("sql/init_db.sql");
			sql_file.executeMultipleCommands(ddl_commands);
		}
		catch (Exception e) {

			logger.error(e);
		}
	}
}
