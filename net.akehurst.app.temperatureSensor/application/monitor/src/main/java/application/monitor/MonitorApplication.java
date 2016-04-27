package application.monitor;

import computational.monitor.Monitor;
import net.akehurst.app.temperatureSensor.engineering.channel.monitorGui.MonitorToGui;
import net.akehurst.application.framework.common.annotations.instance.ComponentInstance;
import net.akehurst.application.framework.common.annotations.instance.ServiceInstance;
import net.akehurst.application.framework.realisation.AbstractApplication;
import net.akehurst.application.framework.technology.filesystem.StandardFilesystem;
import net.akehurst.application.framework.technology.gui.jfx.JfxWindow;
import net.akehurst.application.framework.technology.log4j.Log4JLogger;
import net.akehurst.application.framework.technology.persistence.filesystem.HJsonFile;
import temperatureSensor.engineering.channel.sensorMonitor.SamplerProxy;
import temperatureSensor.technology.comms.socket.SocketComms;

class MonitorApplication extends AbstractApplication {

	public MonitorApplication(String id, String[] args) {
		super(id, args);
	}

//	@Override
//	public void defineArguments() {
//		super.defineArguments();
//		super.defineArgument(true, "port", true, "a port number (Integer)");
//		super.defineArgument(true, "address", true, "a multicast address (String)");
//		super.defineArgument(true, "retries", true, "number of times to retry connectin to server");
//	}
	
	@ServiceInstance
	Log4JLogger logger;
	
	@ServiceInstance
	StandardFilesystem fs;
	
	@ServiceInstance
	HJsonFile configuration;
	
	
	@Override
	public void connectComputationalToEngineering() {
		this.monitor.portSensors().connect(this.samplerProxy.portSamples());
		this.monitor.portUser().connect(this.monitorToGui.portUser());
	}

	@Override
	public void connectEngineeringToTechnology() {
		this.samplerProxy.portComms().connect(this.socketComms.portComms());
		this.monitorToGui.portGui().connect(this.gui.portGui());
	}
	
	@ComponentInstance
	Monitor monitor;

	@ComponentInstance
	SamplerProxy samplerProxy;

	@ComponentInstance
	MonitorToGui monitorToGui;
	
	@ComponentInstance
	JfxWindow gui;

	@ComponentInstance
	SocketComms socketComms;

}