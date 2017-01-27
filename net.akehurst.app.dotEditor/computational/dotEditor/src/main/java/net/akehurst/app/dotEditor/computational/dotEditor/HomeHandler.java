package net.akehurst.app.dotEditor.computational.dotEditor;

import net.akehurst.app.dotEditor.computational.interfaceUser.IUserHomeNotification;
import net.akehurst.app.dotEditor.computational.interfaceUser.IUserHomeRequest;
import net.akehurst.application.framework.realisation.AbstractActiveSignalProcessingObject;

public class HomeHandler extends AbstractActiveSignalProcessingObject implements IUserHomeRequest {

	public HomeHandler(final String afId) {
		super(afId);
	}

	public IUserHomeNotification userHomeNotification;

}
