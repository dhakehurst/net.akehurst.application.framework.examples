package net.akehurst.app.dotEditor.engineering.channel.user;

import net.akehurst.app.dotEditor.computational.interfaceUser.IUserHomeNotification;
import net.akehurst.app.dotEditor.computational.interfaceUser.IUserHomeRequest;
import net.akehurst.app.dotEditor.computational.interfaceUser.IUserWelcomeNotification;
import net.akehurst.app.dotEditor.computational.interfaceUser.IUserWelcomeRequest;
import net.akehurst.application.framework.common.IPort;
import net.akehurst.application.framework.common.annotations.declaration.Component;
import net.akehurst.application.framework.common.annotations.instance.ActiveObjectInstance;
import net.akehurst.application.framework.common.annotations.instance.PortContract;
import net.akehurst.application.framework.common.annotations.instance.PortInstance;
import net.akehurst.application.framework.computational.interfaceUser.authentication.IUserAuthenticationNotification;
import net.akehurst.application.framework.computational.interfaceUser.authentication.IUserAuthenticationRequest;
import net.akehurst.application.framework.realisation.ComponentAbstract;
import net.akehurst.application.framework.technology.interfaceGui.IGuiNotification;
import net.akehurst.application.framework.technology.interfaceGui.IGuiRequest;

@Component
public class UserToGui extends ComponentAbstract {
	public UserToGui(final String id) {
		super(id);
	}

	@ActiveObjectInstance
	GuiHandler handler;

	@Override
	public void afConnectParts() {
		this.portGui().connectInternal(this.handler);
		this.portUserInterface().connectInternal(this.handler.sceneHandlerWelcome);
		this.portUserInterface().connectInternal(this.handler.sceneHandlerAuthentication);
		this.portUserInterface().connectInternal(this.handler.sceneHandlerHome);
	}

	// --------- Ports ---------
	@PortInstance
	@PortContract(provides = IUserWelcomeNotification.class, requires = IUserWelcomeRequest.class)
	@PortContract(provides = IUserAuthenticationNotification.class, requires = IUserAuthenticationRequest.class)
	@PortContract(provides = IUserHomeNotification.class, requires = IUserHomeRequest.class)
	IPort portUserInterface;

	public IPort portUserInterface() {
		return this.portUserInterface;
	}

	@PortInstance
	@PortContract(provides = IGuiNotification.class, requires = IGuiRequest.class)
	IPort portGui;

	public IPort portGui() {
		return this.portGui;
	}
}
