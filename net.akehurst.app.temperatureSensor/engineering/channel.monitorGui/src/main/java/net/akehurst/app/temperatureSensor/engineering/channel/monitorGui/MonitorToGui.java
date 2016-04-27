package net.akehurst.app.temperatureSensor.engineering.channel.monitorGui;

import net.akehurst.application.framework.common.IPort;
import net.akehurst.application.framework.common.annotations.declaration.ProvidesInterfaceForPort;
import net.akehurst.application.framework.common.annotations.instance.ActiveObjectInstance;
import net.akehurst.application.framework.common.annotations.instance.PortInstance;
import net.akehurst.application.framework.realisation.AbstractComponent;
import net.akehurst.application.framework.technology.guiInterface.IGuiNotification;
import net.akehurst.application.framework.technology.guiInterface.IGuiRequest;
import temperatureSensor.computational.userInterface.IUserNotification;
import temperatureSensor.computational.userInterface.IUserRequest;

public class MonitorToGui extends AbstractComponent {

	public MonitorToGui(String id) {
		super(id);
	}

	@ActiveObjectInstance
	@ProvidesInterfaceForPort(portId = "portGui", provides = IGuiNotification.class)
	@ProvidesInterfaceForPort(portId = "portUser", provides = IUserNotification.class)
	UserNotificationHandler handler;
	
	@Override
	public void afConnectParts() {
		this.handler.setGuiRequest( portGui().out(IGuiRequest.class) );
		//this.handler.userRequest = portUser().out(IUserRequest.class);
	}
	
	
	// --------- Ports ---------
	@PortInstance(provides={IUserNotification.class},requires={IUserRequest.class})
	IPort portUser;
	public IPort portUser() {
		return this.portUser;
	}
	
	@PortInstance(provides={IGuiNotification.class},requires={IGuiRequest.class})
	IPort portGui;
	public IPort portGui() {
		return this.portGui;
	}
}
