/**
 * Copyright (C) 2016 Dr. David H. Akehurst (http://dr.david.h.akehurst.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package helloWorld.engineering.channel.user;

import helloWorld.computational.interfaceUser.IUserNotification;
import helloWorld.computational.interfaceUser.IUserRequest;
import helloWorld.computational.interfaceUser.Message;
import net.akehurst.application.framework.common.IPort;
import net.akehurst.application.framework.common.annotations.declaration.Component;
import net.akehurst.application.framework.common.annotations.instance.PortContract;
import net.akehurst.application.framework.common.annotations.instance.PortInstance;
import net.akehurst.application.framework.common.interfaceUser.UserSession;
import net.akehurst.application.framework.realisation.AbstractComponent;
import net.akehurst.application.framework.technology.interfaceGui.console.IConsoleNotification;
import net.akehurst.application.framework.technology.interfaceGui.console.IConsoleRequest;

//the proxy object implements the IUser interface by
// forwarding messages to an output object
@Component
public class UserProxyToText extends AbstractComponent implements IUserNotification, IConsoleNotification {

	public UserProxyToText(final String id) {
		super(id);
	}

	@Override
	public void notifyMessage(final UserSession session, final Message message) {
		this.portConsole().out(IConsoleRequest.class).requestOutput(message.asPrimitive());
	}

	@Override
	public void notifyReady(final UserSession session) {
		this.portUser().out(IUserRequest.class).requestStart(session);
	}

	@Override
	public void notifyKeyPress(final UserSession session) {}

	@PortInstance
	@PortContract(provides = IUserNotification.class, requires = IUserRequest.class)
	IPort portUser;

	public IPort portUser() {
		return this.portUser;
	}

	@PortInstance
	@PortContract(provides = IConsoleNotification.class, requires = IConsoleRequest.class)
	IPort portConsole;

	public IPort portConsole() {
		return this.portConsole;
	}
}
