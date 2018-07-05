package temperatureSensor.technology.comms.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
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

	public MulticastSocketChannel(final String id, final IpAddress address, final IpPort port, final ISocketListener socketListener) {
		super(id);
		this.address = address;
		this.port = port;
		this.socketListener = socketListener;
	}

	// @ConfiguredValue(defaultValue="5")
	// Integer retries;
	//
	// @ConfiguredValue(defaultValue="500")
	// Integer interTryDelay;

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
					try {
						this.multiSocket.receive(pkt);
						this.logger.log(LogLevel.DEBUG, "received " + pkt.getLength() + "bytes");
						final byte[] bytes = Arrays.copyOf(buf, pkt.getLength());
						this.socketListener.receive(bytes);
					} catch (final SocketTimeoutException e) {
						this.logger.log(LogLevel.DEBUG, "timeout ");
					}

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
			this.logger.log(LogLevel.DEBUG, "published to " + this.address + " : " + this.port);
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

	public void startSubscriber(final int retries, final int interTryDelay) {
		final int tries = 0;
		try {
			this.logger.log(LogLevel.DEBUG, "Trying to connect to " + this.address + " : " + this.port);
			this.multiSocket = new MulticastSocket(this.port.asPrimitive());
			this.multiSocket.setSoTimeout(500);
			final InetAddress group = InetAddress.getByName(this.address.asPrimitive());
			this.multiSocket.joinGroup(group);
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
