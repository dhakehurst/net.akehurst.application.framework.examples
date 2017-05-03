package net.akehurst.app.umlEditor.computational.interfaceUser;

import java.util.List;

import net.akehurst.app.umlEditor.computational.interfaceUser.data.Project;
import net.akehurst.application.framework.common.interfaceUser.UserSession;

public interface IUserHomeNotification {

	void notifyProjectList(UserSession session, List<Project> projectList);

}
