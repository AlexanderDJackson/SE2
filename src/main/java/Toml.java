import java.util.IllegalFormatException;
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
			return new String[] { tokens[0] };
		}

		// Find first non-whitespace character
		while(Character.isWhitespace(line.charAt(i))) { i++; }

		if(line.charAt(i) == '#') {
			return new String[] { tokens[0] };
		}

		if(line.charAt(i) == '[') {
			while(line.charAt(++i) != ']') {
				tokens[0] += line.charAt(i);
			}

			return new String[] { tokens[0] };
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

	public void add(HashMap<String, Object> map) {
		if(map != null) { table.putAll(map); }
	}

	public static String parseString(String string) {
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < string.length(); i++) {
			if(string.charAt(i) == '\\') {
				switch(string.charAt(++i)) {
					case 'n':
						sb.append('\n');
						break;
					case 't':
						sb.append('\t');
						break;
					case 'r':
						sb.append('\r');
						break;
					case 'f':
						sb.append('\f');
						break;
					case '\'':
						sb.append('\'');
						break;
					case '\"':
						sb.append('\"');
						break;
					case '\\':
						sb.append('\\');
						break;
					case 'u':
						sb.append((char) Integer.parseInt(string.substring(i + 1, i + 5), 16));
						i += 4;
						break;
					default:
						System.err.println("Invalid escape sequence: " + "\\" + string.charAt(i + 1) + " at location " + i);
				}
			} else {
				sb.append(string.charAt(i));
			}
		}

		return sb.toString();
	}
			
	public static Object parseValue(String value) {
		if(value.charAt(0) == '"') {
			return parseString(value.substring(1, value.length() - 1));
		} else if(value.charAt(0) == '\'') {
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
	public static HashMap<String, Object> parseToml(String tomlString) {
		HashMap<String, Object> table = new HashMap<String, Object>();

		BufferedReader reader = new BufferedReader(new StringReader(tomlString));
		String line;

		try {
			while((line = reader.readLine()) != null) {
				String[] result = tokenize(line);
				if(result[0] != "") {
					System.out.println(Arrays.toString(result) + " " + result.length);
					if(result.length == 1) {
						System.out.println("Found table: " + result[0]);
						HashMap<String, Object> subTable = new HashMap<String, Object>();

						String recurse = "";
						
						while(true) {
							reader.mark(1024);
							if((line = reader.readLine()) == null) {
								break;
							}

							String[] subResult = tokenize(line);
							if(subResult != null && subResult.length < 2) {
								reader.reset();
								break;
							} else {
								recurse += line + "\n";
							}
						}

						System.out.println("Recursing for: " + tomlString);
						subTable = parseToml(recurse);

						if(((HashMap<String, Object>) table.get(result[0])) == null) {
							System.out.println("Recursing for: \n" + recurse);
							subTable.put(result[0], parseToml(recurse));
						} else {
							System.out.println("Recursing for: \n" + recurse);
							((HashMap<String, Object>) table.get(result[0])).put(result[0], parseToml(recurse));
						}

					} else {
						table.put(result[0], parseValue(result[1]));
					}
				} else {
					return null;
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

			String line = "", file = "";
			while((line = reader.readLine()) != null) { file += line + "\n"; }
			Toml toml = new Toml("Guh");
			toml.add(parseToml(file));
			System.out.println(parseToml(file));
			System.out.println(toml.table.toString());
			System.out.println(toml.getString("name"));
			System.out.println(toml.getString("server"));

			/*
			System.out.println("Name\t\"Jos\"\u00E9\nLoc\tSF."); // "h\nh"
			System.out.println(parseValue("\'Name\\t\\\"Jos\\\"\\u00E9\\nLoc\\tSF.\'")); // "h\nh"
			System.out.println(parseValue("\"Name\\t\\\"Jos\\\"\\u00E9\\nLoc\\tSF.\"")); // "h\nh"
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
