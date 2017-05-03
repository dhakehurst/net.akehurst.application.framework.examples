package net.akehurst.app.umlEditor.engineering.channel.user;

import net.akehurst.application.framework.technology.interfaceGui.GuiException;
import net.akehurst.application.framework.technology.interfaceGui.SceneIdentity;

public class SceneHandlerCommon {

	static public SceneIdentity sceneIdHome;

	static public SceneIdentity sceneIdSignIn;

	static public SceneIdentity sceneIdProject;

	static public SceneIdentity sceneIdWelcome;

	static {
		try {
			SceneHandlerCommon.sceneIdHome = new SceneIdentity("home");
			SceneHandlerCommon.sceneIdSignIn = new SceneIdentity("signin");
			SceneHandlerCommon.sceneIdProject = new SceneIdentity("project");
			SceneHandlerCommon.sceneIdWelcome = new SceneIdentity("");
		} catch (final GuiException e) {
			e.printStackTrace();
		}
	}

}
