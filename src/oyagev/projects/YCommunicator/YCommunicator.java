package oyagev.projects.YCommunicator;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

public class YCommunicator {
	
	InputBuffer input;
	OutputBuffer output;
	
	
	public YCommunicator() {
		// TODO Auto-generated constructor stub
		input = new InputBuffer();
		output = new OutputBuffer();
	}
	
	public void write(byte data){
		input.write(data);
		if (input.hasPackets()){
			processIncomingPacket(input.popPacket());
		}
	}
	public byte read(){
		return output.read().byteValue();
	}
	public int available() {
		return output.available();
	}
	public void dispatch(byte type, byte command, byte[] data){
		output.dispatch(type, command, ByteBuffer.wrap(data));
	}
	public void registerDefaultCallback(CallbackInterface callback){
		
	}
	public void registerCallback(byte command, CallbackInterface callback){
		
	}
	
	private void processIncomingPacket(Packet packet){
		Instruction inst = Instruction.fromByteBuffer(packet.getPayload());
		
	}
}
