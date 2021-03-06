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
	
	private Queue<Byte> input_buff;
	private boolean escape_next;
	
	private Queue<Packet> packets;
	
	public InputBuffer(){
		packets = new LinkedList<Packet>();
		
		reset();
	}
	

	
	public void write(byte data){
		
		if (data == 0x7c && !escape_next){
			createPacket();
			reset();
		}else if (data == 0 && !escape_next){
			escape_next = true;
		}else {
			input_buff.add(new Byte(data));
			escape_next = false;
		}
		
		
		/*
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
		*/
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
		int payload_size = input_buff.size() - 2;
		if (payload_size > 0){
			ByteBuffer tmp_payload = ByteBuffer.allocate(payload_size);
			for(int i=0;i<payload_size;i++){
				tmp_payload.put(input_buff.remove().byteValue());
			}
			short checksum =0;
			byte element;
			while (!input_buff.isEmpty()){
				element = input_buff.remove().byteValue();
				checksum = (short) (checksum << 8 + element);
			}
			
			Packet packet = Packet.fromByteBuffer(tmp_payload);
			packets.add(packet);
		}
	}
	
	private void reset(){
		received = 0;
		payload_length = 0;
		payload = null;
		checksum = 0;
		total_expected = 3;
		input_buff = new LinkedList<Byte>();
		escape_next = false;
		
	}
}
