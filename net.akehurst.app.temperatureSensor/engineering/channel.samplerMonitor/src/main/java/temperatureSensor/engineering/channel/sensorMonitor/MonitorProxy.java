package temperatureSensor.engineering.channel.sensorMonitor;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

import net.akehurst.application.framework.common.IPort;
import net.akehurst.application.framework.common.annotations.instance.CommandLineArgument;
import net.akehurst.application.framework.common.annotations.instance.ConfiguredValue;
import net.akehurst.application.framework.common.annotations.instance.PortContract;
import net.akehurst.application.framework.common.annotations.instance.PortInstance;
import net.akehurst.application.framework.common.annotations.instance.ServiceReference;
import net.akehurst.application.framework.realisation.AbstractComponent;
import net.akehurst.application.framework.technology.interfaceComms.ChannelIdentity;
import net.akehurst.application.framework.technology.interfaceComms.IPublishSubscribeNotification;
import net.akehurst.application.framework.technology.interfaceComms.IPublishSubscribeRequest;
import net.akehurst.application.framework.technology.interfaceLogging.ILogger;
import net.akehurst.application.framework.technology.interfaceLogging.LogLevel;
import temperatureSensor.computational.interfaceSampler.ISampleSubscriberNotification;
import temperatureSensor.computational.interfaceSampler.Sample;
import temperatureSensor.technology.interfaceSocket.IpAddress;
import temperatureSensor.technology.interfaceSocket.IpPort;

public class MonitorProxy extends AbstractComponent implements ISampleSubscriberNotification, IPublishSubscribeNotification {

	@ServiceReference
	ILogger logger;

	public MonitorProxy(final String id) {
		super(id);
	}

	@CommandLineArgument(required = true, description = "a multicast address (String)")
	IpAddress address;

	@CommandLineArgument(required = true, description = "a port number (Integer)")
	IpPort port;

	@ConfiguredValue(defaultValue = "commsChannel")
	ChannelIdentity channelId;

	@Override
	public void afRun() {
		try {
			final Map<String, Object> channelConfiguration = new HashMap<>();
			channelConfiguration.put("multicastAddress", this.address);
			channelConfiguration.put("port", this.port);
			this.portComms().out(IPublishSubscribeRequest.class).requestPublisherOf(this.channelId, channelConfiguration);
		} catch (final Exception ex) {
			this.logger.log(LogLevel.ERROR, "Failed to run MonitorProxy", ex);
		}
	}

	// --------- ISampleSubscriberNotification ---------
	@Override
	public void publishSample(final Sample sample) {
		try {

			final byte[] bytes = this.serialiseSample(sample);
			this.portComms().out(IPublishSubscribeRequest.class).requestPublish(this.channelId, bytes);

		} catch (final Exception e) {
			this.logger.log(LogLevel.ERROR, "Failed to publish sample", e);
		}
	}

	byte[] serialiseSample(final Sample source) {
		final JsonObject jsonModel = Json.createObjectBuilder().add("identity", source.getSensorId().asPrimitive())
				.add("temperature", source.getTemperature().asPrimitive()).add("timestamp", source.getTimestamp().asPrimitive()).build();

		final StringWriter strWriter = new StringWriter();
		try (JsonWriter jsonWriter = Json.createWriter(strWriter)) {
			jsonWriter.writeObject(jsonModel);
		}

		final String jsonData = strWriter.toString();

		this.logger.log(LogLevel.INFO, "marshalled sample: " + jsonData);
		return jsonData.getBytes();
	}

	// --------- IPublishSubscribeNotification ---------

	@Override
	public void notifyPublication(final ChannelIdentity channelId, final byte[] data) {
		// this is a one way channel, data is not published back to the sampler
	}

	// --------- Ports ---------
	@PortInstance
	@PortContract(provides = IPublishSubscribeNotification.class, requires = IPublishSubscribeRequest.class)
	IPort portComms;

	public IPort portComms() {
		return this.portComms;
	}
}
