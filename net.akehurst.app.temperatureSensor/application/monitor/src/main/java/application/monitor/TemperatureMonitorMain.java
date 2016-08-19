package application.monitor;

import net.akehurst.application.framework.realisation.ApplicationFramework;

class TemperatureMonitorMain {

	public static void main(String[] args) {
		ApplicationFramework.start(TemperatureMonitorApplication.class, args);
	}

}