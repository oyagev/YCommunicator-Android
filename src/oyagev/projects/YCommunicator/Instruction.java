package oyagev.projects.YCommunicator;

import java.nio.ByteBuffer;


public class Instruction {

	private short type = 0;
	private short command = 0;
	private ByteBuffer data;
	
	public Instruction() {
		type=0;
		command=0;
		data = ByteBuffer.allocate(0);
	}
	public Instruction(short type, short command, ByteBuffer data ){
		this.setType(type);
		this.setCommand(command);
		this.setData(data);
	}
	public short getType() {
		return type;
	}
	public void setType(short type) {
		this.type = type;
	}
	public short getCommand() {
		return command;
	}
	public void setCommand(short command) {
		this.command = command;
	}
	public ByteBuffer getData() {
		return data;
	}
	public void setData(ByteBuffer data) {
		this.data = data;
	}
	public ByteBuffer toByteBuffer(){
		ByteBuffer buff = ByteBuffer.allocate(getData().capacity() + 2);
		buff.put((byte)getType());
		buff.put((byte)getCommand());
		buff.put(getData());
		getData().rewind();
		buff.rewind();
		return buff;
	}
	
	
	public static Instruction fromByteBuffer(ByteBuffer buff){
		buff.rewind();
		short type = (short)buff.get();
		short command = (short)buff.get();
		
		int remaining = buff.remaining();
		byte data_arr[] = new byte[remaining];
		buff.get(data_arr);
		ByteBuffer data = ByteBuffer.wrap(data_arr);
		return new Instruction(type,command,data);
	}
}
