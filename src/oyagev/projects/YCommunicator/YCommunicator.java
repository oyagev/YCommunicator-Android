package oyagev.projects.YCommunicator;

import java.nio.ByteBuffer;
import java.util.Hashtable;


public class YCommunicator {
	
	InputBuffer input;
	OutputBuffer output;
	
	CallbackInterface defaultCallback = null;
	Hashtable<Byte, CallbackInterface> callbacks;
	
	
	public YCommunicator() {
		// TODO Auto-generated constructor stub
		input = new InputBuffer();
		output = new OutputBuffer();
		callbacks = new Hashtable<Byte, CallbackInterface>();
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
		defaultCallback = callback;
	}
	public void registerCallback(byte command, CallbackInterface callback){
		callbacks.put(new Byte(command), callback);
	}
	
	private void processIncomingPacket(Packet packet){
		
		CallbackInterface callback = null;
		
		Instruction inst = Instruction.fromByteBuffer(packet.getPayload());
		Byte command = new Byte((byte)inst.getCommand());
		
		
		if (callbacks.containsKey(command)){
			callback = callbacks.get(command);
		}else if (defaultCallback != null){
			callback = defaultCallback;
		}
		if (callback != null){
			callback.run((byte)inst.getType(), command.byteValue(), inst.getData().array(), (byte)inst.getData().capacity());
		}
	}
}
