/**
 * @author Siddharth Rayabharam
 * @class COMP 512 homework 2
 * @instructor Dr. Truong Tran
 */


package question1;

import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author siddharthrayabharam
 * Child
 *
 */
public class Child extends Thread {
	
	public static final Logger LOGGER = Logger.getLogger(Child.class.getSimpleName());
	
	private Thread parent = null;
	
	public Child(Thread parent) {
		this.parent = parent;
	}
	
	/**
	 * setParent sets the parent thread in child instance
	 * @param parent
	 */
	public void setParent(Thread parent) {
		this.parent = parent;
	}

	@Override
	public void run() {
		LOGGER.log(Level.INFO, "Child thread started");
		try {
			// Get the source channel of global pipe
			Pipe.SourceChannel sourceChannel = Main.PIPE.source();
			StringBuilder sb = new StringBuilder();
			// Open buffer to write the data to it
			ByteBuffer buffer = ByteBuffer.allocate(512);
			while (sourceChannel.read(buffer) > 0) { // read data from the source channel
				buffer.flip();
				while (buffer.hasRemaining()) {
					char ch = (char) buffer.get(); 
					sb.append(ch);
				}
			}
			buffer.clear();
			LOGGER.log(Level.INFO, "Child received parent's message");
			LOGGER.log(Level.INFO, "Message received: {0}", sb);
			
			// Convert the lower case to upper case and upper case to lower case
			for (int i = 0; i < sb.length(); i++) {
				char ch = sb.charAt(i);
				if (Character.isLowerCase(ch)) {
					sb.setCharAt(i, Character.toUpperCase(ch));
				} else {
					sb.setCharAt(i, Character.toLowerCase(ch));
				}
			}
			
			sb.append(" CHILD");
			LOGGER.log(Level.INFO, "Message case conversion is completed");
			// Get the sink channel of global pipe
			Pipe.SinkChannel sinkChannel = Main.PIPE.sink();
			buffer = ByteBuffer.allocate(512);
			buffer.clear();
			//Write data to buffer
			buffer.put(sb.toString().getBytes());
			buffer.flip();
			while(buffer.hasRemaining()) {
				sinkChannel.write(buffer); // Write data in buffer into the sink channel of the pipe
			}
			LOGGER.log(Level.INFO, "Child message sent");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Exception while running child thread");
			e.printStackTrace();
			// Kill parent if exception occurs in child
			if (this.parent.isAlive()) {
				this.parent.interrupt();
			}
			return;
		}
	}

}
