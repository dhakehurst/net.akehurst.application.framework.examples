package temperatureSensor.technology.comms.socket;

import java.util.HashMap;
import java.util.Map;

import net.akehurst.application.framework.common.IApplicationFramework;
import net.akehurst.application.framework.common.IPort;
import net.akehurst.application.framework.common.annotations.instance.PortContract;
import net.akehurst.application.framework.common.annotations.instance.PortInstance;
import net.akehurst.application.framework.common.annotations.instance.ServiceReference;
import net.akehurst.application.framework.realisation.AbstractComponent;
import net.akehurst.application.framework.technology.interfaceComms.ChannelIdentity;
import net.akehurst.application.framework.technology.interfaceComms.IPublishSubscribeNotification;
import net.akehurst.application.framework.technology.interfaceComms.IPublishSubscribeRequest;
import net.akehurst.application.framework.technology.interfaceComms.ISenderReceiverDestination;
import net.akehurst.application.framework.technology.interfaceComms.ISenderReceiverNotification;
import net.akehurst.application.framework.technology.interfaceComms.ISenderReceiverRequest;
import net.akehurst.application.framework.technology.interfaceComms.PublishSubscribeException;
import net.akehurst.application.framework.technology.interfaceLogging.ILogger;
import net.akehurst.application.framework.technology.interfaceLogging.LogLevel;
import temperatureSensor.technology.interfaceSocket.ISocketListener;
import temperatureSensor.technology.interfaceSocket.IpAddress;
import temperatureSensor.technology.interfaceSocket.IpPort;

public class SocketComms extends AbstractComponent implements ISenderReceiverRequest, IPublishSubscribeRequest {

	public SocketComms(final String id) {
		super(id);
		this.channels = new HashMap<>();
		this.pubSubChannels = new HashMap<>();
	}

	@ServiceReference
	IApplicationFramework af;

	@ServiceReference
	ILogger logger;

	Map<ChannelIdentity, SocketChannel> channels;
	Map<ChannelIdentity, MulticastSocketChannel> pubSubChannels;

	private ISocketListener socketListener;

	public ISocketListener getSocketListener() {
		return this.socketListener;
	}

	public void setSocketListener(final ISocketListener socketListener) {
		this.socketListener = socketListener;
	}

	// ---------- ISenderReceiverRequest ---------
	@Override
	public void requestSenderOf() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestReceiverFor() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestSendMessage(final ISenderReceiverDestination destination, final Map<String, Object> data) {
		// TODO Auto-generated method stub

	}

	// ---------- IPublishSubscribeRequest ---------
	@Override
	public void requestPublisherOf(final ChannelIdentity channelId, final Map<String, Object> channelConfiguration) throws PublishSubscribeException {
		try {
			final IpAddress multicastAddress = (IpAddress) channelConfiguration.get("multicastAddress");
			final IpPort port = (IpPort) channelConfiguration.get("port");
			final String id = this.afId() + "[" + channelId.getValue() + "]";
			final MulticastSocketChannel newChannel = new MulticastSocketChannel(id, multicastAddress, port, (bytes) -> {

			});
			this.af.injectIntoActiveObject(newChannel);
			this.pubSubChannels.put(channelId, newChannel);
		} catch (final Exception ex) {
			throw new PublishSubscribeException("Failed to create publisher", ex);
		}
	};

	@Override
	public void requestSubscribeTo(final ChannelIdentity channelId, final Map<String, Object> channelConfiguration) throws PublishSubscribeException {
		try {
			final IpAddress multicastAddress = (IpAddress) channelConfiguration.get("multicastAddress");
			final IpPort port = (IpPort) channelConfiguration.get("port");
			final String id = this.afId() + "[" + channelId.getValue() + "]";
			final MulticastSocketChannel newChannel = new MulticastSocketChannel(id, multicastAddress, port, (bytes) -> {
				this.portComms().out(IPublishSubscribeNotification.class).notifyPublication(channelId, bytes);
			});
			this.af.injectIntoActiveObject(newChannel);
			this.pubSubChannels.put(channelId, newChannel);
			newChannel.afStart();
		} catch (final Exception ex) {
			throw new PublishSubscribeException("Failed to create publisher", ex);
		}
	}

	@Override
	public <T> void requestPublish(final ChannelIdentity channelId, final byte[] data) throws PublishSubscribeException {
		try {
			final MulticastSocketChannel chan = this.pubSubChannels.get(channelId);
			if (null == chan) {
				this.logger.log(LogLevel.ERROR, "Trying to publish but can't find channel with id " + channelId.getValue());
			} else {
				chan.tryPublish(data);
			}
		} catch (final Exception ex) {
			throw new PublishSubscribeException("Failed to publish data on channel " + channelId, ex);
		}
	}

	// --------------------- Client -------------------------------

	// ---------- Ports ---------
	@PortInstance
	@PortContract(provides = ISenderReceiverRequest.class, requires = ISenderReceiverNotification.class)
	@PortContract(provides = IPublishSubscribeRequest.class, requires = IPublishSubscribeNotification.class)
	IPort portComms;

	public IPort portComms() {
		return this.portComms;
	}
}