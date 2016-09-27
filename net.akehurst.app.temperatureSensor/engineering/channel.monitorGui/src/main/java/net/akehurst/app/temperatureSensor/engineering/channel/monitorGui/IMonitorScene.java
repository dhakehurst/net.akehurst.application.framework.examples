package net.akehurst.app.temperatureSensor.engineering.channel.monitorGui;

import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.data.chart.IGuiChart;

public interface IMonitorScene extends IGuiScene {

	IGuiChart getChart();

}
