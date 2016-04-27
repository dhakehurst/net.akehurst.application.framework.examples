package net.akehurst.app.temperatureSensor.engineering.channel.monitorGui;

import net.akehurst.application.framework.technology.guiInterface.IGuiScene;
import net.akehurst.application.framework.technology.guiInterface.controls.IChart;

public interface IMonitorScene extends IGuiScene {

	IChart getChart();
	
}
