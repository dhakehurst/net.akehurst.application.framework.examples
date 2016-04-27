package temperatureSensor.application.sampler;

import computational.sampler.Sampler;
import net.akehurst.application.framework.common.annotations.instance.ActiveObjectInstance;
import net.akehurst.application.framework.common.annotations.instance.ComponentInstance;
import net.akehurst.application.framework.common.annotations.instance.ServiceInstance;
import net.akehurst.application.framework.realisation.AbstractApplication;
import net.akehurst.application.framework.technology.filesystem.StandardFilesystem;
import net.akehurst.application.framework.technology.log4j.Log4JLogger;
import net.akehurst.application.framework.technology.persistence.filesystem.HJsonFile;
import temperatureSensor.computational.interfaceSampler.ISampleSubscriberNotification;
import temperatureSensor.computational.interfaceSensor.ISensorRequest;
import temperatureSensor.engineering.channel.sensorMonitor.MonitorProxy;
import temperatureSensor.engineering.channel.sensorSampler.SensorProxy;
import temperatureSensor.technology.comms.socket.SocketComms;
import temperatureSensor.technology.sensorSimulator.SensorSimulator;

class SamplerApplication extends AbstractApplication {

	public SamplerApplication(String id, String[] args) {
		super(id, args);
	}

	@ServiceInstance
	Log4JLogger logger;
	
	@ServiceInstance
	StandardFilesystem fs;
	
	@ServiceInstance
	HJsonFile configuration;

	@Override
	public void connectComputationalToEngineering() {
		this.sampler.portSensor().provideRequired(ISensorRequest.class, this.sensorProxy);
		this.sampler.portSubscribers().provideRequired(ISampleSubscriberNotification.class, this.monitorProxy);
	}

	@Override
	public void connectEngineeringToTechnology() {
		this.monitorProxy.portComms().connect(this.socketComms.portComms());
		this.sensorProxy.setHardwareSensor(this.hardwareSensor);
	}

	@ComponentInstance
	Sampler sampler;

	@ComponentInstance
	MonitorProxy monitorProxy;

	@ActiveObjectInstance
	SensorProxy sensorProxy;

	@ComponentInstance
	SocketComms socketComms;

	@ActiveObjectInstance
	SensorSimulator hardwareSensor;

}