package net.akehurst.app.temperatureSensor.engineering.channel.monitorGui;

import java.time.LocalDateTime;

import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.data.chart.IGuiXYChart;

public interface IMonitorScene extends IGuiScene {

	IGuiXYChart<LocalDateTime, Double> getChart();

}
