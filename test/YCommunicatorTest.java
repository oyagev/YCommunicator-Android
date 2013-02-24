import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.junit.Test;

import oyagev.projects.YCommunicator.CallbackInterface;
import oyagev.projects.YCommunicator.YCommunicator;

public class YCommunicatorTest {
	
	@Test
	public void testDefaultCallback(){
		YCommunicator comm = new YCommunicator();
		comm.registerDefaultCallback(new DefCallbackTest());
		
		byte[] bytes;
		bytes = new byte[] {5,1,1,2,3,4,0,0,0x7c};
		for (byte b : bytes){
			comm.write(b);
		}
		
			
	}
	
	@Test
	public void testMultipleCallbacks(){
		YCommunicator comm = new YCommunicator();
		comm.registerCallback((byte)1, new DefCallbackTest());
		comm.registerCallback((byte)2, new CallbackTest());
		
		byte[] bytes;
		bytes = new byte[] {5,1,1,2,3,4,0,0,0x7c,
							8,2,4,3,4,6,7,8,0,0,0x7c};
		for (byte b : bytes){
			comm.write(b);
		}
	}
	
	@Test
	public void testDispatch(){
		YCommunicator comm = new YCommunicator();
		comm.dispatch((byte)0, (byte)1, new byte[]{2,3,4,5,6});
		assertEquals(13, comm.available());
		ByteBuffer buff = ByteBuffer.allocate(comm.available());
		buff.put(comm.read());
		assertEquals(12, comm.available());
		
		while(comm.available()>0){
			buff.put(comm.read());
		}
		assertEquals(0, comm.available());
		assertArrayEquals(new byte[]{0,0,1,2,3,4,5,6,0,0,0,0,0x7c}, buff.array());
	}
	
	class DefCallbackTest implements CallbackInterface{
		
		@Override
		public void run(byte type, byte command, byte[] data, byte data_langth) {
			//System.out.println("Running callback 1 ");
			assertEquals(5, type);
			assertEquals(1, command);
			assertArrayEquals(new byte[] {1,2,3}, data);
			
		}
	}
	
	class CallbackTest implements CallbackInterface{
		
		@Override
		public void run(byte type, byte command, byte[] data, byte data_langth) {
			//System.out.println("Running callback 2");
			assertEquals(8, type);
			assertEquals(2, command);
			assertArrayEquals(new byte[] {4,3,4,6,7}, data);
			
		}
	}
	
	
	
	
	
	
}
