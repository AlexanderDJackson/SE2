// Make sure you import the TOML library as shown below
//import SE2Toml.java;

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
        // Pass filename to parseToml function
        toml.parseToml(new File("./example/resources/example.toml"));

        // Output using SE2Toml library's functionalities
        System.out.println("Hi, my name is " + toml.getMap("owner").getString("name") + " and I am the database administrator.");
        System.out.println("I was born on " + toml.getMap("owner").getDateTime("dob") + ".");
        System.out.println("Our database is located at: " + toml.getMap("databsae").getString("server") + ".");
        System.out.println("The connection port is: " + (int) toml.getMap("databsae").getArray("ports")[0] + ".");
        System.out.println("The connection port is: " + (int) toml.getMap("databsae").getArray("ports")[0] + ".");
        System.out.println("There you can access data like: " + (((Object[]) toml.getMap("clients").getArray("data")[0])[0]).toString();
        System.out.println("As well as data like: " + ((int) ((Object[]) toml.getMap("clients").getArray("data")[1])[0]).toString();
    }
}
