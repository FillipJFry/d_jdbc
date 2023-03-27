package com.goit.fry.jdbc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseInitServiceTest {

	@Test
	void mainDoesntThrow() {

		assertDoesNotThrow(() -> DatabaseInitService.main(new String[] { }));
	}
}