package net.akehurst.app.temperatureSensor.engineering.channel.monitorGui;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import net.akehurst.application.framework.common.annotations.instance.ConfiguredValue;
import net.akehurst.application.framework.technology.gui.common.AbstractGuiHandler;
import net.akehurst.application.framework.technology.guiInterface.GuiEvent;
import net.akehurst.application.framework.technology.guiInterface.IGuiScene;
import net.akehurst.application.framework.technology.guiInterface.SceneIdentity;
import net.akehurst.application.framework.technology.guiInterface.StageIdentity;
import net.akehurst.application.framework.technology.guiInterface.elements.IGuiChart;
import net.akehurst.application.framework.technology.guiInterface.elements.IGuiChartData;
import net.akehurst.application.framework.technology.guiInterface.elements.IGuiChartDataItem;
import net.akehurst.application.framework.technology.guiInterface.elements.IGuiChartDataSeries;
import temperatureSensor.computational.userInterface.IUserNotification;

public class UserNotificationHandler extends AbstractGuiHandler implements IUserNotification {

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
					final IGuiChart chart = this.scene.getChart();
					IGuiChartDataSeries<LocalDateTime, Double> series = chart.getSeries(id);
					if (null == series) {
						series = chart.addSeries(id);
					}
					final IGuiChartData<LocalDateTime, Double> data = series.getData();
					data.getItems().add(new IGuiChartDataItem<LocalDateTime, Double>() {
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
		this.scene = this.guiRequest.createScene(this.stageId, this.sceneId, IMonitorScene.class, content);
	}

	@Override
	protected void onSceneLoaded(final GuiEvent event) {
		// TODO Auto-generated method stub
		// super.submit(name, signal)
	}

	@Override
	protected IGuiScene getScene(final SceneIdentity sceneId) {
		return this.scene;
	}

	@Override
	public void notifyReady() {
		final URL rootUrl = this.getClass().getResource("/monitor");
		this.getGuiRequest().createStage(this.stageId, false, rootUrl);
	}

}
