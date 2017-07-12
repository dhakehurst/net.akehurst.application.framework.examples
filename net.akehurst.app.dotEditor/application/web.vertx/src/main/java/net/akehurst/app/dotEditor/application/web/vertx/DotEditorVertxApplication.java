package net.akehurst.app.dotEditor.application.web.vertx;

import net.akehurst.app.dotEditor.computational.dotEditor.DotEditor;
import net.akehurst.app.dotEditor.engineering.channel.user.UserToGui;
import net.akehurst.application.framework.common.annotations.instance.ComponentInstance;
import net.akehurst.application.framework.common.annotations.instance.ServiceInstance;
import net.akehurst.application.framework.engineering.authenticator.AuthenticatorChannel;
import net.akehurst.application.framework.realisation.AbstractApplication;
import net.akehurst.application.framework.service.configuration.file.HJsonConfigurationService;
import net.akehurst.application.framework.technology.authentication.any.AnyAuthenticator;
import net.akehurst.application.framework.technology.filesystem.StandardFilesystem;
import net.akehurst.application.framework.technology.gui.vertx.VertxWebsite;
import net.akehurst.application.framework.technology.log4j.Log4JLogger;

public class DotEditorVertxApplication extends AbstractApplication {

	public DotEditorVertxApplication(final String id) {
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
	DotEditor editor;

	// --- engineering
	@ComponentInstance
	UserToGui user2Gui;

	@ComponentInstance
	AuthenticatorChannel authenticatorChannel;

	// --- technology
	@ComponentInstance
	VertxWebsite gui;

	@ComponentInstance
	AnyAuthenticator authenticator;

	@Override
	public void afConnectParts() {
		// computational <-> engineering
		this.editor.portUser().connect(this.user2Gui.portUserInterface());
		this.editor.portAuth().connect(this.authenticatorChannel.portComputational());
		// engineering <-> technology
		this.user2Gui.portGui().connect(this.gui.portGui());
		this.authenticatorChannel.portTechnology().connect(this.authenticator.portAuth());
	}

}
