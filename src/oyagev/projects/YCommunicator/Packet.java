package oyagev.projects.YCommunicator;

import java.nio.ByteBuffer;

public class Packet {
	
	public static int OVERHEAD=3;
	
	ByteBuffer payload;
	
	
	public Packet(byte[] payload){
		setPayload(ByteBuffer.wrap(payload));
	}
	public Packet(ByteBuffer payload){
		setPayload(payload);
	}
	
	public ByteBuffer getPayload(){
		return payload;
	}
	
	public int getPayloadSize() {
		return payload.capacity();
	}
	private void setPayload(ByteBuffer payload){
		this.payload = payload;
	}
	
	
	public int getChecksum() {
		return 0;
	}
	
	public ByteBuffer toByteBuffer(){
		int size = this.getPayloadSize() + OVERHEAD;
		ByteBuffer buff = ByteBuffer.allocate(size);
		
		buff.put((byte) this.getPayloadSize());
		buff.put(payload);
		buff.putShort((short) this.getChecksum());
		payload.rewind();
		buff.rewind();
		return buff;
	}
	
	public static Packet fromByteBuffer(ByteBuffer payload){
		return new Packet(payload);
	}
	
}
