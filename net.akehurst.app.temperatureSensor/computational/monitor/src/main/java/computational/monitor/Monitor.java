package computational.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.akehurst.application.framework.common.IPort;
import net.akehurst.application.framework.common.annotations.instance.PortContract;
import net.akehurst.application.framework.common.annotations.instance.PortInstance;
import net.akehurst.application.framework.realisation.AbstractComponent;
import temperatureSensor.computational.interfaceSampler.ISampleSubscriberNotification;
import temperatureSensor.computational.interfaceSampler.ISampleSubscriberRequest;
import temperatureSensor.computational.interfaceSampler.Sample;
import temperatureSensor.computational.interfaceSampler.SensorIdentity;
import temperatureSensor.computational.interfaceSensor.TemperatureCelsius;
import temperatureSensor.computational.userInterface.IUserNotification;
import temperatureSensor.computational.userInterface.IUserRequest;

/**
 * This class receives samples from a proxy object that has reference to the ISampleSubscriber interface.
 *
 * It needs to notify the user interface of the information in the sample via the interface IUserNotification.
 *
 * if any sensor gives 3 consecutive samples with temperature over 300 or under -100 an alarm is raised by notifying the user (call appropriate method on
 * IUserNotification).
 *
 */
public class Monitor extends AbstractComponent implements ISampleSubscriberNotification, IUserRequest {

	public Monitor(final String id) {
		super(id);
		this.sampleHistory = new HashMap<>();
	}

	Map<SensorIdentity, List<Sample>> sampleHistory;

	void addSample(final Sample value) {
		List<Sample> list = this.sampleHistory.get(value.getSensorId());
		if (null == list) {
			list = new ArrayList<>();
			this.sampleHistory.put(value.getSensorId(), list);
		}
		list.add(value);
	}

	// --------- ISampleSubscriberNotification ---------

	@Override
	public void publishSample(final Sample sample) {
		this.addSample(sample);

		this.portUser().out(IUserNotification.class).notifyNewPoint(sample.getSensorId().asPrimitive(), sample.getTemperature().asPrimitive(),
				sample.getTimestamp().asPrimitive());

		// this.check(sample.getSensorId());
	}

	void check(final SensorIdentity id) {
		final List<Sample> history = this.sampleHistory.get(id);
		final int len = history.size();
		if (len >= 3) {
			final List<TemperatureCelsius> last3 = new ArrayList<>();
			last3.add(history.get(len - 1).getTemperature());
			last3.add(history.get(len - 2).getTemperature());
			last3.add(history.get(len - 3).getTemperature());

			int max = 0;
			int min = 0;
			for (final TemperatureCelsius t : last3) {
				if (t.asPrimitive() > 300) {
					max++;
				}
				if (t.asPrimitive() < -100) {
					min++;
				}
			}

			if (min == 3 || max == 3) {
				this.portUser().out(IUserNotification.class).raiseAlarm(id.asPrimitive());
			}
		}
	}

	// ---------- Ports ---------
	@PortInstance
	@PortContract(provides = IUserRequest.class, requires = IUserNotification.class)
	IPort portUser;

	public IPort portUser() {
		return this.portUser;
	}

	@PortInstance
	@PortContract(provides = ISampleSubscriberNotification.class, requires = ISampleSubscriberRequest.class)
	IPort portSensors;

	public IPort portSensors() {
		return this.portSensors;
	}
}