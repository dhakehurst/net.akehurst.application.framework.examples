package net.akehurst.example.flightSimulator.gui.channel;

import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.elements.IGuiElement;

public interface IControlsScene extends IGuiScene {

	IGuiElement getThrotle();

	IGuiElement getJoystick();

}
