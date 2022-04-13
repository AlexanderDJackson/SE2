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

	/**
	 * Toml constructor
	 *
	 * @param toml The TOML string to parse
	 */
	public Toml(String name) {
		table = new HashMap<String, Object>();
		name = name;
	}

	public Object get(String key) {
		return table.get(key);
	}

	public String getString(String key) {
		return (String) table.get(key);
	}

	public BigDecimal getNumber(String key) {
		return (BigDecimal) table.get(key);
	}

	public LocalDateTime getDateTime(String key) {
		return (LocalDateTime) table.get(key);
	}

	public LocalDate getDate(String key) {
		return (LocalDate) table.get(key);
	}

	public LocalTime getTime(String key) {
		return (LocalTime) table.get(key);
	}

	public OffsetDateTime getOffsetDateTime(String key) {
		return (OffsetDateTime) table.get(key);
	}

	public Boolean getBoolean(String key) {
		return (Boolean) table.get(key);
	}

	public Object[] getArray(String key) {
		return (Object[]) table.get(key);
	}

	public HashMap<String, Object> getMap(String key) {
		return (HashMap<String, Object>) table.get(key);
	}

	public static String[] tokenize(String line) {
		String[] tokens = new String[] {"", ""};
		int i = 0;

		if(line == null || line.length() <= 1) {
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
			
			if(array.charAt(i) == ' ' && !inString) {
				index = i + 1;
			} else if(array.charAt(i) == '[') {
				index = i;
				int j = 0;

				while(true) {
					if(array.charAt(i) == '"' && array.charAt(i - 1) != '\\') {
						inString = !inString;
					} else if(array.charAt(i) == '[' && !inString) {
						j++;
					} else if(array.charAt(i) == ']' && !inString) {
						j--;
					}

					i++;

					if(j == 0) {
						list.add(parseValue(array.substring(index, i)));
						break;
					}
				}
			} else if((array.charAt(i) == ',' || array.charAt(i) == ']') && !inString) {
				list.add(parseValue(array.substring(index, i)));
				index = i + 1;
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
			return new BigDecimal(value.replaceAll("_", ""));
		}
	}

	/**
	 * Parse the TOML string into a HashMap
	 *
	 * @param tomlString The TOML string to parse
	 */
	public HashMap<String, Object> parseToml(String tableName, String tomlString) {
		HashMap<String, Object> table = new HashMap<String, Object>();

		BufferedReader reader = new BufferedReader(new StringReader(tomlString));

		String line;
		try {
			while((line = reader.readLine()) != null) {
				String[] result = tokenize(line);
				if(result != null) {
					if(result.length == 1) {
						if(table.get(result[0]) == null) {
							table.put(result[0], new HashMap<String, Object>());
						} else {
							((HashMap<String, Object>) table.get(result[0])).put(result[0], parseValue(result[1]));
						}
					} else {
						table.put(result[0], result[1]);
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	
		table.put(tableName, table);
		return table;
	}

	static public void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/example.toml"));

			String line = "", file = "";
			while((line = reader.readLine()) != null) { file += line + "\n"; }
			Toml toml = new Toml("Guh");
			System.out.println(toml.parseToml(toml.name, file));
			/*
			System.out.println(toml.table.values());
			System.out.println(toml.getString("name").toString());
			System.out.println(toml.getString("server"));

			reader = new BufferedReader(new FileReader("src/main/resources/example.toml"));
			while((line = reader.readLine()) != null) {
				if(tokenize(line) != null) {
					System.out.println(toml.get(tokenize(line)[0]));
				}
			}
			//String test = "guh = [1, 2, 3]";
			//String test = "guh = [[1, 2, 3], [4, 5, 6]]";
			String test = "guh = [[[1, 2, 3], [4, 5, 6]], [[7, 8, 9], [10, 11, 12]]]";
			System.out.println(Arrays.toString(tokenize(test)));
			System.out.println(parseValue(tokenize(test)[1]));
			System.out.println(parseValue(tokenize(test)[1]));
			*/
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
