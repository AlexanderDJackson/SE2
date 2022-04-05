import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;

import java.math.BigDecimal;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.StringReader;
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

		for(int i = 0; i < array.length(); i++) {
			inString = array.charAt(i) == '"' && array.charAt(i - 1) != '\\' ? !inString : inString;
			if(array.charAt(i) == '[') {
				int j = 1;

				while(array.charAt(i++) != ']') {
				}
		}
	}

	public static Object parseValue(String value) {
		if(value.charAt(0) == '"') {
			return value.substring(1, value.length() - 1);
		} else if(value.charAt(0) == 't' || value.charAt(0) == 'f') {
			return Boolean.parseBoolean(value);
		} else if(value.charAt(0) == '[' || value.charAt(0) == '{') {
			return parseArray(value);
		} else {
			return new BigDecimal(value);
		}
	}

	/**
	 * Parse the TOML string into a HashMap
	 *
	 * @param tomlString The TOML string to parse
	 */
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

	static public void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/example.toml"));

			String line;
			while((line = reader.readLine()) != null) {
				System.out.println(Arrays.toString(tokenize(line)));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
