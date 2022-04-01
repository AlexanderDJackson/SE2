import java.util.HashMap;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.StringReader;

/**
 * 	TOML (Tom's Obvious, Minimal Language) Java Implementation
 *
 *	Class:	CS375.01	
 *
 *	Start Date:	03/08/22
 *	Due Date:	04/21/22
 *
 *	Authors: Brett Hammit (bah20a@acu.edu)
 *			 Alex Jackson (asj18a@acu.edu)
 *			 Justin Raitz (jmr18c@acu.edu)
 */

/**
 * The Toml class is the main class for the Toml Java implementation.
 * @param
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
	public Object parseNumber(String s) {
		// Value is a "number"
		switch(s) {
			case "inf":
				return Double.POSITIVE_INFINITY;
			case "+inf":
				return Double.POSITIVE_INFINITY;
			case "-inf":
				return Double.NEGATIVE_INFINITY;
			default:
				if(s.toLowerCase().contains("nan")) {
					return Double.NaN;

				// Value is actually a number
				} else {
					System.out.println(s + ": " + Long.decode(s.replace("_", "")));

					return new BigDecimal(Long.decode(s.replace("_", "")));
				}
	}
		}

	public Object[] parseLine(String line) {
		int i = 0;
		Object[] result;

		if(line == null) {
			return null;
		}

		// Remove leading whitespace
		while(line.charAt(i) == ' ' || line.charAt(i) == '\t') {
			i++;
		}

		// Skip blank lines
		if(line.charAt(i) == '\n') {
			return null;
		}

		// Skip comments
		if(line.charAt(i) == '#') {
			return null;
		// Line is a table header
		} else if(line.charAt(i) == '[') {
			result = new Object[1];
			result[0] = new String("");

			// Read to end of table header
			while(line.charAt(++i) != ']') {
				result[0] = result[0].toString() + line.charAt(i);
			}

			return result;
		// Line is a key-value pair
		} else {
			result = new Object[2];
			result[0] = "";

			// Read to end of key
			while(line.charAt(i) != '=' && line.charAt(i) != ' ') {
				result[0] = result[0].toString() + line.charAt(i++);
			}
	
			// Skip whitespace and = sign
			while(line.charAt(i) == '=' || line.charAt(i) == ' ') {
				i++;
			}

			// Value is a string
			if(line.charAt(i) == '"') {
				result[1] = "";
				i++;
				while(line.charAt(i) != '"') {
					result[1] = result[1].toString() + line.charAt(i++);
				}
			// Value is not a string
			} else {
				String temp = "";
				boolean inString = line.charAt(i) == '"';
				boolean inArray = line.charAt(i) == '[';

				// Read to newline
				while(line.charAt(i) != '\n' &&
					// Or beginning of comment unless in a string or array
					((line.charAt(i) != '#' && line.charAt(i) != ' ') || inString || inArray) &&
					// Or beginning of array unless in a array
					 (line.charAt(i) != '[' || inArray)) {

					// Keep track of whether we're in a string
					if(line.charAt(i) == '"') {
						inString = true;
					}
					
					// Keep track of whether we're in a string
					if(line.charAt(i) == '[') {
						inArray = true;
					}
					
					// Add character to value
					// Add character to value
					temp += line.charAt(i++);
				}

				if(temp.charAt(0) == '"' || temp.charAt(0) == '[') {
					result[1] = temp;

				// Value is a boolean
				} else if(temp.equals("true") || temp.equals("false")) {
					result[1] = Boolean.parseBoolean(temp);

				// Value is a date/time/datetime
				} else if(temp.contains(":") && !temp.contains("E-") && !temp.contains("e-")) {
					if(temp.contains(":") && temp.contains("-")) {
						result[1] = LocalDateTime.parse(temp);
					} else if(temp.contains(":")) {
						result[1] = LocalTime.parse(temp);
					} else {
						result[1] = LocalDate.parse(temp);
					}

				// Value is a number type
				// Or NaN, because that's apparently necessary
				} else {
					result[1] = parseNumber(temp);
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
	public void parseToml(String tomlString) {
		BufferedReader reader = new BufferedReader(new StringReader(tomlString));

		String line;
		try {
			while((line = reader.readLine()) != null) {
				Object[] result = parseLine(line + '\n');
				if(result != null) {
					if(result.length == 1) {
						this.table.put(result[0].toString(), new HashMap<String, Object>());
					} else {
						this.table.put(result[0].toString(), result[1]);
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return the float associated with the given key
	 * 
	 * @param key The key
	 */
	public float getFloat(String key) {
		return ((BigDecimal) this.table.get(key)).floatValue();
	}

	/**
	 * Return the int associated with the given key
	 * 
	 * @param key The key
	 */
	public int getInt(String key) {
		return ((BigDecimal) this.table.get(key)).intValue();
	}

	/**
	 * Return the double associated with the given key
	 * 
	 * @param key The key
	 */
	public double getDouble(String key) {
		try {
			return ((BigDecimal) this.table.get(key)).doubleValue();
		} catch(ClassCastException ex) {
			return (Double) this.table.get(key);
		}
	}

	/**
	 * Return the Long associated with the given key
	 * 
	 * @param key The key
	 */
	public Long getLong(String key) {
		return ((BigDecimal) this.table.get(key)).longValue();
	}

	/**
	 * Return the boolean associated with the given key
	 * 
	 * @param key The key
	 */
	public Boolean getBoolean(String key) {
		return (Boolean) this.table.get(key);
	}

	/**
	 * Return the String associated with the given key
	 * 
	 * @param key The key
	 */
	public String getString(String key) {
		return (String) this.table.get(key);
	}

	/**
	 * Return the Date associated with the given key
	 * 
	 * @param key The key
	 */
	public LocalDate getDate(String key) {
		return (LocalDate) this.table.get(key);
	}

	/**
	 * Return the Time associated with the given key
	 * 
	 * @param key The key
	 */
	public LocalTime getTime(String key) {
		return (LocalTime) this.table.get(key);
	}

	/**
	 * Return the DateTime associated with the given key
	 * 
	 * @param key The key
	 */
	public LocalDateTime getDateTime(String key) {
		return (LocalDateTime) this.table.get(key);
	}
}
