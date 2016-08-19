package application.monitor;

import computational.monitor.Monitor;
import net.akehurst.app.temperatureSensor.engineering.channel.monitorGui.MonitorToGui;
import net.akehurst.application.framework.common.annotations.declaration.Application;
import net.akehurst.application.framework.common.annotations.instance.ComponentInstance;
import net.akehurst.application.framework.common.annotations.instance.ServiceInstance;
import net.akehurst.application.framework.realisation.AbstractApplication;
import net.akehurst.application.framework.technology.filesystem.StandardFilesystem;
import net.akehurst.application.framework.technology.gui.jfx.JfxWindow;
import net.akehurst.application.framework.technology.log4j.Log4JLogger;
import net.akehurst.application.framework.technology.persistence.filesystem.HJsonFile;
import temperatureSensor.engineering.channel.sensorMonitor.SamplerProxy;
import temperatureSensor.technology.comms.socket.SocketComms;

@Application
class TemperatureMonitorApplication extends AbstractApplication {

	public TemperatureMonitorApplication(final String id) {
		super(id);
	}

	@ServiceInstance
	Log4JLogger logger;

	@ServiceInstance
	StandardFilesystem fs;

	@ServiceInstance
	HJsonFile configuration;

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

	@Override
	public void afConnectParts() {
		this.monitor.portSensors().connect(this.samplerProxy.portSamples());
		this.monitor.portUser().connect(this.monitorToGui.portUser());

		this.samplerProxy.portComms().connect(this.socketComms.portComms());
		this.monitorToGui.portGui().connect(this.gui.portGui());
	}
}