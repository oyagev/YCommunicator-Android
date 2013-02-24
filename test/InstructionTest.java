import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.junit.Test;

import oyagev.projects.YCommunicator.Instruction;

public class InstructionTest {
	@Test
	public void testCreateFromBytes(){
		byte[] bytes;
		bytes = new byte[] {2,3,4,5};
		Instruction inst = Instruction.fromByteBuffer(ByteBuffer.wrap(bytes));
		assertEquals(2, inst.getType());
		assertEquals(3, inst.getCommand());
		assertEquals(ByteBuffer.wrap(new byte[]{4,5}), inst.getData());
		
		assertArrayEquals(bytes, inst.toByteBuffer().array());
		
		
	}
}
