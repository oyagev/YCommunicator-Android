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
		byte curr;
		while(buff.remaining()>0){
			curr = buff.get();
			if (curr == 0 || curr == 0x7c){
				bytes.add(new Byte((byte)0x00));
			}
			bytes.add(new Byte(curr));
		}
		bytes.add(new Byte((byte)0x7c));
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
