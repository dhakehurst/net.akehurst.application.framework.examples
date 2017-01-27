package net.akehurst.app.dotEditor.computational.dotEditor;

import net.akehurst.app.dotEditor.computational.interfaceUser.IUserWelcomeNotification;
import net.akehurst.app.dotEditor.computational.interfaceUser.IUserWelcomeRequest;
import net.akehurst.application.framework.realisation.AbstractActiveSignalProcessingObject;

public class WelcomeHandler extends AbstractActiveSignalProcessingObject implements IUserWelcomeRequest {

	public WelcomeHandler(final String afId) {
		super(afId);
	}

	public IUserWelcomeNotification userWelcomeNotification;

}
