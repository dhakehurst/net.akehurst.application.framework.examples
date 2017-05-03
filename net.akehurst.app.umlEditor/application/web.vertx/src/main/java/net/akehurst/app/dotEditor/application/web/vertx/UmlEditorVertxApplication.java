package net.akehurst.app.dotEditor.application.web.vertx;

import net.akehurst.app.umlEditor.computational.umlEditor.UmlEditor;
import net.akehurst.app.umlEditor.engineering.channel.user.UserToGui;
import net.akehurst.application.framework.common.annotations.instance.ComponentInstance;
import net.akehurst.application.framework.common.annotations.instance.ServiceInstance;
import net.akehurst.application.framework.engineering.authenticator.AuthenticatorChannel;
import net.akehurst.application.framework.realisation.AbstractApplication;
import net.akehurst.application.framework.service.configuration.file.HJsonConfigurationService;
import net.akehurst.application.framework.technology.authentication.ldap.LdapAuthenticator;
import net.akehurst.application.framework.technology.filesystem.StandardFilesystem;
import net.akehurst.application.framework.technology.gui.vertx.VertxWebsite;
import net.akehurst.application.framework.technology.log4j.Log4JLogger;

public class UmlEditorVertxApplication extends AbstractApplication {

	public UmlEditorVertxApplication(final String id) {
		super(id);
	}

	@ServiceInstance
	Log4JLogger logger;

	@ServiceInstance
	StandardFilesystem fs;

	@ServiceInstance
	HJsonConfigurationService configuration;

	// --- computational
	@ComponentInstance
	UmlEditor editor;

	// --- engineering
	@ComponentInstance
	UserToGui user2Gui;

	@ComponentInstance
	AuthenticatorChannel authenticatorChannel;

	// --- technology
	@ComponentInstance
	VertxWebsite gui;

	@ComponentInstance
	LdapAuthenticator ldap;

	@Override
	public void afConnectParts() {
		// computational <-> engineering
		this.editor.portUser().connect(this.user2Gui.portUser());
		this.editor.portAuth().connect(this.authenticatorChannel.portComputational());
		// engineering <-> technology
		this.user2Gui.portGui().connect(this.gui.portGui());
		this.authenticatorChannel.portTechnology().connect(this.ldap.portAuth());
	}

}
