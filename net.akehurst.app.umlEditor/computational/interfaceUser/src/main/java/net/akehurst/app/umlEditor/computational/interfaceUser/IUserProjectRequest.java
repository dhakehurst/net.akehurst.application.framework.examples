package net.akehurst.app.umlEditor.computational.interfaceUser;

import net.akehurst.application.framework.common.interfaceUser.UserSession;

public interface IUserProjectRequest {

	void requestUpdateText(UserSession session, String text);

}
