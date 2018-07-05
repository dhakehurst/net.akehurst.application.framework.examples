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

	public SensorSimulator(final String id) {
		super(id);
	}

	private long currentValue;
	final long MIN_VALUE = (long) (-273 * Math.pow(2, 54));
	final long MAX_VALUE = (long) (500 * Math.pow(2, 54));

	@Override
	public void afRun() {
		try {

			final Random rnd = new Random(System.currentTimeMillis());

			while (true) {
				long v = rnd.nextLong();
				while (v < this.MIN_VALUE || v > this.MAX_VALUE) {
					v = rnd.nextLong();
				}
				this.currentValue = v;
				Thread.sleep(100);
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void afTerminate() {
		// TODO Auto-generated method stub

	}

	// --------- IHardwareRegister ---------

	@Override
	public long readCurrentValue() {
		return this.currentValue;
	}

}