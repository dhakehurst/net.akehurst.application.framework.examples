package temperatureSensor.technology.comms.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

public class SocketChannel extends AbstractActiveObject {

	@ServiceReference
	ILogger logger;
	
	public SocketChannel(String id, IpAddress address, IpPort port, ISocketListener socketListener) {
		super(id);
		this.address = address;
		this.port = port;
		this.socketListener = socketListener;
	}
	
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
		this.startServer();
		this.startCient(5, 500);
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
	

	public void startSubscriber(IpAddress address, IpPort port, int retries, int interTryDelay) {
		int tries = 0;
//		while (this.clientSocket == null && tries < retries) {
//			tries++;
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
//		}
	}
	
	public void startServer() {
		try {
			this.logger.log(LogLevel.INFO,"Waiting for client");

			this.serverThread = new Thread(new Runnable() {
				public void run() {
					try {
						serverSocket = new ServerSocket(port.asPrimitive());
						while (true) {
							Socket skt = serverSocket.accept();
							DataInputStream inpStrm = new DataInputStream(skt.getInputStream());
							try {
								while (true) {
									String s = inpStrm.readUTF();
									byte[] bytes = s.getBytes();
									logger.log(LogLevel.INFO,"received " + bytes.length + "bytes");
									socketListener.receive(bytes);

								}
							} catch (Exception e) {
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, "serverThread");
			this.serverThread.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startCient( int retries, int interTryDelay) {
		int tries = 0;
		while (this.clientSocket == null && tries < retries) {
			tries++;
			try {
				logger.log(LogLevel.INFO,"Trying to connect to " + address + " : " + port);
				this.clientSocket = new Socket(this.address.asPrimitive(), this.port.asPrimitive());
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
	}
	
	public void trySend(byte[] bytes) throws SocketException {
		Socket skt = null;
		try {
			logger.log(LogLevel.DEBUG,"Trying to connect to " + address + " : " + port);
			skt = new Socket(address.asPrimitive(), port.asPrimitive());
			logger.log(LogLevel.DEBUG,"Connected to " + address + " : " + port);

			DataOutputStream os = new DataOutputStream(skt.getOutputStream());
			os.writeUTF(new String(bytes));

		} catch (Exception e) {
			throw new SocketException(e.getMessage(), e);
		} finally {
			if (null != skt) {
				try {
					skt.close();
				} catch (IOException e) {
					e.printStackTrace();
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
