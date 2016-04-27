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

	public SamplerProxy(String id) {
		super(id);
	}

	@ServiceReference
	ILogger logger;

	@ConfiguredValue(defaultValue = "commsChannel")
	ChannelIdentity channelId;

	@CommandLineArgument(required=true, description="a multicast address (String)")
	IpAddress address;

	@CommandLineArgument(required=true, description="a port number (Integer)")
	IpPort port;

	@Override
	public void afRun() {
		logger.log(LogLevel.TRACE, "afRun");
		try {
			Map<String, Object> channelConfiguration = new HashMap<>();
			channelConfiguration.put("multicastAddress", this.address);
			channelConfiguration.put("port", this.port);
			this.portComms().out(IPublishSubscribeRequest.class).requestSubscribeTo(channelId, channelConfiguration);
		} catch (Exception ex) {
			logger.log(LogLevel.ERROR, "Failed to run MonitorProxy", ex);
		}
	}

	// --------- ISampleSubscriberRequest ----------

	// --------- IPublishSubscribeNotification ---------
	@Override
	public void notifyPublication(ChannelIdentity channelId, byte[] data) {
		try {

			Sample s = this.unserialiseSample(data);
			portSamples().out(ISampleSubscriberNotification.class).publishSample(s);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Sample unserialiseSample(byte[] bytes) {
		String jsonData = new String(bytes);
		JsonReader reader = Json.createReader(new StringReader(jsonData));
		JsonStructure json = reader.read();

		JsonObject jobj = (JsonObject) json;
		String strIdentity = jobj.getString("identity");
		double dblTemperature = jobj.getJsonNumber("temperature").doubleValue();
		long lngTime = jobj.getJsonNumber("timestamp").longValue();

		SensorIdentity sensorId = new SensorIdentity(strIdentity);
		TemperatureCelsius temperature = new TemperatureCelsius(dblTemperature);
		TimeStampMilliseconds timestamp = new TimeStampMilliseconds(lngTime);
		Sample s = new Sample(sensorId, temperature, timestamp);

		return s;
	}

	// --------- Ports ---------
	@PortInstance(
		provides = { IPublishSubscribeNotification.class },
		requires = { IPublishSubscribeRequest.class }
	)
	IPort portComms;

	public IPort portComms() {
		return this.portComms;
	}

	@PortInstance(provides = { ISampleSubscriberRequest.class }, requires = { ISampleSubscriberNotification.class })
	IPort portSamples;

	public IPort portSamples() {
		return this.portSamples;
	}
}
