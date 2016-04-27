package net.akehurst.app.temperatureSensor.engineering.channel.monitorGui;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javafx.scene.chart.LineChart;
import net.akehurst.application.framework.common.annotations.instance.ConfiguredValue;
import net.akehurst.application.framework.technology.gui.common.AbstractGuiHandler;
import net.akehurst.application.framework.technology.guiInterface.GuiEvent;
import net.akehurst.application.framework.technology.guiInterface.IGuiScene;
import net.akehurst.application.framework.technology.guiInterface.controls.IChart;
import net.akehurst.application.framework.technology.guiInterface.controls.IChartData;
import net.akehurst.application.framework.technology.guiInterface.controls.IChartDataItem;
import net.akehurst.application.framework.technology.guiInterface.controls.IChartDataSeries;
import temperatureSensor.computational.userInterface.IUserNotification;

public class UserNotificationHandler extends AbstractGuiHandler implements IUserNotification {

	public UserNotificationHandler(String id) {
		super(id);
	}

	@ConfiguredValue(defaultValue = "/Gui.fxml")
	String urlStr;

	IMonitorScene scene;

	Object lock = new Object();

	// --------- IUserNotification ---------
	@Override
	public void notifyNewPoint(String id, double temp, long time) {
		super.submit("notifyNewPoint", () -> {

			synchronized (lock) {
				if (null == this.scene) {
					// can't do it
				} else {
					IChart chart = this.scene.getChart();
					IChartDataSeries<LocalDateTime, Double> series = chart.getSeries(id);
					if (null == series) {
						series = chart.addSeries(id);
					}
					IChartData<LocalDateTime, Double> data = series.getData();
					data.getItems().add(new IChartDataItem<LocalDateTime, Double>() {
						@Override
						public LocalDateTime getX() {
							return LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC);//.format(DateTimeFormatter.ISO_LOCAL_TIME);
						}

						@Override
						public Double getY() {
							return temp;
						}
					});
				}
			}
		});
	}

	@Override
	public void raiseAlarm(String id) {
		super.submit("raiseAlarm", () -> {

		});
	}

	// --------- AbstractGuiHandler ---------
	@Override
	protected void onStageCreated(GuiEvent event) {
		URL content = this.getClass().getResource(this.urlStr);
		this.scene = this.guiRequest.createScene("Monitor", "mainWindow", IMonitorScene.class, content);
	}

	@Override
	protected void onSceneLoaded(GuiEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected IGuiScene getScene(String sceneId) {
		return this.scene;
	}

	@Override
	public void notifyReady() {
		URL rootUrl = this.getClass().getResource("/monitor");
		this.getGuiRequest().createStage("/monitor", false, rootUrl);
	}

}
