package com.goit.fry;

import org.junit.jupiter.api.Test;

import java.sql.DatabaseMetaData;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

	@Test
	void getInstanceDoesntThrow() {

		assertDoesNotThrow(Database::getInstance);
		assertDoesNotThrow(Database::getInstance);
		try {
			assertDoesNotThrow(Database.getInstance()::getConnection);
			assertDoesNotThrow(Database.getInstance().getConnection()::close);
			Database.reset();
		}
		catch (Exception ignored) { }
	}

	@Test
	void getInstanceReturnsSingletone() {

		try {
			Database inst1 = Database.getInstance();
			Database inst2 = Database.getInstance();

			assertEquals(inst1, inst2);
			assertDoesNotThrow(Database.getInstance()::getConnection);
			assertDoesNotThrow(Database.getInstance().getConnection()::close);
			Database.reset();
		}
		catch (Exception ignored) { }
	}

	@Test
	void connectionIsValid() {

		try {
			Database db = Database.getInstance();
			assertDoesNotThrow(db.getConnection()::getMetaData);
			DatabaseMetaData meta = db.getConnection().getMetaData();
			assertEquals("jdbc:h2:./megasoft", meta.getURL());

			assertDoesNotThrow(Database.getInstance()::getConnection);
			assertDoesNotThrow(Database.getInstance().getConnection()::close);
			Database.reset();
		}
		catch (Exception ignored) { }
	}
}