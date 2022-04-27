import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.FileReader;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.math.BigDecimal;

/**
 * 	TOML (Tom's Obvious, Minimal Language) Java Implementation
 *
 *	Authors: 	Brett Hammit (bah20a@acu.edu)
 *			 	Alex Jackson (asj18a@acu.edu)
 *			 	Justin Raitz (jmr18c@acu.edu)
 *	Start Date:	03/08/22
 *	Due Date:	04/27/22
 *	Class:		CS375.01
 *
 *	Compile: 	Locate folder with maven pom.xml from command prompt
 *				Run %mvn compile
 *	Test:		%mvn test
 *	Clean:		%mvn clean
 *	Execute:	%java -cp target/classes/ Toml (***Only useful if you add a main function)
 *
 *	Description: This java project allows Java users to import this library 
 *				 and easily work with TOML files, extract data from within 
 *				 them, and add new data. It parses whole TOML files and stores
 *				 the key/value pairs and the table headers inside of hash map objects.
 *				 Users can then use the get and set functionalities to extract and
 *				 update the data in the TOML file.
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

	public Toml(String name, HashMap<String, Object> map) {
		table = map;
		this.name = name;
	}

	public Object get(String key) {
		return table.get(key);
	}

	public String getString(String key) {
		return (String) table.get(key);
	}

	public Object getNumber(String key) {
		Object obj = table.get(key);
		if(obj.equals(Double.NaN)) {
			return Double.NaN;
		} else if(obj.equals(Double.POSITIVE_INFINITY)) {
			return Double.POSITIVE_INFINITY;
		} else if(obj.equals(Double.NEGATIVE_INFINITY)) {
			return Double.NEGATIVE_INFINITY;
		} else {
			return (BigDecimal) table.get(key);
		}
	}

	public int getInt(String key) {
		return ((BigDecimal) table.get(key)).intValue();
	}

	public float getFloat(String key) {
		return ((BigDecimal) table.get(key)).floatValue();
	}

	public long getLong(String key) {
		return ((BigDecimal) table.get(key)).longValue();
	}

	public double getDouble(String key) {
		return ((BigDecimal) table.get(key)).doubleValue();
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

	public Toml getMap(String key) {
		return new Toml(key, (HashMap<String, Object>) table.get(key));
	}

	public boolean isEmpty() {
		return table.isEmpty();
	}

	public void add(String key, Object value) {
		this.table.put(key, value);
	}

	public String[] tokenize(String line) {
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

	public Object[] parseArray(String array) {
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

		return list.toArray()
		;
	}

	public void add(HashMap<String, Object> map) {
		if(map != null) { table.putAll(map); }
	}

	public String parseString(String string) {
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
			} else if(string.charAt(i) != '"') {
				sb.append(string.charAt(i));
			}
		}

		return sb.toString();
	}
			
	public Object parseValue(String value) {
		if(value.charAt(0) == '"' || value.charAt(0) == '\'') {
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
			if(value.toLowerCase().contains("nan")) {
				return Double.NaN;
			} else if(value.toLowerCase().contains("inf")) {
				return value.charAt(0) == '-' ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
			} else {
				BigDecimal bd;

				if(!value.contains("b") && !value.contains("o") && !value.contains("x")) {
					bd = new BigDecimal(value.replaceAll("_| ", ""));
				} else {
					switch(value.charAt(1)) {
						case 'x':
							bd = new BigDecimal(Integer.parseInt(value.replaceAll("_| ", "").substring(2), 16));
							break;
						case 'o':
							bd = new BigDecimal(Integer.parseInt(value.replaceAll("_| ", "").substring(2), 8));
							break;
						case 'b':
							bd = new BigDecimal(Integer.parseInt(value.replaceAll("_| ", "").substring(2), 2));
							break;
						default:
							bd = new BigDecimal(Integer.parseInt(value.replaceAll("_| ", "")));
					}
				}

				return bd;
			}
		}
	}

	public HashMap<String, Object> parseToml(String tomlString) {
		HashMap<String, Object> guh = parseToml(tomlString, this.name);
		//return parseToml(tomlString, this.name);
		return guh;
	}

	/**
	 * Remove all TOML accepted comments from a TOML string
	 *
	 * @param tomlString The TOML string to remove comments from
	 */
	public static String removeComments(String tomlString) {
		return tomlString.replaceAll("(?m)^#.*$", "");
	}

	/**
	 * Remove all leading whitespace from a TOML string
	 *
	 * @param tomlString The TOML string to remove leading whitespace from
	 */
	public static String removeLeadingWhitespace(String tomlString) {
		return tomlString.replaceAll("(?m)^[ \t]+", "");
	}

	/**
	 * Remove all blank lines from a TOML string
	 *
	 * @param tomlString The TOML string to remove blank lines from
	 */
	public static String removeBlankLines(String tomlString) {
		return tomlString.replaceAll("(?m)^[ \t]*\r?\n", "");
	}

	/**
	 * Collapse multiline string literals
	 *
	 * @param tomlString The TOML string to collapse multiline string literals in
	 */
	public static String collapseLitStrings(String tomlString) {
        BufferedReader reader = new BufferedReader(new StringReader(tomlString));
        StringJoiner result = new StringJoiner("\n");
        String line = "";
        boolean inLitStr = false;

        try {
            // While there is a new line of the string to be read
			while((line = reader.readLine()) != null) {
                
				// Loop through each character of the line
				for(int i = 0; i < line.length(); i++) {
					// A single quote is found 3 charaters from the end of the line
                    if(line.charAt(i) == '\'' && i + 2 < line.length()) {
						// The next 2 characters are also single quotes (''')
						if(line.charAt(i + 1) == '\'' && line.charAt(i + 2) == '\'') {
							// Not in literal string
							if(!inLitStr) {
								// Set bool to opposite
                                inLitStr = !inLitStr;
								// Remove extra single quotes using substring
								line = line.substring(0, i) + line.substring(i + 2);    

                                // Line does not end with 3 single quotes
								if(!line.endsWith("'''")) {
                                    // Add next line to line string
									line += reader.readLine();
                                }
                            } else {
                                // Set bool to opposite
								inLitStr = !inLitStr;
                                // Remove extra single quotes using substring
								line = line.substring(0, i) + line.substring(i + 2);
                                // Exit for loop
								break;
                            }
                        }
                    } else if (i == line.length() - 1 && inLitStr) { // At last character of line string and in literal string
                        // Add escaped newline and next line to line string
						line += "\\n" + reader.readLine();
                    }
                }
				// Add line string to result StringJoiner
                result.add(line);
            }
        } catch(IOException ex) { // Catch any exceptions and print them off the stack trace
            ex.printStackTrace();
        }
		
		// Return result StringJoiner cast to a string
        return result.toString();
    }

	/**
	 * Collapse multiline strings
	 *
	 * @param tomlString The TOML string to collapse multiline strings in
	 */
	public static String collapseStrings(String tomlString) {
		BufferedReader reader = new BufferedReader(new StringReader(tomlString));
		StringJoiner result = new StringJoiner("\n");
		String line = "";
		Boolean inString = false;

		try {
			// While there is a new line of the string to be read
			while((line = reader.readLine()) != null) {
				
				// Loop through each character of the line
				for(int i = 0; i < line.length(); i ++) {
					// A quote is found 3 charaters from the end of the line
					if(line.charAt(i) == '"' && i + 2 < line.length()) {
						// The next 2 characters are also single quotes (""")
						if(line.charAt(i + 1) == '"' && line.charAt(i + 2) == '"') {
							// Not in string
							if(!inString) {
								// Set bool to opposite
								inString = !inString;
								// Remove extra quotes using substring
								line = line.substring(0, i) + line.substring(i + 2);

								// Line ends with escaped backslash
								if(line.endsWith("\\")) {
									// Remove backslash
									line = line.substring(0, line.length() - 1);
									// Add next line to line string after removing leading whitespace 
									line += reader.readLine().replaceAll("^\\s*", "");
								} else {
									// Add next line to line string
									line += reader.readLine();
								}
							} else {
								// Set bool to opposite
								inString = !inString;
								// Remove extra single quotes using substring
								line = line.substring(0, i) + line.substring(i + 2);
								// Exit for loop
								break;
							}
						}
					} else if(i == line.length() - 1 && inString) { // At last character of line string and in string
						// Line ends with escaped backslash
						if(line.endsWith("\\")) {
							// Remove backslash
							line = line.substring(0, line.length() - 1);
							// Add next line to line string after removing leading whitespace
							line += reader.readLine().replaceAll("^\\s*", "");
							// Decrement i
							i --;
						} else {
							// Add next line to line string
							line += reader.readLine();
						}
					}
				}

				// Add line string to result StringJoiner
                result.add(line);
            }
        } catch(IOException ex) { // Catch any exceptions and print them off the stack trace
            ex.printStackTrace();
        }
		
		// Return result StringJoiner cast to a string
        return result.toString();
	}

	/**
	 * Clean up TOML file for easier parsing by removing uneccesary lines and whitespace
	 *
	 * @param tomlString The TOML string to collapse multiline string literals in
	 */
	public static String preProcess(String tomlString) {
		StringJoiner result = new StringJoiner("\n");
		try {
			// Use collapse functions on tomlString
			tomlString = collapseStrings(tomlString);
			tomlString = collapseLitStrings(tomlString);

			// Create BufferedReader that reads in tomlString with blank lines, comments, and leading whitespace removed
			BufferedReader reader = new BufferedReader(new StringReader(removeBlankLines(removeComments(removeLeadingWhitespace(tomlString)))));
			String line = "";
			int counter = 0;
			Boolean inString = false, appended = false;

			// While there is a new line of the string to be read
			while((line = reader.readLine()) != null) {
				
				// Loop through each character of the line
				for(int i = 0; i < line.length(); i++) {
					// Character is quote
					if(line.charAt(i) == '"') {
						//set bool to opposite
						inString = !inString;
					} else if(!inString) { // Not in string
						// Character is comment symbol (#)
						if(line.charAt(i) == '#') {
							// Add everything in line string before # to result StringJoiner
							result.add(line.substring(0, i));
							// Set bool to true
							appended = true;
						} else if(line.charAt(i) == '[') { // Character is open bracket ([)
							// Increment counter
							counter ++;
						} else if(line.charAt(i) == ']') { // Character is closed bracket (])
							// Decrement counter
							counter --;
						}
					}

					// At last character of line string and counter does not  equal 0
					if(i == line.length() - 1 && counter != 0) {
						// Add next line to line string
						line += reader.readLine();
					}
				}

				// Appended bool is false
				if(!appended) {
					// Add line string to result StringJoiner
					result.add(line);
				}

				// Set bool to false
				appended = false;
			}
		} catch(IOException e) { // Catch any exceptions and print them off the stack trace
			e.printStackTrace();
		}

		// Return result StringJoiner cast to a string
		return result.toString();
	}

	public HashMap<String, Object> parseToml(File file) {
		StringJoiner result = new StringJoiner("\n");

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";

			while((line = br.readLine()) != null) {
				result.add(line);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return parseToml(result.toString());
	}

	/**
	 * Parse the TOML string into a HashMap
	 *
	 * @param tomlString The TOML string to parse
	 */
	public HashMap<String, Object> parseToml(String tomlString, String tableName) {
		HashMap<String, Object> table = new HashMap<String, Object>();

		tomlString = preProcess(tomlString);
		BufferedReader reader = new BufferedReader(new StringReader(tomlString));
		String line;

		try {
			while((line = reader.readLine()) != null) {
				String[] result = tokenize(line);
				if(result[0] != "") {
					if(result.length == 1) {
						HashMap<String, Object> subTable = new HashMap<String, Object>();

						StringJoiner recurse = new StringJoiner("\n");
						
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
								recurse.add(line);
							}
						}

						subTable = parseToml(recurse.toString(), result[0]);

						if(((HashMap<String, Object>) table.get(result[0])) == null) {
							subTable.put(result[0], subTable);
						} else {
							((HashMap<String, Object>) table.get(result[0])).put(result[0], subTable);
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
	
		this.table.putAll(table);
		return table;
	}
}
