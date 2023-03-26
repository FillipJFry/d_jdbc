package com.goit.fry;

import java.util.Calendar;

public class YoungestEldestWorkers {

	private final String age_type;
	private final String name;
	private final Calendar birthday;

	public YoungestEldestWorkers(String age_type, String name, Calendar birthday) {

		this.age_type = age_type;
		this.name = name;
		this.birthday = birthday;
	}

	public String getAgeType() {

		return age_type;
	}

	public String getName() {

		return name;
	}

	public Calendar getBirthday() {

		return birthday;
	}
}
