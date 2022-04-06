import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

import java.time.OffsetDateTime;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;

import java.math.BigDecimal;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;
import java.io.FileReader;

/**
 * 	TOML (Tom's Obvious, Minimal Language) Java Implementation
 *
 *	Authors: Brett Hammit (bah20a@acu.edu)
 *			 Alex Jackson (asj18a@acu.edu)
 *			 Justin Raitz (jmr18c@acu.edu)
 */

/**
 * The Toml class is the main class for the Toml Java implementation.
 */
public class Toml
{
	HashMap<String, Object> table;
	String name;

	/**
	 * Toml constructor
	 *
	 * @param toml The TOML string to parse
	 */
	public Toml() {
		table = new HashMap<String, Object>();
		name = "";
	}

	public static String[] tokenize(String line) {
		String[] tokens = new String[] {"", ""};
		int i = 0;

		if(line == null || line.length() == 0) {
			return null;
		}

		// Find first non-whitespace character
		while(Character.isWhitespace(line.charAt(i))) { i++; }

		if(line.charAt(i) == '#') {
			return null;
		}

		if(line.charAt(i) == '[') {
			while(line.charAt(++i) != ']') {
				tokens[0] += line.charAt(i);
			}

			return new String[] {tokens[0]};
		} else {
			// Read the key
			while(!(line.charAt(i) == '=') && !(line.charAt(i) == ' ')) {
				tokens[0] += line.charAt(i++);
			}

			// Read past the equals sign and whitespace
			while(line.charAt(i) == '=' || line.charAt(i) == ' ') { i++; }

			// Read the value
			while(i != line.length()) {
				tokens[1] += line.charAt(i++);
			}
		}

		return tokens;
	}

	public static Object[] parseArray(String array) {
		ArrayList<Object> list = new ArrayList<Object>();
		Boolean inString = false;
		int index = 1;

		for(int i = 1; i < array.length(); i++) {
			inString = array.charAt(i) == '"' && array.charAt(i - 1) != '\\' ? !inString : inString;

			if(array.charAt(i) == '[') {
				index = i++;
				int j = 1;

				while(array.charAt(i) != ']') {
					if(array.charAt(i) == '"') {
						inString = !inString;
					} else if(array.charAt(i) == '[' && !inString) {
						j++;
					} else if(array.charAt(i) == ']' && !inString) {
						j--;
					}

					System.out.print(array.substring(0, i) + "|" + array.charAt(i) + "|" + array.substring(i + 1));
					System.out.print(" && j: " + j);
					System.out.println(" && i: " + i);

					i++;

					if(j == 0) {
						System.out.println("Parsing: " + array.substring(index, i));
						parseArray(array.substring(index, i + 1));
					}
				}
			} else if(array.charAt(i) == ',' && !inString) {
				System.out.println("Adding: " + array.substring(index, i));
				list.add(parseValue(array.substring(index, i)));
				index = array.charAt(i + 1) != ' ' ? i + 1 : i + 2;
			}else if(i == array.length() - 2) {
				System.out.println("Adding: " + array.substring(index, i));
				list.add(parseValue(array.substring(index, i + 1)));
			}

			inString = array.charAt(i) == '"' && i != 0 && array.charAt(i - 1) != '\\' ? !inString : inString;
		}

		return list.toArray();
	}

	public static Object parseValue(String value) {
		if(value.charAt(0) == '"') {
			return value.substring(1, value.length() - 1);
		} else if(value.contains(":") || value.contains("-") && value.charAt(0) != '-') {
			if(value.contains("-") && !value.contains(":")) {
				return LocalDate.parse(value);
			} else if(value.contains(":") && !value.contains("-")) {
				return LocalTime.parse(value);
			} else if(value.substring(value.indexOf(":")).contains("-")) {
				return OffsetDateTime.parse(value);
			} else {
				return LocalDateTime.parse(value);
			}
		} else if(value.charAt(0) == 't' || value.charAt(0) == 'f') {
			return Boolean.parseBoolean(value);
		} else if(value.charAt(0) == '[' || value.charAt(0) == '{') {
			return parseArray(value);
		} else {
			System.out.println("!!! " + value);
			return new BigDecimal(value.replaceAll("_", ""));
		}
	}

	/**
	 * Parse the TOML string into a HashMap
	 *
	 * @param tomlString The TOML string to parse
	public HashMap<String, Object> parseToml(String tomlString) {
		HashMap<String, Object> table = new HashMap<String, Object>();

		BufferedReader reader = new BufferedReader(new StringReader(tomlString));

		String line;
		try {
			while((line = reader.readLine()) != null) {
				String[] result = parseLine(line + '\n');
				if(result != null) {
					if(result.length == 1) {
						table.put(result[0], new HashMap<String, Object>());
					} else {
						table.put(result[0], result[1]);
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	
		return table;
	}
	 */

	static public void main(String[] args) {
		try {
			/*
			BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/example.toml"));

			String line;
			while((line = reader.readLine()) != null) {
				String[] result = tokenize(line);
				if(result != null) {
					if(result.length == 1) {
						System.out.println(result[0]);
					} else {
						System.out.println(result[0] + ": " + parseValue(result[1]));
						//System.out.println(Arrays.toString(tokenize(line)));
					}
				}
			}
			*/
			String test = "guh = [[\"a\", \"b\", \"c\"], [\"d\", \"e\", \"f\"]]";
			System.out.println(Arrays.toString(tokenize(test)));
			for(String o : ((String[]) parseArray(test)[0])) {
				System.out.println(o);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
