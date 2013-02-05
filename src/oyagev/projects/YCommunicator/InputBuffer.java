package oyagev.projects.YCommunicator;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

public class InputBuffer {
	
	
	private int payload_length ;
	private ByteBuffer payload;
	private int checksum ;
	private int received;
	private int total_expected;
	
	private Queue<Packet> packets;
	
	public InputBuffer(){
		packets = new LinkedList<Packet>();
		reset();
	}
	

	
	public void write(byte data){
		
		if (payload_length == 0){
			payload_length = (int) data;
			payload = ByteBuffer.allocate(payload_length);
			total_expected = payload_length+3;
		}else if (payload.hasRemaining()){
			payload.put(data);
		}else if (received < total_expected){
			checksum = checksum << 8 + data;
			
		}
		received++;
		
		if (received==total_expected){
			createPacket();
			reset();
		}
	}
	
	public boolean hasPackets(){
		return packets.size() > 0;
	}
	public int availablePackets(){
		return packets.size();
	}
	
	public Packet popPacket(){
		if (hasPackets()){
			return packets.poll();
		}
		return null;
	}
	
	private void createPacket(){
		Packet packet = Packet.fromByteBuffer(payload);
		packets.add(packet);
	}
	
	private void reset(){
		received = 0;
		payload_length = 0;
		payload = null;
		checksum = 0;
		total_expected = 3;
		
	}
}
