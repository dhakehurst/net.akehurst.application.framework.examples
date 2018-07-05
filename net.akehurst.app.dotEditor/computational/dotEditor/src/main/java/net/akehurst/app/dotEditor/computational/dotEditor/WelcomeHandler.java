package net.akehurst.app.dotEditor.computational.dotEditor;

import net.akehurst.app.dotEditor.computational.interfaceUser.IUserWelcomeNotification;
import net.akehurst.app.dotEditor.computational.interfaceUser.IUserWelcomeRequest;
import net.akehurst.application.framework.realisation.ActiveSignalProcessingObjectAbstract;

public class WelcomeHandler extends ActiveSignalProcessingObjectAbstract implements IUserWelcomeRequest {

	public WelcomeHandler(final String afId) {
		super(afId);
	}

	public IUserWelcomeNotification userWelcomeNotification;

}
