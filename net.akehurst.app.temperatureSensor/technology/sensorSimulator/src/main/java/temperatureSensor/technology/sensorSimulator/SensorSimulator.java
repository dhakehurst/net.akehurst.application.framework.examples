package temperatureSensor.technology.sensorSimulator;

import java.util.Random;

import net.akehurst.application.framework.realisation.AbstractActiveObject;
import temperatureSensor.technology.interfaceSensor.IHardwareRegister;

/**
 * simulated sensor will create random temperature values in the range -273 < value < 500
 * 
 * the temperature is encoded into a 64 bit register as a floating point number with top 10 bits as the whole number and the other 54 bits decimal fraction.
 * 
 */
public class SensorSimulator extends AbstractActiveObject implements IHardwareRegister {

	public SensorSimulator(String id) {
		super(id);
	}

	private long currentValue;
	final long MIN_VALUE = (long) (-273 * Math.pow(2, 54));
	final long MAX_VALUE = (long) (500 * Math.pow(2, 54));

	@Override
	public void afRun() {
		try {

			Random rnd = new Random(System.currentTimeMillis());

			while (true) {
				long v = rnd.nextLong();
				while (v < MIN_VALUE || v > MAX_VALUE) {
					v = rnd.nextLong();
				}
				this.currentValue = v;
				Thread.sleep(100);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// --------- IHardwareRegister ---------
	
	public long readCurrentValue() {
		return this.currentValue;
	}

}