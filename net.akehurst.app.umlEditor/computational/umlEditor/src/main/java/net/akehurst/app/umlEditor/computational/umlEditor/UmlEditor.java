package net.akehurst.app.umlEditor.computational.umlEditor;

import net.akehurst.app.umlEditor.computational.interfaceUser.IUserHomeNotification;
import net.akehurst.app.umlEditor.computational.interfaceUser.IUserHomeRequest;
import net.akehurst.app.umlEditor.computational.interfaceUser.IUserProjectNotification;
import net.akehurst.app.umlEditor.computational.interfaceUser.IUserProjectRequest;
import net.akehurst.app.umlEditor.computational.interfaceUser.IUserWelcomeNotification;
import net.akehurst.app.umlEditor.computational.interfaceUser.IUserWelcomeRequest;
import net.akehurst.application.framework.common.IPort;
import net.akehurst.application.framework.common.annotations.declaration.Component;
import net.akehurst.application.framework.common.annotations.instance.ActiveObjectInstance;
import net.akehurst.application.framework.common.annotations.instance.ComponentInstance;
import net.akehurst.application.framework.common.annotations.instance.PortContract;
import net.akehurst.application.framework.common.annotations.instance.PortInstance;
import net.akehurst.application.framework.computational.authentication.AuthenticationDelegator;
import net.akehurst.application.framework.computational.interfaceAuthenticator.ICAuthenticatorNotification;
import net.akehurst.application.framework.computational.interfaceAuthenticator.ICAuthenticatorRequest;
import net.akehurst.application.framework.computational.interfaceUser.authentication.IUserAuthenticationNotification;
import net.akehurst.application.framework.computational.interfaceUser.authentication.IUserAuthenticationRequest;
import net.akehurst.application.framework.realisation.AbstractComponent;

@Component
public class UmlEditor extends AbstractComponent {

	public UmlEditor(final String afId) {
		super(afId);
	}

	@ComponentInstance
	AuthenticationDelegator authHandler;

	// @ActiveObjectInstance
	// WelcomeHandler welcomeHandler;

	@ActiveObjectInstance
	HomeHandler homeHandler;

	@ActiveObjectInstance
	ProjectHandler projectHandler;

	@Override
	public void afConnectParts() {
		this.portAuth().connectInternal(this.authHandler.portProvider());
		this.portUser().connectInternal(this.authHandler.portUser());
		// this.portUser().connectInternal(this.welcomeHandler);
		this.portUser().connectInternal(this.homeHandler);
		this.portUser().connectInternal(this.projectHandler);
	}

	// ---------- Ports ---------
	@PortInstance
	@PortContract(provides = IUserWelcomeRequest.class, requires = IUserWelcomeNotification.class)
	@PortContract(provides = IUserAuthenticationRequest.class, requires = IUserAuthenticationNotification.class)
	@PortContract(provides = IUserHomeRequest.class, requires = IUserHomeNotification.class)
	@PortContract(provides = IUserProjectRequest.class, requires = IUserProjectNotification.class)
	private IPort portUser;

	public IPort portUser() {
		return this.portUser;
	}

	@PortInstance
	@PortContract(provides = ICAuthenticatorNotification.class, requires = ICAuthenticatorRequest.class)
	private IPort portAuth;

	public IPort portAuth() {
		return this.portAuth;
	}
}
