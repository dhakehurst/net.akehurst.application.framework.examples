package net.akehurst.app.dotEditor.application.web.vertx;

import net.akehurst.app.dotEditor.computational.dotEditor.DotEditor;
import net.akehurst.app.dotEditor.engineering.channel.user.UserToGui;
import net.akehurst.application.framework.common.annotations.instance.ComponentInstance;
import net.akehurst.application.framework.common.annotations.instance.ServiceInstance;
import net.akehurst.application.framework.engineering.authenticator2Gui.AuthenticatorToGui;
import net.akehurst.application.framework.realisation.AbstractApplication;
import net.akehurst.application.framework.technology.filesystem.StandardFilesystem;
import net.akehurst.application.framework.technology.gui.vertx.VertxWebsite;
import net.akehurst.application.framework.technology.log4j.Log4JLogger;
import net.akehurst.application.framework.technology.persistence.filesystem.HJsonFile;

public class DotEditorVertxApplication extends AbstractApplication {

	public DotEditorVertxApplication(final String id) {
		super(id);
	}

	@ServiceInstance
	Log4JLogger logger;

	@ServiceInstance
	StandardFilesystem fs;

	@ServiceInstance
	HJsonFile configuration;

	// --- computational
	@ComponentInstance
	DotEditor editor;

	// --- engineering
	@ComponentInstance
	UserToGui user2Gui;

	@ComponentInstance
	AuthenticatorToGui authenticatorToGui;

	// --- technology
	@ComponentInstance
	VertxWebsite gui;

	@Override
	public void afConnectParts() {
		// computational <-> engineering
		this.editor.portUser().connect(this.user2Gui.portUserInterface());
		this.editor.portAuth().connect(this.authenticatorToGui.portAuth());
		// engineering <-> technology
		this.user2Gui.portGui().connect(this.gui.portGui());
		this.authenticatorToGui.portGui().connect(this.gui.portGui());
	}

}
