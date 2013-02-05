package oyagev.projects.YCommunicator;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

public class OutputBuffer {
	Queue<Byte> bytes;
	
	public OutputBuffer(){
		bytes = new LinkedList<Byte>();
	}
	
	public void dispatch(byte type, byte command, ByteBuffer data){
		data.rewind();
		Instruction inst = new Instruction((short)type, (short)command, data);
		Packet packet = new Packet(inst.toByteBuffer());
		ByteBuffer buff = packet.toByteBuffer();
		while(buff.remaining()>0){
			bytes.add(new Byte(buff.get()));
		}
	}
	public void dispatch(byte type, byte command, byte[] data){
		dispatch(type, command, ByteBuffer.wrap(data));
	}
	
	public int available(){
		return bytes.size();
	}
	public Byte read(){
		return bytes.remove();
	}
	
}
