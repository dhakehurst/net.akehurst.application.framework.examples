package temperatureSensor.engineering.channel.sensorSampler;

import net.akehurst.application.framework.realisation.AbstractActiveObject;
import temperatureSensor.computational.interfaceSensor.ISensorRequest;
import temperatureSensor.computational.interfaceSensor.TemperatureCelsius;
import temperatureSensor.technology.interfaceSensor.IHardwareRegister;

public class SensorProxy extends AbstractActiveObject implements ISensorRequest {

	public SensorProxy(final String id) {
		super(id);
	}

	@Override
	public void afRun() {
		// TODO Auto-generated method stub

	}

	@Override
	public void afTerminate() {
		// TODO Auto-generated method stub

	}

	private IHardwareRegister hardwareSensor;

	public IHardwareRegister getHardwareSensor() {
		return this.hardwareSensor;
	}

	public void setHardwareSensor(final IHardwareRegister hardwareSensor) {
		this.hardwareSensor = hardwareSensor;
	}

	/*
	 * 1)
	 *
	 * The simulated sensor will create random temperature values in the range -273 < value < 500
	 *
	 * the temperature is encoded into a 64 bit register as a floating point number with top 10 bits as the whole number and the other 54 bits decimal fraction.
	 *
	 * This method needs to Read the temperature from the hardware sensor Convert it to a Temperature in degrees Celsius return this value
	 * 
	 * Note: the TemperatureCelsius class will throw an exception if you try to create a value outside the correct range (-273 < value < 500). This is not a
	 * problem if you convert correctly.
	 * 
	 */
	@Override
	public TemperatureCelsius readTemperature() {

		final long reading = this.getHardwareSensor().readCurrentValue();
		final double value = reading / Math.pow(2, 54);
		final TemperatureCelsius temperature = new TemperatureCelsius(value);
		return temperature;
	}

}
