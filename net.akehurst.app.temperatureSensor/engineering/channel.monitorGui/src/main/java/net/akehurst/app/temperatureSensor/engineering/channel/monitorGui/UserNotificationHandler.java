package net.akehurst.app.temperatureSensor.engineering.channel.monitorGui;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import net.akehurst.application.framework.common.annotations.instance.ConfiguredValue;
import net.akehurst.application.framework.technology.gui.common.AbstractGuiHandler;
import net.akehurst.application.framework.technology.interfaceGui.GuiEvent;
import net.akehurst.application.framework.technology.interfaceGui.IGuiHandler;
import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.IGuiSceneHandler;
import net.akehurst.application.framework.technology.interfaceGui.SceneIdentity;
import net.akehurst.application.framework.technology.interfaceGui.StageIdentity;
import net.akehurst.application.framework.technology.interfaceGui.data.chart.IGuiXYChart;
import net.akehurst.application.framework.technology.interfaceGui.data.chart.IGuiXYChartDataItem;
import net.akehurst.application.framework.technology.interfaceGui.data.chart.IGuiXYChartDataSeries;
import temperatureSensor.computational.userInterface.IUserNotification;

public class UserNotificationHandler extends AbstractGuiHandler implements IGuiSceneHandler, IUserNotification {

	public UserNotificationHandler(final String id) {
		super(id);
	}

	@ConfiguredValue(defaultValue = "/monitor")
	StageIdentity stageId;

	@ConfiguredValue(defaultValue = "monitor")
	SceneIdentity sceneId;

	@ConfiguredValue(defaultValue = "/Gui.fxml")
	String urlStr;

	IMonitorScene scene;

	Object lock = new Object();

	// --------- IUserNotification ---------
	@Override
	public void notifyNewPoint(final String id, final double temp, final long time) {
		super.submit("notifyNewPoint", () -> {

			synchronized (this.lock) {
				if (null == this.scene) {
					// can't do it
				} else {
					final IGuiXYChart<LocalDateTime, Double> chart = this.scene.getChart();
					IGuiXYChartDataSeries<LocalDateTime, Double> series = chart.getSeries(id);
					if (null == series) {
						series = chart.addSeries(id);
					}
					series.getData().add(new IGuiXYChartDataItem<LocalDateTime, Double>() {
						@Override
						public LocalDateTime getX() {
							return LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC);// .format(DateTimeFormatter.ISO_LOCAL_TIME);
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
	public void raiseAlarm(final String id) {
		super.submit("raiseAlarm", () -> {

		});
	}

	// --------- AbstractGuiHandler ---------
	@Override
	protected void onStageCreated(final GuiEvent event) {
		final URL content = this.getClass().getResource(this.urlStr);
		this.createScene(this, this.stageId, content);
	}

	@Override
	public void notifyReady() {
		this.getGuiRequest().createStage(this.stageId, "/monitor", null, null);
	}

	@Override
	public IGuiScene createScene(final IGuiHandler gui, final StageIdentity stageId, final URL content) {
		this.scene = gui.createScene(stageId, this.sceneId, IMonitorScene.class, this, content);
		return this.scene;
	}

	@Override
	public void loaded(final IGuiHandler gui, final IGuiScene guiScene, final GuiEvent event) {
		// TODO Auto-generated method stub

	}
}
