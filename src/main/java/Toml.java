import java.util.HashMap;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.StringJoiner;
import java.io.FileReader;
import java.util.stream.Collectors;

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

	/**
	 * Parse an individual line of the TOML string
	 *
	 * @param line The line of string to parse
	 */
	public Object[] parseLine(String line) {
		int i = 0;
		Object[] result;

		// Return null for null line
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
					temp += line.charAt(i++);
				}

				if(temp.charAt(0) == '"' || temp.charAt(0) == '[') {
					result[1] = temp;
				} else if(temp.equals("true") || temp.equals("false")) {
					result[1] = Boolean.parseBoolean(temp);
				} else if(temp.contains(":") && !temp.contains("E-") && !temp.contains("e-")) {
					if(temp.contains(":") && temp.contains("-")) {
						result[1] = LocalDateTime.parse(temp);
					} else if(temp.contains(":")) {
						result[1] = LocalTime.parse(temp);
					} else {
						result[1] = LocalDate.parse(temp);
					}
				} else {
					if(temp.contains("nan") || temp.contains("inf")) {
						switch(temp) {
							case "inf":
								result[1] = Double.POSITIVE_INFINITY;
								break;
							case "+inf":
								result[1] = Double.POSITIVE_INFINITY;
								break;
							case "-inf":
								result[1] = Double.NEGATIVE_INFINITY;
								break;
							default:
								result[1] = Double.NaN;
								break;
						}
					} else {

						if(temp.contains("x") || temp.contains("o") || temp.contains("b")) {
							String prefix = temp.substring(0, 2);
							temp = temp.substring(2, temp.length());

							switch (prefix) {
								case "0x":
									try {
										result[1] = Integer.valueOf(temp.replace("_", ""), 16);
									} catch(NumberFormatException ex) {
										result[1] = Long.decode((prefix + temp).replace("_", ""));
									}
									break;
								case "0o":
									result[1] = Integer.valueOf(temp.replace("_", ""), 8);
									break;
								case "0b":
									result[1] = Integer.valueOf(temp.replace("_", ""), 2);
									break;
								}
							} else {
								if(temp.substring(1, temp.length()).contains("-") || temp.contains(".")) {
									result[1] = Double.valueOf(temp.replace("_", ""));
								} else if(temp.contains("e") || temp.contains("E")) {
								result[1] = Double.valueOf(temp.replace("_", ""));
								} else {
									result[1] = Integer.parseInt(temp.replace("_", ""));
								}
							}
						}
					}
				}
			}
			
			return result;
		}

	/**
	 * Parse the entire TOML string into a HashMap
	 *
	 * @param tomlString The TOML string to parse
	 */
	public void parseToml(String tomlString) {
		BufferedReader reader = new BufferedReader(new StringReader(tomlString));

		String line;
		try {
			// While there is a new line of the string to be read
			while((line = reader.readLine()) != null) {
				
				// Line consists of key/value pair
				if(line.contains("=") &&
					// Line consists of array
					line.contains("[") && 
					// Line is not string
					(line.indexOf("\"") <= 0 || line.indexOf("\"") > line.indexOf("["))) {
					
					int brack = 1;
					
					// Line doesn't end with closing bracket and unmatched opening brackets remain
					while(!line.endsWith("]") && brack != 0) {
						String temp = reader.readLine();
						// Loop through temp String
						for(int i = 0; i < temp.length() - 1; i++)
						{
							// Increment i if opening bracket
							if(temp.charAt(i) == '[') {
								brack++;
							}
							// Decrement i if closing bracket
							if(temp.charAt(i) == ']') {
								brack--;	
							}
						}
						// Append temp to line
						line += temp;
					}
					//System.out.println(line);
				}

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
					/* System.out.println(line + ": " + i + ": " + inLitStr);
					for(int j = 0; j < i; j ++) { System.out.print(" "); }
					System.out.println("^"); */

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
					/* System.out.println(line + ": " + i + ": " + inString);
					for(int j = 0; j < i; j ++) { System.out.print(" "); }
					System.out.println("^"); */

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
			System.out.println(tomlString);
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

	public static void main(String args[]) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/example.toml"));

			/*
			String line = "", result = "";
			while((line = reader.readLine()) != null) {
				result += line;
			}
			*/

			String result = reader.lines().collect(Collectors.joining("\n"));

			System.out.println("Initial\n_______\n" + result);
			System.out.println("\n\n\nFinal\n_____\n" + preProcess(result));
		
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return the int associated with the given key
	 * 
	 * @param key The key
	 */
	public int getInt(String key) {
		return (Integer) this.table.get(key);
	}

	/**
	 * Return the double associated with the given key
	 * 
	 * @param key The key
	 */
	public double getDouble(String key) {
		return (Double) this.table.get(key);
	}

	/**
	 * Return the Long associated with the given key
	 * 
	 * @param key The key
	 */
	public Long getLong(String key) {
		return (Long) this.table.get(key);
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
