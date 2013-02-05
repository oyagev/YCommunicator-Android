package oyagev.projects.YCommunicator;

public interface CallbackInterface {
	abstract public void run(byte type, byte command, byte[] data, byte data_langth);
}
