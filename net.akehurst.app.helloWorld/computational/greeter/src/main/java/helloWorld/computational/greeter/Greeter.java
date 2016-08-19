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
package helloWorld.computational.greeter;

import helloWorld.computational.interfaceUser.IUserNotification;
import helloWorld.computational.interfaceUser.IUserRequest;
import net.akehurst.application.framework.common.IPort;
import net.akehurst.application.framework.common.annotations.declaration.Component;
import net.akehurst.application.framework.common.annotations.declaration.ProvidesInterfaceForPort;
import net.akehurst.application.framework.common.annotations.instance.ActiveObjectInstance;
import net.akehurst.application.framework.common.annotations.instance.PortInstance;
import net.akehurst.application.framework.realisation.AbstractComponent;

// The main logic of our application is
// the Greeter Component.
// It sends a greeting message to the user.
@Component
public class Greeter extends AbstractComponent {

	public Greeter(final String id) {
		super(id);
	}

	@ActiveObjectInstance
	@ProvidesInterfaceForPort(portId = "portUser", provides = IUserRequest.class)
	UserRequestHandler userRequestHandler;

	@Override
	public void afConnectParts() {
		this.userRequestHandler.userNotification = this.portUser().out(IUserNotification.class);
	}

	// ---------- Ports ---------
	@PortInstance(provides = { IUserRequest.class }, requires = { IUserNotification.class })
	IPort portUser;

	public IPort portUser() {
		return this.portUser;
	}
}
