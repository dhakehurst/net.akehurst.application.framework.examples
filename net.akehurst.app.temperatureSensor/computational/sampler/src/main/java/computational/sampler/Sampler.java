package computational.sampler;

import net.akehurst.application.framework.common.IPort;
import net.akehurst.application.framework.common.annotations.instance.CommandLineArgument;
import net.akehurst.application.framework.common.annotations.instance.ConfiguredValue;
import net.akehurst.application.framework.common.annotations.instance.PortInstance;
import net.akehurst.application.framework.common.annotations.instance.ServiceReference;
import net.akehurst.application.framework.realisation.AbstractComponent;
import net.akehurst.application.framework.technology.interfaceLogging.ILogger;
import temperatureSensor.computational.interfaceSampler.ISampleSubscriberNotification;
import temperatureSensor.computational.interfaceSampler.ISampleSubscriberRequest;
import temperatureSensor.computational.interfaceSampler.Sample;
import temperatureSensor.computational.interfaceSampler.SensorIdentity;
import temperatureSensor.computational.interfaceSampler.TimeStampMilliseconds;
import temperatureSensor.computational.interfaceSensor.ISensorRequest;
import temperatureSensor.computational.interfaceSensor.TemperatureCelsius;

/*
 * 2) Objects of this class create samples. A sample is composed from
 *  - identity : SensorIdentity(String)
 *  - temperature : TemperatureCelsius(double)
 *  - timestamp : TimeStampMilliseconds(long)
 *  
 *  the start method will be called after the object is created and properties are set,
 *  this should cause the object to start collecting samples.
 *  
 *  Samples should be collected at the given rate.
 *  The rate will be set after the object has been constructed
 *  
 *  once a sample has been collected, it should be published to the provided SamplePublisher.
 *  
 *  the object should continue collecting samples forever (until program termination).
 *  
 */
public class Sampler extends AbstractComponent implements ISampleSubscriberRequest  {

	@ServiceReference
	ILogger logger;
	
//	@Service(name="config")
//	IConfiguration config;
	
	public Sampler(String id) {
		super(id);
		this.sensorId = null;
		this.sampleRate = null;
	}

	@Override
	public void afRun() {
		try {
			while (true) {
				TemperatureCelsius temperature = portSensor().out(ISensorRequest.class).readTemperature();
				TimeStampMilliseconds timestamp = new TimeStampMilliseconds(System.currentTimeMillis());

				Sample sample = new Sample(this.sensorId, temperature, timestamp);

				this.portSubscribers().out(ISampleSubscriberNotification.class).publishSample(sample);

				Thread.sleep(1000 / this.sampleRate.asPrimitive());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@CommandLineArgument(required=true, description="The sample rate (Integer)")
	@ConfiguredValue(defaultValue="20")
	private SampleRatePerSecond sampleRate;

	public SampleRatePerSecond getSampleRate() {
		return sampleRate;
	}

	@CommandLineArgument(required=true, description="The sensor identity (String)")
	@ConfiguredValue(defaultValue="sensor")
	private SensorIdentity sensorId;

	public SensorIdentity getSensorId() {
		return sensorId;
	}

	
	// --------- Ports ---------
	@PortInstance(provides={},requires={ISensorRequest.class})
	IPort portSensor;
	public IPort portSensor() {
		return this.portSensor;
	}
	
	@PortInstance(provides={ISampleSubscriberRequest.class},requires={ISampleSubscriberNotification.class})
	IPort portSubscribers;
	public IPort portSubscribers() {
		return this.portSubscribers;
	}
	
}
