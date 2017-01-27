package net.akehurst.example.flightSimulator.gui.channel;

import net.akehurst.application.framework.common.IPort;
import net.akehurst.application.framework.common.annotations.instance.ActiveObjectInstance;
import net.akehurst.application.framework.common.annotations.instance.PortContract;
import net.akehurst.application.framework.common.annotations.instance.PortInstance;
import net.akehurst.application.framework.realisation.AbstractComponent;
import net.akehurst.application.framework.technology.interfaceGui.IGuiNotification;
import net.akehurst.application.framework.technology.interfaceGui.IGuiRequest;
import net.akehurst.example.flightSimulator.computational.pilotInterface.IPilotNotification;
import net.akehurst.example.flightSimulator.computational.pilotInterface.IPilotRequest;

public class PilotToGui extends AbstractComponent {

	public PilotToGui(final String id) {
		super(id);
	}

	@ActiveObjectInstance
	GuiHandler pilotHandler;

	@Override
	public void afConnectParts() {
		this.pilotHandler.setGuiRequest(this.portGui().out(IGuiRequest.class));
		this.portGui().connectInternal(this.pilotHandler);
	}

	@PortInstance
	@PortContract(provides = IPilotNotification.class, requires = IPilotRequest.class)
	IPort portPilot;

	public IPort portPilot() {
		return this.portPilot;
	}

	@PortInstance
	@PortContract(provides = IGuiNotification.class, requires = IGuiRequest.class)
	IPort portGui;

	public IPort portGui() {
		return this.portGui;
	}

}
