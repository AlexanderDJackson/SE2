import java.util.HashMap;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.StringReader;

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

	public String[] parseLine(String line) {
		int i = 0;
		String[] result;

		if(line == null) {
			return null;
		}

		while(line.charAt(i) == ' ' || line.charAt(i) == '\t') {
			i++;
		}

		if(line.charAt(i) == '\n') {
			return null;
		}

		if(line.charAt(i) == '#') {
			return null;
		} else if(line.charAt(i) == '[') {
			result = new String[1];
			result[0] = "";

			while(line.charAt(++i) != ']') {
				result[0] += line.charAt(i);
			}

			return result;
		} else {
			result = new String[2];
			result[0] = "";
			result[1] = "";

			while(line.charAt(i) != '=' && line.charAt(i) != ' ') {
				result[0] += line.charAt(i++);
			}

			while(line.charAt(i) == '=' || line.charAt(i) == ' ') {
				i++;
			}

			if(line.charAt(i) == '"') {
				i++;
				while(line.charAt(i) != '"') {
					result[1] += line.charAt(i++);
				}
			} else {
				boolean inString = false;
				boolean inArray = line.charAt(i) == '[';

				while(line.charAt(i) != '\n' &&
					((line.charAt(i) != '#' && line.charAt(i) != ' ') || inString || inArray) &&
					 (line.charAt(i) != '[' || inArray)) {

					if(line.charAt(i) == '"') {
						inString = true;
					}
					
					if(line.charAt(i) == '[') {
						inArray = !inString && true;
					}
					
					result[1] += line.charAt(i++);
				}
			}
			
			return result;
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
}
