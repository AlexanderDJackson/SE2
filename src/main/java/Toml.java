import java.util.HashMap;
import java.util.stream.Stream;

import java.io.IOException;
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
	public Toml(String toml) { }
	
	/**
	 * Parse the TOML string into a HashMap
	 *
	 * @param tomlString The TOML string to parse
	 */
	public static HashMap<String, Object> parseToml(String tomlString) {
		HashMap<String, Object> table = new HashMap<String, Object>();

		for(int i = 0; i < tomlString.length(); i ++) {
			char c = tomlString.charAt(i);

			try {
				switch(c) {
					case '#':
						while(tomlString.charAt(i) != '\n') { i++; }
					case '\n':
						if(tomlString.charAt(i + 1) != '#') {
							String key = "", value = "";

							while(tomlString.charAt(i) != ' ' || tomlString.charAt(i) == '=') {
								key += tomlString.charAt(i++);
							}
							
							while(tomlString.charAt(i) != '\n') {
								 value += tomlString.charAt(i++);
							}
							
							table.put(key, value);
						}
					default:
						break;
				}
			} catch(IndexOutOfBoundsException e) {
				break;
			}
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

		System.out.println(parseToml(content).toString());

	}
}
