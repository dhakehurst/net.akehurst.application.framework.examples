package net.akehurst.app.dotEditor.application.web.vertx;

import net.akehurst.application.framework.realisation.ApplicationFramework;

public class DotEditorVertxMain {

	public static void main(String[] args) {
		ApplicationFramework.start(DotEditorVertxApplication.class, args);
	}
	
}
