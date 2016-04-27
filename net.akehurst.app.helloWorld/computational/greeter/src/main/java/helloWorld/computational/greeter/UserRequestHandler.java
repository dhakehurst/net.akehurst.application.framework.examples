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

import java.util.concurrent.Future;

import helloWorld.computational.interfaceUser.IUserNotification;
import helloWorld.computational.interfaceUser.IUserRequest;
import helloWorld.computational.interfaceUser.Message;
import net.akehurst.application.framework.common.annotations.instance.CommandLineArgument;
import net.akehurst.application.framework.common.annotations.instance.ConfiguredValue;
import net.akehurst.application.framework.common.annotations.instance.ServiceReference;
import net.akehurst.application.framework.realisation.AbstractActiveSignalProcessingObject;
import net.akehurst.application.framework.technology.interfaceFilesystem.IFile;
import net.akehurst.application.framework.technology.interfaceFilesystem.IFilesystem;

public class UserRequestHandler extends AbstractActiveSignalProcessingObject implements IUserRequest {

	public UserRequestHandler(String id) {
		super(id);
	}
	
	@CommandLineArgument(hasValue=true,description="Override the greeting message.")
	@ConfiguredValue(defaultValue="Hello World!")
	Message message;
	
	IUserNotification userNotification;
	
	// --------- IUserRequest ---------
	@Override
	public Future<Void> requestStart() {
		return super.submit("requestStart", () -> {
			
			this.userNotification.notifyMessage(this.message);

		});
	}
}
