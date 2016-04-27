package temperatureSensor.technology.interfaceSocket;

public interface ISocket {
	void startServer(IpPort port);
	void startCient(IpAddress address, IpPort port, int retries, int interTryDelay);
	
	void send(byte[] bytes);
	void trySend(byte[] bytes, IpAddress address, IpPort port) throws SocketException;
	
	void tryPublish(byte[] bytes, IpAddress address, IpPort port) throws SocketException;
	
}
