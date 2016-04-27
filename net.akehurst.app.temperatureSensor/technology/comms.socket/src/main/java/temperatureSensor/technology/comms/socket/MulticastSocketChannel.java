package temperatureSensor.technology.comms.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import net.akehurst.application.framework.common.annotations.instance.ServiceReference;
import net.akehurst.application.framework.realisation.AbstractActiveObject;
import net.akehurst.application.framework.technology.interfaceLogging.ILogger;
import net.akehurst.application.framework.technology.interfaceLogging.LogLevel;
import temperatureSensor.technology.interfaceSocket.ISocketListener;
import temperatureSensor.technology.interfaceSocket.IpAddress;
import temperatureSensor.technology.interfaceSocket.IpPort;
import temperatureSensor.technology.interfaceSocket.SocketException;

public class MulticastSocketChannel extends AbstractActiveObject {

	@ServiceReference
	ILogger logger;
	
	public MulticastSocketChannel(String id, IpAddress address, IpPort port, ISocketListener socketListener) {
		super(id);
		this.address = address;
		this.port = port;
		this.socketListener = socketListener;
	}
	
//	@ConfiguredValue(defaultValue="5")
//	Integer retries;
//	
//	@ConfiguredValue(defaultValue="500")
//	Integer interTryDelay;
	
	IpAddress address;
	IpPort port;
	
	Socket clientSocket;
	ServerSocket serverSocket;
	MulticastSocket multiSocket;
	
	Thread serverThread;
	Thread listenerThread;
	
	ISocketListener socketListener;
	
	@Override
	public void afRun() {
		this.startSubscriber(5, 500);
	}
	
	public void listen() {
		try {
			if (null!=this.clientSocket) {
				DataInputStream inpStrm = new DataInputStream(clientSocket.getInputStream());
				while (true) {
					String s = inpStrm.readUTF();
					byte[] bytes = s.getBytes();
					logger.log(LogLevel.DEBUG,"received " + bytes.length + "bytes");
					this.socketListener.receive(bytes);
				}
			} else if (null!=this.multiSocket) {
				while(true) {
					byte[] buf = new byte[ this.multiSocket.getReceiveBufferSize() ];
					DatagramPacket pkt = new DatagramPacket(buf, buf.length);
					this.multiSocket.receive(pkt);
					logger.log(LogLevel.DEBUG,"received " + pkt.getLength() + "bytes");
					byte[] bytes = Arrays.copyOf(buf, pkt.getLength());
					this.socketListener.receive(bytes);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void tryPublish(byte[] bytes) throws SocketException {
		MulticastSocket skt = null;
		try {
			logger.log(LogLevel.DEBUG, "Trying to publish to " + address + " : " + port);
			skt = new MulticastSocket();
			InetAddress group = InetAddress.getByName(this.address.asPrimitive());
			DatagramPacket pkt = new DatagramPacket(bytes,  bytes.length, group, this.port.asPrimitive());
			skt.send(pkt);
			logger.log(LogLevel.DEBUG, "published to " + address + " : " + port);
		} catch (Exception e) {
			throw new SocketException(e.getMessage(), e);
		} finally {
			if (null != skt) {
				try {
					skt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	

	public void startSubscriber(int retries, int interTryDelay) {
		int tries = 0;
			try {
				logger.log(LogLevel.DEBUG, "Trying to connect to " + address + " : " + port);
				this.multiSocket = new MulticastSocket(port.asPrimitive());
				InetAddress group = InetAddress.getByName(address.asPrimitive());
				this.multiSocket.joinGroup(group);
				logger.log(LogLevel.INFO,"Connected to " + address + " : " + port);

				this.listenerThread = new Thread(()->this.listen(), "listenerThread");
				this.listenerThread.start();

			} catch (Exception e) {
				if (tries >= retries) {
					e.printStackTrace();
					// end
				} else {
					try {
						Thread.sleep(interTryDelay);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
	}
	
	
	public void send(byte[] bytes) {
		try {
			logger.log(LogLevel.INFO,"sending " + bytes.length + " bytes");
			DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());
			String str = new String(bytes);
			os.writeUTF(str);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
