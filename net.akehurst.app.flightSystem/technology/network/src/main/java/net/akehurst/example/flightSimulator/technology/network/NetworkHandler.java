package net.akehurst.example.flightSimulator.technology.network;

import java.util.Map;

import net.akehurst.application.framework.realisation.AbstractActiveSignalProcessingObject;
import net.akehurst.application.framework.technology.interfaceComms.ChannelIdentity;
import net.akehurst.application.framework.technology.interfaceComms.IPublishSubscribeNotification;
import net.akehurst.application.framework.technology.interfaceComms.IPublishSubscribeRequest;
import net.akehurst.application.framework.technology.interfaceComms.PublishSubscribeException;

public class NetworkHandler extends AbstractActiveSignalProcessingObject implements IPublishSubscribeRequest {

	public NetworkHandler(final String afId) {
		super(afId);
	}

	private Map<ChannelIdentity, IPublishSubscribeNotification> subscribers;

	@Override
	public void requestSubscribeTo(final ChannelIdentity channelId, final Map<String, Object> channelConfiguration) throws PublishSubscribeException {
		super.submit("requestSubscribeTo", () -> {

		});
	}

	@Override
	public void requestPublisherOf(final ChannelIdentity channelId, final Map<String, Object> channelConfiguration) throws PublishSubscribeException {
		super.submit("requestPublisherOf", () -> {

		});
	}

	@Override
	public <T> void requestPublish(final ChannelIdentity channelId, final byte[] data) throws PublishSubscribeException {
		super.submit("requestPublish", () -> {
			for (final IPublishSubscribeNotification sub : this.subscribers) {
				sub.notifyPublication(channelId, data);
			}
		});
	}

}
