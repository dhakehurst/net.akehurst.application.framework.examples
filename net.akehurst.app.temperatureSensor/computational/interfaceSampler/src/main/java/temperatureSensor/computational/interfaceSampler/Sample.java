package temperatureSensor.computational.interfaceSampler;

import net.akehurst.application.framework.common.AbstractDataType;
import temperatureSensor.computational.interfaceSensor.TemperatureCelsius;

// Datatype
public class Sample extends AbstractDataType {

	public Sample(SensorIdentity sensorId, TemperatureCelsius temperature, TimeStampMilliseconds timestamp) {
		super(sensorId, temperature, timestamp);
		this.sensorId = sensorId;
		this.temperature = temperature;
		this.timestamp = timestamp;
	}
	
	SensorIdentity sensorId;
	public SensorIdentity getSensorId() {
		return sensorId;
	}

	private TemperatureCelsius temperature;
	public TemperatureCelsius getTemperature() {
		return temperature;
	}

	private TimeStampMilliseconds timestamp;
	public TimeStampMilliseconds getTimestamp() {
		return timestamp;
	}
	
}
