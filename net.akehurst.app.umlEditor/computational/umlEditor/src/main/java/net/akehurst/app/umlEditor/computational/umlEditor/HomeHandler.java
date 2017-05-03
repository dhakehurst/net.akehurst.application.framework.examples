package net.akehurst.app.umlEditor.computational.umlEditor;

import java.util.ArrayList;
import java.util.List;

import net.akehurst.app.umlEditor.computational.interfaceUser.IUserHomeNotification;
import net.akehurst.app.umlEditor.computational.interfaceUser.IUserHomeRequest;
import net.akehurst.app.umlEditor.computational.interfaceUser.UserException;
import net.akehurst.app.umlEditor.computational.interfaceUser.data.Project;
import net.akehurst.app.umlEditor.computational.interfaceUser.data.ProjectIdentity;
import net.akehurst.application.framework.common.interfaceUser.UserSession;
import net.akehurst.application.framework.realisation.AbstractActiveSignalProcessingObject;

public class HomeHandler extends AbstractActiveSignalProcessingObject implements IUserHomeRequest {

	public HomeHandler(final String afId) {
		super(afId);
	}

	public IUserHomeNotification userHomeNotification;

	@Override
	public void requestSelectHome(final UserSession session) {
		try {
			final List<Project> projectList = new ArrayList<>();

			final Project dummy = new Project(new ProjectIdentity("dummy"));
			projectList.add(dummy);
			this.userHomeNotification.notifyProjectList(session, projectList);
		} catch (final UserException e) {

			e.printStackTrace();
		}
	}

}
