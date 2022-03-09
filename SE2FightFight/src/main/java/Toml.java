import java.util.HashMap;
import java.nio.File;
import java.nio.charset.Charset;

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
	HashMap<String, HashMap<String, Object>> toml;

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
	public Toml parseToml(String tomlString) {
		this.toml = new HashMap<String, HashMap<String, Object>>();
		for(int i = 0; i < tomlString.length(); i ++) {
			char c = tomlString.charAt(i);

			switch(c) {
				case '#':
					System.out.print("#");
					while(tomlString.charAt(i) != '\n') {
						System.out.print(tomlString.charAt(i));
						i++;
					}
					System.out.print("\n");
			}
		}
	}

	public void main(String[] args) {
		String content = Files.readString("example.toml", Charset.defaultCharset());
		parseToml(content);
	}
}
