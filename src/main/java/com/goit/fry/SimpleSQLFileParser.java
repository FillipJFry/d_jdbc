package com.goit.fry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleSQLFileParser {

	private static final Logger logger = LogManager.getRootLogger();
	private final HashSet<String> pattern_hashes;
	private final ArrayList<Pattern> patterns;
	Pattern comment_begin, comment_end, sql_cmd_eol;

	public SimpleSQLFileParser() {

		pattern_hashes = new HashSet<>();
		patterns = new ArrayList<>();

		comment_begin = Pattern.compile("/\\*");
		comment_end = Pattern.compile("\\*/");
		sql_cmd_eol = Pattern.compile("[^;]*;");
	}

	public boolean addPattern(String pattern_str) {

		boolean is_new = pattern_hashes.add(pattern_str);
		if (is_new) patterns.add(Pattern.compile(pattern_str));
		else
			logger.warn("the pattern has already been added: " + pattern_str);

		return is_new;
	}

	String findNext(BufferedReader reader) throws Exception {

		Map.Entry<String, Pattern> line_n_pattern = findLineMatchingPattern(reader);
		if (line_n_pattern == null) return null;
		String line = line_n_pattern.getKey();
		Pattern matched_p = line_n_pattern.getValue();

		StringBuilder sql_command = new StringBuilder();
		sql_command.append(line);
		addSpaceIfNecessary(sql_command, line);

		Matcher m_eol = sql_cmd_eol.matcher(line);
		while (!m_eol.find() &&	(line = reader.readLine()) != null) {

			for (Pattern pattern : patterns) {
				if (pattern == matched_p) continue;
				Matcher m = pattern.matcher(line);
				if (m.find())
					throw new Exception("wrong SQL-command: " +
							sql_command + line + ". Probably semicolon is missing");
			}

			line = line.trim();
			sql_command.append(line);
			addSpaceIfNecessary(sql_command, line);
			m_eol.reset(line);
		}
		if (line == null)
			throw new Exception("no semicolon at the end: " + sql_command);

		return sql_command.toString();
	}

	private Map.Entry<String, Pattern> findLineMatchingPattern(BufferedReader reader) throws IOException {

		boolean inside_comment = false;
		String line = null;
		Matcher m_comm_begin = comment_begin.matcher("");
		Matcher m_comm_end = comment_end.matcher("");

		while ((line = reader.readLine()) != null) {

			if (!inside_comment) {
				m_comm_begin.reset(line);
				inside_comment = m_comm_begin.find();
			}
			for (int i = 0; !inside_comment && i < patterns.size(); i++) {
				Matcher m = patterns.get(i).matcher(line);
				if (m.find())
					return new AbstractMap.SimpleEntry<>(line, patterns.get(i));
			}
			if (inside_comment) {
				m_comm_end.reset(line);
				inside_comment = !m_comm_end.find();
			}
		}

		return null;
	}

	private void addSpaceIfNecessary(StringBuilder sql_command, String line) {

		if (line.length() == 0) return;

		char line_end = line.charAt(line.length() - 1);
		if (line_end != ';' && line_end != ' ')
			sql_command.append(' ');
	}
}
