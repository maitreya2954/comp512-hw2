/**
 * @author Siddharth Rayabharam
 * @class COMP 512 homework 2
 * @instructor Dr. Truong Tran
 */

package question1;

import java.nio.channels.Pipe;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
	
	public static final Logger LOGGER = Logger.getLogger(Main.class.getSimpleName());
	
	public static Pipe PIPE;
	
	static {
		System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
	}

	public static void main(String[] args) throws Exception {
		LOGGER.log(Level.INFO, "Initializing Pipe");
		try {
			// Create a global pipe which can be used by both parent and child
			PIPE = Pipe.open();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error initializing pipe: ", e);
			throw e;
		}
		// Create a parent
		Parent parent = new Parent();
		// Create a child
		Child child = new Child(parent);
		parent.setChild(child);
		// Start parent and child
		parent.start();
		child.start();
		// Wait till both the threads die
		parent.join();
		child.join();
		LOGGER.log(Level.INFO, "Program execution completed");
	}

}
