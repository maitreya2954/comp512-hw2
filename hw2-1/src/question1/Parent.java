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

public class Parent extends Thread {
	
	private Thread child = null;
	
	/**
	 * setChild set the child instace in the parent instance
	 * @param child
	 */
	public void setChild(Thread child) {
		this.child = child;
	}

	public static final Logger LOGGER = Logger.getLogger(Parent.class.getSimpleName());
	@Override
	public void run() {
		LOGGER.log(Level.INFO, "Parent thread started");
		try {
			// Get the sink channel of global pipe
			Pipe.SinkChannel sinkChannel = Main.PIPE.sink();
			String data = "COMP 512 pipe programming parent";
			ByteBuffer buffer = ByteBuffer.allocate(512);
			buffer.clear();
			//Write data to buffer
			buffer.put(data.getBytes());
			buffer.flip();
			while(buffer.hasRemaining()) {
				sinkChannel.write(buffer); // Write data in buffer into the sink channel of the pipe
			}
			LOGGER.log(Level.INFO, "Parent message sent");
//			Thread.sleep(2000L);
			Pipe.SourceChannel sourceChannel = Main.PIPE.source();
			StringBuilder sb = new StringBuilder();
			buffer = ByteBuffer.allocate(512);
			while (sourceChannel.read(buffer) > 0) {
				buffer.flip();
				while (buffer.hasRemaining()) {
					char ch = (char) buffer.get();
					sb.append(ch);
				}
			}
			buffer.clear();
			LOGGER.log(Level.INFO, "Parent received child's message");
			LOGGER.log(Level.INFO, "Message received: {0}", sb);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Exception while running parent thread");
			e.printStackTrace();
			if (this.child.isAlive()) {
				this.child.interrupt();
			}
			return;
		}
	}

}
