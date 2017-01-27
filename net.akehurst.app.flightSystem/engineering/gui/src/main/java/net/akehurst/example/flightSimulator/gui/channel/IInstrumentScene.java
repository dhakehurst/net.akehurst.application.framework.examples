package net.akehurst.example.flightSimulator.gui.channel;

import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.elements.IGuiElement;

public interface IInstrumentScene extends IGuiScene {

	IGuiElement getAirSpeed();

	IGuiElement getAirCompass();

	IGuiElement getAltimeter();

	IGuiElement getHorizon();

	IGuiElement getEngineSpeed();

}
