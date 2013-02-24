
import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.junit.Test;

import oyagev.projects.YCommunicator.Packet;


public class PacketTest {
	
	@Test
    public void testCreatePacket(){
		byte[] bytes = new byte[] {1,2,3,4,5,6,7};
		Packet p = new Packet(bytes);
		assertEquals(0, p.getChecksum());
		assertEquals(bytes.length, p.getPayloadSize());
		ByteBuffer b = p.toByteBuffer();
		assertEquals(bytes.length+Packet.OVERHEAD, b.limit());
		
	}
	
}
