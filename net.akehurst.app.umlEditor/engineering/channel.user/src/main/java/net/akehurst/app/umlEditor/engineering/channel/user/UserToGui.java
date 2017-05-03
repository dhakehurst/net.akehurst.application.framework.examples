package net.akehurst.app.umlEditor.engineering.channel.user;

import net.akehurst.app.umlEditor.computational.interfaceUser.IUserHomeNotification;
import net.akehurst.app.umlEditor.computational.interfaceUser.IUserHomeRequest;
import net.akehurst.app.umlEditor.computational.interfaceUser.IUserProjectNotification;
import net.akehurst.app.umlEditor.computational.interfaceUser.IUserProjectRequest;
import net.akehurst.app.umlEditor.computational.interfaceUser.IUserWelcomeNotification;
import net.akehurst.app.umlEditor.computational.interfaceUser.IUserWelcomeRequest;
import net.akehurst.application.framework.common.IPort;
import net.akehurst.application.framework.common.annotations.declaration.Component;
import net.akehurst.application.framework.common.annotations.instance.ActiveObjectInstance;
import net.akehurst.application.framework.common.annotations.instance.PortContract;
import net.akehurst.application.framework.common.annotations.instance.PortInstance;
import net.akehurst.application.framework.computational.interfaceUser.authentication.IUserAuthenticationNotification;
import net.akehurst.application.framework.computational.interfaceUser.authentication.IUserAuthenticationRequest;
import net.akehurst.application.framework.realisation.AbstractComponent;
import net.akehurst.application.framework.technology.interfaceGui.IGuiNotification;
import net.akehurst.application.framework.technology.interfaceGui.IGuiRequest;

@Component
public class UserToGui extends AbstractComponent {

	public UserToGui(final String afId) {
		super(afId);
	}

	@ActiveObjectInstance
	GuiHandler handler;

	@Override
	public void afConnectParts() {
		this.portGui().connectInternal(this.handler);
		this.portUser().connectInternal(this.handler.sceneHandlerWelcome);
		this.portUser().connectInternal(this.handler.sceneHandlerAuthentication);
		this.portUser().connectInternal(this.handler.sceneHandlerHome);
		this.portUser().connectInternal(this.handler.sceneHandlerProject);
	}

	@PortInstance
	@PortContract(provides = IUserWelcomeNotification.class, requires = IUserWelcomeRequest.class)
	@PortContract(provides = IUserAuthenticationNotification.class, requires = IUserAuthenticationRequest.class)
	@PortContract(provides = IUserHomeNotification.class, requires = IUserHomeRequest.class)
	@PortContract(provides = IUserProjectNotification.class, requires = IUserProjectRequest.class)
	private IPort portUser;

	public IPort portUser() {
		return this.portUser;
	}

	@PortInstance
	@PortContract(provides = IGuiNotification.class, requires = IGuiRequest.class)
	private IPort portGui;

	public IPort portGui() {
		return this.portGui;
	}
}
