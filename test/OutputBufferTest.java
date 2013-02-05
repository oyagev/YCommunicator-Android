import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.junit.Test;

import oyagev.projects.YCommunicator.*;

public class OutputBufferTest {
	
	@Test
	public void TestOutput(){
		OutputBuffer out = new OutputBuffer();
		assertEquals(0, out.available());
		out.dispatch((byte)1, (byte)2, new byte[0]);
		out.dispatch((byte)10, (byte)20, new byte[]{30,40});
		assertEquals(12, out.available());
		assertEquals(2, out.read().intValue());
		assertEquals(1, out.read().intValue());
		assertEquals(2, out.read().intValue());
		assertEquals(0, out.read().intValue());
		assertEquals(0, out.read().intValue());
		
		assertEquals(7, out.available());
		
		assertEquals(4, out.read().intValue());
		assertEquals(10, out.read().intValue());
		assertEquals(20, out.read().intValue());
		assertEquals(30, out.read().intValue());
		assertEquals(40, out.read().intValue());
		assertEquals(0, out.read().intValue());
		assertEquals(0, out.read().intValue());
		
		assertEquals(0, out.available());
		
	}
}
