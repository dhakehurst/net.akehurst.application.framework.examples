package net.akehurst.app.umlEditor.computational.interfaceUser;

import net.akehurst.app.umlEditor.computational.interfaceUser.data.StatechartDiagram;
import net.akehurst.application.framework.common.interfaceUser.UserSession;

public interface IUserProjectNotification {

	void notifyParseTree(UserSession session, String jsonParseTreeStr);

	void notifyDiagramData(UserSession session, StatechartDiagram diagram);
}
