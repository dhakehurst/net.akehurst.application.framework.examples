package temperatureSensor.computational.interfaceSensor;

import net.akehurst.application.framework.common.AbstractDataType;

public class TemperatureCelsius extends AbstractDataType {

	public TemperatureCelsius(double value) {
		super(value);
		if (value < -273.15 || value > 500) {
			throw new IllegalArgumentException("Value is out of range "+value);
		}
		this.value = value;
	}
	
	double value;
	public double asPrimitive() {
		return this.value;
	}
	
}
