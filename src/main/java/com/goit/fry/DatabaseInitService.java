package com.goit.fry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseInitService {

	public static void main(String[] args) {

		ArrayList<String> ddl_queries = new ArrayList<>();
		try (BufferedReader in = new BufferedReader(new FileReader("sql/init_db.sql"))) {

			SimpleSQLInterpreter interpreter = new SimpleSQLInterpreter(in);
			interpreter.addPattern("CREATE TABLE [^;];");
			interpreter.addPattern("DROP TABLE [^;];");
			interpreter.addPattern("ALTER TABLE [^;];");

			String query;
			while ((query = interpreter.findNext()) != null) {
				ddl_queries.add(query);
			}
		}
		catch (IOException e) {

			e.printStackTrace();
			return;
		}

		try (Connection conn = Database.getInstance().getConnection()) {
			try (Statement stmt = conn.createStatement()) {
				for (String query : ddl_queries)
					stmt.executeUpdate(query);
			}
		}
		catch (Exception e) {

			e.printStackTrace();
		}
	}
}
