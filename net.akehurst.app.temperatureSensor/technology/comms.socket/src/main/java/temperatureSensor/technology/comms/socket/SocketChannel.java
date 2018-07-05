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

	public SocketChannel(final String id, final IpAddress address, final IpPort port, final ISocketListener socketListener) {
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

	@Override
	public void afTerminate() {
		// TODO Auto-generated method stub

	}

	public void listen() {
		try {
			if (null != this.clientSocket) {
				final DataInputStream inpStrm = new DataInputStream(this.clientSocket.getInputStream());
				while (true) {
					final String s = inpStrm.readUTF();
					final byte[] bytes = s.getBytes();
					this.logger.log(LogLevel.DEBUG, "received " + bytes.length + "bytes");
					this.socketListener.receive(bytes);
				}
			} else if (null != this.multiSocket) {
				while (true) {
					final byte[] buf = new byte[this.multiSocket.getReceiveBufferSize()];
					final DatagramPacket pkt = new DatagramPacket(buf, buf.length);
					this.multiSocket.receive(pkt);
					this.logger.log(LogLevel.DEBUG, "received " + pkt.getLength() + "bytes");
					final byte[] bytes = Arrays.copyOf(buf, pkt.getLength());
					this.socketListener.receive(bytes);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void tryPublish(final byte[] bytes) throws SocketException {
		MulticastSocket skt = null;
		try {
			this.logger.log(LogLevel.DEBUG, "Trying to publish to " + this.address + " : " + this.port);
			skt = new MulticastSocket();
			final InetAddress group = InetAddress.getByName(this.address.asPrimitive());
			final DatagramPacket pkt = new DatagramPacket(bytes, bytes.length, group, this.port.asPrimitive());
			skt.send(pkt);
		} catch (final Exception e) {
			throw new SocketException(e.getMessage(), e);
		} finally {
			if (null != skt) {
				try {
					skt.close();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void startSubscriber(final IpAddress address, final IpPort port, final int retries, final int interTryDelay) {
		final int tries = 0;
		// while (this.clientSocket == null && tries < retries) {
		// tries++;
		try {
			this.logger.log(LogLevel.DEBUG, "Trying to connect to " + address + " : " + port);
			this.multiSocket = new MulticastSocket(port.asPrimitive());
			final InetAddress group = InetAddress.getByName(address.asPrimitive());
			this.multiSocket.joinGroup(group);
			this.logger.log(LogLevel.INFO, "Connected to " + address + " : " + port);

			this.listenerThread = new Thread(() -> this.listen(), "listenerThread");
			this.listenerThread.start();

		} catch (final Exception e) {
			if (tries >= retries) {
				e.printStackTrace();
				// end
			} else {
				try {
					Thread.sleep(interTryDelay);
				} catch (final InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		// }
	}

	public void startServer() {
		try {
			this.logger.log(LogLevel.INFO, "Waiting for client");

			this.serverThread = new Thread((Runnable) () -> {
				try {
					SocketChannel.this.serverSocket = new ServerSocket(SocketChannel.this.port.asPrimitive());
					while (true) {
						final Socket skt = SocketChannel.this.serverSocket.accept();
						final DataInputStream inpStrm = new DataInputStream(skt.getInputStream());
						try {
							while (true) {
								final String s = inpStrm.readUTF();
								final byte[] bytes = s.getBytes();
								SocketChannel.this.logger.log(LogLevel.INFO, "received " + bytes.length + "bytes");
								SocketChannel.this.socketListener.receive(bytes);

							}
						} catch (final Exception e1) {
						}
					}
				} catch (final Exception e2) {
					e2.printStackTrace();
				}
			}, "serverThread");
			this.serverThread.start();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void startCient(final int retries, final int interTryDelay) {
		int tries = 0;
		while (this.clientSocket == null && tries < retries) {
			tries++;
			try {
				this.logger.log(LogLevel.INFO, "Trying to connect to " + this.address + " : " + this.port);
				this.clientSocket = new Socket(this.address.asPrimitive(), this.port.asPrimitive());
				this.logger.log(LogLevel.INFO, "Connected to " + this.address + " : " + this.port);

				this.listenerThread = new Thread(() -> this.listen(), "listenerThread");
				this.listenerThread.start();

			} catch (final Exception e) {
				if (tries >= retries) {
					e.printStackTrace();
					// end
				} else {
					try {
						Thread.sleep(interTryDelay);
					} catch (final InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	public void trySend(final byte[] bytes) throws SocketException {
		Socket skt = null;
		try {
			this.logger.log(LogLevel.DEBUG, "Trying to connect to " + this.address + " : " + this.port);
			skt = new Socket(this.address.asPrimitive(), this.port.asPrimitive());
			this.logger.log(LogLevel.DEBUG, "Connected to " + this.address + " : " + this.port);

			final DataOutputStream os = new DataOutputStream(skt.getOutputStream());
			os.writeUTF(new String(bytes));

		} catch (final Exception e) {
			throw new SocketException(e.getMessage(), e);
		} finally {
			if (null != skt) {
				try {
					skt.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void send(final byte[] bytes) {
		try {
			this.logger.log(LogLevel.INFO, "sending " + bytes.length + " bytes");
			final DataOutputStream os = new DataOutputStream(this.clientSocket.getOutputStream());
			final String str = new String(bytes);
			os.writeUTF(str);

		} catch (final Exception e) {
			e.printStackTrace();
		}

	}
}
