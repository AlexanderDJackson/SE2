import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;

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

	public static String[] parseLine(String line) {
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
				boolean strings = false;

				while(line.charAt(i) != '\n' && (line.charAt(i) != '#' && !strings)) {

					if(line.charAt(i) == '"') {
						strings = true;
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
	public static HashMap<String, Object> parseToml(String tomlString) {
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

	public static void main(String[] args) {
		String content = "";

		try {
			content = new String(Files.readAllBytes(Paths.get("src/main/resources/example.toml")));
		} catch(IOException e) {
			e.printStackTrace();
		}

		System.out.println("\n\n" + parseToml(content).toString());

	}
}
