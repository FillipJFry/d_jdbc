package com.goit.fry;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabasePopulateService {

	private static final Logger logger = LogManager.getRootLogger();

	public static void main(String[] args) {

		SQLFile sql_file = new SQLFile(logger);
		sql_file.addDMLUpdatePatterns();
		sql_file.addPattern("SET @[a-zA-Z]* = ");
		sql_file.addPattern("CREATE UNIQUE INDEX ");
		sql_file.addPattern("CREATE TEMPORARY TABLE ");
		sql_file.addPattern("DROP TABLE ");
		sql_file.addPattern("DROP INDEX ");

		try {
			List<String> ddl_commands = sql_file.loadMultipleCommands("sql/populate_db.sql");
			sql_file.executeMultipleCommands(ddl_commands);
		}
		catch (Exception e) {

			logger.error(e);
		}
	}
}
