// Make sure you import the TOML library as shown below
import java.io.File;
import java.math.BigDecimal;
import Toml.Toml;

/**
 * 	TOML (Tom's Obvious, Minimal Language) Java Implementation
 *  Example.java
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
 *	Execute:	%java -cp target/classes/ Example
 */

/**
 * The Example class is the main class for the example implementation of Toml.java
 * @param
 */
public class Example {
    /**
     * The main function
     * @param args
     */
    public static void main(String args[]) {
        // Create Toml object
        Toml toml = new Toml();
        // Pass full path of filename to parseToml function
        // Use apropriate filename
        toml.parseToml(new File("C:/Users/jrait/SE2/FightFight/SE2/src/main/resources/example.toml"));

        // Output using SE2Toml library's functionalities
        System.out.println("Hi, my name is " + toml.getString("name") + " and I am the database administrator.");
        System.out.println("I was born on " + toml.getOffsetDateTime("dob") + ".");
        System.out.println("Our database is located at: " + toml.getString("server") + ".");
        System.out.println("The connection port is: " + ((BigDecimal) toml.getArray("ports")[0]).intValue() + ".");
        System.out.println("There you can access data from arrays: " + (((Object[]) toml.getArray("data")[0])[0]).toString());
        System.out.println("As well as data like integers: " + ((BigDecimal) ((Object[]) toml.getArray("data")[1])[0]).intValue());
    }
}
