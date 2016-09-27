package temperatureSensor.engineering.channel.sensorMonitor;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;

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
import temperatureSensor.computational.interfaceSampler.ISampleSubscriberRequest;
import temperatureSensor.computational.interfaceSampler.Sample;
import temperatureSensor.computational.interfaceSampler.SensorIdentity;
import temperatureSensor.computational.interfaceSampler.TimeStampMilliseconds;
import temperatureSensor.computational.interfaceSensor.TemperatureCelsius;
import temperatureSensor.technology.interfaceSocket.IpAddress;
import temperatureSensor.technology.interfaceSocket.IpPort;

public class SamplerProxy extends AbstractComponent implements ISampleSubscriberRequest, IPublishSubscribeNotification {

	public SamplerProxy(final String id) {
		super(id);
	}

	@ServiceReference
	ILogger logger;

	@ConfiguredValue(defaultValue = "commsChannel")
	ChannelIdentity channelId;

	@CommandLineArgument(required = true, description = "a multicast address (String)")
	IpAddress address;

	@CommandLineArgument(required = true, description = "a port number (Integer)")
	IpPort port;

	@Override
	public void afRun() {
		this.logger.log(LogLevel.TRACE, "afRun");
		try {
			final Map<String, Object> channelConfiguration = new HashMap<>();
			channelConfiguration.put("multicastAddress", this.address);
			channelConfiguration.put("port", this.port);
			this.portComms().out(IPublishSubscribeRequest.class).requestSubscribeTo(this.channelId, channelConfiguration);
		} catch (final Exception ex) {
			this.logger.log(LogLevel.ERROR, "Failed to run MonitorProxy", ex);
		}
	}

	// --------- ISampleSubscriberRequest ----------

	// --------- IPublishSubscribeNotification ---------
	@Override
	public void notifyPublication(final ChannelIdentity channelId, final byte[] data) {
		try {

			final Sample s = this.unserialiseSample(data);
			this.portSamples().out(ISampleSubscriberNotification.class).publishSample(s);

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	Sample unserialiseSample(final byte[] bytes) {
		final String jsonData = new String(bytes);
		final JsonReader reader = Json.createReader(new StringReader(jsonData));
		final JsonStructure json = reader.read();

		final JsonObject jobj = (JsonObject) json;
		final String strIdentity = jobj.getString("identity");
		final double dblTemperature = jobj.getJsonNumber("temperature").doubleValue();
		final long lngTime = jobj.getJsonNumber("timestamp").longValue();

		final SensorIdentity sensorId = new SensorIdentity(strIdentity);
		final TemperatureCelsius temperature = new TemperatureCelsius(dblTemperature);
		final TimeStampMilliseconds timestamp = new TimeStampMilliseconds(lngTime);
		final Sample s = new Sample(sensorId, temperature, timestamp);

		return s;
	}

	// --------- Ports ---------
	@PortInstance
	@PortContract(provides = IPublishSubscribeNotification.class, requires = IPublishSubscribeRequest.class)
	IPort portComms;

	public IPort portComms() {
		return this.portComms;
	}

	@PortInstance
	@PortContract(provides = ISampleSubscriberRequest.class, requires = ISampleSubscriberNotification.class)
	IPort portSamples;

	public IPort portSamples() {
		return this.portSamples;
	}
}
