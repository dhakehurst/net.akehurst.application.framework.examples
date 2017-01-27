package net.akehurst.app.dotEditor.computational.dotEditor;

import net.akehurst.app.dotEditor.computational.interfaceUser.IUserHomeNotification;
import net.akehurst.app.dotEditor.computational.interfaceUser.IUserHomeRequest;
import net.akehurst.app.dotEditor.computational.interfaceUser.IUserWelcomeNotification;
import net.akehurst.app.dotEditor.computational.interfaceUser.IUserWelcomeRequest;
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
public class DotEditor extends AbstractComponent {

	public DotEditor(final String afId) {
		super(afId);
	}

	@ComponentInstance
	AuthenticationDelegator authHandler;

	@ActiveObjectInstance
	WelcomeHandler welcomeHandler;

	@ActiveObjectInstance
	HomeHandler homeHandler;

	@Override
	public void afConnectParts() {
		this.portAuth().connectInternal(this.authHandler.portProvider());
		this.portUser().connectInternal(this.authHandler.portUser());
		this.portUser().connectInternal(this.welcomeHandler);
		this.portUser().connectInternal(this.homeHandler);

	}

	// ---------- Ports ---------
	@PortInstance
	@PortContract(provides = IUserWelcomeRequest.class, requires = IUserWelcomeNotification.class)
	@PortContract(provides = IUserHomeRequest.class, requires = IUserHomeNotification.class)
	@PortContract(provides = IUserAuthenticationRequest.class, requires = IUserAuthenticationNotification.class)
	IPort portUser;

	public IPort portUser() {
		return this.portUser;
	}

	@PortInstance
	@PortContract(provides = ICAuthenticatorNotification.class, requires = ICAuthenticatorRequest.class)
	IPort portAuth;

	public IPort portAuth() {
		return this.portAuth;
	}
}
