package net.akehurst.example.flightSimulator.gui.channel;

import java.net.URL;

import net.akehurst.application.framework.common.annotations.instance.ConfiguredValue;
import net.akehurst.application.framework.common.annotations.instance.IdentifiableObjectInstance;
import net.akehurst.application.framework.common.interfaceUser.UserSession;
import net.akehurst.application.framework.technology.gui.common.AbstractGuiHandler;
import net.akehurst.application.framework.technology.interfaceGui.GuiEvent;
import net.akehurst.application.framework.technology.interfaceGui.StageIdentity;

public class GuiHandler extends AbstractGuiHandler {

	public GuiHandler(final String id) {
		super(id);
	}

	@ConfiguredValue(defaultValue = "/GuiInstrumentPanel.fxml")
	String instrumentsUrl;

	@ConfiguredValue(defaultValue = "/GuiControlsPanel.fxml")
	String controlsUrl;

	@ConfiguredValue(defaultValue = "/instruments")
	StageIdentity stageIdInstruments;

	@ConfiguredValue(defaultValue = "/controls")
	StageIdentity stageIdControls;

	@IdentifiableObjectInstance
	SceneHandlerControls sceneHandlerControls;

	@IdentifiableObjectInstance
	SceneHandlerInstruments sceneHandlerInstruments;

	// TODO: remove this
	UserSession session;

	// --------- AbstractGuiHandler ---------
	@Override
	protected void onStageCreated(final GuiEvent event) {

		final StageIdentity currentStage = event.getSignature().getStageId();
		if (this.stageIdControls.equals(currentStage)) {
			final URL content = this.getClass().getResource(this.controlsUrl);
			this.sceneHandlerControls.createScene(this, this.stageIdControls, content);
		} else if (this.stageIdInstruments.equals(currentStage)) {
			final URL content = this.getClass().getResource(this.instrumentsUrl);
			this.sceneHandlerInstruments.createScene(this, this.stageIdInstruments, content);
		}

	}

	@Override
	public void notifyReady() {
		final URL instrumentsStage = this.getClass().getResource("/flightSystem");
		final URL controlsStage = this.getClass().getResource("/flightSystem");
		this.getGuiRequest().createStage(this.stageIdInstruments, false, instrumentsStage);
		this.getGuiRequest().createStage(this.stageIdControls, false, controlsStage);
	}

}
